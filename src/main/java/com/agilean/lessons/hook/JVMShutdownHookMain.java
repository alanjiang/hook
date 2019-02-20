package com.agilean.lessons.hook;

public class JVMShutdownHookMain {

	public static void main(String[] args) {
		 new JVMShutDownHook(); 
        
        System.out.println(">>> Sleeping for 5 seconds, try ctrl-C now if you like."); 
         
        try { 
            Thread.sleep(15000);     // (-: give u the time to try ctrl-C 
        } catch (InterruptedException ie) { 
            ie.printStackTrace(); 
        } 
         
        System.out.println(">>> Slept for 10 seconds and the main thread exited."); 

	}

}
