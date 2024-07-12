package com.better.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo {
    private String serviceNameAsInterface;

    private String serviceHost;

    private Integer servicePort;

    private String version;
}
