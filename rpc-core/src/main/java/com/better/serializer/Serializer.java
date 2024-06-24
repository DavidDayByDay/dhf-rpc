package com.better.serializer;

public interface Serializer {
    /**
     *接口内所有方法默认是public abstract
     *接口内只有静态变量，可以定义具有方法体的static方法或者default方法
     */

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> clazz);


}
