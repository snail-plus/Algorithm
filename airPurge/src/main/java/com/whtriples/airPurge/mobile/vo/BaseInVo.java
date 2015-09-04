package com.whtriples.airPurge.mobile.vo;

import com.whtriples.airPurge.cache.TokenCache;
import com.whtriples.airPurge.cache.TokenCache.UserMap;
import com.whtriples.airPurge.mobile.exception.AirPurgeError;
import com.whtriples.airPurge.util.Reflections;
import com.whtriples.airPurge.util.UUIDs;

import org.apache.commons.lang3.StringUtils;

public class BaseInVo {

	private String user_id;
	
    private String token;

    public <T extends BaseOutVo> T createOutVo(Class<T> voClass) {
        return createOutVo(voClass, true);
    }

    public <T extends BaseOutVo> T createOutVo(Class<T> voClass, Boolean isCheckToken) {
        T t = Reflections.newInstance(voClass);

        AirPurgeError error = null;
        if(isCheckToken) {
        	error = checkToken();
        }
        if (error == null) {
            error = this.validate();
        }
        t.setErrorCode(error.code);
        t.setError(error);
        t.setErrorMsg(error.message);
        t.setTimestamp(UUIDs.getTimestamp());
        return t;
    }

    private AirPurgeError checkToken() {
    	if(StringUtils.isEmpty(token)) {
    		return AirPurgeError.TOKEN_ERROR;
    	}
    	if(!StringUtils.isNumeric(user_id)){
    		return AirPurgeError.USER_ID_ERROR;
    	}
    	
    	UserMap userMap = TokenCache.getUserMap(user_id);
    	
    	if(userMap == null){
    		return AirPurgeError.USER_NOT_EXIST_ERROR;
    	}
    	if("0".equals(userMap.getUser_status()) || !token.equals(userMap.getToken())){
    		return AirPurgeError.TOKEN_ERROR;
    	}
    	return null;
	}
   

    public AirPurgeError validate() {
        return AirPurgeError.SUCCESS;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


}