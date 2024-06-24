package com.better.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class DefaultLengthFieldFrameDecoder extends LengthFieldBasedFrameDecoder {
    //default for the protocol
    public DefaultLengthFieldFrameDecoder(){
        super(1024,12,4);
    }


    public DefaultLengthFieldFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
