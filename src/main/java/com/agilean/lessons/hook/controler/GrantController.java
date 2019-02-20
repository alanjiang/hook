package com.agilean.lessons.hook.controler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agilean.lessons.hook.JVMShutDownHook;
import com.agilean.lessons.json.BaseMessage;



@Controller
public class GrantController {
	protected final static  Logger log = LogManager.getLogger(GrantController.class);
	
	@RequestMapping(value= "/greeting",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	public @ResponseBody BaseMessage greeting( Model m){
		 
		BaseMessage res=new BaseMessage();
	    res.setResCode("0");
	    res.setResMsg("success");
	    String result="HHHHHHHHHHHHHHHHHHHHHHHHHHHH"
	    		+ "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"
	    		+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
	    log.info(result);
	    new JVMShutDownHook(); 
	    try { 
	        Thread.sleep(35000);     // give u the time to try ctrl-C 
	    } catch (InterruptedException ie) { 
	        ie.printStackTrace(); 
	    } 
	            System.out.println(">>> Sleeping for 35 seconds, try ctrl-C now if you like."); 
	    return res;
	}
}
