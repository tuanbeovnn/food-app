package com.foodapp.backend.utils;

public class SQLUtils {

    public static String formatLikeStringSql(String value){
        String valueLike = escapeSQL(value);
        return "%"+valueLike+"%";
    }

    public static String escapeSQL(String value){
        String result = value.trim().replace("/", "\\/").replace("_", "\\_").replace("%", "\\%");
        return result.toLowerCase();
    }



}
