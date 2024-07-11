package com.better.test;

import com.better.discovery.impl.ZookeeperDiscovery;
import com.better.protocol.messages.ServiceRegisterInfo;

public class zkDis {
    public static void main(String[] args) {
        ZookeeperDiscovery discovery = new ZookeeperDiscovery("172.30.49.221:2181");

        ServiceRegisterInfo discover = discovery.discover(Fooo.class.getName() + "-1.0", null);
        ServiceRegisterInfo discover2 = discovery.discover(Me.class.getName() + "-1.0", null);
        ServiceRegisterInfo discover3 = discovery.discover(Fooo.class.getName() + "-1.0", null);

    }
}
