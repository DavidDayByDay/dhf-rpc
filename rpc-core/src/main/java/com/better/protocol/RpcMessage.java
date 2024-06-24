package com.better.protocol;

import lombok.Data;

/**
 * Rpc协议消息类
 */


@Data
public class RpcMessage {
    //请求头
    private MessageHeader messageHeader;
    //消息正文
    private Object messageBody;
}
