package com.better.json;

import com.better.registry.RegistryService;
import com.better.serializer.JSONSerializer;

public class test {
    public static void main(String[] args) {
        JSONSerializer jsonSerializer = new JSONSerializer();
//        Class<?>[] clazz = new Class[]{Integer.class,String.class,Double.class};
        Class<?>[] clazz = new Class<?>[] {String.class,Integer.class, RegistryService.class};

        byte[] serialize = jsonSerializer.serialize(clazz);
        System.out.println(serialize);
        Class[] deserialize = jsonSerializer.deserialize(serialize, Class[].class);
        System.out.println(deserialize);


    }
}
