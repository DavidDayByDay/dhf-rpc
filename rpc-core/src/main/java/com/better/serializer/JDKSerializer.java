package com.better.serializer;

import org.yaml.snakeyaml.serializer.SerializerException;

import java.io.*;

/**
 * JDK serializer 序列化
 */

public class JDKSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        }catch (IOException e){
            throw new SerializerException(e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            T o = (T) ois.readObject();
            return o;
        }catch (IOException | ClassNotFoundException e){
            throw new SerializerException(e.getMessage() + "\n more for possible non-exit class: " + clazz.getName());
        }

    }

}
