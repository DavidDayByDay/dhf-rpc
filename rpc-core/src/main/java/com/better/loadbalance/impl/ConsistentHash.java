//参考： 负载均衡 https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/loadbalance/#23-consistenthashloadbalance
package com.better.loadbalance.impl;

import com.better.loadbalance.AbstractLoadBalance;
import com.better.protocol.ServiceRegisterInfo;
import com.better.protocol.messages.RequestMessage;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConsistentHash extends AbstractLoadBalance {
    //key = serviceName value = ServiceSelector 对该服务集群的选择器
    private static final Map<String,ServiceSelector> selectors = new ConcurrentHashMap<>();


    @Override
    protected ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services, RequestMessage requestMessage) {
        int nowHash = System.identityHashCode(services);
        String serviceName = requestMessage.getServiceName();
        ServiceSelector selector = selectors.get(serviceName);
        //需要更新selector的情况
        if (selector == null || nowHash != selector.identityHash) {
            //更新selector
            selector = new ServiceSelector(nowHash, 160, services);
            selectors.put(serviceName,selector);
        }
        return selector.select(requestMessage);
    }

    static class ServiceSelector{
        private  final TreeMap<Long,ServiceRegisterInfo> virtualInvokers = new TreeMap<>();
        private final Integer identityHash;
        //暂时硬编码
        private final int replicaNumber;


        public ServiceSelector(Integer identityHash, int replicaNumber,List<ServiceRegisterInfo> services) {
            this.identityHash = identityHash;
            this.replicaNumber = replicaNumber;
            prepareInvokersHash(services);
        }

        public ServiceRegisterInfo select(RequestMessage requestMessage) {
            //获取请求的响应hash值
            //查找响应请求最近的服务节点
            return toTrueInstance(prepareRequestHash(requestMessage));
        }

        //1.将所有的service实例及一定数量（默认160）的虚拟节点分配到hash环上
        private void prepareInvokersHash(List<ServiceRegisterInfo> services) {
            for (ServiceRegisterInfo serviceInfo : services) {
                String address = serviceInfo.getServiceHost() + ":" + serviceInfo.getServicePort();
                for (int i = 0; i < replicaNumber / 4; i++) {
                    // 对 address + i 进行 md5 运算，得到一个长度为16字节的数组
                    byte[] digest = md5(address + i);
                    // 对 digest 部分字节进行4次 hash 运算，得到四个不同的 long 型正整数
                    for (int h = 0; h < 4; h++) {
                        // h = 0 时，取 digest 中下标为 0 ~ 3 的4个字节进行位运算
                        // h = 1,时过程同上
                        long m = hash(digest, h);
                        // 将 hash 到 invoker 的映射关系存储到 virtualInvokers 中，
                        // virtualInvokers 需要提供高效的查询操作，因此选用 TreeMap 作为存储结构
                        virtualInvokers.put(m, serviceInfo);
                    }
                }
            }
        }

        //2.将请求hash到环上-- key = methodName + parameterValues
        private long prepareRequestHash(RequestMessage requestMessage){
            StringBuilder sb = new StringBuilder(requestMessage.getMethodName());
            for (Object o : requestMessage.getParameterValues()){
                sb.append(o.toString());
            };
            return hash(md5(sb.toString()), 0);
        }

        //3.查找请求的最近hash实例
        private ServiceRegisterInfo toTrueInstance(long hash){
            Map.Entry<Long, ServiceRegisterInfo> entry = virtualInvokers.tailMap(hash,true).firstEntry();
            return entry == null ? virtualInvokers.firstEntry().getValue() : entry.getValue();
        }

        private byte[] md5(String value) {
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            md5.reset();
            byte[] bytes;
            bytes = value.getBytes(StandardCharsets.UTF_8);
            md5.update(bytes);
            return md5.digest();
        }

        //去除md5值的[4*number,4*number+3]字节作为hash值
        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }
    }




}
