package com.better.annotations;

import java.lang.annotation.*;

/**
 * Field：将该field替换为代理对象
 * Method：处理返回值，如果返回值是实现了一个接口的对象则将该接口的代理实现类返回(为实现)
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RpcReference {
    String version() default "1.0";

    /**
     * serviceName：接口的全限定类名--服务名(除了version)
     */
    String interfaceName() default "";

    /**
     * 接口的类型
     */
    Class<?> interfaceClass() default Void.class;

    //todo 增加额外的功能,是不同服务可以使用不同的负载均衡策略，eg. 选择指定接口，负载均衡方法
    String loadbalance() default "random";

}
