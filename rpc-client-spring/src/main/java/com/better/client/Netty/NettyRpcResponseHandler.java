package com.better.client.Netty;

import com.better.handler.RpcResponseHandler;
import com.better.protocol.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 当收到rpc响应信息时的处理器
 */
@Slf4j
public class NettyRpcResponseHandler extends SimpleChannelInboundHandler<RpcMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        try {
            RpcResponseHandler.handle(rpcMessage);
        } finally {
            //显示地释放内存
            if (rpcMessage != null){
                ReferenceCountUtil.release(rpcMessage);
            }
        }
    }

 }
