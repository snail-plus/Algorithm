package com.whtriples.airPurge.mapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * <p/>
 * 1. 持有Mapper的单例.
 * 2. 返回值类型转换.
 * 3. 批量转换Collection中的所有对象.
 * 4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 *
 * @author calvin
 */
public class BeanMapper {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    @SuppressWarnings("rawtypes")
	public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     */
    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }
    
    public static final Object map2Bean(Class<?> type, Map<String, ? extends Object> map)   
            throws IntrospectionException, IllegalAccessException,  InstantiationException, InvocationTargetException {  
        BeanInfo beanInfo = Introspector.getBeanInfo(type);  
        Object obj = type.newInstance();  
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
        for (int i = 0; i< propertyDescriptors.length; i++) {  
            PropertyDescriptor descriptor = propertyDescriptors[i];  
            String propertyName = descriptor.getName();  
            if (map.containsKey(propertyName)) {  
                Object value = map.get(propertyName);  
                Object[] args = new Object[1];  
                args[0] = value;  
                descriptor.getWriteMethod().invoke(obj, args);  
            }  
        }  
        return obj;  
    }  
    
    
    public static void transMap2Bean(Map<String, String> map, Object obj) throws IllegalAccessException, InvocationTargetException {  
        if (map == null || obj == null) {  
            return;  
        }  
        BeanUtils.populate(obj, map);  
    }  
}