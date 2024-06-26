package com.better.utils;

import com.better.pojos.ServiceInfo;
import com.google.gson.Gson;

import java.util.Map;

public class ServiceInfoConverter {
    public static Gson gson = new Gson();

    //服务签名（key）生成
    public static String serviceKey(String serviceName,String version){
       return String.join("-",serviceName,version);
    }

    public static String serviceKey(ServiceInfo serviceInfo){
        return serviceKey(serviceInfo.getServiceName(),serviceInfo.getVersion());
    }

    public static Map toMap(ServiceInfo serviceInfo) {
        Map map = gson.fromJson(gson.toJson(serviceInfo), Map.class);
        //gson 会将字符数字转化为double
        map.put("servicePort", serviceInfo.getServicePort().toString());
        return map;
    }

    public static ServiceInfo toServiceInfo(Map map) {
        ServiceInfo serviceInfo = gson.fromJson(gson.toJson(map), ServiceInfo.class);
        return serviceInfo;
    }

}
