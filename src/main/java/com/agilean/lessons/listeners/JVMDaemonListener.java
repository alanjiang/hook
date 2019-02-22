package com.agilean.lessons.listeners;

import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.agilean.lessons.hook.JVMShutDownHook;

@SpringBootApplication
@ServletComponentScan(value= "com.agilean.lessons.listeners")
public class JVMDaemonListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		ThreadPoolExecutor threadPoolExecutor=ctx.getBean(ThreadPoolExecutor.class);
		PoolingHttpClientConnectionManager httpPool=ctx.getBean(PoolingHttpClientConnectionManager.class);
		
		System.out.println("--web 监听器启动---");

		 new JVMShutDownHook(threadPoolExecutor,httpPool); 
        
         System.out.println("--JVM 钩子函数注册成功---"); 

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	

}
