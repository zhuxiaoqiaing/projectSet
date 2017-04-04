package com.huawei.test;

public class myTh extends Thread {
@Override
public void run() {
while(true){
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("准备睡眠...");
}	
}
}
