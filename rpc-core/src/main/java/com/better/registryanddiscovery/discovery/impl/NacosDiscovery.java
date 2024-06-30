package com.better.registryanddiscovery.discovery.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.better.loadbalance.LoadBalance;
import com.better.pojos.ServiceInfo;
import com.better.registryanddiscovery.discovery.ServiceDiscovery;
import com.better.utils.ServiceInfoConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 实现服务发现，同时获取服务后在服务端(本地)进行负载均衡配置
 * 基本思路：1.连接注册中心后获取所有可用的服务并在本地进行负载均衡选择
 *         2.采用缓存将发现的服务保存在本地，避免nacos挂掉后服务不可用
 *         3.1 (可选)向nacos注册监听器及时更新服务列表
 *         3.2 (可选)直到服务不可用后再次向注册中心请求并更新本地缓存
 */

@Slf4j
public class NacosDiscovery implements ServiceDiscovery {
    private NamingService namingService;
    //一个本地的服务缓存列表
    private final Map<String, List<ServiceInfo>> serviceCacheMap = new HashMap<>();

    public NacosDiscovery(String serverAddress){
        try {
            namingService = NamingFactory.createNamingService(serverAddress);
            log.info("successfully connect to Nacos for discovery");
        } catch (NacosException e) {
            log.error(String.format("error occurred when connect to Nacos for discovery, service address is :%s", serverAddress));
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServiceInfo discover(String serviceName, LoadBalance loadBalance) {
        return loadBalance.select(getServices(serviceName));
    }

    @Override
    public List<ServiceInfo> getServices(String serviceName) {
        try {
            //判断本地缓存中是否存在服务
            if (serviceCacheMap.containsKey(serviceName)) {
                return serviceCacheMap.get(serviceName);
            }else {
                List<Instance> instances = namingService.getAllInstances(serviceName);
                List<ServiceInfo> serviceInfoCollection = instances.stream().map(instance -> {
                    ServiceInfo serviceInfo = new ServiceInfo();
                    serviceInfo.setServiceName(instance.getServiceName());
                    serviceInfo.setServiceAddress(instance.getIp());
                    serviceInfo.setServicePort(instance.getPort());
                    return serviceInfo;
                }).collect(Collectors.toList());

                //更新本地缓存
                serviceCacheMap.put(serviceName,serviceInfoCollection);
                log.info("find all service instances and cached");

                //注册监听事件
                namingService.subscribe(serviceName,event -> {
                    NamingEvent namingEvent = (NamingEvent) event;
                    log.info("service:{} has changed, there is a listener triggered!", serviceName);
                    //再次更新本地缓存
                    System.out.println(((NamingEvent) event).getInstances().size());
                    serviceCacheMap.put(namingEvent.getServiceName(),namingEvent.getInstances().stream().map(instance -> {
                        System.out.println(instance);
                        return ServiceInfoConverter.toServiceInfo(instance.getMetadata());
                    }).collect(Collectors.toList()));
                    log.info("successfully updated service:{}", serviceName);
                });
            }
            return serviceCacheMap.get(serviceName);

        } catch (NacosException e) {
            log.error("error occurred when try to find all service instances: {}",serviceName);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
