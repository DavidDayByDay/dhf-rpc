package com.better.server.http;

import com.better.exceptions.RpcException;
import com.better.handler.RpcRequestHandler;
import com.better.protocol.messages.RequestMessage;
import com.better.protocol.messages.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 基于 HTTP 的 RpcRequest handler
 *
 */
@Slf4j
public class HttpRpcRequestHandler {



    @SuppressWarnings("Duplicates")
    public void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(resp.getOutputStream());
            // 读取客户端请求
            RequestMessage request = (RequestMessage) ois.readObject();
            log.debug("The server received message is {}.", request);
            // 创建一个 RpcResponse 对象来响应客户端
            ResponseMessage response = new ResponseMessage();
            // 处理请求
            try {
                // 获取请求的服务对应的实例对象反射调用方法的结果
                Object result = RpcRequestHandler.Handle(request);
                response.setResponse(result);
            } catch (Exception e) {
                log.error("The service [{}], the method [{}] invoke failed!", request.getServiceName(), request.getMethodName());
                // 若不设置，堆栈信息过多，导致报错
                response.setException(new RpcException("Error in remote procedure call, " + e.getMessage()));
            }
            log.debug("The response is {}.", response);
            oos.writeObject(response);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("The http server failed to handle client rpc request.", e);
        }
    }

}
