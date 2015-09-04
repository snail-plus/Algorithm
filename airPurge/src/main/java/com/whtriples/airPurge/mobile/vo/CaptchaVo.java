package com.whtriples.airPurge.mobile.vo;

import org.apache.commons.lang3.StringUtils;

import com.whtriples.airPurge.mobile.exception.AirPurgeError;

public class CaptchaVo extends BaseInVo {
	
	private String login_id;
	
	private String captcha_type;
	
	@Override
    public AirPurgeError validate() {
        if (!StringUtils.isNumeric(login_id) || login_id.length() != 11 || !login_id.startsWith("1")) {
        	return AirPurgeError.MOBILE_NO_ERROR;
        }
        if (!StringUtils.isNumeric(captcha_type)) {
        	return AirPurgeError.CAPTCHA_TYPE_ERROR;
        }
        return AirPurgeError.SUCCESS;
    }


	public String getCaptcha_type() {
		return captcha_type;
	}

	public void setCaptcha_type(String captcha_type) {
		this.captcha_type = captcha_type;
	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

}
