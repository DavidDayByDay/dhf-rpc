package com.better.handler;

import com.better.client.Client;
import com.better.config.ClientConfig;
import com.better.discovery.ServiceDiscovery;
import com.better.exceptions.MessageException;
import com.better.protocol.RpcMessage;
import com.better.protocol.messages.ResponseMessage;
import com.better.utils.RpcRequestMessageMaker;
import com.better.wrappers.RpcMessageWrapper;

import java.lang.reflect.Method;

public class RemoteProcedureCall {
    public static Object remoteCall(Client client, ServiceDiscovery serviceDiscovery, Method method, Object[] args, String serviceName, ClientConfig clientConfig) throws MessageException {
        //1.rpcMessageWrapper
        RpcMessageWrapper rpcMessageWrapper = RpcRequestMessageMaker.makeRpcRequestMessageWrapper(serviceDiscovery, method, args, serviceName, clientConfig);
        //2.send rpc
        RpcMessage response = client.sendRpcRequest(rpcMessageWrapper);
        //3.deal with response to adapt the correct return value of the method
        ResponseMessage messageBody = (ResponseMessage) response.getMessageBody();
        return messageBody.getResponse();
    }
}
