package com.better.config;

import com.better.client.Client;
import com.better.client.Http.HttpClient;
import com.better.client.Netty.NettyClient;
import com.better.client.Socket.SocketClient;
import com.better.proxy.ProxyFactory;
import com.better.registryanddiscovery.discovery.ServiceDiscovery;
import com.better.registryanddiscovery.discovery.impl.NacosDiscovery;
import com.better.registryanddiscovery.discovery.impl.ZookeeperDiscovery;
import com.better.spring.RpcDisposalBeanProcessor;
import com.better.spring.RpcProxyBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(ClientConfig.class)
public class ClientAutoConfiguration {
    @Autowired
    private ClientConfig clientConfig;


    //1.通信
    @Bean("rpcClient")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client",name = "connection",havingValue = "netty",matchIfMissing = true)
    @Primary
    public Client nettyClient() {
        return new NettyClient();
    }

    @Bean("rpcClient")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client",name = "connection",havingValue = "http")
    public Client httpClient() {
        return new HttpClient();
    }

    @Bean("rpcClient")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client",name = "connection",havingValue = "socket")
    public Client socketClient() {
        return new SocketClient();
    }

    //2.服务发现
    @Bean("serviceDiscovery")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client",name = "discovery",havingValue = "nacos",matchIfMissing = true)
    @Primary
    public ServiceDiscovery nacosDiscovery() {
        return new NacosDiscovery(clientConfig.getDiscoveryUrl());
    }

    @Bean("serviceDiscovery")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client",name = "discovery",havingValue = "zookeeper")
    public ServiceDiscovery zookeeperDiscovery() {
        return new ZookeeperDiscovery();
    }

    //proxyFactory
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({ServiceDiscovery.class,Client.class})
    public ProxyFactory proxyFactory(@Autowired ServiceDiscovery serviceDiscovery,@Autowired Client client){
        return new ProxyFactory(serviceDiscovery,client,clientConfig);
    }

    //3.后处理bean
    @Bean()
    @ConditionalOnMissingBean
    @ConditionalOnBean({ProxyFactory.class})
    public RpcProxyBeanPostProcessor rpcProxyBeanPostProcessor(@Autowired ProxyFactory proxyFactory) {
        return new RpcProxyBeanPostProcessor(clientConfig,proxyFactory);
    }


    //4.后置资源处理，关闭发现服务
    @Bean
    @ConditionalOnMissingBean
    public RpcDisposalBeanProcessor rpcDisposalBeanProcessor(@Autowired ServiceDiscovery serviceDiscovery){
        return new RpcDisposalBeanProcessor(serviceDiscovery);
    }


}
