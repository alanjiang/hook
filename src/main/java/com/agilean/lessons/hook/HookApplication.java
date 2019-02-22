package com.agilean.lessons.hook;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
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
import com.agilean.lessons.json.ArrayBean;
import com.agilean.lessons.tools.JacksonTool;
@SpringBootApplication
public class HookApplication {
	
	static List<Callable<ResultBean>> tasks=null;
	static List<ResultBean> resultList=null;
	static DecimalFormat df = new DecimalFormat("#.00");
	public static void main(String[] args) {
		
		ConfigurableApplicationContext ctx=SpringApplication.run(HookApplication.class, args);
		RestTemplate restTemplate=ctx.getBean(RestTemplate.class);
		ThreadPoolExecutor executor=ctx.getBean(ThreadPoolExecutor.class);
        
        
        String url=System.getProperty("url");
		String name=System.getProperty("name");
		String method=System.getProperty("method");
		String body=System.getProperty("body");
		System.out.println("---System.getProperty(\"counts\")="+System.getProperty("counts"));
		String countsArray="{\"counts\":"+System.getProperty("counts")+"}";
		ArrayBean arrayBean=JacksonTool.fromJsonToObject(countsArray, ArrayBean.class);//{"counts":[10,30,100,300,500,1000]}
		String logPath=System.getProperty("log");
		
		FileWriter  writer = null;
		try {
			writer = new FileWriter(logPath, true);
			
		}catch(IOException e) {
			System.out.println("---Error----"+e);
			System.exit(0);
		}
		writeHead(writer);
	for(int count:arrayBean.getCounts()) 
	{
		long beginTime=System.currentTimeMillis();
		System.out.println("--beginTime="+beginTime);
		//防止动态扩容
		tasks=new ArrayList<>(count+1);
		resultList=new ArrayList<>(count+1);
        System.out.println("--start presure test count="+count+"----");
        System.out.println("--params:"+url+","+name+","+method+","+body+","+logPath);
		CountDownLatch cdl=new CountDownLatch(count);
		//String url="https://tkb.agilean.cn/#/?viewId=e7b3f9f2a71040d08c9640ddc73b107b";
		
		//executor=(ThreadPoolExecutor) Executors.newFixedThreadPool(1);
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
		
		while(true) {
			
			if(executor.getActiveCount()==0) {
				System.out.println("Done");
				break;
			}
		}
		long endTime=System.currentTimeMillis();
		 System.out.println(">>>endTime="+endTime);
		//executor.shutdown();
		System.out.println("----"+url+" test finished, start write log-----");
		/*****start static the time********/
		long totalTime=(endTime-beginTime);
		AverageResultBean ab=new AverageResultBean();
		ab.setTotalTime(totalTime);
		double avgTime=totalTime/new Double(count).doubleValue();//ms
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
		successRatio=100*(successNum/new Double(count).doubleValue());
		failRatio=100*(failNum/new Double(count).doubleValue());
		ab.setTotalNum(count);
		qps=new Double(count/(totalTime/1000d)).intValue();
		ab.setQps(qps);
		
		concurrentNum=new Double(qps*avgTime/1000d).intValue();
		ab.setConcurrentNum(concurrentNum);
		ab.setSuccessNum(successNum);
		ab.setFailRatio(failRatio);
		ab.setSuccessRatio(successRatio);
		ab.setFailNum(failNum);
		ab.setName(name);
		ab.setUrl(url);
		/*****end static the time********/
		  stat(writer,resultList,ab);
		  System.out.println("----end of presure test for count="+count+"-----");
		}//end of for cycle 
		 System.out.println("----end of presure for url="+url+",program exit");
		 executor.shutdown();
		 try{writer.close();}catch(Exception e) {};
		 System.exit(0);
	}
	
	private static void writeHead(FileWriter writer) {
		
		try {
			
		
			writer.write("接口名称                  URL                                                      总请求数  总时长  平均耗时  失败数  失败率  成功数 成功率  QPS\r\t\n");
		}catch(IOException e) {
			  System.out.println("---WriteLog Error----");
		}
		
	}
	
    private static void stat(FileWriter writer,List<ResultBean> resultList,AverageResultBean ab)	 
    		
    {
		try {  
			/*writer.seek(writer.length());
			writer.writeUTF(" \r\t\n");*/
    	   resultList.forEach((b)->{
    		/*try {
    			writer.writeUTF(b.getName()+" "+b.getUrl()+" "+b.getHttpResCode()+" "+b.getResult()+" "+b.getTime()+"\r\n\t");
    		  }catch(IOException e) {
    			  System.out.println("---WriteLog Error----");
    		}*/
    		
    		
    	});
    	
    	try {
    		
		
			writer.write(ab.getName()+" "+ab.getUrl()+" "+ab.getTotalNum()+"  "+ab.getTotalTime()+"ms  "+df.format(ab.getAvgTime())+"ms  "+ab.getFailNum()+" "+df.format(ab.getFailRatio())+"% "+df.format(ab.getSuccessNum())+" "+df.format(ab.getSuccessRatio())+"% "+df.format(ab.getQps())+"/s\r\t\n");
		  }catch(IOException e) {
			  System.out.println("---WriteLog Error----");
		}
    	
    	
    	
		}catch(Exception e) {
			System.out.println("ERROR1:"+e);
		}
	}

}
