package com.agilean.lessons.hook.config;

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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
@Configuration
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
}
