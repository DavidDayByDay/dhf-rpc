package com.better.client.Http;


import com.better.client.Client;
import com.better.protocol.RpcMessage;
import com.better.protocol.messages.ResponseMessage;
import com.better.wrappers.RpcMessageWrapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpRpcClient implements Client {

    @Override
    public RpcMessage sendRpcRequest(RpcMessageWrapper requestMetadata) {
        try {
            URL url = new URL("http", requestMetadata.getHost(), requestMetadata.getPort(), "/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            // 配置
            OutputStream os = httpURLConnection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            // 发送 RpcRequest 对象
            oos.writeObject(requestMetadata.getRpcMessage().getMessageBody());
            oos.flush();
            oos.close();

            // 构造 RpcMessage 对象
            RpcMessage rpcMessage = new RpcMessage();

            InputStream is = httpURLConnection.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            // 阻塞读取
            ResponseMessage response = (ResponseMessage) ois.readObject();
            rpcMessage.setMessageBody(response);
            return rpcMessage;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
