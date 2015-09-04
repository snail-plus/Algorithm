package com.whtriples.airPurge.base.model;

import com.rps.util.dao.annotation.Table;

@Table("t_d_city")
public class City {

	private String city_id;
	
	private String city_en;
	
	private String city_zh;
	
	private String zone_name;
	
	private String province_name;

	public String getCity_id() {
		return city_id;
	}

	public String getCity_en() {
		return city_en;
	}

	public String getCity_zh() {
		return city_zh;
	}

	public String getZone_name() {
		return zone_name;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public void setCity_en(String city_en) {
		this.city_en = city_en;
	}

	public void setCity_zh(String city_zh) {
		this.city_zh = city_zh;
	}

	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	
}
