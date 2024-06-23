package com.better.enums;

import com.better.exceptions.MessageException;
import lombok.Getter;

public enum MessageType {
    REQUEST((byte) 0),
    RESPONSE((byte) 1),
    /*
    心跳机制用于监控服务状态
     */
    HEARTBEAT_REQUEST((byte) 2),
    HEARTBEAT_RESPONSE((byte) 3);


    @Getter
    private final byte type;

    MessageType(byte type) {
        this.type = type;
    }

    public static MessageType parseByType(byte type) throws MessageException {
        for (MessageType mt : MessageType.values()) {
            if (mt.type == type) {
                return mt;
            }
        }
        throw new MessageException(String.format("the message type %s is not supported", type));
    }
}
