package com.better.server.netty;

import com.better.codec.DefaultLengthFieldFrameDecoder;
import com.better.codec.SharableMessageCodec;
import com.better.server.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRpcServer implements RpcServer {
    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    private final ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;
    private final String host;
    private final int port;


    public NettyRpcServer(String host, int port) {
        this.host = host;
        this.port = port;
        serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                //tcp默认开启nagle算法，进行大数据快传输，减少网络传输，但增加调用延迟
                .childOption(ChannelOption.TCP_NODELAY, true)
                //开启tcp心跳机制，检测连接情况
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //最大已连接tcp缓存数
                .childOption(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new DefaultLengthFieldFrameDecoder());
                        pipeline.addLast(new SharableMessageCodec());
                        pipeline.addLast(new NettyRpcRequestHandler());
                    }
                });
    }


//    public void doBind(String host,int port) {
//        ChannelFuture bind = serverBootstrap.bind(host, port);
//    }
//
//  public void doBind(SocketAddress socketAddress){
//      ChannelFuture bind = serverBootstrap.bind(socketAddress);
//  }


    @Override
    public void start() {
        try {
            channelFuture = serverBootstrap.bind(host, port).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.debug("successfully started server, and the host: {}, port: {}", host, port);
                }
            });
            channelFuture.channel().closeFuture().sync();
            channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.debug("successfully stopped server, and the host: {}, port: {}", host, port);
                }
            });



        } catch (Exception e) {
            log.error("something wrong when start server! port:{}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.debug("groups stop");
        }

    }
}
