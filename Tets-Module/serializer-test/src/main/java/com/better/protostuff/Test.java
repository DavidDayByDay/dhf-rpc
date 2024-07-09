package com.better.protostuff;

import com.better.hessian.Car;
import com.better.serializer.ProtoStuffSerializer;
import com.better.test.Me;

public class Test {
    public static void main(String[] args) {
        ProtoStuffSerializer serializer = new ProtoStuffSerializer();
        Class<?>[] classes = new Class[]{String.class,Integer.class, Car.class};

        Me me = new Me(classes);
        byte[] serialize = serializer.serialize(me);
        Me deserialize = serializer.deserialize(serialize, Me.class);
        System.out.println(deserialize);
    }
}
