package com.better.loadbalance.impl;

import com.better.loadbalance.LoadBalance;
import com.better.pojos.ServiceInfo;

import java.util.List;

public class RoundRobin implements LoadBalance {
    @Override
    public ServiceInfo select(List<ServiceInfo> services) {
        return null;
    }

    @Override
    public ServiceInfo doSelect(List<ServiceInfo> services) {
        return null;
    }
}
