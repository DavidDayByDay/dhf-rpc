package netty;

import com.better.client.Netty.NettyClient;
import com.better.common.RpcMessageWrapper;
import com.better.protocol.RpcMessage;

public class NettyClientTest {
    public static void main(String[] args) {
        NettyClient client = new NettyClient();

        RpcMessageWrapper rpcMessageWrapper = new RpcMessageWrapper();
        rpcMessageWrapper.setRpcMessage(RpcMessage.getDefaultRpcMessageWithRequest());
        rpcMessageWrapper.setPort(8080);
        rpcMessageWrapper.setHostName("127.0.0.1");

        client.sendRpcRequest(rpcMessageWrapper);
    }
}
