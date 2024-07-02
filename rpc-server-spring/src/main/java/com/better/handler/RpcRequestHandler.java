package com.better.handler;

import com.better.pojos.RequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RpcRequestHandler.class);

    public Object Handle(RequestMessage request){
        if (request == null){
            log.debug("request is null when handle in RpcRequestHandler");
            return null;
        }



    }

}
