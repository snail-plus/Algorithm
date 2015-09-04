package com.whtriples.airPurge.mobile.vo;

import com.whtriples.airPurge.mobile.exception.AirPurgeError;
import com.whtriples.airPurge.mobile.exception.BusinessException;

public class BaseOutVo {

    private String timestamp;

    private String errorCode;

    private String errorMsg;

    private AirPurgeError error;
    
    public void setError(BusinessException e) {
        this.setErrorCode(e.getCode());
        this.setErrorMsg(e.getMessage());
    }

    public void setError(AirPurgeError e) {
    	this.error = e;
        this.setErrorCode(e.code);
        this.setErrorMsg(e.message);
    }

    public void setErrorAndHash(BusinessException e) {
        this.setError(e);
    }

    public void setErrorAndHash(AirPurgeError e) {
        this.setError(e);
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean hasError() {
		return error != AirPurgeError.SUCCESS;
	}
}
