package com.better.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseMessage implements Serializable {
    private Object response;

    //当exception不为null时，视为响应异常
    private Exception exception;
}
