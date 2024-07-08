package com.better.impl;

import com.better.annotation.RpcService;
import com.better.api.Hello;

@RpcService
public class HelloService implements Hello {
    @Override
    public String sayHello() {
        return "hello in rpc response";
    }
}
