package com.goolbitg.api.v1.util;

public class StringUtils {

    public static boolean isNullOrBlank(String str) {
        if (str == null) return true;
        return str.isBlank();
    }
}
