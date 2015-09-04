package com.whtriples.airPurge.base.model;

import java.util.List;

import com.rps.util.dao.annotation.ColumnIgnore;
import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;
@Table("t_d_device")
public class Device {

	@Id
	@GenerateByDb
	private Integer device_id;
	
	private String record_time;
	
	private String device_guid;
	
	private Integer par_device_id;

	private Integer device_level;
	
	private String status;
	
	private String remarks;
	
	private String QRCode_url;
	
	private Double lng;

	private Double lat;
	
	private String city_name;
	
	private String city_id;
	
	@ColumnIgnore
	private String device_authority;
	
	@ColumnIgnore
	private String city_en;
	
	@ColumnIgnore
	private Object deviceData;
	
	@ColumnIgnore
	private List<Device> children;
	
	@ColumnIgnore
	private String gear;
	
	@ColumnIgnore
	private String hum;
	
	@ColumnIgnore
	private String pm10;
	
	@ColumnIgnore
	private String pm25;
	
	@ColumnIgnore
	private String run_state;
	
	@ColumnIgnore
	private String temp;
	
	@ColumnIgnore
	private String aqi;
	
	@ColumnIgnore
	private Integer user_id;
	
	public String getGear() {
		return gear;
	}

	public String getHum() {
		return hum;
	}

	public String getPm10() {
		return pm10;
	}

	public String getPm25() {
		return pm25;
	}

	public String getRun_state() {
		return run_state;
	}

	public String getTemp() {
		return temp;
	}

	public void setGear(String gear) {
		this.gear = gear;
	}

	public void setHum(String hum) {
		this.hum = hum;
	}

	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public void setRun_state(String run_state) {
		this.run_state = run_state;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	
	
	public Integer getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Integer device_id) {
		this.device_id = device_id;
	}

	public String getRecord_time() {
		return record_time;
	}

	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}

	public String getDevice_guid() {
		return device_guid;
	}

	public void setDevice_guid(String device_guid) {
		this.device_guid = device_guid;
	}


	public Integer getPar_device_id() {
		return par_device_id;
	}

	public void setPar_device_id(Integer par_device_id) {
		this.par_device_id = par_device_id;
	}

	public List<Device> getChildren() {
		return children;
	}

	public void setChildren(List<Device> children) {
		this.children = children;
	}

	public Integer getDevice_level() {
		return device_level;
	}

	public void setDevice_level(Integer device_level) {
		this.device_level = device_level;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getQRCode_url() {
		return QRCode_url;
	}

	public void setQRCode_url(String qRCode_url) {
		QRCode_url = qRCode_url;
	}

	public Object getDeviceData() {
		return deviceData;
	}

	public void setDeviceData(Object deviceData) {
		this.deviceData = deviceData;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCity_en() {
		return city_en;
	}

	public void setCity_en(String city_en) {
		this.city_en = city_en;
	}

	public String getDevice_authority() {
		return device_authority;
	}

	public void setDevice_authority(String device_authority) {
		this.device_authority = device_authority;
	}

	public String getAqi() {
		return aqi;
	}

	public void setAqi(String aqi) {
		this.aqi = aqi;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

}
