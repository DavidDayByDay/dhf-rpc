package com.better.registry.impl;

import com.better.protocol.ServiceRegisterInfo;
import com.better.registry.RegistryService;
import com.better.utils.ServiceInfoUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;

import java.io.IOException;

@Slf4j
public class ZookeeperRegistry implements RegistryService {
    private final CuratorFramework zkClient;
    private final ServiceDiscovery<Object> serviceDiscovery;
    private final String registryAddress;
    //默认值
    @Setter
    @Getter
    private String nameSpace = "myrpc";
    @Setter
    @Getter
    private String basePath = "/";



    public ZookeeperRegistry(String registryAddress) {
        this.registryAddress = registryAddress;

        zkClient = CuratorFrameworkFactory.builder()
                .connectString(registryAddress)
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(nameSpace)
                .build();

        serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                .client(zkClient)
                .basePath(basePath)
                .build();

        try {
            zkClient.start();
            serviceDiscovery.start();
        }catch (Exception e){
            log.error("zookeeper client or service discovery failed", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void register(ServiceRegisterInfo serviceRegisterInfo) {

        try {
            ServiceInstance<Object> serviceInstance = ServiceInstance.builder()
//                    .payload(serviceRegisterInfo)
                    .address(serviceRegisterInfo.getServiceHost())
                    .port(serviceRegisterInfo.getServicePort())
                    .name(ServiceInfoUtils.serviceKey(serviceRegisterInfo.getServiceNameAsInterface(),serviceRegisterInfo.getVersion()))
                    .serviceType(ServiceType.DYNAMIC)
                    .build();
            serviceDiscovery.registerService(serviceInstance);
        } catch (Exception e) {
            log.debug("register service {} failed",serviceRegisterInfo.getServiceNameAsInterface(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unRegister(ServiceRegisterInfo serviceRegisterInfo) {
        ServiceInstance<Object> serviceInstance = null;
        try {
            serviceInstance = ServiceInstance.builder()
    //                    .payload(serviceRegisterInfo)
                    .address(serviceRegisterInfo.getServiceHost())
                    .port(serviceRegisterInfo.getServicePort())
                    .name(serviceRegisterInfo.getServiceNameAsInterface() + "-" + serviceRegisterInfo.getVersion())
                    .serviceType(ServiceType.DYNAMIC)
                    .build();
            serviceDiscovery.unregisterService(serviceInstance);
        } catch (Exception e) {
            log.debug("unRegister service {} failed",serviceRegisterInfo.getServiceNameAsInterface(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        try {
            if (serviceDiscovery != null) {
                serviceDiscovery.close();
                log.debug("successfully destroy zookeeper serviceDiscovery at {}", registryAddress);
            }
        } catch (IOException e) {
            log.debug("failed to destroy zookeeper serviceDiscovery at {}", registryAddress, e);
            throw new RuntimeException(e);
        }

    }
}
