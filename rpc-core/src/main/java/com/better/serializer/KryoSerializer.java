package com.better.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
public class KryoSerializer implements Serializer {
    /**
     * 1.kryo 线程不安全
     * 2.不要开启注册行为
     */
    private final Kryo kryo;

    public KryoSerializer() {
        kryo = new Kryo();
    }

    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);


        kryo.register(Object[].class);
        kryo.writeClassAndObject(output, obj);

        output.close();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Input input = new Input(bis);
        Object o = kryo.readClassAndObject(input);
        input.close();
        return (T) o;
    }
}


