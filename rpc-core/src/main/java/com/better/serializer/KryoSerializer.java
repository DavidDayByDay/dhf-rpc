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
     * 1.kryo 线程不安全,本框架中每次序列化时都会得到一个新的序列化器对象
     * 2.不要开启注册行为
     */
    public static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>(){
        @Override
        public Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setReferences(false);
            kryo.setRegistrationRequired(false);
            return kryo;
        }
    };

    public KryoSerializer() {
    }


    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);


        Kryo kryo = kryoLocal.get();
//        kryo.register(Object[].class);
//        kryo.register(TestObject.class);
//        kryo.register(Class.class);
//        kryo.register(LoadBalance.class);
        kryo.writeClassAndObject(output, obj);

        output.close();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Input input = new Input(bis);
        Kryo kryo = kryoLocal.get();
        Object o = kryo.readClassAndObject(input);
        input.close();
        return (T) o;
    }
}


