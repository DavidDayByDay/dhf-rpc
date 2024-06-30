package com.better.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestMessage implements Serializable {
    private String serviceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameterValues;

    public static RequestMessage getDefaultRequestMessage() {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setServiceName("test_service");
        requestMessage.setMethodName("test_method");
        requestMessage.setParameterTypes(new Class<?>[]{String.class});
        requestMessage.setParameterValues(new Object[]{"test_value"});
       return requestMessage;
    }

}
