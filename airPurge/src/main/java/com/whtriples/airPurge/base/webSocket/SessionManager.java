package com.whtriples.airPurge.base.webSocket;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.whtriples.airPurge.mapper.JsonMapper;

@Component
public class SessionManager {

	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
	public static final Map<String, BiMap<String, WebSocketSession>> sessionMap = Maps.newConcurrentMap();

	public static WebSocketSession getSocketSessionByToken(String key, String token) {
		if (sessionMap.containsKey(key)) {
			return sessionMap.get(key).get(token);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static void addSocketSession(final WebSocketSession ws, String str) {
		Map<String, String> map = JsonMapper.alwaysMapper().fromJson(str, Map.class);
		ws.getAttributes().putAll(map);
		String key = map.get("device_guid");
		String token = map.get("token");
		if (sessionMap.containsKey(key)) {
			sessionMap.get(key).put(token, ws);
		} else {
			BiMap<String, WebSocketSession> m = HashBiMap.create(512);
			m.put(token, ws);
			sessionMap.put(key, m);
		}
		logger.warn("添加webscoket sessionMap：" + sessionMap +" session seize" + sessionMap.size());
	}

	public static void removeSocketSessionByKey(String key) {
		logger.warn(key + "remove sCoket");
		if (sessionMap.containsKey(key)) {
			sessionMap.remove(key);
		}
	}

	public static void removeSocketSessionByToken(String key, String token) {
		if (sessionMap.containsKey(key) && sessionMap.get(key).containsKey(token)) {
			if (sessionMap.get(key).get(token).isOpen()) {
				try {
					sessionMap.get(key).get(token).close();
				} catch (IOException e) {
					sessionMap.get(key).remove(token);
				}
			}
			sessionMap.get(key).remove(token);
		}
	}

	public static void remove(WebSocketSession ws) {
		logger.warn("------------Delete push start---------------");
		Map<String, Object> map = ws.getAttributes();
		if (map != null && map.get("token") != null) {
			String token = map.get("token").toString();
			String key = map.get("device_guid").toString();
			if (sessionMap.containsKey(key) && sessionMap.get(key).containsKey(token)) {
				WebSocketSession ws1 = sessionMap.get(key).get(token);
				if (!ws1.getId().equals(ws.getId())) {
					return;
				}
			}
			removeSocketSessionByToken(key, token);
		}
		logger.warn("------------Delete push end---------------");
	}

}
