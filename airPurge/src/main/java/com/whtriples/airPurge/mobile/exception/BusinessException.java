package com.whtriples.airPurge.mobile.exception;



public class BusinessException extends Exception {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;
    
    private AirPurgeError error;

    public BusinessException() {
        super();
    }

    public BusinessException(AirPurgeError error) {
        this.error = error;
    }

    /**
	 * @return the error
	 */
	public AirPurgeError getError() {
		return error;
	}

	/**
     * @return the code
     */
    public String getCode() {
        return error.code;
    }

    public String getMessage() {
        return error.message;
    }

}
