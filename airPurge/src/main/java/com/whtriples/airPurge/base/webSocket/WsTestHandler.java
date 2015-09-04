package com.whtriples.airPurge.base.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.whtriples.airPurge.mobile.server.ServerHelper;

public class WsTestHandler extends TextWebSocketHandler {

	private Logger logger = LoggerFactory.getLogger(TextWebSocketHandler.class);
	
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
       logger.warn("Session connected:" + session.getId());
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	logger.info("Session closed:" + session);
        SessionManager.remove(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.warn("receive msg :" + message.getPayload());
        JSONObject jsonObject = (JSONObject) JSONObject.parse(message.getPayload());
        ServerHelper.WebScoketData(jsonObject, session);
    }

    @Override
    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {
        if (session.isOpen()) 
            session.close();
        SessionManager.remove(session);
        logger.warn(exception.getMessage() +" webscoket 异常");
        super.handleTransportError(session, exception);
    }


}