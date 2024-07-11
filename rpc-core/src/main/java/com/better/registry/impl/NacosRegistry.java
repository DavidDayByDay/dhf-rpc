package com.better.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.better.protocol.messages.ServiceRegisterInfo;
import com.better.registry.RegistryService;
import com.better.utils.ServiceInfoUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class NacosRegistry implements RegistryService {
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
    public void register(ServiceRegisterInfo serviceRegisterInfo) {
        Instance instance = new Instance();
        instance.setIp(serviceRegisterInfo.getServiceHost());
        instance.setPort(serviceRegisterInfo.getServicePort());
        instance.setServiceName(serviceRegisterInfo.getServiceNameAsInterface());
        instance.setHealthy(true);
        //可以考虑将元数据打包放入
        Map metaData = ServiceInfoUtils.toMap(serviceRegisterInfo);
        instance.setMetadata(metaData);

        String registryKey = ServiceInfoUtils.serviceKey(serviceRegisterInfo.getServiceNameAsInterface(), serviceRegisterInfo.getVersion());

        try {
            namingService.registerInstance(registryKey, instance);
            log.info("Successfully registered service:" + registryKey);
        } catch (NacosException e) {
            log.debug("nacos register error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unRegister(ServiceRegisterInfo serviceRegisterInfo) {
        Instance instance = new Instance();
        instance.setIp(serviceRegisterInfo.getServiceHost());
        instance.setPort(serviceRegisterInfo.getServicePort());
        instance.setServiceName(serviceRegisterInfo.getServiceNameAsInterface());
        instance.setHealthy(true);

        try {
            namingService.deregisterInstance(serviceRegisterInfo.getServiceNameAsInterface(),instance);
            log.info("Successfully unregistered service:" + serviceRegisterInfo.getServiceNameAsInterface());
        } catch (NacosException e) {
            log.debug("nacos deregister error,service name is:{}", serviceRegisterInfo.getServiceNameAsInterface(), e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void destroy() {
        try {
            log.info("destroying registryService {}",namingService.getServerStatus());
            namingService.shutDown();
            log.info("Successfully destroyed registryService");
        } catch (NacosException e) {
            log.debug("nacos destroy error", e);
            throw new RuntimeException(e);
        }
    }
}
