package com.better.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcService {
    /**
     * 服务名：即接口的全限定类名
     */
    String serviceName() default "";

    /**
     * 服务接口名，即要暴露的服务接口全限定类名，当服务实现多个接口（不规范）时必须声明,或者声明服务名
     */
    Class<?> serviceInterface() default Void.class;

    String version() default "1.0";
}
