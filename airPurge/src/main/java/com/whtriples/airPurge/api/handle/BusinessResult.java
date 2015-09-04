package com.whtriples.airPurge.api.handle;

import com.whtriples.airPurge.mapper.JsonMapper;


public class BusinessResult {

	private boolean success;
	
	private String businessStatus;
	
	private String businessOrderId;

	private String detail;

	private String request;

	private String response;

	private String businessCode;

	private String errorCode;

	private String errorMsg;

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the businessStatus
	 */
	public String getBusinessStatus() {
		return businessStatus;
	}

	/**
	 * @param businessStatus the businessStatus to set
	 */
	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	/**
	 * @return the businessOrderId
	 */
	public String getBusinessOrderId() {
		return businessOrderId;
	}

	/**
	 * @param businessOrderId the businessOrderId to set
	 */
	public void setBusinessOrderId(String businessOrderId) {
		this.businessOrderId = businessOrderId;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return the businessCode
	 */
	public String getBusinessCode() {
		return businessCode;
	}

	/**
	 * @param businessCode the businessCode to set
	 */
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
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
	
	@Override
	public String toString() {
		return JsonMapper.alwaysMapper().toFormatedJson(this);
	}

}
