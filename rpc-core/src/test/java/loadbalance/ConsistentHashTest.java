package loadbalance;

import com.better.loadbalance.impl.ConsistentHash;
import com.better.protocol.ServiceRegisterInfo;
import com.better.protocol.messages.RequestMessage;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashTest {
    public static void main(String[] args) {
        TreeMap<Integer,String> map = new TreeMap<Integer,String>();
        map.put(1,"1");
        map.put(2,"2");
        map.put(3,"3");
        SortedMap<Integer, String> integerStringSortedMap = map.tailMap(4);
        for(Integer key : integerStringSortedMap.keySet()) {
            System.out.println(key+":"+integerStringSortedMap.get(key));
        }
        System.out.println(integerStringSortedMap.firstKey());
    }

    @Test
    public void testConsistentHash() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<ServiceRegisterInfo> list = new ArrayList<>();
        list.add(new ServiceRegisterInfo("Foo","127.0.0.2",8081,"1.0"));
        list.add(new ServiceRegisterInfo("Foo","127.0.0.3",8082,"1.0"));
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setMethodName("foo");
        requestMessage.setServiceName("Foo-1.0");
        requestMessage.setParameterValues(new Object[]{1,"2",new int[]{1,2,3,4}});

        ConsistentHash consistentHash = new ConsistentHash();
        Method method = ConsistentHash.class.getDeclaredMethod("doSelect", List.class, RequestMessage.class);
        method.setAccessible(true);
        ServiceRegisterInfo invoke = (ServiceRegisterInfo) method.invoke(consistentHash, list, requestMessage);
        System.out.println(invoke);
        requestMessage.setParameterValues(new Object[]{1,20});
        ServiceRegisterInfo invoke2 = (ServiceRegisterInfo) method.invoke(consistentHash, list, requestMessage);
        System.out.println(invoke2);


//        for (int i = 0; i < 100; i++){
//
//            if (invoke != invoke2){
//                System.out.println(i + " " +invoke2);
//            }
//        }



    }
}
