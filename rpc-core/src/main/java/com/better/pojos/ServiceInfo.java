package com.better.pojos;

import lombok.Data;

@Data
public class ServiceInfo {
    private String serviceName;

    private String serviceAddress;

    private int servicePort;

    private String version;
}
