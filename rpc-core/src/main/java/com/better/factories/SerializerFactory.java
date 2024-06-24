package com.better.factories;

import com.better.enums.SerializerType;
import com.better.exceptions.MessageException;
import com.better.exceptions.SerializeException;
import com.better.serializer.*;

public class SerializerFactory {
    public static final Serializer getSerializer(byte serializerTypeNum) throws SerializeException {

        try {
            SerializerType serializerType = SerializerType.parseByType(serializerTypeNum);
            switch (serializerType){
                case JDK:
                    return new JDKSerializer();
                case JSON:
                    return new JSONSerializer();
                case HESSIAN:
                    return new HessianSerializer();
                case KRYO:
                    return new KryoSerializer();
                case PROTOSTUFF:
                    return new ProtobufSerializer();
            }
        } catch (MessageException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
}
