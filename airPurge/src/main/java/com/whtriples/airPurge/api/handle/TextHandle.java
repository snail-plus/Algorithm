package com.whtriples.airPurge.api.handle;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.whtriples.airPurge.util.Collections3;


public class TextHandle implements Handle {
	
	private static Logger logger = LoggerFactory.getLogger(TextHandle.class);

	private boolean trim = false;
	
	private String expected;
	
	private String response;
	
	private Callback callback;
	
	private Map<String, String> errorMap = null;
	
	private TextHandle() {
		super();
	}
	
	public static TextHandle builder() {
		TextHandle handle = new TextHandle();
		return handle;
	}
	
	public TextHandle trim() {
		this.trim = true;
		return this;
	}
	
	public TextHandle expected(String expected) {
		this.expected = expected;
		return this;
	}
	
	public TextHandle expected(Callback callback) {
		this.callback = callback;
		return this;
	}
	
	public TextHandle errorMap(Map<String, String> errorMap) {
		this.errorMap = errorMap;
		return this;
	}
	
	@Override
	public TextHandle analyze(String response) {
		this.response = response;
		return this;
	}
	
	@Override
	public String resultValue() {
		Preconditions.checkArgument(!StringUtils.isEmpty(response), "response is null");
		return response;
	}

	@Override
	public boolean isSuccess() {
		Preconditions.checkArgument(!StringUtils.isEmpty(expected) || callback != null, "expected is null");
		if(trim) {
			response = response.trim();
		}
		if(callback != null) {
			return callback.call(response);
		} else {
			return StringUtils.equals(response, expected);
		}
	}
	
	@Override
	public void to(BusinessResult result) {
		boolean success = isSuccess();
		logger.debug("JsonHandle analyze compile {} with {}, result is {}", response, expected, success);
		result.setSuccess(success);
		if(!success && !Collections3.isEmpty(errorMap)) {
			result.setErrorCode(errorMap.get(response));
		}
	}
	
	public interface Callback {
		boolean call(String response);
	}

}
