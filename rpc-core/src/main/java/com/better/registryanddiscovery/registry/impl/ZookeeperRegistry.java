package com.better.registryanddiscovery.registry.impl;

import com.better.pojos.ServiceRegisterInfo;
import com.better.registryanddiscovery.registry.RegistryService;

public class ZookeeperRegistry implements RegistryService {
    //todo
    private String registryAddress;

    public ZookeeperRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }


    @Override
    public void register(ServiceRegisterInfo serviceRegisterInfo) {

    }

    @Override
    public void unRegister(ServiceRegisterInfo serviceRegisterInfo) {

    }

    @Override
    public void destroy() {

    }
}
