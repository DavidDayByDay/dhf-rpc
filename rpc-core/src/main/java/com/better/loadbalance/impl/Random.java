package com.better.loadbalance.impl;

import com.better.loadbalance.AbstractLoadBalance;
import com.better.protocol.ServiceRegisterInfo;
import com.better.protocol.messages.RequestMessage;

import java.util.List;

public class Random extends AbstractLoadBalance {
    @Override
    public ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services, RequestMessage requestMessage) {
        java.util.Random random = new java.util.Random();
        return services.get(random.nextInt(services.size()));
    }
}
