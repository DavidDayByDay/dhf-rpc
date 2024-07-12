package com.better.utils;

import com.better.constants.MessageConstants;
import com.better.enums.LoadbalanceType;
import com.better.enums.MessageType;
import com.better.enums.SerializerType;
import com.better.exceptions.MessageException;
import com.better.protocol.messages.RequestMessage;
import com.better.protocol.ServiceRegisterInfo;
import com.better.config.ClientConfig;
import com.better.protocol.MessageHeader;
import com.better.protocol.RpcMessage;
import com.better.discovery.ServiceDiscovery;
import com.better.wrappers.RpcMessageWrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
    public static RpcMessageWrapper makeRpcRequestMessageWrapper(ServiceDiscovery serviceDiscovery,Method method, Object[] args, String serviceName, ClientConfig clientConfig) throws MessageException {
        //1.rpcMessage
        Class<?>[] parameters = (Class<?>[]) Arrays.stream(method.getParameters()).map(Parameter::getParameterizedType).toArray(java.lang.reflect.Type[]::new);
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
                .serializerType(SerializerType.parseByName(clientConfig.getSerializerType()).getType())
                .messageType(MessageType.REQUEST.getType())
                .status((byte) 0)
                .id(MessageConstants.getMessageIdAtomically())
                .build();
//                .messageLength() 不用设置

        rpcMessage.setMessageHeader(messageHeader);


        //2.wrapper
        RpcMessageWrapper rpcMessageWrapper = new RpcMessageWrapper();
        ServiceRegisterInfo serviceRegisterInfo = serviceDiscovery.discover(requestMessage, LoadbalanceType.parseByName(clientConfig.getLoadbalance()));


        rpcMessageWrapper.setRpcMessage(rpcMessage);
        rpcMessageWrapper.setHost(serviceRegisterInfo.getServiceHost());
        rpcMessageWrapper.setPort(serviceRegisterInfo.getServicePort());
        rpcMessageWrapper.setTimeOut(clientConfig.getTimeOut());

        return rpcMessageWrapper;
    }
}
