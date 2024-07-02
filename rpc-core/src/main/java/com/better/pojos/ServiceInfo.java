package com.better.pojos;

import lombok.Data;

@Data
public class ServiceInfo {
    private String serviceName;

    private String serviceHost;

    private Integer servicePort;

    private String version;
}
