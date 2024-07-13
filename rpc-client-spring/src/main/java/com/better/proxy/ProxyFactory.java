package com.better.proxy;

import com.better.client.Client;
import com.better.config.ClientConfig;
import com.better.discovery.ServiceDiscovery;
import com.better.handler.CglibProxyInterceptor;
import com.better.handler.JDKProxyHandler;
import com.better.utils.ServiceInfoUtils;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 单例使用
 */
public class ProxyFactory {
    private final ServiceDiscovery serviceDiscovery;
    private final Client client;
//    private final ClientConfig clientConfig;

    private static final Map<String, Object> PROXY_MAP = new ConcurrentHashMap<>();

    public ProxyFactory(ServiceDiscovery serviceDiscovery, Client client, ClientConfig clientConfig) {
        this.serviceDiscovery = serviceDiscovery;
        this.client = client;
        //默认配置
//        this.clientConfig = clientConfig;
    }

    //修改proxyFactory的逻辑使得makeProxy可以对不同的服务注入不同配置的Config
    public <T> T makeProxy(Class<T> superClass,String version,ClientConfig clientConfig){
        if (superClass.isInterface()){
            return doMakeProxy(superClass,version,true,clientConfig);
        }else {
            return doMakeProxy(superClass,version,false,clientConfig);
        }
    }


    private  <T> T doMakeProxy(Class<T> interfaceClass, String version, boolean usingJDK,ClientConfig clientConfig) {
        String serviceName = ServiceInfoUtils.serviceKey(interfaceClass.getName(), version);

        //todo 添加cglib实现
        if (usingJDK) {
            return (T) PROXY_MAP.computeIfAbsent(serviceName, k -> {
                return Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, new JDKProxyHandler(
                        client, clientConfig, serviceName, serviceDiscovery));
            });
        } else {
            //cglib
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(interfaceClass);
            enhancer.setCallback(new CglibProxyInterceptor(client,clientConfig,serviceName,serviceDiscovery));
            return (T) enhancer.create();
        }
    }


}
