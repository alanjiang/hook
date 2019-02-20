package com.agilean.lessons.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.agilean.lessons.hook.JVMShutDownHook;

@SpringBootApplication
@ServletComponentScan(value= "com.agilean.lessons.listeners")
public class JVMDaemonListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println(">>>>>>contextInitialized<<<<");

         
        try { 
            Thread.sleep(35000);     // (-: give u the time to try ctrl-C 
        } catch (InterruptedException ie) { 
            ie.printStackTrace(); 
        } 
         
        System.out.println(">>>thread exited."); 

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	

}
