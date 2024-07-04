package com.better.provider;

import com.better.exceptions.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LocalServiceProvider {

    private static final Map<String, Object> localServiceCache = new ConcurrentHashMap<>();

    public static Object getService(String serviceName) {
        if (localServiceCache.containsKey(serviceName)) {
            return localServiceCache.get(serviceName);
        }else {
//            log.debug("the requested service is not found: {}", serviceName);
            throw new RpcException("the requested service is not found" + serviceName);
        }
    }

    public static void putService(String serviceName, Object service) {
        if (localServiceCache.containsKey(serviceName)) {
            log.warn("the service is already exist" + serviceName);
        }
        localServiceCache.put(serviceName, service);
        log.info("the service {} is put into the cache", serviceName);
    }

    public static void removeService(String serviceName) {
        localServiceCache.remove(serviceName);
        log.info("the service {} is removed from the cache", serviceName);
    }



}
