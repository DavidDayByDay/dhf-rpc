package com.better.codec;

import com.better.constants.MessageConstants;
import com.better.enums.MessageType;
import com.better.exceptions.MessageException;
import com.better.factories.SerializerFactory;
import com.better.message.RequestMessage;
import com.better.message.ResponseMessage;
import com.better.protocol.MessageHeader;
import com.better.protocol.RpcMessage;
import com.better.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Arrays;
import java.util.List;

@Sharable
public class SharableMessageCodec extends MessageToMessageCodec<ByteBuf, RpcMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, List<Object> list) throws Exception {
        //todo 每次调用都会声明一个ByteBuf --？是否使用了池化
        ByteBuf buf = ctx.alloc().buffer();

        MessageHeader messageHeader = rpcMessage.getMessageHeader();
        buf.writeBytes(messageHeader.getMagicNumber());
        buf.writeByte(messageHeader.getVersion());
        buf.writeByte(messageHeader.getSerializerType());
        buf.writeByte(messageHeader.getMessageType());
        buf.writeByte(messageHeader.getStatus());
        buf.writeInt(messageHeader.getId());
        Object message = rpcMessage.getMessageBody();
        //消息体序列化
        //1.获取序列化方法
        Serializer serializer = SerializerFactory.getSerializer(messageHeader.getSerializerType());
        byte[] bytes = serializer.serialize(message);
        //消息长度 int
        int length = bytes.length;
        buf.writeInt(length);
        buf.writeBytes(bytes);
        //完成消息写入并向下传递
        list.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        //1.验证魔数和版本号
        byte[] magicNumber = new byte[MessageConstants.MAGIC_NUMBER.length];
        buf.readBytes(magicNumber, 0, magicNumber.length);
        for (int i = 0; i < magicNumber.length; i++) {
            if (magicNumber[i] != MessageConstants.MAGIC_NUMBER[i]) {
                throw new MessageException(String.format("incorrect magic number: %s", Arrays.toString(magicNumber)));
            }
        }

        byte version = buf.readByte();
        if (version != MessageConstants.VERSION) {
            throw new MessageException(String.format("incorrect version: %s", version));
        }
        //先拿到所有基本信息
        byte serializerType = buf.readByte();
        byte messageType = buf.readByte();
        byte messageStatus = buf.readByte();
        int id = buf.readInt();
        int messageLength = buf.readInt();
        byte[] bytes = new byte[messageLength];
        //得到消息体
        buf.readBytes(bytes, 0, messageLength);

        //构造新的rpcMessage对象进行传递
        MessageHeader messageHead = MessageHeader.builder()
                .magicNumber(magicNumber)
                .version(version)
                .serializerType(serializerType)
                .messageType(messageType)
                .status(messageStatus)
                .id(id)
                .messageLength(messageLength)
                .build();

        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMessageHeader(messageHead);

        //2.根据序列化算法反序列化
        Serializer serializer = SerializerFactory.getSerializer(serializerType);
        MessageType actualMessageType = MessageType.parseByType(messageType);
        boolean isDone = false;
        switch (actualMessageType) {
            case REQUEST -> {
                RequestMessage request = serializer.deserialize(bytes, RequestMessage.class);
                rpcMessage.setMessageBody(request);
                isDone = true;
            }
            case RESPONSE -> {
                ResponseMessage response = serializer.deserialize(bytes,ResponseMessage.class);
                rpcMessage.setMessageBody(response);
                isDone = true;
            }
            //default-->HeartBeatMessage
            default -> {
                if (isDone != true){
                    String heartBeatMessage = serializer.deserialize(bytes, String.class);
                    rpcMessage.setMessageBody(heartBeatMessage);
                    isDone = true;
                }
            }
        }
        //传递
        list.add(rpcMessage);
    }

}
