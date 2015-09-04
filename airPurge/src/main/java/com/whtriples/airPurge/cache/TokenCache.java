package com.whtriples.airPurge.cache;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rps.util.D;
import com.whtriples.airPurge.mapper.BeanMapper;
import com.whtriples.airPurge.redis.JedisTemplate;
import com.whtriples.airPurge.util.Collections3;

public class TokenCache {

	private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

	public static final String KEY_PROFIX = "user_token";

	private static JedisTemplate jedisTemplate;

	public static void init(JedisTemplate jedisTemplate) {
		TokenCache.jedisTemplate = jedisTemplate;
	}

	public static UserMap getUserMap(String login_id) {
		UserMap usermap = null;
		Map<String, String> userMap = get(login_id);
		if (!Collections3.isEmpty(userMap)) {
			try {
				usermap = new UserMap();
				BeanMapper.transMap2Bean(userMap, usermap);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return usermap;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> get(String key) {
		Map<String, String> context = jedisTemplate.hgetAll(KEY_PROFIX + key);
		if (Collections3.isEmpty(context)) {
			logger.warn("从数据库获取用户信息");
			Map<String, String> userMap = D
					.sql("select token,user_id,user_status from t_p_user where user_id = ?")
					.one(Map.class, key);
			if (userMap != null) {
				put(KEY_PROFIX + key, userMap);
				context = userMap;
			}
		} else {
			logger.warn("从redis获取用户信息");
		}
		return context;
	}

	public static void put(String key, Map<String, String> value) {
		logger.warn("缓存user:" + value);
		jedisTemplate.hmset(key, value);
	}

	public static class UserMap {

		private String token;

		private String user_id;

		private String user_status;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getUser_status() {
			return user_status;
		}

		public void setUser_status(String user_status) {
			this.user_status = user_status;
		}

		@Override
		public String toString() {
			return "login_id: " + this.user_id + " token:" + this.token
					+ " status: " + this.user_status;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
	}
}
