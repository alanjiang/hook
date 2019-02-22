package com.agilean.lessons.hook;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class JVMShutDownHook implements Runnable {
	 private ThreadPoolExecutor threadPoolExecutor;
	 private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
	 public JVMShutDownHook(ThreadPoolExecutor threadPoolExecutor,PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
		 this.threadPoolExecutor=threadPoolExecutor;
		 this.poolingHttpClientConnectionManager=poolingHttpClientConnectionManager;
		 Runtime.getRuntime().addShutdownHook(new Thread(this)); 
	 }
     public JVMShutDownHook() {
  
     }
     //when jvm is killed, execute the logic
	@Override
	public void run() {
            System.out.println("--JVM退出前维持线程池状态----"); 
            while(true) {
            	try{TimeUnit.SECONDS.sleep(3);}catch(Exception e) {};
        		if(poolingHttpClientConnectionManager.getTotalStats().getLeased()==0 && threadPoolExecutor.getActiveCount()==0) {
        			System.out.println("---Http线程池开始释放---");
            		break;
        		}
        	}

            	threadPoolExecutor.shutdown();
            	poolingHttpClientConnectionManager.shutdown();
            
             System.out.println("--JVM钩子函数执行完毕----"); 
        } 
         
           
        
  
}
