package com.huawei.core;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.filetool.util.myUtil;
import com.huawei.domain.graph;
import com.huawei.domain.graph.ctype;
import com.huawei.domain.graphInfo;
import com.huawei.domain.graphInfo.consumeInfo;
import com.huawei.domain.graphInfo.linkInfo;
public class graphSearch implements Serializable{
//图信息
public graph g;
//判断是否在stack中,用于进行搜索
public Map<Integer,Boolean> states=new HashMap();
//存放当前遍历的路径
public Stack<Integer> stack=new Stack();
//c存放所有的最大流的路径
public Map<Stack,Integer> MaxFlowpath=new HashMap();
//队列
public Queue<Integer> q=new LinkedList();
//最大流量
public int maxf;
//最小费用
public int miniCost;
//服务器成本
public int serverCost;
//消费节点个数
public int consumeNumber;
//消费者的带宽总需求(最大流)
public int consumeAllCost;
//记录与消费节点直连的网络以及带宽
public Map<Integer,Integer> connectedwithConsume=new HashMap();
/**
 * 存放消费节点的逻辑标号跟真实标号的映射的偏移量
 * 因为消费节点跟一般网络节点都是从0开始标号，我们这里使用的标号来区分不同节点
 * 所以在处理时，需要把消费节点进行一定的映射，使得网络中所有节点的编号不一样
 */
public static int ConsumeOffset=0; 
/**
 * 构造函数
 * @param g
 */
public graphSearch(graph g){
	this.g=g;
}
/**
 * 判断x点是否在stack中,即是否已经搜索过
 * @param x
 * @return
 */
public Boolean getState(int x){
	if(states.containsKey(x)){
		return states.get(x);
	}
	else
		return false;
}
/**
 * 清除状态
 */
public void clearState(){
	states.clear();
}
/**
 * 默认构造
 */
public graphSearch(){}
/**
 * 初始化图结构
 */
public void  initGraph(){
	g=new graph(9);
	g.initVertext();
	g.addDoubleEdge(0,1,4,4);
	g.addEdge(0,2,1,1);
	g.addDoubleEdge(1,3,1,1);
	g.addDoubleEdge(1,5,2,2);
	g.addDoubleEdge(2,4,4,4);
	g.addDoubleEdge(3,6,2,2);
	g.addDoubleEdge(4,5,2,2);
	g.addDoubleEdge(6,5,2,2);
	//建立超级源点
	g.addDoubleEdge(7,0,Integer.MAX_VALUE,0,1);
	g.addDoubleEdge(7,2,Integer.MAX_VALUE,0,1);
	//建立超级汇点
	g.addDoubleEdge(3,8,Integer.MAX_VALUE,0,1);
	g.addDoubleEdge(6,8,Integer.MAX_VALUE,0,1);
}
/**
 * 初始化图
 * @param gi，图中的信息,从文件中读出
 */
public void initGraph(graphInfo gi){
	//得到消费者的数目
	consumeNumber=gi.consumeNum;
	serverCost=gi.getServerCost();
	//存放消费节点的逻辑标号跟真实标号的映射的偏移量,即网络节点数量
	ConsumeOffset=gi.getVertexs();
	//加上超级源点和汇点
	int allVertexs=ConsumeOffset+consumeNumber+2;
	//System.out.println(allVertexs);
	//设置总节点数
	g=new graph(allVertexs);
	g.initVertext();
	ArrayList<linkInfo> lks=gi.getLs();
	//初始化所有的点
	for(linkInfo lk:lks){
		g.addDoubleEdge(lk.start,lk.end,lk.bandwith,lk.cost,lk.cost);
	}
	//初始化消费节点
	ArrayList<consumeInfo> cs=gi.getCs();
	for(consumeInfo ci:cs){
		//带宽总需求
		consumeAllCost+=ci.getRequireBand();
		//费用0,权重1,带宽
		//消费者从普通节点之后继续编号
		int start=ConsumeOffset+ci.consumeId;
		g.addDoubleEdge(start,ci.connectedId,ci.requireBand,0,ci.requireBand);
		//保存与消费节点直连的网络节点以及相应的带宽,用于后续的服务器选点问题
		connectedwithConsume.put(ci.connectedId,ci.requireBand);
		//设置该节点为消费节点类型
		g.setVertexData(start,ci.requireBand);
		//建立超级源点(超级源点到消费节点，0费用，需求带宽,权重值为1)
		g.addDoubleEdge(g.vertexNum-2,start,Integer.MAX_VALUE,0,ci.requireBand);
	}
}
/**
 * 建立超级汇点
 * @param serverids，服务节点号
 */
public void createSuperEnd(ArrayList<Integer> serverids){
	for(int s:serverids){
		//System.out.println("s:"+s+"      "+"end:"+(g.vertexNum-1));
		g.addDoubleEdge(s,g.vertexNum-1,Integer.MAX_VALUE,0,10000);
	}
}
//public void initAll(graphInfo gi){
//	initGraph(gi);
//	distances=new int[g.vertexNum+2];
//	paths=new int[g.vertexNum+2];
//}
/**
 * 初始化所有参数
 */
public void init(graphInfo gi){
	initGraph(gi);
	distances=new int[g.vertexNum];
	paths=new int[g.vertexNum];
}

/**
 * 寻找与x节点相邻的y之后的邻接点
 * @param x
 * @param y
 * @return
 */
public int getNextNode(int x,int y){
int next_node=-1;
LinkedList<Integer> edges=g.getEdges(x);
if(edges==null) return -1;
int size=edges.size();
int adjvex=g.findEdge(y,edges)+1;
//没有下一个节点
if(adjvex>=size){
	return Integer.MAX_VALUE;
}
for(int i=adjvex;i<size;i++){
	//相邻节点
	int id=edges.get(i);
	if(!getState(id)){
		return id;
	}
}
return -1;
}

/**
 * 深度遍历,找出路径
 * @param x
 * @param y
 * @return
 */
public boolean DFS(int x,int y){
stack.clear();
states.clear();
int top_node;
int adjvex_node=-1;
int next_node;
stack.add(x);
states.put(x,true);
while(!stack.isEmpty()){
	top_node=stack.peek();
	if(top_node==y){
		//打印该路径
		System.out.println("方案:");
		g.printPath(stack);
		//calculateAllWeight();
		return true;
	}
	else{
		next_node=getNextNode(top_node,adjvex_node);
		if(next_node!=-1&&next_node!=Integer.MAX_VALUE){
			//两点之间还有容量
			int s=g.calculate(top_node,next_node,ctype.bandwidth);
			if(s>0)
			  {
			stack.push(next_node);
			states.put(next_node,true);
			adjvex_node=-1;
			}
			else{
				adjvex_node=next_node;
			}
		}
		else {
			adjvex_node=stack.pop();
			//不在stack中
			states.put(adjvex_node,false);
		}
	}
}
return false;
}
/**
 * 计算该链路上各节点之间的最小流量值或者最小费用值
 * @param c,表示计算流量还是费用
 * @return
 */
public int getMinIncQ(ctype c){
//stack中存放当前的可曾广路径
int min=Integer.MAX_VALUE;
if(stack.size()==0){
	return 0;
}
for(int i=1;i<(stack.size()-1);i++){
	int a=g.calculate(stack.get(i-1),stack.get(i),c);
	if(min>a)
		min=a;
}
return min;	
}
/**
 * 对可增广路上节点进行修正,更新
 * @param c，修正类型,分为流量和单位费用
 * @delta 修正的值为多少
 * @return 返回修正的最小值
 */
public void revise(ctype c,int delta){
	if(c==ctype.bandwidth)
	  maxf+=delta;
	for(int i=1;i<(stack.size()-1);i++){
		int x=stack.get(i-1);
		int y=stack.get(i);
		//节点间当前的状态值
		int value=g.calculate(x,y,c);
		//正向更新
		g.revise(x,y,value-delta,c);
		//反向更新
		g.revise(y,x,value+delta,c);
	}
}
/**
 * 得出在一条链路上的费用,
 * 一条链路费用=费用单价和*流量
 * @param q,流量
 * @return 返回该链路上的费用
 */
public int getCostInOnePath(int q){
	int sum=0;
	for(int i=1;i<stack.size();i++){
		sum+=g.calculate(stack.get(i-1),stack.get(i),ctype.cost);
	}
	//System.out.println("sum:---->"+sum+"##########"+"q:--->"+q);
	return sum*q;
}
/**
 * x,y间可达的最大流算法实现
 * @param x,起点
 * @param y，终点
 */
public void maxFlow(int x,int y){
	while(DFS(x,y)){
		//打印当前路径
		//获得该可增广链路上最小的流量
		int delta=getMinIncQ(ctype.bandwidth);
		//更新该可增光链路上的流量值,正向和反向
		 revise(ctype.bandwidth,delta);
	}
	System.out.println("最大流为"+maxf);
}
/**
 * 最短路算法,找出一条最短路
 * @param args
 */
public int distances[];
public int paths[];
public boolean SPFS(int x,int y){
	//清空stack
	q.clear();
	clearState();
	for(int i=0;i<distances.length;i++){
		distances[i]=Integer.MAX_VALUE;
		states.put(i,false);
		paths[i]=0;
	}
	distances[x]=0;
	states.put(x,true);
	q.offer(x);
	while(!q.isEmpty()){
		int top=q.poll();
		states.put(top,false);//出队标志
		//得到所有相邻节点
		if(top==4)
		{
			
		}
	LinkedList<Integer> list=g.getEdges(top);
	for(Integer s:list){
		int weight=g.calculate(top,s,ctype.weight);
		//费用小于0则跳过
		if(weight<=0)
			continue;
		//如果满足松弛原则进行操作
		if(distances[s]>(distances[top]+weight)){
			distances[s]=distances[top]+weight;
			//保存s的前一个路径top,用于回溯
		   paths[s]=top;
		   //如果不在队列中，则进入队列，置元素标记为已在队列中
		   if(!states.get(s)){
			   q.offer(s);
			   states.put(s,true);
		}}
	}
	}
	if(distances[y]==Integer.MAX_VALUE)
		return false;
	//得到当前的路径
	//getSFPSPath(y);
	return true;
}
/**
 * 打印最短路的路径序列
 * @param k
 */
public void getSFPSPath(int k){
       if(paths[k]!=0)
    	   getSFPSPath(paths[k]);
       System.out.println("k"+"->"+k);
}
/**
 * 将路径写入stack中
 * @param x
 * @param k
 */
public void getSFPS(int x,int k){
	stack.clear();
	while(true){
		stack.add(k);
		if(paths[k]==0 &&k==x)
			break;
		k=paths[k];
	}
	Collections.reverse(stack);
	
}
/**
 * 最小费用最大流算法实现
 * @param x,超级源点
 * @param y，超级汇点
 * 
 */
public void miniCostMaxStream(int x,int y){
	int deltaBand=0;
	while(SPFS(x,y)){
		//从stack获得当前的可增广路径
		getSFPS(x,y);
		//System.out.println("路径为"+stack);
		//解决直接连接的问题
          if(stack.size()<=3){
			//System.out.println("直连问题...");
		for(int i=1;i<stack.size();i++){
		 g.revise(stack.get(i-1),stack.get(i),0,ctype.bandwidth);
		 g.revise(stack.get(i),stack.get(i-1),0,ctype.bandwidth);
		 g.revise(stack.get(i-1),stack.get(i),0,ctype.weight);
		 g.revise(stack.get(i),stack.get(i-1),0,ctype.weight);
		}
		if(stack.size()==3){
			deltaBand=g.getVertexData(stack.get(1));
			maxf+=deltaBand;
		}
		}
		else{//非直连
		//流量增量
		deltaBand =getMinIncQ(ctype.bandwidth);
		//System.out.println("deltaBand"+deltaBand);
		//修正流量
		revise(ctype.bandwidth,deltaBand);
		//费用增量(用来求最小流)
		int deltaCost=getMinIncQ(ctype.weight);
		revise(ctype.weight,deltaCost);
		//修正费用,用于寻找最短路径
//		System.out.println("deltaBand"+deltaBand);
//		System.out.println("deltaWeight"+deltaCost);
//		System.out.println("weight:"+g.calculate(58,34,ctype.weight));
//		System.out.println("路径为"+stack);
		miniCost+=getCostInOnePath(deltaBand);
		}
  		//必须复制对象
  		Stack stackCopy=(Stack) stack.clone();
      	MaxFlowpath.put(stackCopy,deltaBand);
	}
}
/**
 *入口函数, 直接提供给华为官网赛题接口
 * @param gi
 * @throws Exception 
 */
public void run(randomServer randoms,minCostInfo mincostinfo,int number){
	ArrayList s=randoms.randomChoose(number);
	createSuperEnd(s);
	miniCostMaxStream(g.vertexNum-2,g.vertexNum-1);
	//System.out.println("最大流-->"+maxf);
	//System.out.println("消费者带宽需求为-->"+consumeAllCost);
	miniCost+=s.size()*serverCost;
	//System.out.println("miniCost"+miniCost);
	//System.out.println("maxFlowPath"+MaxFlowpath);
	if(maxf==consumeAllCost){
		if(miniCost<mincostinfo.getMiniCost()){
			mincostinfo.setMiniCost(miniCost);
			mincostinfo.setMaxf(maxf);
			mincostinfo.setServers(s);
			mincostinfo.setConsumeOffset(ConsumeOffset);
			mincostinfo.setPaths(MaxFlowpath);
			//是否找到可行解
			mincostinfo.setFind(true);
		}
}
	}
/**
 * 对网络进行分层分析
 */
public void clusterbyLevel(){
ArrayList<Integer> cur=new ArrayList();
cur.add(g.vertexNum-2);
ArrayList<ArrayList<Integer>> datas=new ArrayList();
Set<Integer> isIn=new HashSet();
boolean isEnd=false;
datas.add(cur);
isIn.add(cur.get(0));
ArrayList<Integer> old=null;
while(!isEnd){
	 old=new ArrayList();
	 //获得该队列中所有的元素
	for(int s:cur){
		if(!isIn.contains(s))
		 isIn.add(s);
		LinkedList<Integer> some=g.getEdges(s);
		for(int i=0;i<some.size();i++){
			int num=some.get(i);
		if(!isIn.contains(num)){
			isIn.add(num);
		    old.add(num);
		}
		}
	}
	if(old.size()==0){
		break;
	}
	datas.add(old);
	cur=(ArrayList<Integer>) old.clone();
}

for(ArrayList<Integer> list:datas){
	for(int s:list){
		System.out.print(s+"->");
	}
	System.out.println();
}
}
public static void main(String[]args){
	graphInfo gi=new graphInfo();
	if(args==null||args.length==0){
		System.err.println("参数输入错误");
		System.err.println("请输入文件...");
	    System.exit(-1);	
	}
	gi.parsefromFile(args[0]);
	//创建随机服务
	graphSearch gs=new graphSearch();
	//参数初始化
	InitParams ip=new InitParams();
	gs.init(gi);
	randomServer randomS=new NServersByDirectNet(gs);
	randomS.createRandomServer();
	minCostInfo mci=new minCostInfo();
	byte[] gsByte=null;
	try {
		gsByte = myUtil.objToByte(gs);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	int connectedSize=gs.connectedwithConsume.size()-1;
		for(int j=connectedSize;j>Math.floor(connectedSize/2);j--){
			try {
				gs=myUtil.byteToObj(gsByte,graphSearch.class);
			} catch (Exception e) {
				System.out.println("不能使用序列化...");
				gs=new graphSearch();
			}
			gs.run(randomS,mci,j);
		}
//有解则输出,无解则将直连的答案传入
	String []results=mci.outputStringForMaxFlowPath(gi);
	for(String re:results){
		System.out.println(re);
}
}
public static void testSearch(){
	graphInfo gi=new graphInfo();
	gi.parsefromFile("case0.txt");
	//创建随机服务
	graphSearch gs=new graphSearch();
	//参数初始化
	InitParams ip=new InitParams();
	gs.init(gi);
	randomServer randomS=new NServersByLevelNet(gs);
	randomS.createRandomServer();
	minCostInfo mci=new minCostInfo();
	byte[] gsByte=null;
	try {
		gsByte = myUtil.objToByte(gs);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	int connectedSize=gs.connectedwithConsume.size()-1;
		for(int j=connectedSize;j>Math.floor(connectedSize/2);j--){
			try {
				gs=myUtil.byteToObj(gsByte,graphSearch.class);
			} catch (Exception e) {
				System.out.println("不能使用序列化...");
				gs=new graphSearch();
			}
			gs.run(randomS,mci,j);
		}
//有解则输出,无解则将直连的答案传入
	String []results=mci.outputStringForMaxFlowPath(gi);
	for(String re:results){
		System.out.println(re);
}
}
}
