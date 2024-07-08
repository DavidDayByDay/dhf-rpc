package com.better;

import com.better.annotation.RpcComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@RpcComponentScan
public class ServerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ServerApplication.class, args);
        System.out.println(run.getBeanFactory().getBean("rpcServer"));
        System.out.println(run.getBeanFactory().getBean("registryService"));
    }
}
