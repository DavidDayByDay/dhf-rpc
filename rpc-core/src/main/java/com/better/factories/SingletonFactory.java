package com.better.factories;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工厂
 * 例如netty的channelProvider
 * key = name of class; value = instance
 */

@Slf4j
//@Deprecated
public class SingletonFactory {
    //class作为key太大了
    private static final Map<String,Object> OBEJECT_MAP = new ConcurrentHashMap<String, Object>();

    public static <T> T getInstance(Class<T> clazz){
        //key = 全类名
        String name = clazz.getName();
        if(OBEJECT_MAP.containsKey(name)){
            return (T) OBEJECT_MAP.get(name);
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
