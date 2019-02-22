package com.agilean.lessons.hook.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
@Configuration
@EnableScheduling
@PropertySource("classpath:/application.properties")
@ComponentScan(value= {"com.agilean.lessons"})
public class CommonConfig {
	
	   @Autowired
	   Environment env;
	 /****start Http Client *****/ 
	   
	   @Bean
	   PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
		   PoolingHttpClientConnectionManager poolHttpManager=new PoolingHttpClientConnectionManager();
			poolHttpManager.setMaxTotal(Integer.valueOf(env.getProperty("http.pool.connection.max").trim()));
			poolHttpManager.setDefaultMaxPerRoute(Integer.valueOf(env.getProperty("http.route.per.max").trim()));
			//validateAfterInactivity 空闲永久连接检查间隔，这个牵扯的还比较多
	        //官方推荐使用这个来检查永久链接的可用性，而不推荐每次请求的时候才去检查        
			poolHttpManager.setValidateAfterInactivity(2000);
			 //CloseableHttpClient httpClient=null;
			
			return poolHttpManager;
	   }
	   
	   @Bean 
	   ConnectionKeepAliveStrategy keepAliveStrategy() {
		    
		    	return (HttpResponse response, HttpContext context)->{
		               HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		           while (it.hasNext()) {
		                HeaderElement he = it.nextElement();
		                String param = he.getName();
		                String value = he.getValue();
		                if (value != null && param.equalsIgnoreCase("timeout")){
		                    return Long.parseLong(value) * 1000;
		                }
		            }
		                return Integer.valueOf(env.getProperty("http.keep.alive.time").trim());
		    	  };   
	   }
	   
	   @Bean
	   RequestConfig requestConfig() {
		   
		   return RequestConfig.custom()  
		            .setConnectionRequestTimeout(Integer.valueOf(env.getProperty("http.requst.timeout").trim()) )  
		            .setConnectTimeout(Integer.valueOf(env.getProperty("http.connection.timeout").trim()))  
		            .setSocketTimeout(Integer.valueOf(env.getProperty("http.socket.timeout").trim()))  
		            .build();  
	   }
	   
	   @Bean
	   CloseableHttpClient httpClient(RequestConfig requestConfig,PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,ConnectionKeepAliveStrategy keepAliveStrategy) {
	      
	       return HttpClients.custom()
	               .setDefaultRequestConfig(requestConfig)
	               .setConnectionManager(poolingHttpClientConnectionManager)
	               .setKeepAliveStrategy(keepAliveStrategy)
	               .build();
	   
	   
	   }
	   
	   @Bean
	   HttpComponentsClientHttpRequestFactory clientHttpRequestFactory ( CloseableHttpClient httpClient) {
		   
	       return new HttpComponentsClientHttpRequestFactory(httpClient);
	   }
	  
	   @Bean
	   public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory clientHttpRequestFactory) {
	       RestTemplate restTemplate = new RestTemplate();
	       restTemplate.setRequestFactory(clientHttpRequestFactory);
	       restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
	     
	       return restTemplate;
	   }
	   
	   /****end Http Client *****/ 
	   
	    @Bean
	    public ThreadPoolExecutor threadPoolExecutor () {
	    	ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 100, 10, 
	    			                                              TimeUnit.SECONDS, 
	    	
	    			                                              new LinkedBlockingDeque<>(1000));
	    	
	        return executor;

	
	   
	   /*******Thread Pool***********/
	   
	    }
	   
	    
	    
	    
	    
	    
	    
	    
	    
        private void monitorThreads() {
        	
        	ThreadGroup tg=getRootThreadGroup();
        	List<String> ts=getThreadsName(tg);
        	ts.forEach((name)->{
        		
        		System.out.println(">>>>name="+name);
        		
        	});
        	
        }
            private  ThreadGroup getRootThreadGroup() 
            {
        	   ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        	   while (true) 
        	   {
        	       if (rootGroup.getParent() != null) {
        	            rootGroup = rootGroup.getParent();
        	       } else {
        	        break;
        	    }
        	}
        	    return rootGroup;
        	}
            
            public  List<String> getThreadsName(ThreadGroup group) 
            {
            	List<String> threadList = new ArrayList<String>();
            	Thread[] threads = new Thread[group.activeCount()];
            	int count = group.enumerate(threads, false);
            	for (int i = 0; i < count; i++) {
            	threadList.add(group.getName() + "线程组: " + threads[i].getName());
            	}
            	return threadList;
            	}
	   
}
