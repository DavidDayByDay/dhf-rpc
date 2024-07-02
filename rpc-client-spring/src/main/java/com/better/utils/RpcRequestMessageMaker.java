package com.better.utils;

import com.better.constants.MessageConstants;
import com.better.enums.LoadbalanceType;
import com.better.enums.MessageType;
import com.better.enums.SerializerType;
import com.better.exceptions.MessageException;
import com.better.pojos.RequestMessage;
import com.better.pojos.ServiceInfo;
import com.better.properties.RpcClientProperties;
import com.better.protocol.MessageHeader;
import com.better.protocol.RpcMessage;
import com.better.registryanddiscovery.discovery.ServiceDiscovery;
import com.better.wrappers.RpcMessageWrapper;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 提取rpc请求信息工具类
 * 1.需要服务发现 --> host port serviceName
 * 2.需要
 */
public class RpcRequestMessageMaker {

    /**
     * requestMessage生成
     *
     * @param method
     * @param args
     * @return 返回的requestMessage还缺少要远程调用的serviceName
     */
    public static RpcMessageWrapper makeRpcRequestMessageWrapper(ServiceDiscovery serviceDiscovery,Method method, Object[] args, String serviceName, RpcClientProperties properties) throws MessageException {
        //1.rpcMessage
        Class<?>[] parameters = Arrays.stream(method.getParameters()).map(parameter -> parameter.getClass()).toArray(Class<?>[]::new);
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setMethodName(method.getName());
        requestMessage.setParameterTypes(parameters);
        requestMessage.setParameterValues(args);
        requestMessage.setServiceName(serviceName);

        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMessageBody(requestMessage);

        MessageHeader messageHeader = MessageHeader.builder()
                .magicNumber(MessageConstants.MAGIC_NUMBER)
                .version(MessageConstants.VERSION)
                .serializerType(SerializerType.parseByName(properties.getSerializerType()).getType())
                .messageType(MessageType.REQUEST.getType())
                .status((byte) 0)
                .id(MessageConstants.getMessageIdAtomically())
                .build();
//                .messageLength() 不同设置

        rpcMessage.setMessageHeader(messageHeader);


        //2.wrapper
        RpcMessageWrapper rpcMessageWrapper = new RpcMessageWrapper();
        ServiceInfo serviceInfo = serviceDiscovery.discover(serviceName, LoadbalanceType.parseByName(properties.getLoadbalance()));


        rpcMessageWrapper.setRpcMessage(rpcMessage);
        rpcMessageWrapper.setHost(serviceInfo.getServiceHost());
        rpcMessageWrapper.setPort(serviceInfo.getServicePort());
        rpcMessageWrapper.setTimeOut(properties.getTimeOut());

        return rpcMessageWrapper;
    }
}
