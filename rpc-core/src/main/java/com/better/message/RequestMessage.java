package com.better.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestMessage implements Serializable {
    private String serviceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameterValues;

}
