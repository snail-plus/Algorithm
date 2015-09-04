package com.whtriples.airPurge.api.handle;


public interface Handle {
	
	public static final String FORMAT_ERROR_CODE = "8889";
	public static final String FORMAT_ERROR_MSG = "业务方返回格式错误";
	
	public abstract Handle analyze(String response);
	public abstract String resultValue();
	public abstract boolean isSuccess();
	public abstract void to(BusinessResult result);

}
