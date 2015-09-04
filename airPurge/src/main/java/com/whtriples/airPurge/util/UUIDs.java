package com.whtriples.airPurge.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UUIDs {

    public static final AtomicInteger seed19 = new AtomicInteger(1);

    public static final AtomicInteger seed15 = new AtomicInteger(1);

    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getTimestamp() {
        return new DateTime().toString("yyyyMMddHHmmss");
    }

    public static String getOrderId19() {
        seed19.compareAndSet(100000, 1);
        return getTimestamp() + StringUtils.leftPad(String.valueOf(seed19.getAndIncrement()), 5, '0');
    }

    public static String getOrderId15() {
        seed19.compareAndSet(10, 1);
        return getTimestamp() + seed19.getAndIncrement();
    }

    public static void main(String args[]){
    	System.out.println(UUIDs.getRandomUUID());
    }
}
