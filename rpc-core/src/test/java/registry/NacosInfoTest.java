package registry;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.better.protocol.ServiceRegisterInfo;
import com.better.registry.impl.NacosRegistry;
import com.better.utils.ServiceInfoUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class NacosInfoTest {
    ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo();
    ServiceRegisterInfo serviceRegisterInfo1 = new ServiceRegisterInfo();
    Instance instance = new Instance();

    @Before
    public void prepareInfo(){
        serviceRegisterInfo.setServiceNameAsInterface("test");
        serviceRegisterInfo.setServicePort(8800);
        serviceRegisterInfo.setServiceHost("127.0.0.1");
        serviceRegisterInfo.setVersion("1");

        Map map = ServiceInfoUtils.toMap(serviceRegisterInfo);
        instance.setMetadata(map);
        ServiceRegisterInfo serviceRegisterInfo1 = ServiceInfoUtils.toServiceInfo(map);
        System.out.println(serviceRegisterInfo1);

    }

    @Test
    public void test() {
        NacosRegistry nacosRegistry = new NacosRegistry("127.0.0.1:8848");

        nacosRegistry.register(serviceRegisterInfo);
        try {
            Thread.sleep(5000);
            nacosRegistry.unRegister(serviceRegisterInfo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test2(){
        Map map = ServiceInfoUtils.toMap(serviceRegisterInfo);
        for(Object key : map.keySet()){
            System.out.println(key+":"+map.get(key) + "\n"+ map.get(key).getClass());
        }

    }
}
