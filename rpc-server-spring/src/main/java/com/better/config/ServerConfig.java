package com.better.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
@ConfigurationProperties(prefix = "myrpc")
public class ServerConfig {
    private String host;
    private int port;
    private String appName;
    private String registry;
    private String registryAddress;
    private String connection;

    public ServerConfig() throws UnknownHostException {
        this.host = InetAddress.getLocalHost().getHostAddress();
        this.port = 8080;
        this.appName = "provide-1";
        this.registryAddress = "127.0.0.1:8848";
        this.registry = "Nacos";
        this.connection = "Netty";
    }
}
