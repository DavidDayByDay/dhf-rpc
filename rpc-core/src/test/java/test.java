import com.better.constants.MessageConstants;
import com.better.enums.MessageStatus;
import com.better.enums.SerializerType;
import com.better.exceptions.SerializeException;
import com.better.factories.SerializerFactory;
import com.better.protocol.messages.ServiceRegisterInfo;
import com.better.protocol.MessageHeader;
import com.better.discovery.impl.NacosDiscovery;
import com.better.test.Fooo;
import com.better.utils.ServiceInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class test {
//    @Test
//    public void test1(){
//        System.out.println(1);
//    }

    @Test
    public void test() {
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo();
        serviceRegisterInfo.setServiceHost("127.0.0.1");
        serviceRegisterInfo.setServicePort(8888);
        serviceRegisterInfo.setServiceNameAsInterface("test");
        serviceRegisterInfo.setVersion("1");
        Map map = ServiceInfoUtils.toMap(serviceRegisterInfo);
        map.put("servicePort",8888);
        for (Object key : map.keySet()) {
            System.out.println(key + ":" + map.get(key) + "\nclass:" + map.get(key).getClass());
        }
    }

    @Test
    public void testString(){
        String servicename = ServiceInfoUtils.getServiceNameByInterface(Fooo.class);
        servicename = ServiceInfoUtils.serviceKey(servicename,"1.0");
        System.out.println(servicename);

    }

    @Test
    public void testDiscovery(){
        NacosDiscovery nacosDiscovery = new NacosDiscovery("127.0.0.1:8848");
        List<ServiceRegisterInfo> services = nacosDiscovery.getServices("Fooo-1.0");
        services.forEach(System.out::println);
    }

    @Test
    public void test1(){
        Map<Class<?>, Object> objectMap = new HashMap<Class<?>, Object>();
        objectMap.put(ServiceRegisterInfo.class, new ServiceRegisterInfo());
        objectMap.put(ServiceInfoUtils.class, new ServiceInfoUtils());
    }



    public static void main(String[] args) {
        byte[] bytes = "hello world".getBytes(StandardCharsets.UTF_8);
        MessageHeader messageHeader = MessageHeader.buildMessageHeader((byte) 1, (byte) 2, (byte) 1, bytes.length);
        MessageHeader messageHeader2 = MessageHeader.buildMessageHeader((byte) 1, (byte) 2, (byte) 1, bytes.length);
        System.out.println(messageHeader);
        System.out.println(messageHeader2);


        SerializerType json = SerializerType.PROTOSTUFF;
        byte type = json.getType();
        System.out.println(type);

        log.debug("hele");

//        try {
//            MessageType.parseByType((byte) 100);
//        } catch (MessageException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }

        MessageStatus messageStatus = MessageStatus.SUCCESS;
        System.out.println(MessageStatus.isSuccess(messageStatus.getType()));
//        System.out.println(messageStatus.getClass().getModifiers());


        try {
            System.out.println(SerializerFactory.getSerializer((byte) 3));
        } catch (SerializeException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Arrays.toString(MessageConstants.MAGIC_NUMBER));

        int q = 1;
        boolean isDone = false;

        switch (q){
            case 1:
                System.out.println(1);
                isDone = true;

            default:
                if (isDone != true){
                    System.out.println(1000);
                }

        }
    }


}
