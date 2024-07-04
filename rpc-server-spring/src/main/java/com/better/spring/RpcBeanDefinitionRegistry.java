package com.better.spring;

import com.better.annotation.RpcComponentScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;


@Slf4j
public class RpcBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar,ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
       this.resourceLoader = resourceLoader;
    }

    /**
     * 将所有包含 @RpcService 注解的Bean注入, 该方法运行于spring原有的扫描之后
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //获取所有注解的属性和值
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcComponentScan.class.getName()));
        //获取到basePackage的值
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        //如果没有设置basePackage 扫描路径,就扫描对应包下面的值
        if(basePackages.length == 0){
            basePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }

        //自定义的包扫描器
        RpcServiceClassPathScanner scanner = new RpcServiceClassPathScanner(registry);

        if(resourceLoader != null){
            scanner.setResourceLoader(resourceLoader);
        }
        //这里实现的是根据名称来注入
//        scanner.setBeanNameGenerator(new RpcBeanNameGenerator());
        //扫描指定路径下的接口
        int scan = scanner.scan(basePackages);
        log.info("scanned {} rpcService Bean registered in spring container",scan);

    }
}
