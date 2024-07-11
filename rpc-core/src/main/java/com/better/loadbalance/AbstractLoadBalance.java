package com.better.loadbalance;

import com.better.protocol.messages.ServiceRegisterInfo;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public ServiceRegisterInfo select(List<ServiceRegisterInfo> services) {
        if (services == null || services.isEmpty()) {
            return null;
        }

        //如果服务列表中只有一个服务
        if (services.size() == 1) {
            return services.get(0);
        }

        return doSelect(services);
    }

    /*
    doSelect 收到的List，size >= 2
     */
    protected abstract ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services);

}
