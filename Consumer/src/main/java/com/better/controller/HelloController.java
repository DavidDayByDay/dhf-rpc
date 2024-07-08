package com.better.controller;

import com.better.annotations.RpcReference;
import com.better.api.Hello;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RpcReference
    private Hello helloService;

    @GetMapping("/hello")
    public String hello() {
        return helloService.sayHello();
//        return "hello you!";
    }
}
