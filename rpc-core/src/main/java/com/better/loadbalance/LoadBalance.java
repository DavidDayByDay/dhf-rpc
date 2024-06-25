package com.better.loadbalance;

import com.better.pojos.ServiceInfo;

import java.util.List;

/**
 * 负载均衡策略
 */
public interface LoadBalance {
    default ServiceInfo select(List<ServiceInfo> services) {
        return doSelect(services);
    }

    ServiceInfo doSelect(List<ServiceInfo> services);
}
