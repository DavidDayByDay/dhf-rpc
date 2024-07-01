package com.better.wrappers;

import com.better.protocol.RpcMessage;
import lombok.Data;

import java.net.InetSocketAddress;

/**
 * 发起远程调用的必要信息准备
 */
@Data
public class RpcMessageWrapper {
    private RpcMessage rpcMessage;

    private String host;

    private Integer port;

    private Integer timeOut;

    private InetSocketAddress address;
}
