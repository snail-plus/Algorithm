package com.whtriples.airPurge.mobile.push;

import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.cache.DeviceCache;
import com.whtriples.airPurge.mobile.server.ServerHelper;
import com.whtriples.airPurge.util.Constant;

/**
 * 业务处理类
 * @author Administrator
 *
 */
public class ServerReaderHandler extends SimpleChannelHandler {

	private static final Logger logger = LoggerFactory.getLogger(ServerReaderHandler.class);

	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.warn("-----------------channelConnected----------- : " + e.getChannel());
		super.channelConnected(ctx, e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.warn("-----------------channelDisconnected----------- : " + e.getChannel());
		super.channelDisconnected(ctx, e); 
	}

	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.warn("channelClosed" + e.getChannel().toString() +"" +e.getFuture().getCause());
		PushResources.removeChannel(e.getChannel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		logger.warn("channelClosed" + e.getChannel().toString()  + e.getFuture().getCause());
		PushResources.removeChannel(e.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws InterruptedException {
		String receiveStr = (String) e.getMessage();
		JSONObject jsonObject = (JSONObject) JSONObject.parse(receiveStr);
		Object object = jsonObject.get("cmd");
		if(object == null) {
			object = jsonObject.get("reply");
		}
		if(object == null) {
			return;
		}
		switch (object.toString()) {
		case Constant.ClientCommand.CONNECT: // 请求连接
			ServerHelper.handerConn(e.getChannel(),jsonObject);
			break;
		case Constant.ClientCommand.HEARTBEAT: // 心跳
			logger.warn("心跳：" + jsonObject);
			ServerHelper.sendHeartBeat(e.getChannel());
			break;
		case Constant.ClientCommand.DISCONNECT: // 请求断开连接
			PushResources.removeChannel(e.getChannel());
			break;
		case Constant.ClientCommand.STATE: // 风机实时数据
			//logger.warn("state:" + jsonObject.toString());
			Device deviceByGuid = DeviceCache.getDeviceByGuid(PushResources.getAllChannelMap().inverse().get(e.getChannel()));
			if(deviceByGuid == null) {
				PushResources.removeChannel(e.getChannel());
				return;
			}
			ServerHelper.handerData(e.getChannel(),jsonObject);
			break;
		case Constant.ClientCommand.CONTROL: // 本地服务器响应控制指令
			logger.warn("response content：" + jsonObject);
		default:
			break;
		}
	}

}
