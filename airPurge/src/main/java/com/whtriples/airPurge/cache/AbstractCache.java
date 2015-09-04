package com.whtriples.airPurge.cache;

import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class AbstractCache<K, V> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected long version = 0L;

    /**
     * 缓存Map
     */
    protected Cache<K, V> cache = null;

    /**
     * 缓存初始化方法
     */
    protected abstract void loadCache();

    /**
     * 根据key获取value
     *
     * @param key 缓存key
     * @return value
     * @throws ExecutionException
     */
    public final V get(K key) {
        return cache.getIfPresent(key);
    }

    public Map<K, V> asMap() {
        return cache.asMap();
    }

}
