package com.whtriples.airPurge.mobile.server;

import org.jboss.netty.channel.Channel;

import com.alibaba.fastjson.JSONObject;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.cache.DeviceCache;
import com.whtriples.airPurge.mobile.push.PushResources;
import com.whtriples.airPurge.mobile.push.PushService;

public class ConnThread implements Runnable{

	private JSONObject jsonObject;
	
	private Channel channel;
	
	public ConnThread(Channel channel, JSONObject jsonData){
		this.jsonObject = jsonData;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		Object object = jsonObject.get("user");
		//设备验证
		Device device = DeviceCache.getDeviceByGuid(object.toString());
		JSONObject json = new JSONObject();
		if("0".equals(device.getStatus()) || device == null){
			json.put("reply", "connect");
			json.put("state", "error");
			json.put("msg", "login fail,device not exist");
			PushService.sendMessage(channel, json.toString());
			channel.close();
			return;
		}
		boolean result = PushResources.addChannel(object.toString(), channel);
		if(result){
			json.put("reply", "connect");
			json.put("state", "ok");
			json.put("msg", "login success");
		}
		PushService.sendMessage(channel, json.toString());
	}

}
