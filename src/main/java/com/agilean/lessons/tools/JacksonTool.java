package com.agilean.lessons.tools;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
public class JacksonTool {
		
		private static final Log log = LogFactory.getLog(JacksonTool.class);
		private  final static ObjectMapper mapper = new ObjectMapper(); 
	    static{
	    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    }
		public static <T> T fromJsonToObject(String json,Class<T> clazz){
			try{
				mapper.setSerializationInclusion(Include.NON_NULL);
			    return mapper.readValue(json, clazz); 
			}catch(IOException e)
			{
				e.printStackTrace();	
				return null;
			}
		 }
		
		 public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) 
		 {   
			     return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
	     }   
	
	     public static String  fromObjectToJson(Object o){ 
	    	 try{
	    	  mapper.setSerializationInclusion(Include.NON_NULL);  
	    	  return mapper.writeValueAsString(o);  
	    	 }catch(Exception e)
	    	 {
	    		 e.printStackTrace();
	    	 }
	    	 return null;
	     }    
}
