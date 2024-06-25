package com.better.protocol;

import com.better.constants.MessageConstants;
import lombok.Builder;
import lombok.Data;

import static com.better.constants.MessageConstants.MAGIC_NUMBER;
import static com.better.constants.MessageConstants.VERSION;

/**
 * 自定义协议--消息头
 * <pre>
 *   --------------------------------------------------------------------
 *  | 魔数 (4byte) | 版本号 (1byte)  | 序列化算法 (1byte)  | 消息类型 (1byte) |
 *  -------------------------------------------------------------------
 *  |  状态类型 (1byte)  |     消息序列号 (4byte)   |     消息长度 (4byte)    |
 *  --------------------------------------------------------------------
 * </pre>
 *
 * 序列化算法以及消息类型和状态类型都会用Enum类表示
 */

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class MessageHeader {
    //魔数，用于迅速判断是否为有效信息
    private byte[] magicNumber;
    //Rpc版本
    private byte version;
    //序列化类型（JSON，kryo，JDK,Hessian,protostuff）
    private byte serializerType;
    //消息类型（请求，响应，心跳）
    private byte messageType;
    //状态类型（成功，失败）
    private byte status;
    //消息序列号（唯一Id）
    private int id;
    //消息长度
    private int messageLength;

    //具体的序列化方式应该由用户选择，消息状态及类型，长度不应由用户决定
    public static MessageHeader buildMessageHeader(byte serializerType,byte messageType,byte status,int length){
        return MessageHeader.builder()
                .magicNumber(MAGIC_NUMBER)
                .version(VERSION)
                .serializerType(serializerType)
                .messageType(messageType)
                .status(status)
                .id(MessageConstants.getMessageIdAtomically())
                .messageLength(length)
                .build();

    }

    public static MessageHeader defualtMessageHeader(){
        return MessageHeader.builder()
                .magicNumber(MAGIC_NUMBER)
                .version(VERSION)
                .serializerType((byte) 0)
                .messageType((byte) 0)
                .status((byte) 0)
                .id(MessageConstants.getMessageIdAtomically())
                .messageLength(0)
                .build();
    }


}
