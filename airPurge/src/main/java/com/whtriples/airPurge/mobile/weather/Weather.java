package com.whtriples.airPurge.mobile.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.whtriples.airPurge.util.ConfigUtil;

public class Weather {

	private static final Logger logger = LoggerFactory.getLogger(Weather.class);
	
	public static final String CHARSET = "UTF-8";
	public static final String COMMON_KEY = ConfigUtil.getConfig("common_key");// 申请的对应key
	public static final String WEATHER_TODAY = ConfigUtil.getConfig("weather_url");
	public static final String LOCATION_URL =  ConfigUtil.getConfig("location_url")+ "?output=json&key="+ COMMON_KEY +"&location=";
	
	public static void main(String[] args) throws Exception {
		JSONObject aqiByCityName = getAqiByCityName("wuhan");
		System.out.println("now: " + aqiByCityName.get("now"));
		System.out.println(JSONObject.parseObject(aqiByCityName.getString("aqi")).get("city"));
		 }
	
	
	public static JSONObject getAqiByCityName(String cityName) throws Exception{
		ConfigUtil.getConfig("weather.today");
		Map<String,String> parameterMap = Maps.newHashMap();
		parameterMap.put("city", cityName);
		String doPost = doPost(WEATHER_TODAY, parameterMap);
		logger.warn("result: " + doPost);
		JSONArray result = JSONObject.parseArray((JSONObject.parseObject(doPost).get("HeWeather data service 3.0").toString()));
		if(result == null){
			return null;
		}
		return result.getJSONObject(0);
	} 
	
	public static String getCityName(String location) throws Exception{
		if(StringUtils.isEmpty(location)){
			throw new RuntimeException("经纬度为空");
		}
		String urlAll = new StringBuffer(LOCATION_URL).append(location.trim()).toString();
		String locationResult = doGet(urlAll);
		JSONObject cityObject = JSONObject.parseObject(locationResult);//转化为JSON类
		System.out.println(cityObject);
		JSONObject jsonObject = cityObject.getJSONObject("result").getJSONObject("addressComponent");
		String cityName = jsonObject.getString("province")+"," +jsonObject.getString("city");
		return cityName;
	}
	
	public static String doPost(String url, Map<String,String> parameterMap) throws Exception  {
        
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator<String> iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String)iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String)parameterMap.get(key);
                } else {
                    value = "";
                }
                
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
            
            //logger.warn("parameterBuffer: " + parameterBuffer);
        }
        URL localURL = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) localURL.openConnection();
		connection.setRequestProperty("apikey",  COMMON_KEY);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", CHARSET);
        connection.setRequestProperty("charsert", CHARSET);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));
        
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        try {
            outputStream = connection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream,CHARSET);
            
            outputStreamWriter.write(parameterBuffer.toString());
            outputStreamWriter.flush();
            
            if (connection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
            }
            
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            
            if (outputStream != null) {
                outputStream.close();
            }
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }

        return resultBuffer.toString();
    }
	
	public static String doGet(String url) throws Exception {
		URL localURL = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) localURL.openConnection();
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(5000);
		connection.setRequestMethod("GET");
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		if (connection.getResponseCode() >= 300) {
			throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
		}
		try {
			inputStream = connection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);
			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return resultBuffer.toString();
	}

	public static String buildParameter(String requestURL,Map<String,String> parameterMap){
		StringBuffer parameterBuffer = new StringBuffer(requestURL);
		if (parameterMap != null) {
            Iterator<String> iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String)iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String)parameterMap.get(key);
                } else {
                    value = "";
                }
                
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
		
		return parameterBuffer.toString();
	}
	
	
}
