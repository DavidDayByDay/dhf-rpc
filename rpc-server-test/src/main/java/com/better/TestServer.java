package com.better;

import com.better.codec.DefaultLengthFieldFrameDecoder;
import com.better.codec.SharableMessageCodec;
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
//                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
//                            @Override
//                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
//                                log.info("channelActive");
//                            }
//                        });
                        pipeline.addLast(new DefaultLengthFieldFrameDecoder());
                        pipeline.addLast(new SharableMessageCodec());
                        pipeline.addLast(new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println(msg);
                                RpcMessage defaultRpcMessageWithResponse = RpcMessage.getDefaultRpcMessageWithResponse();
                                ctx.writeAndFlush(defaultRpcMessageWithResponse);
                                ctx.write(msg, promise);
                            }

                        });
                    }
                })
                .bind(8080);

    }
}
