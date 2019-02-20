package com.agilean.lessons.hook;

public class JVMShutDownHook implements Runnable {
     public JVMShutDownHook() {
    	 Runtime.getRuntime().addShutdownHook(new Thread(this)); 
         System.out.println(">>> shutdown hook registered"); 
     }
	@Override
	public void run() {
		
            System.out.println("/n>>> About to execute: " + JVMShutDownHook.class.getName() + ".run() to clean up before JVM exits."); 
            this.cleanUp(); 
            System.out.println(">>> Finished execution: " + JVMShutDownHook.class.getName() + ".run()"); 
        } 
         
           
        private void cleanUp() { 
            for(int i=0; i < 7; i++) { 
                System.out.println(i); 
            } 
         
	}

}
