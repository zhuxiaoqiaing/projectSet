package com.huawei.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.huawei.domain.graphInfo;
/**
 * 用于输出结果，即网络成本与费用
 * @author cristo
 *
 */
public class minCostInfo {
//是否找到满足最大流的解
public boolean isFind=false;
public boolean isFind() {
	return isFind;
}
public void setFind(boolean isFind) {
	this.isFind = isFind;
}
//当前的路径
public Map<Stack,Integer> paths=new HashMap();
//最小费用
public int miniCost=Integer.MAX_VALUE;
//最大流
public int maxf=0;
//存放选择方案
public ArrayList<Integer> servers=new ArrayList();
//逻辑消费节点与消费节点直接的映射,用于最后还原消费节点真实标号
public int ConsumeOffset=0;
//链路数(其实就是总的路径数)
public int linkNumber=0;
public int getConsumeOffset() {
	return ConsumeOffset;
}
public void setConsumeOffset(int consumeOffset) {
	ConsumeOffset = consumeOffset;
}

public ArrayList getServers() {
	return servers;
}
public void setServers(ArrayList servers) {
	this.servers = servers;
}
public int getLinkNumber() {
	return linkNumber;
}
public void setLinkNumber(int linkNumber) {
	this.linkNumber = linkNumber;
}
public int getMiniCost() {
	return miniCost;
}
public void setMiniCost(int miniCost) {
	this.miniCost = miniCost;
}
public int getMaxf() {
	return maxf;
}
public void setMaxf(int maxf) {
	this.maxf = maxf;
}
public Map<Stack, Integer> getPaths() {
	return paths;
}
public void setPaths(Map<Stack, Integer> paths) {
	this.paths = paths;
}
/**
 * 当求出最好解输出结果时, 将消费节点变成题目要求的序号
 */
private ArrayList<String> outputForMaxFlowPath(){
	ArrayList<String> finalPath=new ArrayList<String>();
	StringBuilder sb=new StringBuilder();
	Iterator<Stack> it=paths.keySet().iterator();
	while(it.hasNext()){
		Stack<Integer> key=it.next();
		int value=paths.get(key);
		if(key.size()>=3){
			//去掉带宽为0的路径
			if(value==0)
				continue;
			for(int i=(key.size()-2);i>=1;i--){
				int vertex=key.get(i);
				if(i==1){
					vertex=vertex-ConsumeOffset;
				}
				sb.append(vertex+" ");
			}
			sb.append(value);
			finalPath.add(sb.toString());
			sb.delete(0,sb.length());
		}
	}
	linkNumber=finalPath.size();
	return finalPath;
}
/**
 * 用于输出最大流
 * @return
 */
public String[] outputStringForMaxFlowPath(graphInfo gi){
	System.out.println("最小费用为:"+miniCost);
	System.out.println("服务器选择如下:");
	System.out.println("服务器个数为"+servers.size());
	int j=0;
	for(int s:servers){
		System.out.print(s+"-->");
		if(j%10==0&&j!=0){
			System.out.println();
		}
		j++;
	}
	System.out.println();
	String[] Strresults=null;
	if(!isFind){
		//将与消费节点直连的网络节点作为最终解
		return gi.getDirectedAnswer();
	}
	else{
		ArrayList<String> results=outputForMaxFlowPath();
		Strresults=new String[results.size()+2];
		Strresults[0]=String.valueOf(linkNumber);
		Strresults[1]="";
		int i=2;
		for(String result:results){
			Strresults[i]=result;
			i+=1;
		}
}
	return Strresults;
}
}
