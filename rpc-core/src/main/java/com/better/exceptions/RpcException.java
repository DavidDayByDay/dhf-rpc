package com.better.exceptions;

public class RpcException extends RuntimeException {
    public RpcException(String messge){
        super(messge);
    }

    public RpcException(String messge, Throwable cause){
        super(messge, cause);
    }

}
