package com.better.loadbalance.impl;

import com.better.loadbalance.LoadBalance;
import com.better.pojos.ServiceInfo;

import java.util.List;

public class RoundRobin implements LoadBalance {
    @Override
    public ServiceInfo select(List<ServiceInfo> services) {
        return doSelect(services);
    }

    private ServiceInfo doSelect(List<ServiceInfo> services) {
        //todo
        return services.get(0);
    }
}
