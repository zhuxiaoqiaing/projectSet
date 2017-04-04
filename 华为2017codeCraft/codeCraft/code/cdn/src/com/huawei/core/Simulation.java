package com.huawei.core;
import java.util.ArrayList;
import java.util.Random;
public class Simulation {
	int T=50;//降温次数
	int bestT=0;//最好的温度
	int N=100;//迭代步数
    private float a=0.98f;// 降温系数  
    private float t0=100f;// 初始温度
	Random random=new Random(System.currentTimeMillis());
	private int bestEvaluation;
	private int currentEvaluation;
	private int xEvaluation;
	public randomServer randomserver;
	public Simulation(randomServer randomserver){
		this.randomserver=randomserver;
	}
	//评价函数
	public int evaluate(int[]x,int g[]){
		return getMatrix(x,g);
	}
	public void updateX(){
		
	}
	//实现数组相乘
	public int getMatrix(int []x,int[] y){
		int remax=0;
		for(int i=0;i<x.length;i++){
			remax+=x[i]*y[i];
		}
		return remax;
	}
	//执行
	public void solve(){
		//默认当前最大
		bestEvaluation=evaluate();
        int k = 0;// 降温次数  
        int n = 0;// 迭代步数  
        float t = t0; 
        while(k<T){//温度控制
        	n=0;
        	while(n<N){//迭代步数
        	//取x附近的点
        	updateX();
        	//求currentX时的评价函数
        	currentEvaluation=evaluate();
        	//保存最大评价值
        	if(currentEvaluation>bestEvaluation){
        		bestEvaluation=currentEvaluation;
        		bestT=k;
        	}
        	//评价函数的变化值
        	int delt=currentEvaluation-xEvaluation;
        	//如果评价函数值更大则进行更新,否则以一定概率更新
        	//由于我们这里求的是最大值,所以exp((delt)/t)
        	 if(delt>0||Math.exp((delt)/ t) > random.nextFloat()){
         		//将当前点变成currentx的值,并更新当前评价函数值
         		xEvaluation=currentEvaluation;
        	}
        	 n+=1;
        	}
        	t=a*t;
        	k+=1;
        }
        }  
    private int evaluate() {
		// TODO Auto-generated method stub
		return 1;
	}
	public static void main(String[] args) {
	}
}

