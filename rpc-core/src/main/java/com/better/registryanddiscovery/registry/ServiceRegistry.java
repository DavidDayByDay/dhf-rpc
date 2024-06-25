package com.better.registryanddiscovery.registry;


import com.better.pojos.ServiceInfo;

public interface ServiceRegistry {
    void register(ServiceInfo serviceInfo);

    void unRegister(ServiceInfo serviceInfo);

    /**
     * 关闭与服务注册中心的链接
     */
    void destroy();
}
