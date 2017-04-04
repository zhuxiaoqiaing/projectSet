package com.huawei.core;
import java.io.IOException;
import com.filetool.util.myUtil;
import com.huawei.core.InitParams.serverRange;
import com.huawei.domain.graphInfo;
public class huaweiDriver {
public long beginTime=0;
/**
 * 程序的入口
 * @param content
 * @return
 */
	public String[] driver(String[]content) {
		//用于存放最终获得服务器的结果
		String[] results=null;
		beginTime=System.currentTimeMillis();
		graphInfo gi=new graphInfo();
		gi.parse(content);
		//创建图搜索的核心类
		graphSearch gs=new graphSearch();
		gs.init(gi);
		//将对象初始状态保存,用于进行多轮测试
		byte[] gsByte =null;
		try {
			gsByte=myUtil.objToByte(gs);
		} catch (IOException e1) {
			System.out.println("序列化出错...");
		}
		//创建2个随机分析器
		randomServer randomS=new NServersByDirectNet(gs);
		randomS.createRandomServer();
		randomServer randomLevel=new NServersByLevelNet(gs);
		randomLevel.createRandomServer();
		minCostInfo mci=new minCostInfo();
		//总的消费节点数
		int EstimateNumber=gs.connectedwithConsume.size()-1;
		try {
			for(int j=EstimateNumber;j>=Math.floor(EstimateNumber/8);j--){
				if(isEnd()){
					throw new Exception("定时结束...");
				}
				//第一个分类器
				try {
					gs=myUtil.byteToObj(gsByte,graphSearch.class);
				} catch (Exception e) {
					System.out.println("不能使用序列化...");
					gs=new graphSearch();
				}
				//判断是否结束
				if(isEnd()){
					throw new Exception("定时结束...");
				}
				//核心程序
				gs.run(randomS,mci,j);
				//第二个随机分析器
				try {
					gs=myUtil.byteToObj(gsByte,graphSearch.class);
				} catch (Exception e) {
					System.out.println("不能使用序列化...");
					gs=new graphSearch();
				}
				if(isEnd()){
					throw new Exception("定时结束...");
				}
				//核心程序
				gs.run(randomLevel,mci,j);
				}
		} catch (Exception e) {
			System.out.println("超时了,准备最后的清理工作..");
		}
		finally{
			//有解则输出,无解则将直连的答案传入
		results=mci.outputStringForMaxFlowPath(gi);
		return results;
	}
		}
	/**
	 * 定时器结束测试
	 * @param beginTime
	 * @return
	 */
	public  boolean isEnd(){
		long endTime=System.currentTimeMillis();
		long times=endTime-beginTime;
		if(times>=1000*86){
			return true;
		}
	    return false;
}
//public static void main(String[]args){
//	huaweiDriver huawei=new huaweiDriver();
//	huawei.driver("");
//}
}
