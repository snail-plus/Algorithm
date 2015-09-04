package com.whtriples.airPurge.api.param;

import java.util.List;

import com.google.common.collect.Lists;
import com.whtriples.airPurge.mapper.JsonMapper;

public class ArrayParam extends AbstractParam implements Param {
	
	private List<Object> list = null;
	
	private ArrayParam() {
		super();
	}

	public static ArrayParam builder() {
		ArrayParam param = new ArrayParam();
		param.list = Lists.newArrayList();
		return param;
	}
	
	public ArrayParam add(String obj) {
		list.add(obj);
		return this;
	}
	
	public ArrayParam add(Object obj) {
		list.add(obj);
		return this;
	}
	
	public Object[] toArray() {
		return list.toArray();
	}
	
	@Override
	public String toString() {
		return this.toString(DEFAULT_FORMAT);
	}

	@Override
	public String toString(boolean format) {
		if(format) {
			return JsonMapper.alwaysMapper().toFormatedJson(list);
		} else {
			return JsonMapper.alwaysMapper().toJson(list);
		}
	} 

}
