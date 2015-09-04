package com.whtriples.airPurge.mobile.server;

import java.io.IOException;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.base.webSocket.SessionManager;
import com.whtriples.airPurge.cache.CacheMap;
import com.whtriples.airPurge.cache.DeviceCache;
import com.whtriples.airPurge.mobile.push.PushResources;
import com.whtriples.airPurge.mobile.push.PushService;
import com.whtriples.airPurge.util.Collections3;

public class WebScoketThread implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(WebScoketThread.class);

	private JSONObject jsonObject;
	private WebSocketSession session;

	public WebScoketThread(JSONObject jsonObject, WebSocketSession session) {
		this.jsonObject = jsonObject;
		this.session = session;
	}

	public void run() {
		Object object = jsonObject.get("cmd");
		if(object != null){
			switch (object.toString()) {
			case "connect": // 请求连接
				Device deviceByGuid = DeviceCache.getDeviceByGuid(jsonObject.get("device_guid").toString());
				if(null == deviceByGuid || "0".equals(deviceByGuid.getStatus())){
					try {
						session.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				SessionManager.addSocketSession(session, jsonObject.toString());
				Map<String, String> cachedata = CacheMap.cacheData;
				if(Collections3.isEmpty(cachedata)){
					return;
				}
				String deviceData = cachedata.get(deviceByGuid.getDevice_guid()).toString();
				if(deviceData != null){
					System.out.println("deviceData" + deviceData);
					TextMessage tm = new TextMessage(deviceData.getBytes());
					try {
						session.sendMessage(tm);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			case "control": // 手机控制指令
				Map<String, Object> map = session.getAttributes();
				String key = map.get("device_guid").toString();
				logger.warn("PushResources.getAllChannelMap()" + PushResources.getAllChannelMap());
				Channel channel = PushResources.getAllChannelMap().get(key.substring(0, 32));
				logger.warn("发送控制指令jsonObject:" + jsonObject.toString());
				PushService.sendMessage(channel, jsonObject.toString());
				//记录操作日志
				ServerHelper.receivePoolExecutor.execute(new LogThread(jsonObject,map));
			default:
				break;
			}
		}else{
			try {
				logger.warn("close session:" + session);
				session.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

	
	public static void main(String[] args) {
		System.out.println(("87498fb23dee40a0a94acea618707a31_0_1".substring(0,32)));
	}
}
