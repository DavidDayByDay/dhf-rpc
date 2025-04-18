package com.better.server.socket;

import com.better.exceptions.RpcException;
import com.better.handler.RpcRequestHandler;
import com.better.protocol.messages.RequestMessage;
import com.better.protocol.messages.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 处理 RpcRequest，基于 Socket 通信

 */
@Slf4j
public class SocketRpcRequestHandler implements Runnable {
    private final Socket socket;

    public SocketRpcRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void run() {
        log.debug("The server handle client message by thread {}.", Thread.currentThread().getName());
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            // 注意：SocketServer 接受和发送的数据为：RpcRequest, RpcResponse
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // 直接读取客户端发送过来的 RpcRequest，此时不需要进行编解码，无需消息协议
            RequestMessage request = (RequestMessage) ois.readObject();
            log.debug("The server received message is {}.", request);
            // 创建一个 RpcResponse 对象用来响应给客户端
            ResponseMessage response = new ResponseMessage();
            // 处理请求
            try {
                // 获取请求的服务对应的实例对象反射调用方法的结果
                Object handle = RpcRequestHandler.Handle(request);
                response.setResponse(handle);
            } catch (Exception e) {
                log.error("The service [{}], the method [{}] invoke failed!", request.getServiceName(), request.getMethodName());
                // 若不设置，堆栈信息过多，导致报错
                response.setException(new RpcException("Error in remote procedure call, " + e.getMessage()));
            }
            log.debug("The response is {}.", response);
            oos.writeObject(response);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("The socket server failed to handle client rpc request.", e);
        }
    }
}
