package com.huawei.test;
import java.util.ArrayList;
import com.filetool.util.FileUtil;
public class graphInfo {
//网络节点数
public int vertexs=0;
//网络链接数,即边数
public int edges=0;
public int getServerCost() {
	return serverCost;
}
public void setServerCost(int serverCost) {
	this.serverCost = serverCost;
}
//服务器成本
public int serverCost=0;
public int getVertexs() {
	return vertexs;
}
public void setVertexs(int vertexs) {
	this.vertexs = vertexs;
}
public int getEdges() {
	return edges;
}
public void setEdges(int edges) {
	this.edges = edges;
}
public int getConsumeNum() {
	return consumeNum;
}
public void setConsumeNum(int consumeNum) {
	this.consumeNum = consumeNum;
}
public ArrayList<linkInfo> getLs() {
	return ls;
}
public void setLs(ArrayList<linkInfo> ls) {
	this.ls = ls;
}
public ArrayList<consumeInfo> getCs() {
	return cs;
}
public void setCs(ArrayList<consumeInfo> cs) {
	this.cs = cs;
}
//消费节点数量
public int consumeNum=0;
public  class linkInfo{

	@Override
	public String toString() {
		return "linkInfo [start=" + start + ", end=" + end + ", bandwith="
				+ bandwith + ", cost=" + cost + "]";
	}
	public int start;
	public int end;
	public int bandwith;
	public int cost;
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getBandwith() {
		return bandwith;
	}
	public void setBandwith(int bandwith) {
		this.bandwith = bandwith;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
}
public class consumeInfo{
	public int consumeId;
	public int connectedId;
	public int requireBand;
	public int getConsumeId() {
		return consumeId;
	}
	public void setConsumeId(int consumeId) {
		this.consumeId = consumeId;
	}
	public int getConnectedId() {
		return connectedId;
	}
	public void setConnectedId(int connectedId) {
		this.connectedId = connectedId;
	}
	public int getRequireBand() {
		return requireBand;
	}
	public void setRequireBand(int requireBand) {
		this.requireBand = requireBand;
	}
	@Override
	public String toString() {
		return "consumeInfo [consumeId=" + consumeId + ", connectedId="
				+ connectedId + ", requireBand=" + requireBand + "]";
	}
	
}
public ArrayList<linkInfo> ls=new ArrayList();
public ArrayList<consumeInfo> cs=new ArrayList();
public void parse(){
	String graphFilePath="case0.txt";
	 String[] graphContent = FileUtil.read(graphFilePath, null);
	String[] totalInfo=graphContent[0].split(" ");
	//网络节点数量 网络链路数量 消费节点数量
	vertexs=Integer.valueOf(totalInfo[0]);
	edges=Integer.valueOf(totalInfo[1]);
	consumeNum=Integer.valueOf(totalInfo[2]);
	//System.out.println(linkLength);
	serverCost=Integer.valueOf(graphContent[2]);
	for(int i=4;i<4+edges;i++){
		String[] lkc=graphContent[i].split(" ");
		if(lkc.length==4){
		linkInfo lk=new linkInfo();
		lk.setStart(Integer.parseInt(lkc[0]));
		lk.setEnd(Integer.parseInt(lkc[1]));
		lk.setBandwith(Integer.parseInt(lkc[2]));
		lk.setCost(Integer.parseInt(lkc[3]));
		ls.add(lk);
	}
		}
	for(int i=5+edges;i<graphContent.length;i++){
		//System.out.println(graphContent[i]);
		String[] lkc=graphContent[i].split(" ");
		if(lkc.length==3){
		consumeInfo ci=new consumeInfo();
		ci.setConsumeId(Integer.parseInt(lkc[0]));
		ci.setConnectedId(Integer.parseInt(lkc[1]));
		ci.setRequireBand(Integer.parseInt(lkc[2]));
		cs.add(ci);
	}
}}
public static void main(String[]args){
	graphInfo g=new graphInfo();
	g.parse();
}
}
