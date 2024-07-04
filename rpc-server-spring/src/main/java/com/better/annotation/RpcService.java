package com.better.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcService {
    String serviceName() default "";

    Class<?> serviceInterface() default Void.class;

    String version() default "1.0";
}
