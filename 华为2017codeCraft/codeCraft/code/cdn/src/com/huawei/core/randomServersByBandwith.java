package com.huawei.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import com.huawei.domain.Edge;
import com.huawei.domain.VertexImportance;
import com.huawei.domain.graph;
/**
 *基于与节点相关的链路的总带宽选择服务器
 * @author cristo
 *
 */
public class randomServersByBandwith extends randomServer {
	public randomServersByBandwith(graphSearch gs) {
		super(gs);
	}
	@Override
	public void createRandomServer(int... number) {
		ArrayList<VertexImportance> vios=new ArrayList();
		for(int i=0;i<(gs.g.vertexNum-2-gs.consumeNumber);i++){
			LinkedList<Integer> vertexi=gs.g.getEdges(i);
			//节点的度
			if(vertexi!=null&&vertexi.size()>=3){
				Map <Integer,Edge>EdgeMap=gs.g.getEdgeMap(i);
				if(EdgeMap==null||EdgeMap.size()<=0)
					continue;
				int allband=0;
				for(int j=0;j<vertexi.size();j++){
				allband+=EdgeMap.get(vertexi.get(j)).bandwidth;
				}
				VertexImportance vi=new VertexImportance(i,allband,vertexi.size());
				vios.add(vi);
			}
		}
		//进行排序
		Collections.sort(vios,new VertexImportanceComparator());
		//Collections.sort(vios,new VertexImportanceComparator());
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
}
