package com.better.client;

import com.better.common.RpcMessageWrapper;
import com.better.protocol.RpcMessage;

public interface Client {
    RpcMessage sendRpcRequest(RpcMessageWrapper rpcMessageWrapper);
}
