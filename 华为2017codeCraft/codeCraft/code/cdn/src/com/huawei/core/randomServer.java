package com.huawei.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.huawei.domain.graph;
public abstract class randomServer{
/**
 * 存放挑选的服务节点集合
 */
public  ArrayList<Integer> servers=new ArrayList();
public graphSearch gs;
public  int serverNumber=0;
public  Random random=new Random();
Set<Integer> set=new HashSet();
public randomServer(graphSearch gs){
	this.gs=gs;
}
/**
 * 集合中随机生产number服务器元素出来
 * @number
 * @return
 */
public abstract void createRandomServer(int... number);
/**
 * 随机产生n个服务器列表
 * @param number
 * @return
 */
public ArrayList randomChoose(int number) {
	set.clear();
	int temp=0;
	while(set.size()<number){
	temp=random.nextInt(serverNumber);
	set.add(servers.get(temp));
	}
	return convertSetTArray(set);
}
/**
 * 将set集合转化成数组形式
 * @param set
 * @return
 */
public ArrayList convertSetTArray(Set<Integer> set){
	ArrayList result=new ArrayList(set);
	return result;
}
}
