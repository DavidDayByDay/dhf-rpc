package com.better.factories;

import com.better.enums.SerializerType;
import com.better.exceptions.MessageException;
import com.better.exceptions.SerializeException;
import com.better.serializer.*;

public class SerializerFactory {
    public static Serializer getSerializer(byte serializerTypeNum) throws SerializeException {
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
                    return new ProtoStuffSerializer();
            }
        } catch (MessageException e) {
            throw new RuntimeException(e);
        }
        throw new SerializeException("unknown serializer typeNum: " + serializerTypeNum);
    }
}
