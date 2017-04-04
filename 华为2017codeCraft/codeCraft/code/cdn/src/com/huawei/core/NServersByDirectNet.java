package com.huawei.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.huawei.domain.Edge;
import com.huawei.domain.VertexImportance;

public class NServersByDirectNet extends randomServer {
	public NServersByDirectNet(graphSearch gs) {
		super(gs);
	}
	@Override
	public void createRandomServer(int... number) {
		ArrayList<VertexImportance> vios=convertMapToObjectList(gs.connectedwithConsume);
		//进行排序
		//System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
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
	private ArrayList convertMapToObjectList(Map<Integer,Integer> map){
		ArrayList<VertexImportance> Vos=new ArrayList();
		Iterator<Integer> it=map.keySet().iterator();
		while(it.hasNext()){
			int key=it.next();
			int value=map.get(key);
			VertexImportance vi=new VertexImportance(key,value);
			Vos.add(vi);
		}
		return Vos;
	}
}
