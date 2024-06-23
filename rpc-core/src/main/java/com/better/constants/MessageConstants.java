package com.better.constants;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageConstants {
    //建议消息唯一序列号id
    private static final AtomicInteger id = new AtomicInteger(0);


    public static final byte[] MAGIC_NUMBER = new byte[] {(byte) 'M',(byte) 'R',(byte) 'P',(byte) 'C'};

    public static final byte VERSION = 1;

    public static int getMessageId(){
        return id.getAndIncrement();
    }

}
