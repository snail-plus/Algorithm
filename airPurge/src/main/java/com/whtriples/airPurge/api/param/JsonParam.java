package com.whtriples.airPurge.api.param;

import java.util.Stack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.whtriples.airPurge.mapper.JsonMapper;

public class JsonParam extends AbstractParam implements Param {

	private ObjectNode document;

	private ObjectNode node;
	
	private ObjectNode parent;
	
	private Stack<Object[]> arrayNode = new Stack<Object[]>();
	
	private JsonParam() {
		super();
	}

	public static JsonParam builder() {
		JsonParam param = new JsonParam();
		ObjectNode document = new ObjectMapper().createObjectNode();
		param.document = document;
		param.node = document;
		return param;
	}
	
	public Param end() {
		node = parent;
		return this;
	}
	
	public Param add() {
		Preconditions.checkArgument(!arrayNode.isEmpty(), "not in Array not support add(name)!");
		node = ((ArrayNode) arrayNode.peek()[0]).addObject();
		return this;
	}

	public Param add(String name) {
		node = node.putObject(name);
		return this;
	}
	
	public Param addValue(String text) {
		((ArrayNode) arrayNode.peek()[0]).add(text);
		return this;
	}
	
	public Param addValue(byte text) {
		((ArrayNode) arrayNode.peek()[0]).add(text);
		return this;
	}

	public Param addValue(short text) {
		((ArrayNode) arrayNode.peek()[0]).add(text);
		return this;
	}

	public Param addValue(int text) {
		((ArrayNode) arrayNode.peek()[0]).add(text);
		return this;
	}

	public Param addValue(long text) {
		((ArrayNode) arrayNode.peek()[0]).add(text);
		return this;
	}

	public Param addValue(float text) {
		((ArrayNode) arrayNode.peek()[0]).add(text);
		return this;
	}

	public Param addValue(double text) {
		((ArrayNode) arrayNode.peek()[0]).add(text);
		return this;
	}

	public Param addValue(boolean text) {
		((ArrayNode) arrayNode.peek()[0]).add(text);
		return this;
	}
	
	public Param array(String name) {
		arrayNode.push(new Object[] {node.putArray(name), node});
		return this;
	}
	
	public Param arrayEnd() {
		node = (ObjectNode) arrayNode.pop()[1];
		return this;
	}

	public Param set(String name, String text) {
		node.put(name, text);
		return this;
	}

	public Param set(String name, byte text) {
		node.put(name, text);
		return this;
	}

	public Param set(String name, short text) {
		node.put(name, text);
		return this;
	}

	public Param set(String name, int text) {
		node.put(name, text);
		return this;
	}

	public Param set(String name, long text) {
		node.put(name, text);
		return this;
	}

	public Param set(String name, float text) {
		node.put(name, text);
		return this;
	}

	public Param set(String name, double text) {
		node.put(name, text);
		return this;
	}

	public Param set(String name, boolean text) {
		node.put(name, text);
		return this;
	}
	
	public String toString() {
		return toString(DEFAULT_FORMAT);
	}

	public String toString(boolean format) {
		if(format) {
			return JsonMapper.alwaysMapper().toFormatedJson(document);
		} else {
			return document.toString();
		}
	}
	
	public ObjectNode toJson() {
		return document;
	}
	
}
