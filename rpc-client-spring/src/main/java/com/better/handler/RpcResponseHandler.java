package com.better.handler;

import com.better.enums.MessageType;
import com.better.exceptions.MessageException;
import com.better.exceptions.RpcException;
import com.better.protocol.messages.ResponseMessage;
import com.better.protocol.RpcMessage;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RpcResponseHandler {
    //未处理的promise集合，key = 消息序列号,value = promise对象
    public static final Map<Integer, Promise<RpcMessage>> UNPROCESSED_RESPONSES = new ConcurrentHashMap<>();


    public static void handle(RpcMessage rpcMessage) throws MessageException {
        MessageType messageType = MessageType.parseByType(rpcMessage.getMessageHeader().getMessageType());
        if (messageType == MessageType.RESPONSE) {
            log.debug("received response message: {}", rpcMessage);
            int sequenceId = rpcMessage.getMessageHeader().getId();
            Promise<RpcMessage> promise = UNPROCESSED_RESPONSES.get(sequenceId);
            //不存在该请求的记录promise
            if (promise == null) {
                log.debug("unlogged promise for sequenceId: {}", sequenceId);
                throw new RpcException("unlogged promise for sequenceId: " + sequenceId);
            }

            //该请求的promise已经处理过了
            if (promise.isSuccess()) {
                log.debug("promise is already completed for sequenceId: {}", sequenceId);
                throw new RpcException("promise is already completed for sequenceId: " + sequenceId);
            }

            ResponseMessage responseMessage = (ResponseMessage) rpcMessage.getMessageBody();
            Exception exception = responseMessage.getException();
            if (exception != null) {
                log.debug("find exception in response for sequenceId: {}", sequenceId);
                promise.setFailure(exception);
            } else {
                promise.setSuccess(rpcMessage);
                log.info("successfully get response for sequenceId: {}", sequenceId);
            }
        }

    }
}
