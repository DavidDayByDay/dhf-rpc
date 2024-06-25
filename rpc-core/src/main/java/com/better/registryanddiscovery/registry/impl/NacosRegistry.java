package com.better.registryanddiscovery.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.better.pojos.ServiceInfo;
import com.better.registryanddiscovery.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NacosRegistry implements ServiceRegistry {
    private NamingService namingService;

    public NacosRegistry(String serverAddress)  {
        NacosNamingService nacosNamingService = null;
        try {
            nacosNamingService = new NacosNamingService(serverAddress);
            this.namingService = nacosNamingService;
        } catch (NacosException e) {
            log.error(String.format("an error occurred when start a naming service,the input serverAddress:%s", serverAddress));
            throw new RuntimeException(e);
        }

    }

    @Override
    public void register(ServiceInfo serviceInfo) {
        Instance instance = new Instance();
        instance.setIp(serviceInfo.getServiceAddress());
        instance.setPort(serviceInfo.getServicePort());
        instance.setServiceName(serviceInfo.getServiceName());
        instance.setHealthy(true);
//        instance.setMetadata();

        try {
            namingService.registerInstance(serviceInfo.getServiceName(), instance);
            log.info("Successfully registered service:" + serviceInfo.getServiceName());
        } catch (NacosException e) {
            log.debug("nacos register error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unRegister(ServiceInfo serviceInfo) {
        Instance instance = new Instance();
        instance.setIp(serviceInfo.getServiceAddress());
        instance.setPort(serviceInfo.getServicePort());
        instance.setServiceName(serviceInfo.getServiceName());
        instance.setHealthy(true);
//        instance.setMetadata();

        try {
            namingService.deregisterInstance(serviceInfo.getServiceName(),instance);
            log.info("Successfully unregistered service:" + serviceInfo.getServiceName());
        } catch (NacosException e) {
            log.debug("nacos deregister error,service name is:{}",serviceInfo.getServiceName(), e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void destroy() {
        try {
            namingService.shutDown();
            log.info("Successfully destroyed");
        } catch (NacosException e) {
            log.debug("nacos destroy error", e);
            throw new RuntimeException(e);
        }
    }
}
