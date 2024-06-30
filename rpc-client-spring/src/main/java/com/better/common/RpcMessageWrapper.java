package com.better.common;

import com.better.protocol.RpcMessage;
import lombok.Data;

@Data
public class RpcMessageWrapper {
    private RpcMessage rpcMessage;

    private String hostName;

    private Integer port;

    private Integer timeOut;
}
