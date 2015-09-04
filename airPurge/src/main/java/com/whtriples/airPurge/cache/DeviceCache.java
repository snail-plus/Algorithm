package com.whtriples.airPurge.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.rps.util.D;
import com.whtriples.airPurge.base.model.Device;
import com.whtriples.airPurge.base.model.Transducer;
import com.whtriples.airPurge.util.Constant;
/**
 * 缓存设备相关数据
 * @author Administrator
 *
 */
public class DeviceCache {

	public static Cache<String, List<Device>> cache = CacheBuilder.newBuilder().expireAfterWrite(60 * 2, TimeUnit.SECONDS)
			.maximumSize(10).removalListener(new RemovalListener<String, List<Device>>() {
				public void onRemoval(RemovalNotification<String, List<Device>> notification) {
				}
			}).build();

	public static Cache<String, List<Device>> cacheDevice = CacheBuilder.newBuilder().expireAfterWrite(60 * 2, TimeUnit.SECONDS)
			.maximumSize(10).removalListener(new RemovalListener<String, List<Device>>() {
				public void onRemoval(RemovalNotification<String, List<Device>> notification) {
				}
			}).build();
	
	public static Cache<String, List<Transducer>> cacheAqi = CacheBuilder.newBuilder()
			.expireAfterWrite(18000, TimeUnit.SECONDS).maximumSize(10)
			.removalListener(new RemovalListener<String, List<Transducer>>() {
				public void onRemoval(RemovalNotification<String, List<Transducer>> notification) {
				}
			}).build();
	
    //只查询今天的数据
	public static List<Transducer> getByCityId(final String key) {
		try {
			return DeviceCache.cacheAqi.get(key, new Callable<List<Transducer>>() {
				public List<Transducer> call() {
					String sql = " select * from t_d_transducer where city_id is not null and record_time >= DATE_SUB(curdate(), INTERVAL 0 DAY) order by record_time desc";
					List<Transducer> Data = D.sql(sql).list(Transducer.class);
					return Data;
				}
			});
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Device> getDevice(final String key) {
		try {
			return DeviceCache.cache.get(key, new Callable<List<Device>>() {
				public List<Device> call() {
					String sql = "select d.*,du.device_authority,du.user_id from t_d_device d left join t_d_device_user du on d.device_id=du.device_id where device_level =3";
					List<Device> deviceList = D.sql(sql).list(Device.class);
					return deviceList;
				}
			});
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Device> getBykey(final String key) {
		try {
			return DeviceCache.cache.get(key, new Callable<List<Device>>() {
				public List<Device> call() {
					String sql = "select d.*,c.city_en from t_d_device d left join t_d_city c on d.city_id=c.city_id where d.device_level = 1 or d.device_level=3";
					List<Device> deviceList = D.sql(sql).list(Device.class);
					return deviceList;
				}
			});
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据guid获取设备
	 * 
	 * @param merchantId
	 * @return
	 */
	public static Device getDeviceByGuid(String device_guid) {
		Device device = null;
		List<Device> deviceList = getBykey(Constant.DEVICE_LIST);
		if (null == deviceList || deviceList.isEmpty()) {
			return device;
		}
		for (Device device_ : deviceList) {
			if (device_.getDevice_guid().equals(device_guid) && "1".equals(device_.getStatus())) {
				device = device_;
				break;
			}
		}
		return device;
	}

	//缓存用户绑定的设备
	public static List<Device> getDeviceByUserId(String user_id) {
		List<Device> devices = new ArrayList<Device>();
		List<Device> deviceList = getDevice(Constant.USER_DEVICE_LIST);
		if (null == deviceList || deviceList.isEmpty()) {
			return devices;
		}
		for (Device device_ : deviceList) {
			if (device_.getUser_id()!= null && Integer.parseInt(user_id) == device_.getUser_id() && "1".equals(device_.getStatus())) {
				devices.add(device_);
			}
		}
		return devices;
	}
	
	/**
	 * 根据城市编号获取天气状况
	 * 倒序排列取时间最近的天气数据
	 * @param cityId
	 * @return
	 */
	public static Transducer getAqiByCityId(String cityId) {
		Transducer transducer = null;
		List<Transducer> transducerData = getByCityId(Constant.TRANSDUCER_DATA);
		if (null == transducerData || transducerData.isEmpty()) {
			return transducer;
		}
		for (Transducer transducer_ : transducerData) {
			if (transducer_.getCity_id().equals(cityId)) {
				transducer = transducer_;
				break;
			}
		}
		return transducer;
	}
}
