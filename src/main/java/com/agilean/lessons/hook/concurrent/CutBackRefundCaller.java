package com.agilean.lessons.hook.concurrent;
import java.util.concurrent.TimeUnit;
import org.springframework.web.client.RestTemplate;
public class CutBackRefundCaller implements Runnable {
	
    private String url;
    private int no;
	private RestTemplate restTemplate;
	public CutBackRefundCaller(String url,int no,RestTemplate restTemplate) {
		super();
		this.url = url;
		this.no=no;
		this.restTemplate = restTemplate;
	}
	@Override
	public void run() {
		 System.out.println("---A端开始第"+no+"个HTTP调用B端服务开始执行扣款-----");
		try {
		   restTemplate.getForEntity(url+"/"+no, String.class);
		   System.out.println("---A端第+"+no+"个HTTP调用B端执行扣款成功收到返回结束-----");
		      try { 
			       TimeUnit.SECONDS.sleep(5);    // give u the time to try ctrl-C 
			    } catch (InterruptedException ie) { 
			        
			    } 
		      
		}catch(Exception e) {
			 e.printStackTrace();
			 System.out.println("---A端第+"+no+"个HTTP调用B端执行扣款失败-----");
		}
		
		
	}

}
