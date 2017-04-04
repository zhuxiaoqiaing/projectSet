package com.huawei.domain;

import java.io.Serializable;

//边节点
public class Edge implements Serializable{
//对应的点
public int vertexId;
//该链路的贷款
public int bandwidth;
//单位时间内的费用
public int cost;
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + bandwidth;
	result = prime * result + cost;
	result = prime * result + vertexId;
	result = prime * result + weight;
	return result;
}
@Override
public String toString() {
	return "Edge [vertexId=" + vertexId + ", bandwidth=" + bandwidth
			+ ", cost=" + cost + ", weight=" + weight + "]";
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Edge other = (Edge) obj;
	if (bandwidth != other.bandwidth)
		return false;
	if (cost != other.cost)
		return false;
	if (vertexId != other.vertexId)
		return false;
	if (weight != other.weight)
		return false;
	return true;
}
//初始化等于cost
public int weight;
public int getVertexId() {
	return vertexId;
}
public int getBandwidth() {
	return bandwidth;
}
public int getWeight() {
	return weight;
}
public void setWeight(int weight) {
	this.weight = weight;
}
public void setBandwidth(int bandwidth) {
	this.bandwidth = bandwidth;
}
public int getCost() {
	return cost;
}
public void setCost(int cost) {
	this.cost = cost;
}
public void setVertexId(int vertexId) {
	this.vertexId = vertexId;
}

}
