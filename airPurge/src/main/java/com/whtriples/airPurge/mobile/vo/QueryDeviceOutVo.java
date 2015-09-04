package com.whtriples.airPurge.mobile.vo;

import java.util.List;

import com.whtriples.airPurge.base.model.Device;


public class QueryDeviceOutVo extends BaseOutVo{

	private List<Device> devices;

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
}
