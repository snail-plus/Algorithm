package com.whtriples.airPurge.config;

import java.text.SimpleDateFormat;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimestampConfig {

    public static final DateTimeFormatter timestampFormat = DateTimeFormat
            .forPattern("yyyyMMddHHmmss");

    public static final SimpleDateFormat datetampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static final DateTimeFormatter dateFormat = DateTimeFormat
            .forPattern("yyyyMMdd");

    public static final int dayExpired = 2;

}
