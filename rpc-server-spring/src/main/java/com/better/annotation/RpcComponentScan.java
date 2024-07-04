package com.better.annotation;

import com.better.spring.RpcBeanDefinitionRegistry;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcBeanDefinitionRegistry.class)
public @interface RpcComponentScan {

    @AliasFor("value")
    String[] basePackages() default {};

    @AliasFor("basePackages")
    String[] values() default {};

}
