package com.whtriples.airPurge.mobile.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.base.model.Transducer;
import com.whtriples.airPurge.mobile.weather.Weather;
import com.whtriples.airPurge.redis.JedisTemplate;
import com.whtriples.airPurge.util.Collections3;
/**
 * 定时获取天气数据
 * @author Administrator
 *
 */
@Component 
public class WeatherTask {
	
   @Autowired
   private  JedisTemplate jedisTemplate;
	
   private Logger logger = LoggerFactory.getLogger(this.getClass());
   
	@SuppressWarnings("unchecked")
	@Scheduled(cron="0 0 */1 * * ? ")
	public void getWeatherData(){
		logger.warn("--------执行请求天气定时任务----------");
		List<Device> deviceList = D.sql("select d.*,c.city_en,c.city_zh from t_d_device d left join t_d_city c on d.city_id=c.city_id where device_level = 1 ").many(Device.class);
		Map<String,String> cityMap = Collections3.extractToMap(deviceList, "city_id","city_en");
		for (Map.Entry<String, String> map :cityMap.entrySet()) {
			getAqiByCityId(map.getKey(),map.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Scheduled(cron="0 0 */1 * * ? ")
	public void clearRedisData(){
		logger.warn("--------执行清除redis数据定时任务----------");
		List<Device> deviceList = D.sql("select d.*,c.city_en,c.city_zh from t_d_device d left join t_d_city c on d.city_id=c.city_id where device_level = 3 ").many(Device.class);
		Map<String,String> cityMap = Collections3.extractToMap(deviceList, "device_guid","city_id");
		for (Map.Entry<String, String> map :cityMap.entrySet()) {
			String device_guid = map.getKey();
			String city_id = map.getValue();
			boolean flag = jedisTemplate.del(device_guid+ "_pm25", device_guid+ "_temp",device_guid+ "_hum",city_id+ "_pm25", city_id+ "_temp",city_id+ "_hum");
			if(flag){
				logger.warn("-----清除redis缓存数据完成-----" + "device_guid: " + device_guid + "  city_id: " + city_id);
			}
		}
	}
	public void getAqiByCityId(String cityId,String cityEn){
		try {
			JSONObject data = Weather.getAqiByCityName(cityEn);
			if(data == null) {
				logger.warn("获取天气数据失败");
				return;
			}
			JSONObject now = JSONObject.parseObject(data.get("now").toString());
			JSONObject parseObject = JSONObject.parseObject(data.getString("aqi"));
			Transducer transducer = new Transducer();
			JSONObject aqiData = null;
			if(parseObject != null) {
				 aqiData = JSONObject.parseObject(parseObject.get("city").toString());
				 transducer.setPm10(aqiData.get("pm10").toString());
			     transducer.setPm25(aqiData.get("pm25").toString());
			}else{
				transducer.setPm10("0");
			    transducer.setPm25("0");
			}
			transducer.setHum(Double.parseDouble(now.get("hum").toString()));
			transducer.setTemp(Double.parseDouble(now.get("tmp").toString()));
			transducer.setCity_id(cityId);
			transducer.setRecord_time(new Date());
			D.insert(transducer);
		} catch (Exception e) {
			logger.warn("get Data fail");
			e.printStackTrace();
		}
	}
}
