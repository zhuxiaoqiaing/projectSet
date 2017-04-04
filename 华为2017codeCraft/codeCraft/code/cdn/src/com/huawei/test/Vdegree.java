package com.huawei.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
/**
 * 存放节点以及对应的度
 * @author cristo
 *
 * @param <Object>
 */
public class Vdegree<Object> implements Comparable<Object> {
int vertexId;
int degree;
public Vdegree(int vertexId, int degree) {
	super();
	this.vertexId = vertexId;
	this.degree = degree;
}
public int getVertexId() {
	return vertexId;
}
public void setVertexId(int vertexId) {
	this.vertexId = vertexId;
}
public int getDegree() {
	return degree;
}
public void setDegree(int degree) {
	this.degree = degree;
}
@Override
public String toString() {
	return "Vdegree [vertexId=" + vertexId + ", degree=" + degree + "]";
}
@Override
public int compareTo(Object o) {
	Vdegree other=(Vdegree)o;
	if(this.degree!=other.degree)
		return this.degree-other.degree;
	return this.vertexId>other.vertexId?-1:1;
}
public static void main(String[]args){
	Vdegree v1=new Vdegree(1,2);
	Vdegree v2=new Vdegree(2,2);
	Vdegree[] l=new Vdegree[]{v1,v2};
	ArrayList<Vdegree>list=new ArrayList();
	list.add(v1);
	list.add(v2);
	Arrays.sort(l);
	for(int i=0;i<l.length;i++){
		System.out.println(l[i]);
	}
}
}
