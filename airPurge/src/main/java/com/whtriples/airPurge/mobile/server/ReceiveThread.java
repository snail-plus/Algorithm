package com.whtriples.airPurge.mobile.server;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.BiMap;
import com.google.common.collect.Maps;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.base.model.Transducer;
import com.whtriples.airPurge.base.webSocket.SessionManager;
import com.whtriples.airPurge.cache.CacheMap;
import com.whtriples.airPurge.cache.DeviceCache;
import com.whtriples.airPurge.mobile.push.PushResources;
/**
 * 处理接收设备的数据
 * @author Administrator
 *
 */
public class ReceiveThread implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(ReceiveThread.class);
	private static Map<String, Date> map = Maps.newConcurrentMap();

	private JSONObject jsonObject;

	private Channel channel;
	
	public ReceiveThread(Channel channel,JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		this.channel = channel;
	}

	public void run() {
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");
			for (Object object : jsonArray) {
				Transducer transducer = new Transducer();
				JSONArray temp = (JSONArray) object;
				String device_guid = temp.get(0).toString();
				transducer.setDevice_guid(device_guid);
				//10分钟存储一次数据
				if (map.get(device_guid) == null) {
					map.put(device_guid, new Date());
					transducer.setSign(new Date());
				} else if ((new Date().getTime()) - map.get(device_guid).getTime() >= 1000 * 60 * 10) {
					map.put(device_guid, new Date());
					transducer.setSign(new Date());
				}
				transducer.setPm25(temp.get(2).toString());
				transducer.setPm10(temp.get(3).toString());
				transducer.setTemp(Double.parseDouble(temp.get(4).toString()));
				transducer.setHum(Double.parseDouble(temp.get(5).toString()));
				String ctrlmode = temp.get(36).toString();
				if("0".equals(ctrlmode)){
					transducer.setGear(temp.get(44).toString());
				}else{
					transducer.setGear(temp.get(35).toString());
				}
				transducer.setCtrlmode(ctrlmode);
				Device device = DeviceCache.getDeviceByGuid(device_guid.substring(0, 32));
				if(device == null || "0".equals(device.getStatus())){
					PushResources.removeChannel(this.channel);
					return;
				}
				//获取设备所在城市的天气状况
				Transducer localData = DeviceCache.getAqiByCityId(device.getCity_id());
				transducer.setCityName(device.getCity_name());
				transducer.setAqi(localData==null?"":localData.getPm25());
				transducer.setRun_state(temp.get(6).toString());
				//缓存设备数据
				CacheMap.cacheData.put(transducer.getDevice_guid(), JSONObject.toJSONString(transducer));
				transducer.setErr_state(temp.get(7).toString());
				transducer.setStatus(temp.get(46).toString());
                if(transducer.getSign() != null){
                	transducer.setRecord_time(new Date());
                	transducer.setComm_state(temp.get(8).toString());
                	D.insert(transducer);
                }

				BiMap<String, WebSocketSession> biMap = SessionManager.sessionMap.get(device_guid);
				if (biMap != null) {
					for (Map.Entry<String, WebSocketSession> scoketMap : biMap.entrySet()) {
						WebSocketSession ws = scoketMap.getValue();
						logger.info("ws: " + ws);
						if (ws != null) {
							if (ws.isOpen()) {
								try {
									//检查顶级设备是否被禁用
									Device validateDevice = DeviceCache.getDeviceByGuid(device_guid.substring(0, 32));
									if(validateDevice== null || "0".equals("validateDevice")){
										logger.warn("发送数据失败请检查设备是否被禁用，device_guid:" + device_guid.substring(0, 32));
										SessionManager.remove(ws);
										return;
									}
									TextMessage tem = new TextMessage(JSONObject.toJSONString(transducer).getBytes());
									logger.warn("发送webscoket数据:" + JSONObject.toJSONString(transducer));
									ws.sendMessage(tem);
								} catch (IOException e) {
									SessionManager.remove(ws);
									e.printStackTrace();
								}
							}

						}
					}
				}
			}
	}

}
