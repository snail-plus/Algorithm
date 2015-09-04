package com.whtriples.airPurge.jmx;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;
import com.whtriples.airPurge.util.ConfigUtil;



public class AnnotationTestBean {

    public static final String CONFIG_FILE = "application.properties";
  
     private Map<String,String> propsMap;
    
	  private static Properties props = new Properties();

	    static {
	        loadProperty();
	    }

	    /**
	     * 从配置文件中读取所有的属性
	     */
	    private static void loadProperty() {
	        try {
	            InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
	            props.load(in);
	        } catch (Exception e) {
	        }
	    }

		public Map<String,String> getPropsMap() {
			return propsMap;
		}

		public void setPropsMap(Map<String,String> propsMap) {
			this.propsMap = propsMap;
		}
	    
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		public void get() {
	         Map<String,String> propsMap4 = Maps.newHashMap();

	    	Iterator<?> it=props.entrySet().iterator();
	    	while(it.hasNext()){
	    	    Map.Entry<String,String> entry=(Map.Entry)it.next();
	    	    propsMap4.put(entry.getKey(), entry.getValue());
	    	}
	    	this.setPropsMap(propsMap4);
	    }
	    

	   

	   
	




}
