package com.better.client.Socket;


import com.better.client.Client;
import com.better.exceptions.RpcException;
import com.better.protocol.RpcMessage;
import com.better.protocol.messages.ResponseMessage;
import com.better.wrappers.RpcMessageWrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 基于 Socket 实现 RpcClient 类
 */
public class SocketRpcClient implements Client {

    @Override
    public RpcMessage sendRpcRequest(RpcMessageWrapper rpcMessageWrapper) {
        // 获取服务器地址和端口，构建 socket address
        InetSocketAddress socketAddress = rpcMessageWrapper.getAddress();
        try (Socket socket = new Socket()) {
            // 与服务器建立连接
            socket.connect(socketAddress);
            // 注意：SocketClient 发送和接受的数据为：RpcRequest, RpcResponse
            // 发送数据给服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(rpcMessageWrapper.getRpcMessage());
            //flush to solve package corruption
            //or use a length filed
            oos.flush();
            // 阻塞等待服务端的响应
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ResponseMessage response = (ResponseMessage) ois.readObject();
            // 封装成 RpcMessage 对象
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setMessageBody(response);
            return rpcMessage;
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("The socket client failed to send or receive message.", e);
        }
    }


}
