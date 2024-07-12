package com.better.loadbalance.impl;
//参考： 负载均衡 https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/loadbalance/#23-consistenthashloadbalance

import com.better.loadbalance.AbstractLoadBalance;
import com.better.protocol.ServiceRegisterInfo;
import com.better.protocol.messages.RequestMessage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHash extends AbstractLoadBalance {
    private  final TreeMap<Long,ServiceRegisterInfo> virtualInvokers = new TreeMap<>();
    private Integer identityHash;
    //暂时硬编码
    private int replicaNumber = 160;

    @Override
    public ServiceRegisterInfo doSelect(List<ServiceRegisterInfo> services, RequestMessage requestMessage) {
        //获取请求的响应hash值
        long requestHash = prepareRequestHash(requestMessage);
        int servicesHash = System.identityHashCode(services);
        //需要更新identityHash的情况
        if (identityHash == null || identityHash != servicesHash){
            prepareInvokersHash(services);
            identityHash = servicesHash;
        }
        //查找响应请求最近的服务节点
        return getVirtualInvoker(requestHash);
    }

    //1.将所有的service实例及一定数量（160）的虚拟节点分配到hash环上
    private void prepareInvokersHash(List<ServiceRegisterInfo> services) {
        for (ServiceRegisterInfo serviceInfo : services) {
            String address = serviceInfo.getServiceHost() + ":" + serviceInfo.getServicePort();
            for (int i = 0; i < replicaNumber / 4; i++) {
                // 对 address + i 进行 md5 运算，得到一个长度为16的字节数组
                byte[] digest = md5(address + i);
                // 对 digest 部分字节进行4次 hash 运算，得到四个不同的 long 型正整数
                for (int h = 0; h < 4; h++) {
                    // h = 0 时，取 digest 中下标为 0 ~ 3 的4个字节进行位运算
                    // h = 1 时，取 digest 中下标为 4 ~ 7 的4个字节进行位运算
                    // h = 2, h = 3 时过程同上
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
        return hash(md5(sb.toString()),0);
    }

    //3.查找请求的最近hash实例
    private ServiceRegisterInfo getVirtualInvoker(long hash){
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

    private long hash(byte[] digest, int number) {
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                | (digest[number * 4] & 0xFF))
                & 0xFFFFFFFFL;
    }
}
