package com.agilean.lessons.hook.concurrent;

import java.util.concurrent.CountDownLatch;

public class CompleteReminderer implements Runnable {
	private CountDownLatch  countDownLatch;
   
	public CompleteReminderer(CountDownLatch countDownLatch) {
		super();
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void run() {
		try {
			this.countDownLatch.await();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(">>>>Done<<<<");

	}

}
