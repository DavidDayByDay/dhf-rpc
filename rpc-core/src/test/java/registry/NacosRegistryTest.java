package registry;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.better.pojos.ServiceInfo;
import com.better.registryanddiscovery.discovery.impl.NacosDiscovery;
import com.better.registryanddiscovery.registry.impl.NacosRegistry;
import org.junit.Before;
import org.junit.Test;

public class NacosRegistryTest {
    ServiceInfo serviceInfo = new ServiceInfo();
    Instance instance = new Instance();

    @Before
    public void prepareInfo(){
        serviceInfo.setServiceName("test");
        serviceInfo.setServicePort(8800);
        serviceInfo.setServiceAddress("127.0.0.1:8800");
        serviceInfo.setVersion("1");

//        Map map = ServiceInfoConverter.toMap(serviceInfo);
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


            Thread.sleep(1000);

            NacosDiscovery nacosDiscovery = new NacosDiscovery("127.0.0.1:8848");
            System.out.println(nacosDiscovery.getServices("test"));


            ServiceInfo serviceInfo1 = new ServiceInfo();
            serviceInfo1.setServiceName("test");
            serviceInfo1.setServicePort(8801);
            serviceInfo1.setServiceAddress("127.0.0.1:8801");
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
