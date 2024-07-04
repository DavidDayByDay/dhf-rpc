package com.better.pojos;

import lombok.Data;

@Data
public class ServiceRegisterInfo {
    private String serviceNameAsInterface;

    private String serviceHost;

    private Integer servicePort;

    private String version;
}
