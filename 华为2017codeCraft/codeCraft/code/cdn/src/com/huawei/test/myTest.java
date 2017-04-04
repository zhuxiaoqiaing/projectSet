package com.huawei.test;

import java.util.Arrays;
import java.util.Random;

public class myTest {
public static Random random=new Random();
public static void main(String[]args){
	//int[] s=new int[]{3,7,3,6,4,4};
	//Arrays.sort(s);
	//System.out.println(s[0]);
//	boolean k=hasNumber(-1,s);
//	System.out.println(k);
	int s=random.nextInt(2);
	System.out.println(s);
	st(s);
	System.out.println(s);
}
public static boolean hasNumber(int number,int[]random){
	return Arrays.binarySearch(random,number)==-1;
}
public static void st(int s){
	s+=1;
}
public static int[] randomChoose(int number) {
	int[]ranNumber=new int[number];
	int i=0;
	int temp=0;
	while(true){
	temp=random.nextInt(100);
	System.out.println(temp);
	if(!hasNumber(temp,ranNumber)){
		System.out.println("not..");
		ranNumber[i]=temp;
		i++;
		if(i>=number)
			break;
	}
	}
	return ranNumber;
}
}
