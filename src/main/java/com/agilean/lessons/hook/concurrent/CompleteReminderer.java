package com.agilean.lessons.hook.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class CompleteReminderer implements Callable<ResultBean> {
	private CountDownLatch  countDownLatch;
   
	public CompleteReminderer(CountDownLatch countDownLatch) {
		super();
		this.countDownLatch = countDownLatch;
	}

	@Override
	public ResultBean call() {
		ResultBean rb=new ResultBean();
		try {
			this.countDownLatch.await();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(">>>>Done<<<<");
		rb.setName("reminder");
		rb.setResult("REMINDER");
		return rb;

	}

}
