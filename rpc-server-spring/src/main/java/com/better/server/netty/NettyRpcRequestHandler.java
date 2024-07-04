package com.better.server.netty;

import com.better.enums.MessageStatus;
import com.better.enums.MessageType;
import com.better.handler.RpcRequestHandler;
import com.better.pojos.RequestMessage;
import com.better.pojos.ResponseMessage;
import com.better.protocol.MessageHeader;
import com.better.protocol.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty 服务端的请求处理器
 */

public class NettyRpcRequestHandler extends SimpleChannelInboundHandler<RpcMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        //1.检查是否request类型
        if (rpcMessage.getMessageHeader().getMessageType() != MessageType.REQUEST.getType()){
            return;
        }

        RequestMessage request = (RequestMessage) rpcMessage.getMessageBody();

        //2.通过反射调用对应方法
        Object result = RpcRequestHandler.Handle(request);
        //3.包装结果
        MessageHeader messageHeader = rpcMessage.getMessageHeader();
        messageHeader.setMessageType(MessageType.RESPONSE.getType());
        messageHeader.setStatus(MessageStatus.SUCCESS.getType());

        //rpcMessage
        RpcMessage response = new RpcMessage();
        response.setMessageHeader(messageHeader);

        //responseMessage
        ResponseMessage responseMessageBody = new ResponseMessage();
        responseMessageBody.setException(null);
        responseMessageBody.setResponse(result);
        response.setMessageBody(responseMessageBody);

        //4.写回
        ctx.writeAndFlush(response);

    }
}
