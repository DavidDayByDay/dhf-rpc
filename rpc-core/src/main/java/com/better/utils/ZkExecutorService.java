package com.better.utils;

import lombok.Getter;
import org.apache.curator.utils.CloseableExecutorService;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper发现服务监听器使用的线程池
 */
@Getter
public class ZkExecutorService{
    private final CloseableExecutorService executor = new CloseableExecutorService(new ThreadPoolExecutor(
            1,20,1000, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()
    ),true);

}
