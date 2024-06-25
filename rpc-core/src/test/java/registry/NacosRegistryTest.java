package registry;

import com.better.pojos.ServiceInfo;
import com.better.registryanddiscovery.registry.impl.NacosRegistry;
import org.junit.Test;

public class NacosRegistryTest {
    @Test
    public void test() {
        NacosRegistry nacosRegistry = new NacosRegistry("127.0.0.1:8848");
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceName("test");
        serviceInfo.setServicePort(8800);
        serviceInfo.setServiceAddress("127.0.0.1:8800");
        serviceInfo.setVersion("1");

        nacosRegistry.register(serviceInfo);
        try {
            Thread.sleep(5000);
            nacosRegistry.unRegister(serviceInfo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
