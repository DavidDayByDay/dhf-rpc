package netty;

import com.better.client.Netty.NettyClient;
import com.better.properties.RpcClientProperties;
import com.better.proxy.ProxyFactory;
import com.better.registryanddiscovery.discovery.ServiceDiscovery;
import com.better.registryanddiscovery.discovery.impl.NacosDiscovery;
import com.better.test.Fooo;

import java.net.ServerSocket;

public class NettyClientTest {
    public static void main(String[] args) {
        NettyClient client = new NettyClient();

//        RpcMessageWrapper rpcMessageWrapper = new RpcMessageWrapper();
//        rpcMessageWrapper.setRpcMessage(RpcMessage.getDefaultRpcMessageWithRequest());
//        rpcMessageWrapper.setPort(8080);
//        rpcMessageWrapper.setHost("127.0.0.1");

//        client.sendRpcRequest(rpcMessageWrapper);
        ServiceDiscovery serviceDiscovery = new NacosDiscovery("127.0.0.1:8848");
        RpcClientProperties properties = new RpcClientProperties();
        ProxyFactory proxyFactory = new ProxyFactory(serviceDiscovery, client, properties);
        Fooo foo = proxyFactory.makeProxy(Fooo.class, "1.0");
        foo.foo();


        ServerSocket serverSocket = null;
    }

//    static interface Fooo{
//        public void foo();
//    }

//    static class Foo implements Fooo{
//        public void foo(){
//            System.out.println("hello foooooooo~~~");
//        };
//    }
}
