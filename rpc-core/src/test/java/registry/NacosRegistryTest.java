package registry;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.better.pojos.ServiceInfo;
import com.better.registryanddiscovery.discovery.impl.NacosDiscovery;
import com.better.registryanddiscovery.registry.impl.NacosRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

public class NacosRegistryTest {
    ServiceInfo serviceInfo = new ServiceInfo();
    Instance instance = new Instance();

    @Before
    public void prepareInfo(){
        serviceInfo.setServiceName("Fooo");
        serviceInfo.setServicePort(8080);
        serviceInfo.setServiceHost("127.0.0.1");
        serviceInfo.setVersion("1.0");

//        Map map = ServiceInfoUtils.toMap(serviceInfo);
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
                        nacosRegistry.register(serviceInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();
//            nacosRegistry.register(serviceInfo);


            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            Thread.sleep(10000000);

            NacosDiscovery nacosDiscovery = new NacosDiscovery("127.0.0.1:8848");
            System.out.println(nacosDiscovery.getServices("test"));


            ServiceInfo serviceInfo1 = new ServiceInfo();
            serviceInfo1.setServiceName("test");
            serviceInfo1.setServicePort(8801);
            serviceInfo1.setServiceHost("127.0.0.1");
            serviceInfo1.setVersion("1");
//            nacosRegistry.register(serviceInfo1);
            NacosRegistry nacosRegistry1 = new NacosRegistry("127.0.0.1:8848");
            nacosRegistry1.register(serviceInfo1);

            System.out.println(nacosDiscovery.getServices("test"));

            Thread.sleep(50000);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
