package com.whtriples.airPurge.mobile.vo;

import org.apache.commons.lang3.StringUtils;

import com.whtriples.airPurge.mobile.exception.AirPurgeError;

public class ModifyPwdInVo extends BaseInVo{

	private String login_id;
	
	private String old_pwd;
	
	private String new_pwd;
	
	@Override
	public AirPurgeError validate(){
		if(StringUtils.isEmpty(login_id)){
			return AirPurgeError.USER_NAME_ERROR;
		}
		if(StringUtils.isEmpty(old_pwd)){
			return AirPurgeError.PWD_ERROR;
		}
		if(StringUtils.isEmpty(new_pwd)){
			return AirPurgeError.PWD_ERROR;
		}
		return AirPurgeError.SUCCESS;
	}
	
	public String getOld_pwd() {
		return old_pwd;
	}
	public void setOld_pwd(String old_pwd) {
		this.old_pwd = old_pwd;
	}
	public String getNew_pwd() {
		return new_pwd;
	}
	public void setNew_pwd(String new_pwd) {
		this.new_pwd = new_pwd;
	}
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
	
}
