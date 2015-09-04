package com.whtriples.airPurge.cache;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.whtriples.airPurge.redis.JedisTemplate;
import com.whtriples.airPurge.util.ConfigUtil;
import com.whtriples.airPurge.util.DateUtil;


public class CaptchaCache {
	
	private static final String KEY_PROFIX = "sms_capter";
	
	//缓存时间(s)：当天的23点59分59秒-当前时间
	private static final Integer seconds = (int) ((DateUtil.getTodayNight().getTime()-new Date().getTime())/1000);
	
	private static final Integer effect_time = 5*60*1000;//有效时间：5分钟
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String SEND_SMS = ConfigUtil.getConfig("sendSms");
	
	private static JedisTemplate jedisTemplate;
	
	public static void init(JedisTemplate jedisTemplate) {
		CaptchaCache.jedisTemplate = jedisTemplate;
	}
	
	public static String get(String key) {
		String context = jedisTemplate.get(KEY_PROFIX+key);
		return context;
	}
	
	public static void set(String key, String value){
		jedisTemplate.setex(KEY_PROFIX+key, value, seconds);
	}
	
	
	//验证码校验
	public static boolean checkCaptcha(String mobileNo,String inputCaptcha,String type){
		if(Integer.valueOf(SEND_SMS) == 1){
			 String key = mobileNo+"_"+type;
			 String value = CaptchaCache.get(key);
			 if(value != null && value != ""){
				String ymdHms = value.substring(6,20);
				String dateStr = ymdHms.substring(0,4)+"-"+ymdHms.substring(4,6)+ "-"+ymdHms.substring(6,8)+" "
				+ymdHms.substring(8,10)+":"+ymdHms.substring(10,12)+":"+ymdHms.substring(12);
				Date lastTime;
				try {
					lastTime = df.parse(dateStr);
				} catch (ParseException e) {
					return false;
				}
				if((new Date().getTime() - lastTime.getTime()) < effect_time){
					 String captcha = value.substring(0,6);
					 return captcha.equals(inputCaptcha);
				}else{
					 return false;
				}
			 }else{
				 return false;
			 }
		}else{
			return "123456".equals(inputCaptcha);
		}
	}
	 
}
