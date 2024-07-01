package com.better.client.Netty;

import com.better.client.Client;
import com.better.codec.DefaultLengthFieldFrameDecoder;
import com.better.codec.SharableMessageCodec;
import com.better.wrappers.RpcMessageWrapper;
import com.better.exceptions.RpcException;
import com.better.factories.SingletonFactory;
import com.better.handler.RpcResponseHandler;
import com.better.protocol.RpcMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 1.首先应该构建通信连接的channel
 * 2.该client具有一个发送rpc请求的方法
 */
@Slf4j
public class NettyClient implements Client {
    private final Bootstrap bootstrap;
    private final EventLoopGroup group = new NioEventLoopGroup();
    private final ChannelProvider channelProvider;


    public NettyClient() {
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);


        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //粘包，半包处理
                        pipeline.addLast(new DefaultLengthFieldFrameDecoder());
                        //编解码
                        pipeline.addLast(new SharableMessageCodec());
                        //rpc事件处理器,在客户端应该添加的是响应（response）信息处理器
                        pipeline.addLast(new RpcResponseHandler());
                    }
                });
        //到此，bootstrap构造完成
    }


    /**
     * 再做一次分离，将getChannel和connect分离
     * 先查找channel缓存
     */
    public Channel getChannel(String host, int port){
        Channel channel = channelProvider.getChannel(host, port);

        if (channel != null){
            return channel;
        }else {
            //缓存未命中,通过connect更新缓存
            channel = connect(host,port);
            if (channel == null){
                log.debug("connect failed in Method getChannel");
            }
        }
        return channel;
    }

    /**
     * 拿到连接完成的channel
     */
    public Channel connect(String host, int port) {
            //1.同步等待获取连接
//            try {
//                log.debug("Connecting to " + host + ":" + port + " in sync mode!");
//                bootstrap.connect(host, port).sync();
//            } catch (InterruptedException e) {
//                log.debug("Failed to connect to " + host + ":" + port + " in sync mode!");
//                throw new RuntimeException(e);
//            }

            //2.异步获取连接并等待通知,借助于另一个completableFuture进行通知
        CompletableFuture<Channel> channelCompletableFuture = new CompletableFuture<>();
        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
                log.info("Connected to " + host + ":" + port + " in async mode!");
                if (future.isSuccess()) {
                    //到达这里，响应地址的channel一定不在缓存中
                    channelProvider.setChannel(host,port,future.channel());
                    log.debug("successfully update channel cache and notified!");
                    //通知
                    channelCompletableFuture.complete(future.channel());
                }else {
                    throw new RuntimeException("future callback failed when connected to " + host + ":" + port + " in async mode!");
                }
            });

        try {
            Channel channel = channelCompletableFuture.get();
            log.debug("successfully get connected channel to " + host + ":" + port + " in async mode!");
            channel.closeFuture().addListener(future -> {
                log.debug("connection closed: host=" + host + " port=" + port + " channel=" + channel);
            });
            return channel;
        } catch (Exception e) {
//            log.debug("unexpect exception when try to get channel from CompletableFuture!", e.getMessage());
            throw new RpcException("unexpect exception when try to get channel from CompletableFuture!",e);
        }
    }


    /**
     * 发送消息后有两件主要要做的事情
     * 1.当消息发送成功/失败时要进行通知
     * 2.当收到响应消息后，需要知道唯一的请求序列号，这个序列号与RpcMessage序号一致,并且响应结果要返回到发起请求的方法调用
     * @return 响应信息
     */
    @Override
    public RpcMessage sendRpcRequest(RpcMessageWrapper rpcMessageWrapper) {
        String hostName = rpcMessageWrapper.getHost();
        int port = rpcMessageWrapper.getPort();
        Channel channel = getChannel(hostName,port);
        //promise对象用来记录rpc调用的情况
        DefaultPromise<RpcMessage> promise = new DefaultPromise<>(channel.eventLoop());
        RpcResponseHandler.UNPROCESSED_RESPONSES.put(rpcMessageWrapper.getRpcMessage().getMessageHeader().getId(),promise);

        channel.writeAndFlush(rpcMessageWrapper.getRpcMessage()).addListener((ChannelFutureListener)future -> {
            if (future.isSuccess()) {
                log.debug("successfully send rpc message to " + hostName + ":" + port + " in async mode!");
            }else {
                log.error("fail to send rpcMessage to " + hostName + ":" + port + " in async mode!",future.cause());
                promise.setFailure(future.cause());
            }
        });

        return getRpcMessage(rpcMessageWrapper.getTimeOut(), promise);
    }

    private RpcMessage getRpcMessage(Integer timeOut, DefaultPromise<RpcMessage> promise) {
        //等待接受rpc调用产生的响应结果
        try {
            if(timeOut == null && timeOut <= 0) {
                promise.await();
            }else {
                promise.await(timeOut, TimeUnit.MILLISECONDS);
            }

            if (promise.isSuccess()){
                RpcMessage rpcMessage = promise.getNow();
                if (rpcMessage != null){
                    return rpcMessage;
                }
            }else {
                if (promise.cause() == null){
                    promise.setFailure(new RpcException("fail to get rpc response,and remote failed reason unknown!"));
                }
                log.debug("rpc failed when try to get result from promise!");
            }

        }catch (Exception e){
            log.debug("something wrong when wait for promise to complete! {}",e.getMessage());
        }
        return null;
    }
}
