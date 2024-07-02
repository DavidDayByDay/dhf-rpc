package com.better.server.netty;

import com.better.enums.MessageType;
import com.better.pojos.RequestMessage;
import com.better.protocol.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty 服务端的请求处理器
 */

public class NettyRpcResponseHandler extends SimpleChannelInboundHandler<RpcMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        //1.检查是否request类型
        if (rpcMessage.getMessageHeader().getMessageType() != MessageType.REQUEST.getType()){
            return;
        }

        RequestMessage request = (RequestMessage) rpcMessage.getMessageBody();
        //todo 2.client端是根据当前服务已注册的服务名找服务地址的
//        if (request.getServiceName() != ServerConfig)

        //3.通过反射调用对应方法


    }
}
