package com.better.test;

public class Foo implements Fooo{

    @Override
    public void foo(int q) {
        System.out.println("foo in server side!,and value is " + q);
    }
}
