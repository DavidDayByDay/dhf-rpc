package com.better.annotation;

import com.better.spring.RpcBeanDefinitionRegistry;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * 扫描所有加了rpcService的bean
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcBeanDefinitionRegistry.class)
public @interface RpcComponentScan {

    @AliasFor("values")
    String[] basePackages() default {};

    @AliasFor("basePackages")
    String[] values() default {};

}
