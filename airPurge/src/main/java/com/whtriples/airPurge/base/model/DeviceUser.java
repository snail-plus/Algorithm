package com.whtriples.airPurge.base.model;

import com.rps.util.dao.annotation.ColumnIgnore;
import com.rps.util.dao.annotation.Pk;
import com.rps.util.dao.annotation.Table;

@Table("t_d_device_user")
public class DeviceUser {

	@Pk
	private Integer device_id;
	
	@Pk
	private Integer user_id;

	private Integer device_authority;
	
	@ColumnIgnore
	private String device_guid;
	
	@ColumnIgnore
	private String remarks;
	
	@ColumnIgnore
	private String user_name;
	
	public Integer getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Integer device_id) {
		this.device_id = device_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getDevice_authority() {
		return device_authority;
	}

	public void setDevice_authority(Integer device_authority) {
		this.device_authority = device_authority;
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

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
}
