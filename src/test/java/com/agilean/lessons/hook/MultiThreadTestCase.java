package com.agilean.lessons.hook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.agilean.lessons.hook.concurrent.CompleteReminderer;
import com.agilean.lessons.hook.concurrent.HttpCaller;
import com.agilean.lessons.hook.concurrent.ResultBean;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiThreadTestCase {
	static ThreadPoolExecutor executor;
	static List<Callable<ResultBean>> tasks=new ArrayList<Callable<ResultBean>>();
	@Autowired
	private RestTemplate restTemplate;
	@Test
	public void testInit() {
		System.out.println(">>>>start test<<<<");
		
		System.out.println(">>>>restTemplate="+restTemplate);
		 int count=10;
		CountDownLatch cdl=new CountDownLatch(count);
		String url="https://tkb.agilean.cn/#/?viewId=e7b3f9f2a71040d08c9640ddc73b107b";
		
		executor=(ThreadPoolExecutor) Executors.newFixedThreadPool(1);

		/*for(int i=0;i<count;i++) {
			System.out.println(">>>>>i="+i);
			 HttpCaller caller=new HttpCaller(url,"Test",restTemplate,cdl);
			 tasks.add(caller);
		}*/
		
		CompleteReminderer r=new CompleteReminderer(cdl);
		tasks.add(r);
		
		try{
			executor.invokeAll(tasks);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		while(true) {
			
			if(executor.getActiveCount()==0) {
				System.out.println("Done");
				break;
			}
		}
		executor.shutdown();
		
	}
}
