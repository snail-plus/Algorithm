package com.whtriples.airPurge.mobile.task;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.rps.util.D;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.util.Collections3;
/**
 * @author Administrator
 *
 */
@Component 
public class ContainerListener implements SmartApplicationListener{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WeatherTask task;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
        if(((ApplicationContextEvent) event).getApplicationContext().getParent() == null){
        	logger.warn("--------初始化空气质量数据--------");
    		List<Device> deviceList = D.sql("select d.*,c.city_en,c.city_zh from t_d_device d left join t_d_city c on d.city_id=c.city_id where device_level = 1 ").many(Device.class);
    		Map<String,String> cityMap = Collections3.extractToMap(deviceList, "city_id","city_en");
    		for (Map.Entry<String, String> map :cityMap.entrySet()) {
    			task.getAqiByCityId(map.getKey(), map.getValue());
    		}
    		logger.warn("--------初始化空气质量数据结束----------");
        }
		
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
		return eventType == ContextRefreshedEvent.class;
	}

	@Override
	public boolean supportsSourceType(Class<?> sourceType) {
		return sourceType == XmlWebApplicationContext.class;
	}

}
