package com.better.loadbalance.impl;

import com.better.loadbalance.AbstractLoadBalance;
import com.better.protocol.messages.ServiceRegisterInfo;

import java.util.List;

public class RoundRobin extends AbstractLoadBalance {
    @Override
    public ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services) {
        return services.get(0);
    }
}
