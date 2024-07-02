package com.better.config;

import lombok.Data;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
public class ServerConfig {
    public String host;
    private int port;
    private String serviceName;
    private String registry;
    private String registryAddress;
    private String connection;

    public ServerConfig() throws UnknownHostException {
        this.host = InetAddress.getLocalHost().getHostAddress();
        this.port = 8080;
        this.serviceName = "provide-1";
        this.registryAddress = "127.0.0.1:8848";
        this.registry = "Nacos";
        this.connection = "Netty";
    }
}
