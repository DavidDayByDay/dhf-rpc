package com.better.serializer;

import com.better.loadbalance.LoadBalance;

import java.io.Serializable;

public class TestObject<T> implements Serializable {
//    public static final long serialVersionUID = 1L;

    String name;
    int age;
    Class<T> hodingClass;
    public TestObject() {}

    public TestObject(String name, int age, Class<T> hodingClass) {
        this.name = name;
        this.age = age;
        this.hodingClass = hodingClass;
    }



    public static void main(String[] args) throws InterruptedException {
        TestObject<LoadBalance> testObject = new TestObject<>("Jack", 23, LoadBalance.class);

        JDKSerializer jdkSerializer = new JDKSerializer();
        byte[] jdkSerialize = jdkSerializer.serialize(testObject);

        KryoSerializer kryoSerializer = new KryoSerializer();
        byte[] kryoSerialize = kryoSerializer.serialize(testObject);

        TestObject<LoadBalance> reload = kryoSerializer.deserialize(kryoSerialize, TestObject.class);
        System.out.println(reload.hodingClass);

        long now = System.currentTimeMillis();
//        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i =0 ; i < 100000;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    KryoSerializer.kryoLocal.set(new Kryo());
//                    System.out.println(atomicInteger.incrementAndGet());
                    byte[] serialize = kryoSerializer.serialize(testObject);
                    kryoSerializer.deserialize(serialize, TestObject.class);
                }
            }).start();
        }
        long curr = System.currentTimeMillis();
        System.out.println("elapsed time: " + (curr - now));


        System.out.println(jdkSerialize.length);
        System.out.println(kryoSerialize.length);



    }
}
