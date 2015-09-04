package com.whtriples.airPurge.base.model;

import java.util.Date;

import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

@Table("t_c_app_version")
public class AppVersion {
	@Id
	@GenerateByDb
	private Integer app_version_id;
	private String app_type;
	private String version_code;
	private String version_name;
	private String content;
	private String force_update;
	private String app_url;
	private String status;
	private Date create_time;
	
	public Integer getApp_version_id() {
		return app_version_id;
	}
	public void setApp_version_id(Integer app_version_id) {
		this.app_version_id = app_version_id;
	}
	public String getApp_type() {
		return app_type;
	}
	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}
	public String getVersion_code() {
		return version_code;
	}
	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}
	public String getVersion_name() {
		return version_name;
	}
	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getForce_update() {
		return force_update;
	}
	public void setForce_update(String force_update) {
		this.force_update = force_update;
	}
	public String getApp_url() {
		return app_url;
	}
	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}