package com.better.loadbalance;

import com.better.protocol.messages.ServiceRegisterInfo;

import java.util.List;

/**
 * 负载均衡策略
 * 负载均衡的实现需要各个LoadBalance保持单例使用
 */
public interface LoadBalance {
    ServiceRegisterInfo select(List<ServiceRegisterInfo> services);

}
