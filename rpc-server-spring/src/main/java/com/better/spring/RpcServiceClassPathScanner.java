package com.better.spring;

import com.better.annotation.RpcService;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public class RpcServiceClassPathScanner extends ClassPathBeanDefinitionScanner {
    public RpcServiceClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        //添加过滤条件，这里是只添加了@RpcService的注解才会被扫描到
        addIncludeFilter(new AnnotationTypeFilter(RpcService.class));
        //调用spring的扫描
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        return beanDefinitionHolders;
    }

}
