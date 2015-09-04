package com.whtriples.airPurge.api.handle;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.whtriples.airPurge.util.Exceptions;


public class XmlHandle implements Handle {
	
	private static Logger logger = LoggerFactory.getLogger(XmlHandle.class);

	private String path;
	
	private String expected;
	
	private Callback callback;
	
	private String response;
	
	private String errCodePath;
	
	private String errMsgPath;
	
	private String orderIdPath;
	
	private Document rootNode;
	
	private String textValue;
	
	private XmlHandle() {
		super();
	}
	
	public static XmlHandle builder() {
		XmlHandle handle = new XmlHandle();
		return handle;
	}
	
	public XmlHandle path(String path) {
		this.path = path;
		return this;
	}
	
	public XmlHandle expected(String expected) {
		this.expected = expected;
		return this;
	}
	
	public XmlHandle expected(Callback callback) {
		this.callback = callback;
		return this;
	}
	
	public XmlHandle errCodePath(String errCodePath) {
		this.errCodePath = errCodePath;
		return this;
	}
	
	public XmlHandle errMsgPath(String errMsgPath) {
		this.errMsgPath = errMsgPath;
		return this;
	}
	
	public Handle businessOrderIdPath(String orderIdPath) {
		this.orderIdPath = orderIdPath;
		return this;
	}
	
	public XmlHandle rootNode(Document rootNode) {
		this.rootNode = rootNode;
		return this;
	}
	
	public static String textValue(String response, String path) {
		Preconditions.checkArgument(!StringUtils.isEmpty(response), "response is null");
		Preconditions.checkArgument(!StringUtils.isEmpty(path), "path is null");
		return textValue(rootNode(response), path);
	}
	
	public static String textValue(Node rootNode, String path) {
		Node node = pathNode(rootNode, path);
		if(node == null) {
			return null;
		}
		return node.getText();
	}
	
	public static Node pathNode(String response, String path) {
		return pathNode(rootNode(response), path);
	}
	
	public static List<Node> pathNodes(String response, String path) {
		return pathNodes(rootNode(response), path);
	}
	
	public static Node pathNode(Node rootNode, String path) {
		if(rootNode == null) {
			return null;
		}
		return rootNode.selectSingleNode(path);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Node> pathNodes(Document rootNode, String path) {
		if(rootNode == null) {
			return null;
		}
		return (List)rootNode.selectNodes(path);
	}
	
	public static Document rootNode(String response) {
        Document document = null;
        try {
        	document = DocumentHelper.parseText(StringUtils.trim(response));
		} catch(Exception e) {
			throw Exceptions.unchecked(e);
		}
		return document;
	}
	
	@Override
	public XmlHandle analyze(String response) {
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
		Preconditions.checkArgument(!StringUtils.isEmpty(expected) || callback != null, "expected is null");
		if(callback != null) {
			return callback.call(response);
		} else {
			return StringUtils.equals(resultValue(), expected);
		}
	}
	
	@Override
	public void to(BusinessResult result) {
		boolean success = false;
		try {
			success = isSuccess();
		} catch(Exception e) {
			result.setSuccess(false);
			result.setErrorCode(FORMAT_ERROR_CODE);
			result.setErrorMsg(FORMAT_ERROR_MSG);
			return;
		}
		logger.debug("XmlHandle analyze compile {} with {}, result is {}", textValue, expected, success);
		result.setSuccess(success);
		if(!success) {
			if(errCodePath == null) {
				result.setErrorCode(textValue);
			}
			if(errMsgPath != null) {
				result.setErrorMsg(textValue(rootNode, errMsgPath));
			}
		} else {
			if(orderIdPath == null) {
				result.setBusinessOrderId(textValue(rootNode, orderIdPath));
			}
		}
	}
	
	public interface Callback {
		boolean call(String response);
	}

}
