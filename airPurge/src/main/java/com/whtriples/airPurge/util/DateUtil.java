package com.whtriples.airPurge.util;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {
	
	public static Date addDays(Date date,Integer addNum){
		DateTime dateTime = new DateTime(date);
		return dateTime.plusDays(addNum).toDate();
	}
	
	public static Date string2Date(String timeStr){
		DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd");
		DateTime dateTime = DateTime.parse(timeStr, format);
		return dateTime.toDate();
	}
	
	public static Date getFirstDayOfWeek(){
		DateTime dateTime = new DateTime(new Date()).dayOfWeek().withMinimumValue();
		return dateTime.toDate();
	}
	
	public static Date getLastDayOfWeek(){
		DateTime dateTime = new DateTime(new Date()).dayOfWeek().withMaximumValue();
		return dateTime.toDate();
	}
	
	public static Date getFirstDayOfMonth(){
		DateTime dateTime = new DateTime(new Date()).dayOfMonth().withMinimumValue();
		return dateTime.toDate();
	}
	
	public static Date getLastDayOfMonth(){
		DateTime dateTime = new DateTime(new Date()).dayOfMonth().withMaximumValue();
		return dateTime.toDate();
	}
	
	//获取当前日期的凌晨0点时间
	public static Date getTodayMorning(){
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(new Date());
		  calendar.set(Calendar.HOUR_OF_DAY, 0);
		  calendar.set(Calendar.MINUTE, 0);
		  calendar.set(Calendar.SECOND, 0);
		  return calendar.getTime();
	}
	
	//获取当前日期的晚上23点时间
	public static Date getTodayNight(){
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(new Date());
		  calendar.set(Calendar.HOUR_OF_DAY, 23);
		  calendar.set(Calendar.MINUTE, 59);
		  calendar.set(Calendar.SECOND, 59);
		  return calendar.getTime();
	}
	
	//获取本周第一天
	public static Date getWeekFirstDay(){
		  Calendar calendar = Calendar.getInstance();
		  int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		  if (dayofweek == 0)
		   dayofweek = 7;
		  calendar.add(Calendar.DATE, -dayofweek + 1);
		  calendar.set(Calendar.HOUR_OF_DAY, 0);
		  calendar.set(Calendar.MINUTE, 0);
		  calendar.set(Calendar.SECOND, 0);
		  return calendar.getTime();
	}
	
	//获取本周最后一天
	public static Date getWeekLastDay(){
		 Calendar calendar = Calendar.getInstance();
		  int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		  if (dayofweek == 0)
		   dayofweek = 7;
		  calendar.add(Calendar.DATE, -dayofweek + 7);
		  calendar.set(Calendar.HOUR_OF_DAY, 23);
		  calendar.set(Calendar.MINUTE, 59);
		  calendar.set(Calendar.SECOND, 59);
		  return calendar.getTime();
	}
	
	//获取当前月第一天
	public static Date getMonthFirstDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
	     return calendar.getTime();
	}
	
	//获取当前月最后一天
	public static Date getMonthLastDay(){
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
	     return calendar.getTime();
	}
	
	//获取上月第一天
	public static Date getPrevMonthFirstDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
	     return calendar.getTime();
	}
	
	//获取上月最后一天
	public static Date getPrevMonthLastDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
	    return calendar.getTime();
	}
	
	//获取最近三个月最早那一月份的第一天
	public static Date getThreeMonthFirstDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		return calendar.getTime();
	}
	
	public static void main(String[] args) {
		System.out.println(addDays(getFirstDayOfWeek(),2));
	}
}
