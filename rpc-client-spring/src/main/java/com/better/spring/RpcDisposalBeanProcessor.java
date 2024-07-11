package com.better.spring;

import com.better.discovery.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

@Slf4j
public class RpcDisposalBeanProcessor implements DisposableBean {
    private final ServiceDiscovery serviceDiscovery;

    public RpcDisposalBeanProcessor(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void destroy() throws Exception {
        try {
            if (serviceDiscovery != null) {
                serviceDiscovery.destroy();
                log.info("successfully destroyed serviceDiscovery for client");
            }
        }catch (Exception e){
            log.error("error occured while destroying service discovery", e);
        }
    }
}
