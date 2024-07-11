package com.better.utils;

import com.better.protocol.messages.ServiceRegisterInfo;
import com.google.gson.Gson;

import java.util.Map;

public class ServiceInfoUtils {
    public static Gson gson = new Gson();

    /**
     * 服务签名（key）生成
     * 同时也是服务的注册名
     */

    public static String serviceKey(String serviceName,String version){
       return String.join("-",serviceName,version);
    }

    public static String serviceKey(ServiceRegisterInfo serviceRegisterInfo){
        return serviceKey(serviceRegisterInfo.getServiceNameAsInterface(), serviceRegisterInfo.getVersion());
    }

    public static Map toMap(ServiceRegisterInfo serviceRegisterInfo) {
        Map map = gson.fromJson(gson.toJson(serviceRegisterInfo), Map.class);
        //gson 会将字符数字转化为double
        map.put("servicePort", serviceRegisterInfo.getServicePort().toString());
        return map;
    }

    public static ServiceRegisterInfo toServiceInfo(Map map) {
        ServiceRegisterInfo serviceRegisterInfo = gson.fromJson(gson.toJson(map), ServiceRegisterInfo.class);
        return serviceRegisterInfo;
    }

    //约定的注册服务命名方式--client和server得到的接口名默认是相同的否则应该手动指定相同的两端发现和注册的服务命名
    public static <T> String getServiceNameByInterface(Class<T> interfaceClass) {
        String fullName = interfaceClass.getName();
        String name = fullName.substring(fullName.lastIndexOf(".") + 1);
//        String key = ServiceInfoUtils.serviceKey(name, version);
        return name;
    }

}
