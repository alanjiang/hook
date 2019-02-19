package com.agilean.lessons.hook;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.agilean.lessons.hook.concurrent.CompleteReminderer;
import com.agilean.lessons.hook.concurrent.HttpCaller;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiThreadTestCase {

	@Autowired
	private RestTemplate restTemplate;
	
	@Test
	public void testInit() {
		
		 int count=5000;
		CountDownLatch cdl=new CountDownLatch(count);
		String url="http://localhost:8080/greeting";
		
		ExecutorService executor=Executors.newCachedThreadPool();
		for(int i=0;i<count;i++) {
			
			 HttpCaller caller=new HttpCaller(url,restTemplate,cdl);
			 Thread t=new Thread(caller);
			 executor.execute(t);
		}
		
		CompleteReminderer r=new CompleteReminderer(cdl);
		Thread rt=new Thread(r);
		executor.execute(rt);
		
		executor.shutdown();
		
	}
}
