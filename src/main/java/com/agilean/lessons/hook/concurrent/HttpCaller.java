package com.agilean.lessons.hook.concurrent;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.agilean.lessons.constants.Constant;
public class HttpCaller implements Callable<ResultBean> {
	private CountDownLatch  countDownLatch;
    private String url;
    private String name;
	private String method;
	private String body;
	private RestTemplate restTemplate;
	
	
	
	public HttpCaller(String url, String name,String method,String body,
			RestTemplate restTemplate,CountDownLatch  countDownLatch) {
		super();
		this.url = url;
		this.name=name;
		this.method=method;
		this.body=body;
		this.restTemplate = restTemplate;
		this.countDownLatch =countDownLatch;
	}



	@Override
	public ResultBean call() {
		ResultBean rb=new ResultBean();
		try {
			long start=System.currentTimeMillis();
			HttpHeaders requestHeaders = new HttpHeaders();
		    requestHeaders.add("agilean-token", Constant.Token_Agilean);
		    HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);
		    ResponseEntity<String> res=null;
		    if("GET".equalsIgnoreCase(method)) {
		    	res=restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		        System.out.println("--res="+res);
		    }
		    if(res!=null)
		    {
		    	rb.setHttpResCode(res.getStatusCodeValue());
		    	if(res.getStatusCodeValue()==200) {
		    		rb.setResult("SUCCESS");
		    	}else {
		    		rb.setResult("FAIL");
		    	}
		    	
		    }else {
		    	rb.setHttpResCode(300);
		    	rb.setResult("ERROR");
		    }
			long end=System.currentTimeMillis();
			long time=(end-start);
			rb.setTime(time);
			rb.setName(name);
			rb.setUrl(url);
			
		}catch(Exception e) {
			 e.printStackTrace();
			 rb.setResult("ERROR");
		}finally {
			this.countDownLatch.countDown();
			//System.out.println(">>>>>"+this.countDownLatch.getCount());
		}
		//System.out.println(">>>>>HttpCaller finished<<<<<");
		
		return rb;
	}

}
