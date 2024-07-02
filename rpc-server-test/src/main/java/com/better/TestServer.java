package com.better;

import com.better.codec.DefaultLengthFieldFrameDecoder;
import com.better.codec.SharableMessageCodec;
import com.better.protocol.MessageHeader;
import com.better.protocol.RpcMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ChannelFuture channelActive = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new DefaultLengthFieldFrameDecoder());
                        pipeline.addLast(new SharableMessageCodec());
                        pipeline.addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                RpcMessage rpcMessage = (RpcMessage) msg;
                                log.debug("received msg: {}", msg);
                                RpcMessage defaultRpcMessageWithResponse = RpcMessage.getDefaultRpcMessageWithResponse();
                                int id = ((RpcMessage) msg).getMessageHeader().getId();
                                MessageHeader messageHeader = defaultRpcMessageWithResponse.getMessageHeader();
                                messageHeader.setId(id);
                                defaultRpcMessageWithResponse.setMessageHeader(messageHeader);
                                ctx.writeAndFlush(defaultRpcMessageWithResponse);
                                log.debug("send response: {}", msg);

                            }
                                         }
                        );
                    }
                })
                .bind(8080);

    }
}
