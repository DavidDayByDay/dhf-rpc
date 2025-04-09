package com.better.spring;

import com.better.annotations.RpcReference;
import com.better.config.ClientConfig;
import com.better.exceptions.RpcException;
import com.better.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * 将已经注册的Bean中的所有{@RpcReference}的field替换为代理对象
 */
@Slf4j
public class RpcProxyBeanPostProcessor implements BeanPostProcessor {
    private final ProxyFactory proxyFactory;

    //默认配置，用于和{@RpcReference}注解上的值比较并进行修改
    private final ClientConfig clientConfig;


    public RpcProxyBeanPostProcessor(ClientConfig clientConfig, ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
        this.clientConfig = clientConfig;
    }

    /**
     * @param bean 已经注册的原始Bean
     * @return 返回值是增强后的Bean
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object proxy = null;
        Class<?> superClass;
        String className = "";
        //检查是否存在@RpcReference的field
        try {
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(RpcReference.class)) {
                    RpcReference annotation = field.getAnnotation(RpcReference.class);
                    //为该field创建代理
                    superClass = annotation.interfaceClass();
                    className = annotation.interfaceName();
                    String loadbalance = annotation.loadbalance();

                    if (superClass.equals(Void.class)) {
                        className = annotation.interfaceName();
                        if (!"".equals(className)) {
                            superClass = Class.forName(className);
                        } else {
                            superClass = field.getType();
                        }
                    }

                    ClientConfig newClientConfig = new ClientConfig(clientConfig);
                    if (!loadbalance.equals(clientConfig.getLoadbalance())) {
                        newClientConfig.setLoadbalance(loadbalance);
                    }
                    proxy = proxyFactory.makeProxy(superClass, annotation.version(), newClientConfig);
                    //再次检查
                    if (proxy != null) {
                        field.setAccessible(true);
                        field.set(bean, proxy);
                    } else {
                        throw new RpcException("making null proxy");
                    }
                }


            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            log.debug("error in make proxy for class:{}", className);
            throw new RuntimeException(e);
        }
        return bean;
    }

}
