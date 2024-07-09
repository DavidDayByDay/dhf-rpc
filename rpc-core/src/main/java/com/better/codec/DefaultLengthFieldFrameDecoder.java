package com.better.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 使用netty的{@LengthFieldBasedFrameDecoder},是一个出入站处理器
 */
public class DefaultLengthFieldFrameDecoder extends LengthFieldBasedFrameDecoder {
    //default for the protocol
    public DefaultLengthFieldFrameDecoder(){
        super(1024,12,4);
    }


    public DefaultLengthFieldFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
