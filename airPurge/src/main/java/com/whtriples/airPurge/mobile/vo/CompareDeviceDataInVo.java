package com.whtriples.airPurge.mobile.vo;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.whtriples.airPurge.mobile.exception.AirPurgeError;

public class CompareDeviceDataInVo extends BaseInVo{

	private String device_guid;

	private String type;
	
	@Override
	public AirPurgeError validate() {
	   if(StringUtils.isEmpty(device_guid)){
		   return AirPurgeError.DEVICE_GUID_EMPTY_ERROR;
	   }
	   if(StringUtils.isEmpty(type)){
			return AirPurgeError.DATA_TYPE_ERROR;
	   }
	   if(!Lists.newArrayList("hum", "temp", "pm25").contains(type)){
			return AirPurgeError.DATA_TYPE_ERROR;
		}
        return AirPurgeError.SUCCESS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDevice_guid() {
		return device_guid;
	}

	public void setDevice_guid(String device_guid) {
		this.device_guid = device_guid;
	}
	 
}
