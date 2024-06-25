package com.better.registryanddiscovery.discovery;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.better.loadbalance.LoadBalance;
import com.better.pojos.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * 实现服务发现，同时获取服务后在服务端进行负载均衡配置
 * 基本思路：1.连接注册中心后获取所有可用的服务并在本地进行负载均衡选择
 *         2.采用缓存将发现的服务保存在本地，避免nacos挂掉后服务不可用
 *         3.1 (可选)向nacos注册监听器及时更新服务列表
 *         3.2 (可选)直到服务不可用后再次向注册中心请求并更新本地缓存
 */
public class NacosDiscovery implements ServiceDiscovery{
    private static final Logger log = LoggerFactory.getLogger(NacosDiscovery.class);
    private NamingService namingService;

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

        return null;
    }

    @Override
    public List<ServiceInfo> getServices(String serviceName) {
        try {
            List<Instance> Instances = namingService.getAllInstances(serviceName);
            log.info("find all service instances");


        } catch (NacosException e) {
            log.error("error occurred when find all service instances: {}",serviceName);
            throw new RuntimeException(e);
        }


        return Collections.emptyList();
    }

    @Override
    public void destroy() {

    }
}
