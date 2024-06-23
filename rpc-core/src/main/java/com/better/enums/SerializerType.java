package com.better.enums;

import com.better.exceptions.MessageException;
import lombok.Getter;

public enum SerializerType {
    /**
     * JDK
     */
    JDK((byte) 0),
    /**
     * JSON 序列化算法
     */
    JSON((byte) 1),

    /**
     * HESSIAN 序列化算法
     */
    HESSIAN((byte) 2),

    /**
     * KRYO 序列化算法
     */
    KRYO((byte) 3),

    /**
     * PROTOSTUFF 序列化算法
     */
    PROTOSTUFF((byte) 4);



    @Getter
    private final byte type;

    SerializerType(byte type) {
        this.type = type;
    }

    public static SerializerType parseByName(String serializerName) throws MessageException {
        for (SerializerType type : SerializerType.values()){
            if (type.name().equals(serializerName)){
                return type;
            }
        }
        throw new MessageException("no exist serializer type: " + serializerName);
    }

    public static SerializerType parseByType(byte type) throws MessageException {
        for (SerializerType serializerType : SerializerType.values()){
            if (serializerType.getType()==type){
                return serializerType;
            }
        }
        throw new MessageException(String.format("no such type %s SerializerType exist!",type));
    }

}
