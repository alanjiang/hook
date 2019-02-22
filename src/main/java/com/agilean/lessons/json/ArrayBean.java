package com.agilean.lessons.json;

import com.agilean.lessons.tools.JacksonTool;

public class ArrayBean {
   
	private int[] counts;

	public int[] getCounts() {
		return counts;
	}

	public void setCounts(int[] counts) {
		this.counts = counts;
	}
	
	public static void main(String[] args) {
		
		String array="{\"counts\":[1,2,3]}";
		
		ArrayBean bean=JacksonTool.fromJsonToObject(array, ArrayBean.class);
		
		System.out.println("--length="+bean.getCounts().length);
	}
}
