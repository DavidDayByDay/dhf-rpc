package registry;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.better.pojos.ServiceInfo;
import com.better.registryanddiscovery.registry.impl.NacosRegistry;
import com.better.utils.ServiceInfoConverter;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class NacosInfoTest {
    ServiceInfo serviceInfo = new ServiceInfo();
    ServiceInfo serviceInfo1 = new ServiceInfo();
    Instance instance = new Instance();

    @Before
    public void prepareInfo(){
        serviceInfo.setServiceName("test");
        serviceInfo.setServicePort(8800);
        serviceInfo.setServiceAddress("127.0.0.1:8800");
        serviceInfo.setVersion("1");

        Map map = ServiceInfoConverter.toMap(serviceInfo);
        instance.setMetadata(map);
        ServiceInfo serviceInfo1 = ServiceInfoConverter.toServiceInfo(map);
        System.out.println(serviceInfo1);

    }

    @Test
    public void test() {
        NacosRegistry nacosRegistry = new NacosRegistry("127.0.0.1:8848");

        nacosRegistry.register(serviceInfo);
        try {
            Thread.sleep(5000);
            nacosRegistry.unRegister(serviceInfo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test2(){
        Map map = ServiceInfoConverter.toMap(serviceInfo);
        for(Object key : map.keySet()){
            System.out.println(key+":"+map.get(key) + "\n"+ map.get(key).getClass());
        }

    }
}
