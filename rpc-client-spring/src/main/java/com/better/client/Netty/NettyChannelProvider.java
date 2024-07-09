package com.better.client.Netty;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例，由singletonfactory获取
 * channel缓存类，主要提供两个方法
 * 1.更新缓存
 * 2.获取缓存对象
 */
public class NettyChannelProvider {
    private  static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    //检查缓存，并对channel进行活跃状态检查
    public static Channel getChannel(String hostName, Integer port) {
        String key = hostName + ":" + port;
        if(channelMap.containsKey(key)){
            Channel channel = channelMap.get(key);
            if(channel != null && channel.isActive()){
                return channel;
            }else {
                channelMap.remove(key);
            }
        }
        return null;
    }

    public static Channel getChannel(InetSocketAddress inetSocketAddress) {
        return getChannel(inetSocketAddress.getHostString(), inetSocketAddress.getPort());
    }

    public static void setChannel(String hostName, Integer port, Channel channel) {
        channelMap.put(hostName + ":" + port, channel);
    }

    public static void setChannel(InetSocketAddress inetSocketAddress, Channel channel) {
        channelMap.put(inetSocketAddress.getHostString() + ":" + inetSocketAddress.getPort(), channel);
    }

}
