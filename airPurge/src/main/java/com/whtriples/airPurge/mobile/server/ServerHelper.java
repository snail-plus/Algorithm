package com.whtriples.airPurge.mobile.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;
import com.whtriples.airPurge.mobile.push.PushService;

public class ServerHelper {

	private static final Logger logger = LoggerFactory.getLogger(ServerHelper.class);

	private static int threadAmount = Runtime.getRuntime().availableProcessors() * 2;// 根据CPU取值

	public static ThreadPoolExecutor receivePoolExecutor = new ThreadPoolExecutor(0,threadAmount, 1000 * 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000), ThreadFac.getFac("Recieve-"),new ThreadPoolExecutor.CallerRunsPolicy());

	public static void sendHeartBeat(Channel channel) {
		JSONObject json = new JSONObject();
		json.put("reply", "heartbeat");
		json.put("state", "ok");
		json.put("msg", "ok");
		PushService.sendMessage(channel, json.toString());
	}

    public static void handerConn(Channel channel,JSONObject jsonObject){
    	logger.warn("处理连接请求");
		receivePoolExecutor.execute(new ConnThread(channel,jsonObject));
    }
    
	public static void handerData(Channel channel,JSONObject jsonObject) throws InterruptedException {
		//logger.warn("处理风机数据");
		receivePoolExecutor.execute(new ReceiveThread(channel,jsonObject));
	}
	
	public static void WebScoketData(JSONObject jsonObject, WebSocketSession session) throws InterruptedException {
		logger.warn("处理webscoket数据");
		receivePoolExecutor.execute(new WebScoketThread(jsonObject, session));
	}

}
