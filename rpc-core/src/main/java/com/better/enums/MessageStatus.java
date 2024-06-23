package com.better.enums;

import lombok.Getter;

public enum MessageStatus {
    SUCCESS((byte) 0),
    FAILED((byte) 1);

    @Getter
    private final byte type;

    MessageStatus(byte type) {
        this.type = type;
    }

    public static boolean isSuccess(byte type) {
//        return MessageStatus.SUCCESS.type == type;
        return SUCCESS.type == type;
    }
}
