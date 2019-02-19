package com.agilean.lessons.hook.concurrent;

import java.util.concurrent.CountDownLatch;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import com.agilean.lessons.json.BaseMessage;

public class HttpCaller implements Runnable {
	private CountDownLatch  countDownLatch;
    private String url;
    
	//private String method;
	//private String body;
	
	private RestTemplate restTemplate;
	
	
	
	public HttpCaller(String url, RestTemplate restTemplate,CountDownLatch  countDownLatch) {
		super();
		this.url = url;
		this.restTemplate = restTemplate;
		this.countDownLatch =countDownLatch;
	}



	@Override
	public void run() {
		
		try {
            ResponseEntity<BaseMessage>	res=restTemplate.getForEntity(url, BaseMessage.class);
			
			System.out.println(">>>res="+res.getStatusCode()+","+res.getStatusCodeValue());
	        
		}catch(Exception e) {
			 e.printStackTrace();
		}finally {
			this.countDownLatch.countDown();
			System.out.println(">>>>>"+this.countDownLatch.getCount());
		}
		
	}

}
