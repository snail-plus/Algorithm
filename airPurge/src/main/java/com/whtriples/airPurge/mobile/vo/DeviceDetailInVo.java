package com.whtriples.airPurge.mobile.vo;

import org.apache.commons.lang3.StringUtils;

import com.whtriples.airPurge.mobile.exception.AirPurgeError;

public class DeviceDetailInVo extends BaseInVo{

	@Override
	public AirPurgeError validate(){
		if(StringUtils.isEmpty(device_id)){
			return AirPurgeError.DEVICE_ID_ERROR;
		}
		return AirPurgeError.SUCCESS;
	}
	private String device_id;
	
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
}
