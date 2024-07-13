package com.better.handler;

import com.better.client.Client;
import com.better.config.ClientConfig;
import com.better.discovery.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 对rpc调用信息进行包装的工具类
 */
public class JDKProxyHandler implements InvocationHandler {
    private final Client client;
    private final ServiceDiscovery serviceDiscovery;
    private final ClientConfig clientConfig;
    private final String serviceName;

    public JDKProxyHandler(Client client, ClientConfig clientConfig, String serviceName, ServiceDiscovery serviceDiscovery) {
        this.client = client;
        this.clientConfig = clientConfig;
        this.serviceName = serviceName;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return RemoteRrocedureCall.remoteCall(client,serviceDiscovery,method,args,serviceName,clientConfig);
    }


}

