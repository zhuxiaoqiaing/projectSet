package com.huawei.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.huawei.domain.Edge;
import com.huawei.domain.VertexImportance;
/**
 * 进行分层网络分析
 * @author cristo
 *
 */
public class NServersByLevelNet extends randomServer {
	//存放所有层的信息
	ArrayList<Map<Integer,Integer>> datas=null;
	//保存网络中间层信息
	Map<Integer,Integer> midNetNode=new HashMap();
	
	public ArrayList<Map<Integer, Integer>> getDatas() {
		return datas;
	}
	public void setDatas(ArrayList<Map<Integer, Integer>> datas) {
		this.datas = datas;
	}
	public Map<Integer, Integer> getMidNetNode() {
		return midNetNode;
	}
	public void setMidNetNode(Map<Integer, Integer> midNetNode) {
		this.midNetNode = midNetNode;
	}
	/**
	 * 构造函数
	 * @param gs
	 */
	public NServersByLevelNet(graphSearch gs) {
		super(gs);
	}
	@Override
	public void createRandomServer(int... number) {
		//获得所有层信息
		datas=clusterbyLevel();
		int midSize=datas.size()/2;
		//获得中间层节点信息
		midNetNode=datas.get(midSize);
		//直连的消费节点的排序
		ArrayList<VertexImportance> vios=convertMapToObjectList(midNetNode);
		//进行排序
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		//VertexImportanceComparator
		Collections.sort(vios,new VertexImportanceComparator());
		if(number.length==1)
			serverNumber=number[0];
		else{
			serverNumber=Integer.MAX_VALUE;
		}
		//选择两者间最小值
		serverNumber=Math.min(serverNumber,vios.size());
		for(int i=0;i<serverNumber;i++){
			VertexImportance vi=vios.get(i);
			servers.add(vi.getVertexId());
		}
	}
	/**
	 * 产生前n个服务器列表
	 * @param number
	 * @return
	 */
	public ArrayList randomChoose(int number) {
		set.clear();
		for(int i=0;i<number;i++){
			set.add(servers.get(i));
		}
		return convertSetTArray(set);
	}
	/**
	 * 将map中的信息进行封装
	 * @param map
	 * @return
	 */
	private ArrayList convertMapToObjectList(Map<Integer,Integer> map){
		ArrayList<VertexImportance> Vos=new ArrayList();
		Iterator<Integer> it=map.keySet().iterator();
		while(it.hasNext()){
			int key=it.next();
			int value=map.get(key);
			VertexImportance vi=new VertexImportance(key,0,value);
			Vos.add(vi);
		}
		return Vos;
	}
/**
* 对网络进行分层分析,从源点出发,进行分层。
* 从原点出发获得所有层的节点
*/
public ArrayList clusterbyLevel(){
	ArrayList<Map<Integer,Integer>> datasNet=new ArrayList();
	//加入原点
	Map<Integer,Integer> cur=new HashMap();
	//加入
	put(cur,gs.g.vertexNum-2);
	Set<Integer> isIn=new HashSet();
	boolean isEnd=false;
	datasNet.add(cur);
	isIn.add(cur.get(0));
	//存放上一层所有的节点
	Map<Integer,Integer> old=null;
	while(true){
		 old=new HashMap();
		 //获得该队列中所有的元素
		 Set<Integer> keyset=cur.keySet();
		for(int s:keyset){
			if(!isIn.contains(s))
			 isIn.add(s);
			//遍历与该节点相关的所有节点如队列
			LinkedList<Integer> some=gs.g.getEdges(s);
			for(int i=0;i<some.size();i++){
				int num=some.get(i);
			if(!isIn.contains(num)){
				isIn.add(num);
			   put(old,num);
			}
			}
		}
		if(old.size()==0){
			break;
		}
		datasNet.add(old);
		//进行复制
		copy(old,cur);
	}
	return datasNet;
	}
/**
 * map复制
 * @param old
 * @param cur
 */
public void copy(Map old,Map cur){
	cur.clear();
	Set<Integer> set=old.keySet();
	for(int s:set){
		cur.put(s,old.get(s));
	}
}
/**
 * 设置map中key,value为相应点的度
 * @param map
 * @param vertexid
 */
public void put(Map<Integer,Integer> map,int vertexid){
	int value=gs.g.getDegree(vertexid);
	map.put(vertexid,value);
}
}