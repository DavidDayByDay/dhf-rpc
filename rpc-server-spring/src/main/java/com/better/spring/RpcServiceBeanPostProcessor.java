package com.better.spring;

import com.better.annotation.RpcService;
import com.better.config.ServerConfig;
import com.better.exceptions.RpcException;
import com.better.protocol.ServiceRegisterInfo;
import com.better.provider.LocalServiceProvider;
import com.better.registry.RegistryService;
import com.better.server.RpcServer;
import com.better.utils.ServiceInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

/**
 * rpcService Bean的后处理器，用于将已经扫描到的rpcService 注册到注册中心
 */
@Slf4j
public class RpcServiceBeanPostProcessor implements BeanPostProcessor, CommandLineRunner {
    private final RegistryService registryService;

    private final ServerConfig serverConfig;

    private final RpcServer rpcServer;

    public RpcServiceBeanPostProcessor(RegistryService registryService, ServerConfig serverConfig, RpcServer rpcServer) {
        this.registryService = registryService;
        this.serverConfig = serverConfig;
        this.rpcServer = rpcServer;
    }

    /**
     * @param bean     已经注册的原始Bean
     * @param beanName
     * @return 返回值是增强后的Bean
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //1.判断该bean上是否有RpcService注解
        Class<?> aClass = bean.getClass();
        boolean annotationPresent = aClass.isAnnotationPresent(RpcService.class);
        if (annotationPresent) {
            //2.注册对应的服务
            //拿到注解信息
            RpcService rpcService = aClass.getAnnotation(RpcService.class);
            //生成注册信息
            ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo();
            //获取提供的全限定接口名即服务名
            String serviceName = rpcService.serviceName();
            if ("".equals(serviceName)) {
//                serviceName = ServiceInfoUtils.getServiceNameByInterface(aClass);
                //获取要暴露的接口的全限定类名
                serviceName = rpcService.serviceInterface().getName();
                boolean equalsVoid = rpcService.serviceInterface().equals(Void.class);
                if(equalsVoid){
                    Class<?>[] interfaces = aClass.getInterfaces();
                    if(interfaces.length > 1){
                        throw new RpcException("the service name or interface to be exposed must be specified when more than one interface is implemented");
                    }
                    serviceName = interfaces[0].getName();
                }
            }
            serviceRegisterInfo.setServiceNameAsInterface(serviceName);
            serviceRegisterInfo.setVersion(rpcService.version());
            serviceRegisterInfo.setServiceHost(serverConfig.getHost());
            serviceRegisterInfo.setServicePort(serverConfig.getPort());
            registryService.register(serviceRegisterInfo);
            log.info("the service is registered with name: {} and version: {}", serviceName, rpcService.version());

            //3.进行本地服务缓存
            LocalServiceProvider.putService(ServiceInfoUtils.serviceKey(serviceRegisterInfo), bean);
        }
        //返回原始bean
        return bean;
    }


    /**
     * 有SpringBoot提供的开机自启动方法
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            new Thread(() -> rpcServer.start()).start();
            log.info("the service is started with host: {}, port: {} on spring boot!", serverConfig.getHost(), serverConfig.getPort());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("the service is closing...and all registered service will close");
                registryService.destroy();
            }) {
            });
        } catch (Exception e) {
//            log.error("something went wrong when starting the service on spring boot!");
            throw new RuntimeException("something went wrong when starting the service on spring boot!", e);
        }


    }
}
