package com.whtriples.airPurge.mobile.vo;

import org.apache.commons.lang3.StringUtils;

import com.whtriples.airPurge.mobile.exception.AirPurgeError;

public class RegisterVo extends BaseInVo {

	private String login_id;
	
	private String pwd;
	
	private String captcha;

	
	@Override
	public AirPurgeError validate(){
		//System.out.println("用户loginid:" +login_id);
		if(StringUtils.isEmpty(login_id)){
			return AirPurgeError.USER_NAME_ERROR;
		}
		if(StringUtils.isEmpty(pwd)){
			return AirPurgeError.PWD_ERROR;
		}
		return AirPurgeError.SUCCESS;
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

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}