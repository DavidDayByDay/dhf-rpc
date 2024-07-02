package com.better.protocol;

import com.better.pojos.RequestMessage;
import com.better.pojos.ResponseMessage;
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
        MessageHeader messageHeader1 = MessageHeader.defualtMessageHeader();
        messageHeader1.setMessageType((byte) 1);
        MessageHeader responseMessageHead = messageHeader1;
        rpcMessage.setMessageHeader(responseMessageHead);
        return rpcMessage;
    }
}
