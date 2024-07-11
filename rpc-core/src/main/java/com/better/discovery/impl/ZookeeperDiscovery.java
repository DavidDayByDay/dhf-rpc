package com.better.discovery.impl;

import com.better.discovery.ServiceDiscovery;
import com.better.factories.SingletonFactory;
import com.better.loadbalance.LoadBalance;
import com.better.protocol.messages.ServiceRegisterInfo;
import com.better.utils.ZkExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableExecutorService;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.better.constants.ZookeeperConstants.BASE_DIR;
import static com.better.constants.ZookeeperConstants.NAME_SPACE;

@Slf4j
public class ZookeeperDiscovery implements ServiceDiscovery {
    //一个本地的服务缓存列表,内部是cruator提供的serviceCache缓存实现
    private static final Map<String,ServiceCache<Object>> serviceCacheMap = new ConcurrentHashMap<>();
    //发现服务用ExecutorService
    private final CloseableExecutorService zookeeperExecutorService = SingletonFactory.getInstance(ZkExecutorService.class).getExecutor();

    private final String discoveryAddress;
    private final CuratorFramework zkClient;
    private final org.apache.curator.x.discovery.ServiceDiscovery<Object> serviceDiscovery;

    public ZookeeperDiscovery(String discoveryAddress) {
        this.discoveryAddress = discoveryAddress;
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(discoveryAddress)
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(new ExponentialBackoffRetry(500, 3))
                .namespace(NAME_SPACE)
                .build();

        serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                .client(zkClient)
                .basePath(BASE_DIR)
                .build();

        try {
            zkClient.start();
            serviceDiscovery.start();
        } catch (Exception e) {
            log.error("zookeeper client or service discovery failed", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public ServiceRegisterInfo discover(String serviceName, LoadBalance loadBalance) {
        //todo
        return getServices(serviceName).get(0);
    }

    @Override
    public List<ServiceRegisterInfo> getServices(String serviceName) {
        if (serviceCacheMap.containsKey(serviceName)) {
            return  toServiceRegisterInfo( serviceCacheMap.get(serviceName).getInstances());

        } else {
            //每第一次进行服务发现时，为每个单独的服务创建本地缓存
            ServiceCache<Object> serviceCache = serviceDiscovery.serviceCacheBuilder()
                    .name(serviceName)
                    .executorService(zookeeperExecutorService)
                    .build();
            try {
                serviceCache.start();
                serviceCacheMap.put(serviceName, serviceCache);
                return toServiceRegisterInfo(serviceCache.getInstances());
            } catch (Exception e) {
                log.error("service discovery failed for {}", serviceName, e);
                throw new RuntimeException(e);
            }
        }
//        throw new RuntimeException("service discovery failed");
    }

    private List<ServiceRegisterInfo> toServiceRegisterInfo(List<ServiceInstance<Object>> serviceCollection) {
        return serviceCollection.stream().map(serviceInstance -> {
            return ServiceRegisterInfo.builder()
                    .serviceHost(serviceInstance.getAddress())
                    .servicePort(serviceInstance.getPort())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public void destroy() {
        try {
            serviceDiscovery.close();
            zkClient.close();
        }catch (Exception e){
            log.debug("zookeeper client or service discovery failed to be closed for discovery address:{}",discoveryAddress, e);
            throw new RuntimeException(e);
        }
    }
}
