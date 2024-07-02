package com.better.enums;

import com.better.loadbalance.LoadBalance;
import com.better.loadbalance.impl.RoundRobin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum LoadbalanceType {

    Random((byte) 0),
    RoudRobin((byte) 1),
    ConsistentHash((byte) 2);


    @Getter
    private byte type;

    LoadbalanceType(byte type){
        this.type = type;
    }

    public static LoadBalance parseByName(String name){
        if(name.equalsIgnoreCase("ConsistentHash")){
            return null;
        }else if (name.equalsIgnoreCase("RoundRobin")){
            return new RoundRobin();
        }else if (name.equalsIgnoreCase("Random")){
            return null;
        }
        throw new IllegalArgumentException("no such loadbalance type");
    }
}
