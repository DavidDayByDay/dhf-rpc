package com.better.handler;

import com.better.client.Client;
import com.better.protocol.messages.ResponseMessage;
import com.better.config.ClientConfig;
import com.better.protocol.RpcMessage;
import com.better.discovery.ServiceDiscovery;
import com.better.utils.RpcRequestMessageMaker;
import com.better.wrappers.RpcMessageWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 对rpc调用信息进行包装的工具类
 */
public class JDKProxyHandler implements InvocationHandler {
    private final Client client;
    private final ServiceDiscovery serviceDiscovery;
    private final ClientConfig config;
    private final String serviceName;

    public JDKProxyHandler(Client client, ClientConfig clientConfig, String serviceName, ServiceDiscovery serviceDiscovery) {
        this.client = client;
        this.config = clientConfig;
        this.serviceName = serviceName;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.rpcMessageWrapper
        RpcMessageWrapper rpcMessageWrapper = RpcRequestMessageMaker.makeRpcRequestMessageWrapper(serviceDiscovery, method, args, serviceName, config);
        //2.send rpc
        RpcMessage response = client.sendRpcRequest(rpcMessageWrapper);
        //todo 3.deal with response to adapt the correct return value of the method
        ResponseMessage messageBody = (ResponseMessage) response.getMessageBody();
        return messageBody.getResponse();
    }
}

