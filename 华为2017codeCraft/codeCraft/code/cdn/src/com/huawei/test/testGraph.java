package com.huawei.test;

import java.util.HashMap;
import java.util.Map;

public class testGraph {
public Map<Integer, Edge> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Edge> map) {
		this.map = map;
	}
public Map<Integer,Edge> map;
public  static void main(String[]args){
	Map <Integer,Integer> map=new HashMap();
	map.put(1,1);
	int s=map.get(0);
	System.out.println(s);
}
}
