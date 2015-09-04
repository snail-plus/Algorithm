package com.whtriples.airPurge.api.param;

import java.util.Map;

import com.google.common.collect.Maps;
import com.whtriples.airPurge.mapper.JsonMapper;

public class KvParam extends AbstractParam implements Param {
	
	private Map<String, Object> kv = Maps.newLinkedHashMap();

	private KvParam() {
		
		super();
	}
	
	public static KvParam builder() {
		KvParam param = new KvParam();
		param.kv = Maps.newLinkedHashMap();
		return param;
	}
	
	public static Param builderAutoSort() {
		KvParam param = new KvParam();
		param.kv = Maps.newTreeMap();
		return param;
	}
	
	public Map<String, Object> getKv() {
		return kv;
	}
	
	@Override
	public Param set(String name, String text) {
		kv.put(name, text);
		return this;
	}

	@Override
	public Param set(String name, byte text) {
		kv.put(name, text);
		return this;
	}

	@Override
	public Param set(String name, short text) {
		kv.put(name, text);
		return this;
	}

	@Override
	public Param set(String name, int text) {
		kv.put(name, text);
		return this;
	}

	@Override
	public Param set(String name, long text) {
		kv.put(name, text);
		return this;
	}

	@Override
	public Param set(String name, float text) {
		kv.put(name, text);
		return this;
	}

	@Override
	public Param set(String name, double text) {
		kv.put(name, text);
		return this;
	}

	@Override
	public Param set(String name, boolean text) {
		kv.put(name, text);
		return this;
	}
	
	public String toString() {
		return toString(DEFAULT_FORMAT);
	}

	public String toString(boolean format) {
		if(format) {
			return JsonMapper.alwaysMapper().toFormatedJson(kv);
		} else {
			return kv.toString();
		}
	}

}
