package com.whtriples.airPurge.mobile.vo;

import org.apache.commons.lang3.StringUtils;

import com.whtriples.airPurge.mobile.exception.AirPurgeError;

public class LoginVo extends BaseInVo {

	private String login_id;
	
	private String pwd;
	
	@Override
	public AirPurgeError validate(){
		if(StringUtils.isEmpty(login_id)){
			return AirPurgeError.USER_NAME_ERROR;
		}
		if(StringUtils.isEmpty(pwd)){
			return AirPurgeError.PWD_EMPTY_ERROR;
		}
		return AirPurgeError.SUCCESS;
	}
	
	@Override
	public String toString(){
		return "login_id: " + login_id + "\r\n" + "pwd: " + pwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
}