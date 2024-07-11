package com.better.proxy;

import com.better.client.Client;
import com.better.handler.JDKProxyHandler;
import com.better.config.ClientConfig;
import com.better.discovery.ServiceDiscovery;
import com.better.utils.ServiceInfoUtils;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 单例使用
 */
public class ProxyFactory {
    private final ServiceDiscovery serviceDiscovery;
    private final Client client;
    private final ClientConfig config;

    private static final Map<String, Object> PROXY_MAP = new ConcurrentHashMap<>();

    public ProxyFactory(ServiceDiscovery serviceDiscovery, Client client, ClientConfig config) {
        this.serviceDiscovery = serviceDiscovery;
        this.client = client;
        //默认配置
        this.config = config;
    }

    //todo 修改proxyFactory的逻辑使得makeProxy可以对不同的服务注入不同配置的Config
    public <T> T makeProxy(Class<T> interfaceClass, String version,boolean usingJDK) {
        String serviceName = ServiceInfoUtils.serviceKey(interfaceClass.getName(),version);

        //todo 添加cglib实现
        if (usingJDK){
            return (T) PROXY_MAP.computeIfAbsent(serviceName, k -> {
                return Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, new JDKProxyHandler(
                        client, config, serviceName, serviceDiscovery));
            });
        }
        else return null;
    }



}
