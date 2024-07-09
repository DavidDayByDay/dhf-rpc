package com.better.serializer;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoStuffSerializer implements Serializer{

    private final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public <T> byte[] serialize(T obj) {
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
        try {
            byte[] byteArray = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
            return byteArray;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(clazz);
        T message = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data,message,schema);
        return message;
    }
}
