package com.whtriples.airPurge.mobile.server;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.Log;

class LogThread implements Runnable{

	private JSONObject jsonObject;
	private Map<String, Object> map;
	
	public LogThread(JSONObject jsonObject,Map<String, Object> map){
		this.jsonObject = jsonObject;
		this.map = map;
	}
	
	@Override
	public void run() {
		Log log = new Log();
		log.setRecord_time(new Date());
		log.setDevice_guid(map.get("device_guid").toString());
		log.setUser_id(Integer.parseInt(map.get("user_id").toString()));
		log.setOperation(jsonObject.toJSONString());
		D.insert(log);
	}
	
}