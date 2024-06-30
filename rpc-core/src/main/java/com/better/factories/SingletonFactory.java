package com.better.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工厂
 * 列入netty的channelProvider
 * key = name of class; value = instance
 */
public class SingletonFactory {
    //class作为key太大了
    private static final Map<String,Object> OBEJECT_MAP = new ConcurrentHashMap<String, Object>();
    private static final Logger log = LoggerFactory.getLogger(SingletonFactory.class);

    public static <T> T getInstance(Class<T> clazz){
        String name = clazz.getName();
        if(OBEJECT_MAP.containsKey(name)){
            return (T)OBEJECT_MAP.get(name);
        }else {
            try {
                T instance = clazz.getDeclaredConstructor().newInstance();
                OBEJECT_MAP.put(name, instance);
                return instance;
            } catch (Exception e) {
                log.debug("something wrong when constructing instance of "+clazz.getName() + " in SingletonFactory");
                throw new RuntimeException(e);
            }
        }
    }





}
