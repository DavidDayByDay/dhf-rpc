import com.better.annotations.RpcReference;

import java.lang.reflect.Field;

public class ProxyTest {
    public static void main(String[] args) {
//        ProxyFactory proxyFactory = new ProxyFactory(new NacosDiscovery("127.0.0.1:8080"),new NettyClient(),new ClientConfig());

        Field[] declaredFields = TestClass.class.getDeclaredFields();


        for (Field field : declaredFields) {
            RpcReference annotation = field.getAnnotation(RpcReference.class);
            System.out.println(field.getClass());
            System.out.println(field.getType());
            System.out.println(field.getType().isInterface());

        }


    }

}
