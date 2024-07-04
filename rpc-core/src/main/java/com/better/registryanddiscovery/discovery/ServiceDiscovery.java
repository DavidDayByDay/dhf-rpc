package com.better.registryanddiscovery.discovery;

import com.better.loadbalance.LoadBalance;
import com.better.pojos.ServiceRegisterInfo;

import java.util.List;

public interface ServiceDiscovery {
    /**
     * 从可能的多个服务中通过负载均衡找到一个
     * @param serviceName
     * @return 一个选定的服务
     */
    ServiceRegisterInfo discover(String serviceName, LoadBalance loadBalance);

    List<ServiceRegisterInfo> getServices(String serviceName);

    void destroy();





}
