package com.agilean.lessons.hook;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.agilean.lessons.hook.concurrent.AverageResultBean;
import com.agilean.lessons.hook.concurrent.CompleteReminderer;
import com.agilean.lessons.hook.concurrent.HttpCaller;
import com.agilean.lessons.hook.concurrent.ResultBean;
@SpringBootApplication
public class HookApplication {
	static ThreadPoolExecutor executor;
	static List<Callable<ResultBean>> tasks=null;
	static List<ResultBean> resultList=null;
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx=SpringApplication.run(HookApplication.class, args);
		RestTemplate restTemplate=ctx.getBean(RestTemplate.class);
        if(args==null || args.length<6) {
			
			System.out.println("---Parameters Error----");
			System.exit(0);
		}
 
        long beginTime=System.currentTimeMillis();
        System.out.println(">>>beginTime="+beginTime);
        String url=args[0];
		String name=args[1];
		String method=args[2];
		String body=args[3];
		int count=Integer.parseInt(args[4]);
		String logPath=args[5];
		Path path = Paths.get(logPath);
		//防止动态扩容
		tasks=new ArrayList<>(count+1);
		resultList=new ArrayList<>(count+1);
		RandomAccessFile writer = null;
		
		try {
			writer = new RandomAccessFile(logPath, "rw");
		}catch(IOException e) {
			System.out.println("---Error----"+e);
			System.exit(0);
		}
        System.out.println("--start presure test----");
        System.out.println("--params:"+url+","+name+","+method+","+body+","+logPath);
		CountDownLatch cdl=new CountDownLatch(count);
		//String url="https://tkb.agilean.cn/#/?viewId=e7b3f9f2a71040d08c9640ddc73b107b";
		executor=(ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		for(int i=0;i<count;i++) {
			 
			 HttpCaller caller=new HttpCaller(url,name,method,
					 body,restTemplate,cdl);
			 tasks.add(caller);
		}
		
		CompleteReminderer r=new CompleteReminderer(cdl);
		tasks.add(r);
		
		try{
			
			List<Future<ResultBean>> results=executor.invokeAll(tasks);
			for(Future<ResultBean> resultBean: results) {
				try {
					resultList.add(resultBean.get());
				}catch(Exception e) {
					System.out.println("---Execute Error----");
					System.exit(0);
				}
				
			}
			tasks.clear();
			results.clear();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		/*while(true) {
			
			if(executor.getActiveCount()==0) {
				System.out.println("Done");
				break;
			}
		}*/
		long endTime=System.currentTimeMillis();
		 System.out.println(">>>endTime="+endTime);
		executor.shutdown();
		System.out.println("----"+url+" test finished, start write log-----");
		/*****start static the time********/
		long totalTime=(endTime-beginTime);
		AverageResultBean ab=new AverageResultBean();
		ab.setTotalTime(totalTime);
		double avgTime=totalTime/count;//ms
		ab.setAvgTime(avgTime);
		int successNum=0,failNum=0;
		double successRatio=0d,failRatio=0d,qps=0d;
		int concurrentNum=0;
		for(ResultBean rb:resultList) {
			if("REMINDER".equalsIgnoreCase(rb.getResult())) {
				//keep it
			}
			else if("SUCCESS".equalsIgnoreCase(rb.getResult())){
				successNum++;
			}else {
				failNum++;
			}
		}
		successRatio=100*(successNum/count);
		failRatio=100*(failNum/count);
		ab.setTotalNum(count);
		qps=new Long(count/(totalTime/1000)).intValue();
		ab.setQps(qps);
		concurrentNum=new Double(qps*(avgTime/1000)).intValue();
		ab.setConcurrentNum(concurrentNum);
		ab.setSuccessNum(successNum);
		ab.setFailRatio(failRatio);
		ab.setSuccessRatio(successRatio);
		ab.setFailNum(failNum);
		ab.setName(name);
		ab.setUrl(url);
		/*****end static the time********/
		writeHead(writer);
		stat(writer,resultList,ab);
		System.out.println("----end of presure test-----");
		System.exit(0);
	}
	
	private static void writeHead(RandomAccessFile writer) {
		
		try {
			writer.writeUTF("接口名称  URL                                                总请求数  总时长  平均耗时  并发量   失败数  失败率  成功数 成功率  QPS\r\t\n");
		}catch(IOException e) {
			  System.out.println("---WriteLog Error----");
		}
		
	}
	
    private static void stat(RandomAccessFile writer,List<ResultBean> resultList,AverageResultBean ab)	 
    		
    {
		try {  
    	resultList.forEach((b)->{
    		try {
    			writer.writeUTF(b.getName()+" "+b.getUrl()+" "+b.getHttpResCode()+" "+b.getResult()+" "+b.getTime()+"\r\n\t");
    		  }catch(IOException e) {
    			  System.out.println("---WriteLog Error----");
    		}
    		
    		
    	});
    	
    	try {
    	
			writer.writeUTF(ab.getName()+" "+ab.getUrl()+" "+ab.getTotalNum()+"  "+ab.getTotalTime()+"ms  "+ab.getAvgTime()+"ms "+ab.getConcurrentNum()+" "+ab.getFailNum()+" "+ab.getFailRatio()+"% "+ab.getSuccessNum()+" "+ab.getSuccessRatio()+"% "+ab.getQps()+"/s\r\t\n");
		  }catch(IOException e) {
			  System.out.println("---WriteLog Error----");
		}
    	
    	
    	
		}catch(Exception e) {
			System.out.println("ERROR1:"+e);
		}finally {
			if(writer!=null) {
				try{
					writer.close();
				}catch(Exception e) {
					System.out.println("ERROR2:"+e);
				}
			}
		}
	}

}
