package com.huawei.servercore;

import java.util.Random;

public class positionServer {
//初始温度
public int T=100;
//用于控制温度下降速率
public float t=0f;
//控温因子
public float a=0.98f;
//迭代次数
public int N=100;
//外层迭代次数
public int k=0;
//产生随机概率
public Random random=new Random(System.currentTimeMillis());
//当前评估值
public int currentEvaluation=0;
//上一轮的评估值
public int oldEvaluation=0;
//保存最好的结果
public int bestEvaluation=0;
public int bestX=0;
public int[] values=new int[]{1,2,3,4,5};
public int pickInitX(){
	return 1;
}
/**
 * 更新解
 * @param old
 * @param current
 */
public int updateX(int old){
	int index=getIndex(old);
	if(index==-1)
	random.nextInt(5);
	if(index==0||index==4){
		return values[random.nextInt(5)];
	}
	boolean isPlus=random.nextBoolean();
	if(isPlus){
		return values[index+1];
	}
	return values[index-1];
}
public int assess(int x){
	return 3*x-x*x;
}
public int getIndex(int x){
	for(int i=0;i<values.length;i++){
		if (values[i]==x)
			return i;
	}
	return -1;
}
public void solve(){
	int old=pickInitX();
	int current=old;
	oldEvaluation=assess(old);
	bestX=old;
	bestEvaluation=oldEvaluation;
	while(k<T){
		int n=0;
		while(n<N){
			current=updateX(old);
			currentEvaluation=assess(current);
			//保存当前最好的解
			if(currentEvaluation>bestEvaluation){
				bestEvaluation=currentEvaluation;
				bestX=current;
			}
			int delta=currentEvaluation-oldEvaluation;
			//新解比原来的解好
			if(delta>0){
				old=current;
				oldEvaluation=currentEvaluation;
			}
			//如果新解比原来的解差,也一定的概率向新解移动
			if(delta<0&&Math.exp(delta/t)>random.nextFloat()){
				old=current;
				oldEvaluation=currentEvaluation;
			}
			n+=1;
		}
		t=a*t;
		k+=1;
	}
}
public static void main(String[]args){
	positionServer ps=new positionServer();
	ps.solve();
	System.out.println(ps.bestX);
	System.out.println(ps.bestEvaluation);
}
}
