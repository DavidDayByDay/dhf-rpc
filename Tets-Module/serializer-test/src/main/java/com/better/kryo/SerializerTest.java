package com.better.kryo;

import com.better.hessian.Car;
import com.better.pojos.RequestMessage;
import com.better.protocol.RpcMessage;
import com.better.serializer.KryoSerializer;
import com.better.test.Me;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SerializerTest {
    public static void main(String[] args) throws InterruptedException {
        KryoSerializer kryo = new KryoSerializer();

        Class<?>[] classes = new Class[]{String.class,Integer.class, Car.class};

        RpcMessage msg = new RpcMessage();
        msg.setMessageBody(new RequestMessage());

        Me me = new Me(classes);

//        Object[] objects = new Object[]{"hello",1,new Car(Model.EDSEL, Color.BLACK)};
        Object[] objects = new Object[]{"hello",1};
//
//        byte[] serialize = kryo.serialize(objects);
        byte[] serialize = kryo.serialize(classes);

        Class[] deserialize = kryo.deserialize(serialize, Class[].class);
//        Object[] deserialize = kryo.deserialize(serialize,Object[].class);
//        for (Object c : deserialize){
        System.out.println(deserialize);

        AtomicInteger atomicInteger = new AtomicInteger();
        List<Integer> list = new ArrayList<>();


        String[] strings = new String[100];
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                String s = new String();
                int index = atomicInteger.getAndIncrement();
                list.add(index);
                s = String.valueOf(index);
                byte[] serialize1 = kryo.serialize(s + s + s + s + s + s);

                strings[index] = kryo.deserialize(serialize1,String.class);
            }).start();
        }

        Thread.sleep(2000);
        for (String s : strings) {
            System.out.println(s);
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }





    }




}
