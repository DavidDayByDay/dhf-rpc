package com.better.config;

import lombok.Data;

@Data
public class ClientConfig {
    private String serializerType;
    private String loadbalance;
    private String connnection;
    private String registry;
    private String registryAddress;
    private Integer timeOut;

    public ClientConfig() {
        this.loadbalance = "RoundRobin";
        this.serializerType = "JDK";
        this.connnection = "netty";
        this.registry = "nacos";
        this.registryAddress = "127.0.0.1:8848";
        this.timeOut = 5000;
    }

}
