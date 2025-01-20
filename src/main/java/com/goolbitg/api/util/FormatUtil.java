package com.goolbitg.api.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * FormatUtil
 */
public class FormatUtil {

    public static LocalTime parseTime(String src) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(src, timeFormatter);
    }

    public static String formatTime(LocalTime time) {
        if (time == null) return null;

        return time.getHour() + ":"
            + padInt(time.getMinute()) + ":"
            + padInt(time.getSecond());
    }

    private static String padInt(int value) {
        if (value < 10)
            return "0" + value;
        else
            return "" + value;
    }

}
