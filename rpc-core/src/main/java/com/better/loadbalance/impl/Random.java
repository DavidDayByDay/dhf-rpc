package com.better.loadbalance.impl;

import com.better.loadbalance.AbstractLoadBalance;
import com.better.protocol.messages.ServiceRegisterInfo;

import java.util.List;

public class Random extends AbstractLoadBalance {
    @Override
    public ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services) {
        java.util.Random random = new java.util.Random();
        return services.get(random.nextInt(services.size()));
    }
}
