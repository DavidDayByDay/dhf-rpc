package com.better;

import com.better.pojos.ServiceRegisterInfo;
import com.better.provider.LocalServiceProvider;
import com.better.registryanddiscovery.registry.impl.NacosRegistry;
import com.better.server.netty.NettyRpcServer;
import com.better.test.Foo;

public class Server {
    public static void main(String[] args) throws InterruptedException {
        NettyRpcServer server = new NettyRpcServer("127.0.0.1", 8080);
        NacosRegistry nacosRegistry = new NacosRegistry("127.0.0.1:8848");

        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo();
        serviceRegisterInfo.setServiceHost("127.0.0.1");
        serviceRegisterInfo.setServicePort(8080);
        serviceRegisterInfo.setServiceNameAsInterface("Fooo");
        serviceRegisterInfo.setVersion("1.0");

        String serviceName = "Fooo-1.0";

        nacosRegistry.register(serviceRegisterInfo);
        Foo foo = new Foo();
        LocalServiceProvider.putService(serviceName,foo);

        Thread thread = new Thread(() -> {
            server.start();
        });

        thread.start();
//        server.start();
    }
}
