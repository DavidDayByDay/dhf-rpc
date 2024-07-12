package com.better.enums;

import com.better.factories.SingletonFactory;
import com.better.loadbalance.LoadBalance;
import com.better.loadbalance.impl.RoundRobin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum LoadbalanceType {

    Random((byte) 0),
    RoundRobin((byte) 1),
    ConsistentHash((byte) 2);


    private final byte type;

    LoadbalanceType(byte type){
        this.type = type;
    }

    public static LoadBalance parseByName(String name){
        if(name.equalsIgnoreCase("ConsistentHash")){
            return SingletonFactory.getInstance(com.better.loadbalance.impl.ConsistentHash.class);
        }else if (name.equalsIgnoreCase("RoundRobin")){
            return SingletonFactory.getInstance(RoundRobin.class);
        }else if (name.equalsIgnoreCase("Random")){
            return SingletonFactory.getInstance(com.better.loadbalance.impl.Random.class);
        }
        throw new IllegalArgumentException("no such loadbalance type");
    }
}
