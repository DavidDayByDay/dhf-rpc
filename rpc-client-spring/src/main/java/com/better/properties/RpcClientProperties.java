package com.better.properties;

import lombok.Data;

@Data
public class RpcClientProperties {
    private String serializerType;
    private String loadbalance;
    private String connnection;
    private String registry;
    private String registryAddress;
    private Integer timeOut;

    public RpcClientProperties() {
        this.loadbalance = "random";
        this.serializerType = "HESSIAN";
        this.connnection = "netty";
        this.registry = "nacos";
        this.registryAddress = "127.0.0.1:2181";
        this.timeOut = 5000;
    }

}
