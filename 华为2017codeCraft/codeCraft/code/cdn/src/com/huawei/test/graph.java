package com.huawei.test;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
public class graph {
public Vertex[] vertexList;
//点个数
public int vertexNum;
//边个数
public int edgeLength;
public enum ctype{
	bandwidth,cost,weight
}
public graph(int vertexNum){
	this.vertexNum=vertexNum;
	vertexList=new Vertex[vertexNum];
}
public graph(int vertexNum,int edgeLength){
	this.vertexNum=vertexNum;
	vertexList=new Vertex[vertexNum];
	this.edgeLength=edgeLength;
}
/**
 * 初始化
 */
public void initVertext(){
	for(int i=0;i<this.vertexNum;i++){
		Vertex vertext=new Vertex();
		vertext.data=Vflag.others;
		vertext.edges=null;
		vertext.edgeMap=new HashMap();
		vertexList[i]=vertext;
		//System.out.println("i"+vertexList[i]);
	}
}
/**
 * 得到相应节点的度(作为后期选择服务器位置的参考依据)
 * @param x,节点号
 * @return
 */
public int getDegree(int x){
	LinkedList<Integer> edges=getEdges(x);
	int edgeNumber=0;
	if(null!=edges&&edges.size()>=1)
		edgeNumber=edges.size();
	return edgeNumber;
}
/**
 * 修改某节点的类型
 * @param x，节点号
 * @param value，节点类型值
 */
public void setVertexData(int x ,int value){
	vertexList[x].setData(value);
}
/**
 * 得到某节点的类型
 * @param x，节点号
 */
public int getVertexData(int x){
	return vertexList[x].getData();
}
/**
 * 得到相应节点的所有邻接点
 * @param x
 * @return
 */
public LinkedList<Integer> getEdges(int x){
	return vertexList[x].getEdges();
}
/**
 * 得到相应节点的所有邻接点信息映射表
 * @param x
 * @return
 */
public Map<Integer,Edge> getEdgeMap(int x){
	return vertexList[x].getEdgeMap();
}
/**
 * 设置相应点的所有邻接点
 * @param x
 * @param iedges
 */
public void setEdges(int x,LinkedList<Integer> iedges){
	 vertexList[x].setEdges(iedges);;
}
/**
 * 设置相应点的所有邻接点的映射表(邻接点号,邻接点信息)
 * @param x
 * @param iedges
 */
public void setEdgeMap(int x,Map<Integer,Edge>iedgeMap){
	 vertexList[x].setEdgeMap(iedgeMap);
}
/**
 * 添加双向边
 * @param x,起点
 * @param y，终点
 * @param bandwidth，带宽
 * @param cost，费用
 * @param weight，权重
 */
public void addDoubleEdge(int x,int y,int bandwidth,int cost,int...weight){
	addEdge(x,y,bandwidth,cost,weight);
	addEdge(y,x,bandwidth,cost,weight);
}
/**
 * 添加边操作
 * @param x,起点
 * @param y，终点
 * @param bandwidth 带宽
 * @param cost 费用
 */
public void addEdge(int x,int y,int bandwidth,int cost,int...weight){
	Edge edge=new Edge();
	edge.setVertexId(y);
	edge.setBandwidth(bandwidth);
	edge.setCost(cost);
	if(weight.length==0)
	edge.setWeight(cost);
	else
		edge.setWeight(weight[0]);
	LinkedList<Integer> iedges=getEdges(x);
	Map<Integer,Edge> map=getEdgeMap(x);
	if(null==iedges){
		iedges=new LinkedList();
		iedges.add(y);
		map.put(y,edge);
		setEdgeMap(x,map);
		setEdges(x,iedges);
	}
	else{
		iedges.add(y);
		map.put(y,edge);
	}
}
/**
 * 打印与某点相关的邻接点信息
 * @param xiaoqiang
 */
public void printEdge(int index){
	LinkedList<Integer> edges=getEdges(index);
	if(edges==null){
		return;
	}
	for(int edge :edges){
		System.out.println(edge);
	}
}
/**
 * 判断y在边表中中的下标
 * @param y
 * @param edges
 * @return
 */
public int findEdge(int y,LinkedList<Integer> edges){
	if(null==edges||y==-1)
		return -1;
	for(int i=0;i<edges.size();i++){
		if(edges.get(i)==y){
			return i;
		}
	}
	return -1;
}
/**
 * 修改x,y点之间的参数
 * @param x
 * @param y
 * @param value
 * @param c 带宽或者费用
 */
public void revise(int x,int y,int value,ctype c){
	Map<Integer,Edge> map=getEdgeMap(x);
	if(null!=map){
	Edge edge=map.get(y);
	switch(c){
	case bandwidth:
	edge.setBandwidth(value);
	break;
	case cost:
	edge.setCost(value);
	break;
	case weight:
	edge.setWeight(value);
	break;
	}}
}
/**
 * 计算2点间的参数列表
 * @param x
 * @param y
 * @return
 */
public int calculate(int x,int y,ctype cal){
	Map<Integer,Edge> map=getEdgeMap(x);
	if(null==map)
		return 0;
	if(null!=map){
		if(map.containsKey(y)){
	if(cal==ctype.bandwidth) 
	return map.get(y).bandwidth;
	if(cal==ctype.cost)
	return map.get(y).cost;
	}
	if(cal==ctype.weight)
	 return map.get(y).weight; 
}
	return 0;
	}
/**
 * 格式化路径
 * @param path
 */
public void printPath(List<Integer> list){
	StringBuilder sb=new StringBuilder();
	for(int i :list){
		sb.append(i+"->");
	}
	sb.delete(sb.length()-2,sb.length());
	System.out.println(sb.toString());
}
public static void main(String[]args) throws InterruptedException{

}
}
