package com.better.protocol;

import com.better.protocol.messages.RequestMessage;
import com.better.protocol.messages.ResponseMessage;
import lombok.Data;

/**
 * Rpc协议消息类
 */


@Data
public class RpcMessage {
    //请求头
    private MessageHeader messageHeader;
    //消息正文
    private Object messageBody;

    public static RpcMessage getDefaultRpcMessageWithRequest(){
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMessageBody(RequestMessage.getDefaultRequestMessage());
        rpcMessage.setMessageHeader(MessageHeader.defualtMessageHeader());
        return rpcMessage;
    }

    public static RpcMessage getDefaultRpcMessageWithResponse(){
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMessageBody(ResponseMessage.getDefaultResponseMessage());
        MessageHeader messageHeader = MessageHeader.defualtMessageHeader();
        messageHeader.setMessageType((byte) 1);
        rpcMessage.setMessageHeader(messageHeader);
        return rpcMessage;
    }
}
