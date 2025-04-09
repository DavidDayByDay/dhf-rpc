package com.better.test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public static void main(String[] args) {
        int total = 100;
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();


        for (int i = 0; i < total; i++) {
            int finalI = i;
            Thread t = new Thread(() -> {
//                while (true) {
                    int q = atomicInteger.get();
                    while (q != finalI) {
                        q = atomicInteger.get();
                        if (Thread.interrupted()){
                            return;
                        }
                    }
                    System.out.println(q + 1);
                    atomicInteger.set((atomicInteger.get() + 1) % total);
//                }
            });
            t.start();

        }
    }
}
