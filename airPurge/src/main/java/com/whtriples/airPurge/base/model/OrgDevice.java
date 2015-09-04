package com.whtriples.airPurge.base.model;

import com.rps.util.dao.annotation.Pk;
import com.rps.util.dao.annotation.Table;

@Table("t_d_org_device")
public class OrgDevice {

	@Pk
	private Integer org_id;
	
	@Pk
	private Integer device_id;

	public Integer getOrg_id() {
		return org_id;
	}

	public void setOrg_id(Integer org_id) {
		this.org_id = org_id;
	}

	public Integer getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Integer device_id) {
		this.device_id = device_id;
	}
	
}
