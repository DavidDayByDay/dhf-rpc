package com.better.client;

import com.better.wrappers.RpcMessageWrapper;
import com.better.protocol.RpcMessage;

//todo  socket
public interface Client {
    RpcMessage sendRpcRequest(RpcMessageWrapper rpcMessageWrapper);
}
