package com.huawei.test;
import java.util.LinkedList;
import java.util.Map;
public class Vertex {
//存放消费节点的带宽
public int data;
public LinkedList<Integer> edges;
public Map<Integer,Edge> edgeMap;
public int getData() {
	return data;
}
public void setData(int data) {
	this.data = data;
}
public LinkedList<Integer> getEdges() {
	return edges;
}
public void setEdges(LinkedList<Integer> edges) {
	this.edges = edges;
}
public Map<Integer, Edge> getEdgeMap() {
	return edgeMap;
}
public void setEdgeMap(Map<Integer,Edge> iedgeMap) {
	this.edgeMap = edgeMap;
}
}
