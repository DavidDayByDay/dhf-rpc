package com.better.registry;


import com.better.protocol.ServiceRegisterInfo;

public interface RegistryService {
    void register(ServiceRegisterInfo serviceRegisterInfo);

    void unRegister(ServiceRegisterInfo serviceRegisterInfo);

    /**
     * 关闭与服务注册中心的链接
     */
    void destroy();
}
