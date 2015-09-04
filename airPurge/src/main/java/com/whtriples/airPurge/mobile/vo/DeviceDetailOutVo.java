package com.whtriples.airPurge.mobile.vo;


public class DeviceDetailOutVo extends BaseOutVo{

    private Integer device_id;
	
	private String record_time;
	
	private String device_guid;

	private String remarks;

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
