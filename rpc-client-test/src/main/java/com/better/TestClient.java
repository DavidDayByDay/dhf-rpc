package com.better;

import com.better.codec.DefaultLengthFieldFrameDecoder;
import com.better.codec.SharableMessageCodec;
import com.better.protocol.MessageHeader;
import com.better.protocol.RpcMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestClient {
    public static void main(String[] args) {
        //准备一条测试信息
        RpcMessage rpcMessage = new RpcMessage();
        MessageHeader header = MessageHeader.builder()
                .magicNumber(new byte[]{(byte) 'M', (byte) 'R', (byte) 'P', (byte) 'C'})
                .version((byte) 1)
                .serializerType((byte) 0)
                .messageType((byte) 0)
                .status((byte) 0)
                .id(1)
                .build();

        rpcMessage.setMessageHeader(header);
        rpcMessage.setMessageBody(Integer.valueOf(1000));


        try {
            new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DefaultLengthFieldFrameDecoder());
                            socketChannel.pipeline().addLast(new SharableMessageCodec());
                        }
                    })
                    .connect("127.0.0.1", 8080)
                    .sync()
                    .channel();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
