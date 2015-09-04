package com.whtriples.airPurge.base.model;

import java.util.Date;

import com.rps.util.dao.annotation.ColumnIgnore;
import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

@Table("t_d_transducer")
public class Transducer {

	@Id
	@GenerateByDb
	private Integer device_id;
	
	private Date record_time;
	
	private String device_guid;
	
	private Integer par_device_id;
	
	private String pm25;
	
	private String pm10;
	
	private String city_id;
	
	@ColumnIgnore
	private String org_name;
	
	@ColumnIgnore
	private String city_name;
	
	/**
	 * 温度
	 */
	private Double temp;
	
	/**
	 * 湿度
	 */
	private Double hum;
	
	private String run_state;
	
	private String err_state;
	
	private String comm_state;

	@ColumnIgnore
	private String remarks;
	
	@ColumnIgnore
	private String status;
	
	@ColumnIgnore
	private String ctrlmode;
	
	@ColumnIgnore
	private String gear;
	
	@ColumnIgnore
	private Date sign;
	
	@ColumnIgnore
	private String aqi;
	
	@ColumnIgnore
	private String cityName;
	
	public Integer getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Integer device_id) {
		this.device_id = device_id;
	}

	public Date getRecord_time() {
		return record_time;
	}

	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}

	public String getDevice_guid() {
		return device_guid;
	}

	public void setDevice_guid(String device_guid) {
		this.device_guid = device_guid;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public String getPm10() {
		return pm10;
	}

	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	public Double getTemp() {
		return temp;
	}

	public void setTemp(Double temp) {
		this.temp = temp;
	}

	public Double getHum() {
		return hum;
	}

	public void setHum(Double hum) {
		this.hum = hum;
	}

	public String getRun_state() {
		return run_state;
	}

	public void setRun_state(String run_state) {
		this.run_state = run_state;
	}

	public String getErr_state() {
		return err_state;
	}

	public void setErr_state(String err_state) {
		this.err_state = err_state;
	}

	public String getComm_state() {
		return comm_state;
	}

	public void setComm_state(String comm_state) {
		this.comm_state = comm_state;
	}

	public Integer getPar_device_id() {
		return par_device_id;
	}

	public void setPar_device_id(Integer par_device_id) {
		this.par_device_id = par_device_id;
	}

	public Date getSign() {
		return sign;
	}

	public void setSign(Date sign) {
		this.sign = sign;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCtrlmode() {
		return ctrlmode;
	}

	public void setCtrlmode(String ctrlmode) {
		this.ctrlmode = ctrlmode;
	}

	public String getGear() {
		return gear;
	}

	public void setGear(String gear) {
		this.gear = gear;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAqi() {
		return aqi;
	}

	public void setAqi(String aqi) {
		this.aqi = aqi;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

}
