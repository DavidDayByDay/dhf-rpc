package com.better.discovery.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.better.discovery.ServiceDiscovery;
import com.better.loadbalance.LoadBalance;
import com.better.protocol.ServiceRegisterInfo;
import com.better.protocol.messages.RequestMessage;
import com.better.utils.ServiceInfoUtils;
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
    private final NamingService namingService;
    //一个本地的服务缓存列表
    private static final Map<String, List<ServiceRegisterInfo>> serviceCacheMap = new HashMap<>();

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
    public ServiceRegisterInfo discover(RequestMessage requestMessage, LoadBalance loadBalance) {
        return loadBalance.select(getServices(requestMessage.getServiceName()),requestMessage);
    }

    @Override
    public List<ServiceRegisterInfo> getServices(String serviceName) {
        try {
            //判断本地缓存中是否存在服务
            if (serviceCacheMap.containsKey(serviceName)) {
                return serviceCacheMap.get(serviceName);
            }else {
                List<Instance> instances = namingService.getAllInstances(serviceName);
                List<ServiceRegisterInfo> serviceRegisterInfoCollection = instances.stream().map(instance -> {
                    ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo();
//                    serviceRegisterInfo.setServiceNameAsInterface(instance.getServiceName());
                    serviceRegisterInfo.setServiceHost(instance.getIp());
                    serviceRegisterInfo.setServicePort(instance.getPort());
                    return serviceRegisterInfo;
                }).collect(Collectors.toList());

                //更新本地缓存
                serviceCacheMap.put(serviceName, serviceRegisterInfoCollection);
                log.info("find all service instances and cached");

                //注册监听事件
                namingService.subscribe(serviceName,event -> {
                    NamingEvent namingEvent = (NamingEvent) event;
                    log.info("service:{} has changed, there is a listener triggered!", serviceName);
                    //再次更新本地缓存
                    System.out.println(((NamingEvent) event).getInstances().size());
                    serviceCacheMap.put(namingEvent.getServiceName(),namingEvent.getInstances().stream().map(instance -> {
                        System.out.println(instance);
                        return ServiceInfoUtils.toServiceInfo(instance.getMetadata());
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
        try {
            namingService.shutDown();
            log.debug("successfully shutdown Nacos discovery");
        }catch (Exception e){
            log.debug("error occurred when destroy");
            throw new RuntimeException(e);
        }
    }
}
