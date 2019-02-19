package com.agilean.lessons.hook;

import java.util.concurrent.TimeUnit;

public class Demo {

	public static void main(String[] args) {
		Thread t1=new Thread() {

			@Override
			public void run() {
				try {
			    	 TimeUnit.SECONDS.sleep(2);
			     }catch(InterruptedException e) {
			    	 e.printStackTrace();
			     }
				System.out.println(">>>>>>t1<<<<<<");
			}
			
		};
		
         Thread t2=new Thread() {
        	 @Override
 			public void run() {
        		 try {
			    	 TimeUnit.SECONDS.sleep(2);
			     }catch(InterruptedException e) {
			    	 e.printStackTrace();
			     }
 				System.out.println(">>>>>>t2<<<<<<");
 			}
		};
		
		  Thread shutDownThread=new Thread() {
			  @Override
	 			public void run() {
				     try {
				    	 TimeUnit.SECONDS.sleep(3);
				     }catch(InterruptedException e) {
				    	 e.printStackTrace();
				     }
				    
	 				System.out.println(">>>>>>shutDownHook<<<<<<");
	 			}
			  
		  };

           Runtime.getRuntime().addShutdownHook(shutDownThread);
         
           t1.start();
           t2.start();
        	 
        		
	}

}
