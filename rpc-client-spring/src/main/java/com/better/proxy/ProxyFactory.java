package com.better.proxy;

import com.better.client.Client;
import com.better.handler.JDKProxyHandler;
import com.better.config.ClientConfig;
import com.better.registryanddiscovery.discovery.ServiceDiscovery;
import com.better.utils.ServiceInfoUtils;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 单例使用
 */
public class ProxyFactory {
    private ServiceDiscovery serviceDiscovery;
    private Client client;
    private ClientConfig config;

    private static final Map<String, Object> PROXY_MAP = new ConcurrentHashMap<>();

    public ProxyFactory(ServiceDiscovery serviceDiscovery, Client client, ClientConfig config) {
        this.serviceDiscovery = serviceDiscovery;
        this.client = client;
        this.config = config;
    }

    public <T> T makeProxy(Class<T> interfaceClass, String version) {
        String servicename = ServiceInfoUtils.serviceKey(ServiceInfoUtils.getServiceNameByInterface(interfaceClass),version);

        return (T) PROXY_MAP.computeIfAbsent(servicename, k -> {
            return Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, new JDKProxyHandler(
                    client, config, servicename, serviceDiscovery));
        });
    }



}
