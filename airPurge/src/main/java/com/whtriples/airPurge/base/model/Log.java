package com.whtriples.airPurge.base.model;

import java.util.Date;

import com.rps.util.dao.annotation.ColumnIgnore;
import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

@Table("t_d_log")
public class Log {

	@Id
	@GenerateByDb
	private Integer log_id;
	
	private Integer user_id;
	
	private Date record_time;
	
	private String device_guid;
	
	private String Operation;

	@ColumnIgnore
	private String user_name;
	
	public Integer getLog_id() {
		return log_id;
	}

	public void setLog_id(Integer log_id) {
		this.log_id = log_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Date getRecord_time() {
		return record_time;
	}

	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}

	public String getDevice_guid() {
		return device_guid;
	}

	public void setDevice_guid(String device_guid) {
		this.device_guid = device_guid;
	}

	public String getOperation() {
		return Operation;
	}

	public void setOperation(String operation) {
		Operation = operation;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
}
