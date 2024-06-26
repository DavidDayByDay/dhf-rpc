package com.better.registryanddiscovery.discovery.impl;

import com.better.loadbalance.LoadBalance;
import com.better.pojos.ServiceInfo;
import com.better.registryanddiscovery.discovery.ServiceDiscovery;

import java.util.Collections;
import java.util.List;

public class ZookeeperDiscovery implements ServiceDiscovery {
    //todo
    @Override
    public ServiceInfo discover(String serviceName, LoadBalance loadBalance) {
        return null;
    }

    @Override
    public List<ServiceInfo> getServices(String serviceName) {
        return Collections.emptyList();
    }

    @Override
    public void destroy() {

    }
}
