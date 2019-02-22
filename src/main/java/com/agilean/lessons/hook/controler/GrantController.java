package com.agilean.lessons.hook.controler;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.agilean.lessons.json.BaseMessage;

@Controller
public class GrantController {
	protected final static  Logger log = LogManager.getLogger(GrantController.class);
	@RequestMapping(value= "/fund/cutback/{no}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	public @ResponseBody BaseMessage greeting( Model m,@PathVariable int no){
		BaseMessage res=new BaseMessage();
	    res.setResCode("0");
	    res.setResMsg("success");
	    System.out.println("--- B端银行服务执行第"+no+"笔扣款请求-----");
	    try { 
	    System.out.println("--- B端银行执行第"+no+"笔扣款中-----");
	       TimeUnit.SECONDS.sleep(5);    // give u the time to try ctrl-C 
	    } catch (InterruptedException ie) { 
	        ie.printStackTrace(); 
	    } 
	    System.out.println("---B端银行服务第"+no+"笔扣款成功并返回响应-----");
	    return res;
	}
}
