package com.whtriples.airPurge.mobile.vo;

import org.apache.commons.lang3.StringUtils;

import com.whtriples.airPurge.mobile.exception.AirPurgeError;

public class BindDeviceInVo extends BaseInVo{

	private String device_guid;

	public String getDevice_guid() {
		return device_guid;
	}

	public void setDevice_guid(String device_guid) {
		this.device_guid = device_guid;
	}
	
	@Override
	public AirPurgeError validate(){
		if(StringUtils.isEmpty(device_guid)){
			return AirPurgeError.DEVICE_GUID_EMPTY_ERROR;
		}
		return AirPurgeError.SUCCESS;
	}
}
