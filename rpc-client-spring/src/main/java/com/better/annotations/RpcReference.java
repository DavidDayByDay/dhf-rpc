package com.better.annotations;

import java.lang.annotation.*;

/**
 * Field：将该field替换为代理对象
 * Method：处理返回值，如果返回值是实现了一个接口的对象则将该接口的代理实现类返回
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RpcReference {
    String version() default "1.0";

    /**
     * serviceName：接口的全限定类名
     */
    String serviceName() default "";

    /**
     * 接口的类型
     */
    Class<?> serviceInterface() default void.class;

    //todo 增加额外的功能，eg. 选择指定接口，负载均衡方法
    String loadbalance() default "random";

}
