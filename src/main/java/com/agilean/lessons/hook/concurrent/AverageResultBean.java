package com.agilean.lessons.hook.concurrent;

public class AverageResultBean {
   private double avgTime;//ms
   private long totalTime;//ms
   private long totalNum;//模拟100人请求
   private double qps;//qunatity per second
   private int concurrentNum;//并发数
   private int successNum;//成功数
   private int failNum;//失败数
   private double successRatio;//成功率
   private double failRatio;
   
   private String name;
   
   private String url;
   /*
    * 每秒处理请求数： QPS=totalNum/(totalTime/1000)
    * 并发数= QPS*平均响应时间 : concurrentNum=QPS*(avgTime/1000)
    * 平均响应时间为 = (totalTime/1000)/totalNum
    */

public double getAvgTime() {
	return avgTime;
}

public void setAvgTime(double avgTime) {
	this.avgTime = avgTime;
}

public long getTotalTime() {
	return totalTime;
}

public void setTotalTime(long totalTime) {
	this.totalTime = totalTime;
}

public long getTotalNum() {
	return totalNum;
}

public void setTotalNum(long totalNum) {
	this.totalNum = totalNum;
}

public double getQps() {
	return qps;
}

public void setQps(double qps) {
	this.qps = qps;
}

public int getConcurrentNum() {
	return concurrentNum;
}

public void setConcurrentNum(int concurrentNum) {
	this.concurrentNum = concurrentNum;
}

public int getSuccessNum() {
	return successNum;
}

public void setSuccessNum(int successNum) {
	this.successNum = successNum;
}

public int getFailNum() {
	return failNum;
}

public void setFailNum(int failNum) {
	this.failNum = failNum;
}



public double getSuccessRatio() {
	return successRatio;
}

public void setSuccessRatio(double successRatio) {
	this.successRatio = successRatio;
}

public double getFailRatio() {
	return failRatio;
}

public void setFailRatio(double failRatio) {
	this.failRatio = failRatio;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}


   
   
   
}
