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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ChannelFuture channelActive = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
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
        Channel channel = channelActive.channel();
        log.debug("connecting channel: {}", channel);
        Channel channel1 = channelActive.sync().channel();
        log.debug("connecting channel1: {}", channel1);

        channelActive.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.debug("connect success");
            }
        });

        channelActive.channel().closeFuture().addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.debug("close channel");
            }
        });



    }
}
