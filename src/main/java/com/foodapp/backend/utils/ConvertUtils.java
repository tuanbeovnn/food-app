package com.foodapp.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ConvertUtils {
    private String stringValue;

    public ConvertUtils(String stringValue) {
        this.stringValue = stringValue;
    }

    public <T> T toModel(Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(stringValue, tClass);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ConvertUtils of(String jsonValue) {
        return new ConvertUtils(jsonValue);
    }
}
