package com.whtriples.airPurge.mobile.vo;

import org.apache.commons.lang3.StringUtils;

import com.whtriples.airPurge.mobile.exception.AirPurgeError;

public class DeleteDetailInVo extends BaseInVo{

	private String device_guid;

	private String device_id;
	
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

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
}
