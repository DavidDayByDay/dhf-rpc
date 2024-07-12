package com.better.test;

import com.better.protocol.ServiceRegisterInfo;
import com.better.registry.impl.ZookeeperRegistry;

public class Zk {
    public static void main(String[] args) throws InterruptedException {
        ZookeeperRegistry zookeeperRegistry = new ZookeeperRegistry("172.30.49.221:2181");
        ServiceRegisterInfo registerInfo = ServiceRegisterInfo.builder()
                .serviceHost("127.0.0.1")
                .servicePort(8088)
                .serviceNameAsInterface(Fooo.class.getName())
                .version("1.0")
                .build();
        ServiceRegisterInfo registerInfo1 = ServiceRegisterInfo.builder()
                .serviceHost("127.0.0.2")
                .servicePort(8088)
                .serviceNameAsInterface(Fooo.class.getName())
                .version("1.0")
                .build();

        ServiceRegisterInfo registerInfo2 = ServiceRegisterInfo.builder()
                .serviceHost("127.0.0.3")
                .servicePort(8088)
                .serviceNameAsInterface(Me.class.getName())
                .version("1.0")
                .build();

        zookeeperRegistry.register(registerInfo);
        zookeeperRegistry.register(registerInfo2);

        zookeeperRegistry.register(registerInfo1);
        Thread.sleep(30000);

    }
}
