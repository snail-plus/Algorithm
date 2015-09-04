package com.whtriples.airPurge.util;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils {
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static <T> T readValue(String json,Class<T> valueType){
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
			objectMapper.setDateFormat(formatter);
			return objectMapper.readValue(json, valueType);
		} catch (Exception e) {
           e.printStackTrace();
           return null;
		}
	}
	
	
	public static String writeValue(Object bean){
		try {
			 
			return objectMapper.writeValueAsString(bean);
		} catch (Exception e) {
           e.printStackTrace();
           return null;
		}
	}
	
	
}
