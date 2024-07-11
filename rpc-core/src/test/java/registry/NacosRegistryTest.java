package registry;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.better.protocol.messages.ServiceRegisterInfo;
import com.better.discovery.impl.NacosDiscovery;
import com.better.registry.impl.NacosRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

public class NacosRegistryTest {
    ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo();
    Instance instance = new Instance();

    @Before
    public void prepareInfo(){
        serviceRegisterInfo.setServiceNameAsInterface("Fooo");
        serviceRegisterInfo.setServicePort(8080);
        serviceRegisterInfo.setServiceHost("127.0.0.1");
        serviceRegisterInfo.setVersion("1.0");

//        Map map = ServiceInfoUtils.toMap(serviceRegisterInfo);
//        instance.setPort(8800);
//        instance.setIp("127.0.0.1");
//        instance.setServiceName(map.get("serviceName").toString());
//        instance.setHealthy(true);
//        instance.setMetadata(map);

    }

    @Test
    public void test1() {
        try {
            NacosRegistry nacosRegistry = new NacosRegistry("127.0.0.1:8848");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        nacosRegistry.register(serviceRegisterInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();
//            nacosRegistry.register(serviceRegisterInfo);


            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            Thread.sleep(10000000);

            NacosDiscovery nacosDiscovery = new NacosDiscovery("127.0.0.1:8848");
            System.out.println(nacosDiscovery.getServices("test"));


            ServiceRegisterInfo serviceRegisterInfo1 = new ServiceRegisterInfo();
            serviceRegisterInfo1.setServiceNameAsInterface("test");
            serviceRegisterInfo1.setServicePort(8801);
            serviceRegisterInfo1.setServiceHost("127.0.0.1");
            serviceRegisterInfo1.setVersion("1");
//            nacosRegistry.register(serviceRegisterInfo1);
            NacosRegistry nacosRegistry1 = new NacosRegistry("127.0.0.1:8848");
            nacosRegistry1.register(serviceRegisterInfo1);

            System.out.println(nacosDiscovery.getServices("test"));

            Thread.sleep(50000);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
