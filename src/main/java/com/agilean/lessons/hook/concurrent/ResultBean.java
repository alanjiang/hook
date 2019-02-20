package com.agilean.lessons.hook.concurrent;

public class ResultBean {
   
	private int httpResCode;//200,404
	private String result;//fail/success
	private long time;
    private String url;
    private String name;
	public int getHttpResCode() {
		return httpResCode;
	}
	public void setHttpResCode(int httpResCode) {
		this.httpResCode = httpResCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
	
	
}
