package com.whtriples.airPurge.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CacheCenter {

    /**
     * 日志 logger
     */
    private static Logger logger = LoggerFactory.getLogger(CacheCenter.class);

    /**
     * 注入BeanFactory
     */
    @Autowired
    private BeanFactory beanFactory = null;

    /**
     * Spring注入所有Cache类字节码
     */
    private List<Class<AbstractCache<?, ?>>> cacheClassList = null;

    /**
     * @param cacheClassList 参数 cacheClassList
     */
    public void setCacheClassList(List<Class<AbstractCache<?, ?>>> cacheClassList) {
        this.cacheClassList = cacheClassList;
    }

    /**
     * @return the cacheClassList
     */
    public List<Class<AbstractCache<?, ?>>> getCacheClassList() {
        return cacheClassList;
    }

    /**
     * 初始化所有缓存
     */
    public void loadCache() {
        System.out.println("------------ loadCache ------------");
        // 添加缓存Class
        for (Class<AbstractCache<?, ?>> cacheClass : cacheClassList) {
            AbstractCache<?, ?> cacheBean = beanFactory.getBean(cacheClass);
            cacheBean.loadCache();
        }
        System.out.println("------------ loadCache finish ------------");
        logger.debug("Cache init finish");
    }

}
