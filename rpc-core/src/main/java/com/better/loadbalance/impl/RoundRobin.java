package com.better.loadbalance.impl;

import com.better.loadbalance.LoadBalance;
import com.better.pojos.ServiceRegisterInfo;

import java.util.List;

public class RoundRobin implements LoadBalance {
    @Override
    public ServiceRegisterInfo select(List<ServiceRegisterInfo> services) {
        return doSelect(services);
    }

    private ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services) {
        //todo 抽象类
        return services.get(0);
    }
}
