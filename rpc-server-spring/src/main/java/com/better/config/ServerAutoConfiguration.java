package com.better.config;

import com.better.registryanddiscovery.registry.RegistryService;
import com.better.registryanddiscovery.registry.impl.NacosRegistry;
import com.better.registryanddiscovery.registry.impl.ZookeeperRegistry;
import com.better.server.RpcServer;
import com.better.server.http.HttpRpcServer;
import com.better.server.netty.NettyRpcServer;
import com.better.server.socket.SocketServer;
import com.better.spring.RpcServiceBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(ServerConfig.class)
public class ServerAutoConfiguration {
    @Autowired
    private ServerConfig serverConfig;

    //1.配置Server,默认使用netty
    @Bean("rpcServer")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server",name = "connection",havingValue = "netty", matchIfMissing = true)
    public RpcServer rpcServer() {
        return new NettyRpcServer(serverConfig.getHost(), serverConfig.getPort());
    }

    @Bean("rpcServer")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server",name = "connection",havingValue = "http")
    public RpcServer httpServer(){
        return new HttpRpcServer();
    }


    @Bean("rpcServer")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server",name = "connection",havingValue = "socket")
    public RpcServer socketServer(){
        return new SocketServer();
    }

    //2.配置注册服务,默认使用nacos
    @Bean("registryService")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server",name = "registry",havingValue = "nacos",matchIfMissing = true)
    public RegistryService nacosRegistryService(){
        return new NacosRegistry(serverConfig.getRegistryAddress());
    }

    @Bean("registryService")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server",name = "registry",havingValue = "zookeeper")
    public RegistryService zookeeperRegistryService(){
        return new ZookeeperRegistry(serverConfig.getRegistryAddress());
    }

    @Bean
    @ConditionalOnMissingBean()
    @ConditionalOnBean({RegistryService.class ,RpcServer.class})
    public RpcServiceBeanPostProcessor rpcServiceBeanPostProcessor(@Autowired RegistryService registryService,
                                                                   @Autowired RpcServer rpcServer,
                                                                   @Autowired ServerConfig serverConfig){
        return new RpcServiceBeanPostProcessor(registryService,serverConfig,rpcServer);
    }


}
