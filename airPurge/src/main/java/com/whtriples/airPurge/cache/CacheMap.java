package com.whtriples.airPurge.cache;

import java.util.Map;

import com.google.common.collect.Maps;

public class CacheMap {

	public static final Map<String, String> cacheData = Maps.newConcurrentMap();
	public static final Map<String, String> cacheAqi = Maps.newConcurrentMap();
	public static final Map<String, String> cacheCity = Maps.newConcurrentMap();
}
