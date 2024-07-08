package com.better;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ClientApplication.class, args);
//        ConfigurableListableBeanFactory beanFactory = run.getBeanFactory();
//        Object bean = beanFactory.getBean("HelloController");
//        System.out.println(bean);
    }
}
