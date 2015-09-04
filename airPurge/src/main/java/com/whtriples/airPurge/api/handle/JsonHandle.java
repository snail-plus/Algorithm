package com.whtriples.airPurge.api.handle;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.common.base.Preconditions;
import com.whtriples.airPurge.util.Exceptions;


public class JsonHandle implements Handle {
	
	private static Logger logger = LoggerFactory.getLogger(JsonHandle.class);

	private String path;
	
	private String expected;
	
	private String response;
	
	private String errCodePath;
	
	private String errMsgPath;
	
	private String orderIdPath;
	
	private Map<String, String> errMsgMap;
	
	private JsonNode rootNode = null;
	
	private String textValue = null;
	
	private JsonHandle() {
		super();
	}
	
	public static JsonHandle builder() {
		JsonHandle handle = new JsonHandle();
		return handle;
	}
	
	public JsonHandle path(String path) {
		this.path = path;
		return this;
	}
	
	public JsonHandle expected(String expected) {
		this.expected = expected;
		return this;
	}
	
	public JsonHandle errCodePath(String errCodePath) {
		this.errCodePath = errCodePath;
		return this;
	}
	
	public JsonHandle errMsgPath(String errMsgPath) {
		this.errMsgPath = errMsgPath;
		return this;
	}
	
	public Handle businessOrderIdPath(String orderIdPath) {
		this.orderIdPath = orderIdPath;
		return this;
	}
	
	public JsonHandle rootNode(JsonNode rootNode) {
		this.rootNode = rootNode;
		return this;
	}
	
	public JsonHandle errMsgMap(Map<String, String> errMsgMap) {
		this.errMsgMap = errMsgMap;
		return this;
	}
	
	public static String textValue(String response, String path) {
		Preconditions.checkArgument(!StringUtils.isEmpty(response), "response is null");
		Preconditions.checkArgument(!StringUtils.isEmpty(path), "path is null");
		return textValue(rootNode(response), path);
	}
	
	public static String textValue(JsonNode rootNode, String path) {
		JsonNode pathNode = pathNode(rootNode, path);
		if(pathNode == null || pathNode == NullNode.instance) {
			return null;
		}
		return pathNode.asText();
	}
	
	public static JsonNode rootNode(String response) {
		JsonNode rootNode = null;
		try {
			rootNode = new ObjectMapper().readTree(response);
		} catch(Exception e) {
			throw Exceptions.unchecked(e);
		}
		return rootNode;
	}
	
	public static JsonNode pathNode(JsonNode rootNode, String path) {
		if(rootNode == null || rootNode == NullNode.instance) {
			return null;
		}
		
		String[] fieldNames = path.split("\\.");
		Preconditions.checkArgument(!ArrayUtils.isEmpty(fieldNames), "path format error");
		
		JsonNode node = rootNode;
		try {
			if(node == null || node == NullNode.instance) {
				return null;
			}
			for(String fieldName : fieldNames) {
				node = node.get(fieldName);
				if(node == null || node == NullNode.instance) {
					return null;
				}
			}
		} catch(Exception e) {
			throw Exceptions.unchecked(e);
		}
		return node;
	}
	
	public static JsonNode pathNode(String response, String path) {
		return pathNode(rootNode(response), path);
	}
	
	@Override
	public JsonHandle analyze(String response) {
		this.response = response;
		return this;
	}
	
	@Override
	public String resultValue() {
		Preconditions.checkArgument(!StringUtils.isEmpty(path), "path is null");
		if(rootNode == null) {
			Preconditions.checkArgument(!StringUtils.isEmpty(response), "response is null");
			rootNode = rootNode(response);
		}
		return textValue(rootNode, path);
	}
	
	@Override
	public boolean isSuccess() {
		Preconditions.checkArgument(!StringUtils.isEmpty(expected), "expected is null");
		String textValue = resultValue();
		return StringUtils.equals(textValue, expected);
	}
	
	@Override
	public void to(BusinessResult result) {
		boolean success = false;
		try {
			success = isSuccess();
		} catch(IllegalArgumentException e) {
			throw e;
		} catch(Exception e1) {
			result.setSuccess(false);
			result.setErrorCode(FORMAT_ERROR_CODE);
			result.setErrorMsg(FORMAT_ERROR_MSG);
			return;
		}
		logger.debug("JsonHandle analyze compile {} with {}, result is {}", textValue, expected, success);
		result.setSuccess(success);
		if(!success) {
			if(errCodePath == null) {
				result.setErrorCode(textValue);
			}
			if(errMsgPath != null) {
				result.setErrorMsg(textValue(rootNode, errMsgPath));
			} else if(errMsgMap != null) {
				result.setErrorMsg(errMsgMap.get(textValue));
			}
		} else {
			if(orderIdPath == null) {
				result.setBusinessOrderId(textValue(rootNode, orderIdPath));
			}
		}
	}
	
}
