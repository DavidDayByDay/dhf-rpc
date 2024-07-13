package com.better.handler;

import com.better.client.Client;
import com.better.config.ClientConfig;
import com.better.discovery.ServiceDiscovery;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyInterceptor implements MethodInterceptor {
    private final ServiceDiscovery serviceDiscovery;
    private final String serviceName;
    private final Client client;
    private final ClientConfig clientConfig;

    public CglibProxyInterceptor(Client client, ClientConfig clientConfig, String serviceName, ServiceDiscovery serviceDiscovery) {
        this.client = client;
        this.clientConfig = clientConfig;
        this.serviceDiscovery = serviceDiscovery;
        this.serviceName = serviceName;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return RemoteRrocedureCall.remoteCall(client,serviceDiscovery,method,args,serviceName,clientConfig);
    }
}
