package com.better.handler;

import com.better.client.Client;
import com.better.pojos.ResponseMessage;
import com.better.properties.RpcClientProperties;
import com.better.protocol.RpcMessage;
import com.better.registryanddiscovery.discovery.ServiceDiscovery;
import com.better.utils.RpcRequestMessageMaker;
import com.better.wrappers.RpcMessageWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 对rpc调用信息进行包装的工具类
 */
public class JDKProxyHandler implements InvocationHandler {
    private Client client;
    private ServiceDiscovery serviceDiscovery;
    private RpcClientProperties properties;
    private String serviceName;

    public JDKProxyHandler(Client client, RpcClientProperties properties, String serviceName, ServiceDiscovery serviceDiscovery) {
        this.client = client;
        this.properties = properties;
        this.serviceName = serviceName;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.rpcMessageWrapper
        RpcMessageWrapper rpcMessageWrapper = RpcRequestMessageMaker.makeRpcRequestMessageWrapper(serviceDiscovery, method, args, serviceName, properties);
        //2.send rpc
        RpcMessage response = client.sendRpcRequest(rpcMessageWrapper);
        //todo 3.deal with response to adapt the correct return value of the method
        ResponseMessage messageBody = (ResponseMessage) response.getMessageBody();
        Object result = messageBody.getResponse();

        return result;
    }
}

