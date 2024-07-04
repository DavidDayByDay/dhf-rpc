package com.better.handler;

import com.better.pojos.RequestMessage;
import com.better.provider.LocalServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class RpcRequestHandler {

    public static Object Handle(RequestMessage request) {
        if (request == null) {
            log.debug("request is null when handle in RpcRequestHandler");
            return null;
        }

        //已实现的服务缓存在本地列表中
        String serviceName = request.getServiceName();
        Object o = LocalServiceProvider.getService(serviceName);
//        String interfaceName = serviceName.substring(0,serviceName.lastIndexOf("-"));
        try {
            //约定了接口及其实现的命名规则
//            Class<?> interfaceClass = Class.forName(interfaceName);
            //todo 可以不需要cast
//            Object o = interfaceClass.cast(localServiceCache.get(serviceName));
            Method declaredMethod = o.getClass().getDeclaredMethod(request.getMethodName(), request.getParameterTypes());
            declaredMethod.setAccessible(true);
            //todo
            Object result = declaredMethod.invoke(o,request.getParameterValues());
            return result;
        } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("something went wrong when handle in RpcRequestHandler", e);
            throw new RuntimeException(e);
        }


    }

}
