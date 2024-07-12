package com.better.loadbalance;

import com.better.protocol.ServiceRegisterInfo;
import com.better.protocol.messages.RequestMessage;

import java.util.List;

/**
 * 负载均衡策略
 * 负载均衡的实现需要各个LoadBalance保持单例使用
 * 单例从LoadBalance类的parseByName获取
 */
public interface LoadBalance {
    /**
     *
     * @param services 所有已发现的服务
     * @param requestMessage 请求发起的信息，对于如consistentHash之类的方法需要该参数
     * @return 一个特定的服务
     */
    ServiceRegisterInfo select(List<ServiceRegisterInfo> services, RequestMessage requestMessage);

}
