package com.better.controller;

import com.better.annotations.RpcReference;
import com.better.api.Hello;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RpcReference
    private Hello helloService;

    @GetMapping("/hello/{content}")
    public String hello(String content) {
        return helloService.sayHello(content);
//        return "hello you!";
    }
}
