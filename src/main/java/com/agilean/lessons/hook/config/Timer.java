package com.agilean.lessons.hook.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.agilean.lessons.hook.concurrent.CutBackRefundCaller;
import com.agilean.lessons.hook.concurrent.HttpCaller;

@Component
public class Timer {
	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;
	@Autowired
	private RestTemplate restTemplate;
    //@Scheduled(initialDelay = 1000, fixedDelay = 7200000)
    public void cutBackFund() {
          String url="http://localhost:8080//fund/cutback";
    	  System.out.println("---运行车货扣款任务-----");
    	  int count=10;
    	  for(int i=0;i<count;i++) {
    		 System.out.println("---A端发出第"+i+"笔扣款任务-----");
    		 CutBackRefundCaller caller=new CutBackRefundCaller(url,i,restTemplate);
 			 threadPoolExecutor.submit(caller);
 		  }
    	  while(true) {
  			if(threadPoolExecutor.getActiveCount()==0) {
  				threadPoolExecutor.shutdownNow();
  				System.out.println("---定时扣款任务结束--");
  				break;
  			}
  		  }
    	 
    	  
    	  
    	  
    	 
    }
}
