package com.whtriples.airPurge.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneySerializer extends JsonSerializer<Long> {

    public static String toMoney(Long value) {
        return new BigDecimal((double) value / 100).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String toMoney(String value) {
        return toMoney(Long.parseLong(value));
    }

    public static String toFenStr(String money) {
        return String.valueOf((long) (Double.parseDouble(money) * 100));
    }

    public static Long toFen(String money) {
        return (long) (Double.parseDouble(money) * 100);
    }

    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeString(toMoney(value));
    }

}