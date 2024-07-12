package com.better.loadbalance.impl;

import com.better.loadbalance.AbstractLoadBalance;
import com.better.protocol.ServiceRegisterInfo;
import com.better.protocol.messages.RequestMessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 无权重依次轮询
 */
public class RoundRobin extends AbstractLoadBalance {
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services, RequestMessage requestMessage) {
        //如果atomicInteger达到Max值，重置，需要加锁
        while (atomicInteger.get() == Integer.MAX_VALUE) {
            if (atomicInteger.compareAndSet(Integer.MAX_VALUE,0)) break;
        }
        return services.get(atomicInteger.getAndIncrement() % services.size());
    }
}
