package com.better.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 默认的client设置，会被每个具体调用的注解@RpcReference上的设置覆盖
 */
@Data
@ConfigurationProperties(prefix = "rpc.client")
public class ClientConfig {
    private String serializerType;
    private String loadbalance;
    private String connection;
    private String discovery;
    private String discoveryUrl;
    private Integer timeOut;

    public ClientConfig(ClientConfig config){
        this.loadbalance = config.getLoadbalance();
        this.serializerType = config.getSerializerType();
        this.connection = config.getConnection();
        this.discovery = config.getDiscovery();
        this.discoveryUrl = config.getDiscoveryUrl();
        this.timeOut = config.getTimeOut();
    }

    public ClientConfig() {
        this.loadbalance = "RoundRobin";
        this.serializerType = "JDK";
        this.connection = "netty";
        this.discovery = "nacos";
        this.discoveryUrl = "127.0.0.1:8848";
        this.timeOut = 5000;
    }

}
