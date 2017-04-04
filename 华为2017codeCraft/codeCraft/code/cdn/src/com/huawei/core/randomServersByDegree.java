//package com.huawei.core;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Random;
//import java.util.Set;
//
//import com.huawei.domain.Vdegree;
//import com.huawei.domain.graph;
///**
// * 服务器
// * @author cristo
// *
// */
//public class randomServersByDegree extends randomServer{
//public randomServersByDegree(graphSearch gs){
//super(gs);
//}
///**
// * 度超过3才加入到待选部分
// */
//public void createRandomServerbyDegree() {
//	for(int i=0;i<gs.g.vertexNum;i++){
//		int i_degree=gs.g.getDegree(i);
//		if(i_degree>=3)
//			servers.add(i);
//}
//	//将与消费节点直接连接的网络节点也加入其中
//	ArrayList<Integer> list=gs.connectedwithConsume;
//	for(int connected:list){
//		if(servers.contains(connected))
//			servers.add(connected);
//	}
//	serverNumber=servers.size();
//	//System.out.println("serverNumber"+serverNumber);
//}
//@Override
//public void createRandomServer(int... number) {
//	if(number.length>=1)
//	serverNumber=number[0];
//	createRandomServerbyDegree();
//}
//}
