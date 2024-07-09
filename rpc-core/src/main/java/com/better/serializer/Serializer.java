package com.better.serializer;

import java.io.IOException;

public interface Serializer {
    /**
     *接口内所有方法默认是public abstract
     *接口内只有静态变量，可以定义具有方法体的static方法或者default方法
     */

    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException;


}
