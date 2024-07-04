package com.better.loadbalance;

import com.better.pojos.ServiceRegisterInfo;

import java.util.List;

/**
 * 负载均衡策略
 */
public interface LoadBalance {
    ServiceRegisterInfo select(List<ServiceRegisterInfo> services);

//    ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services);
}
