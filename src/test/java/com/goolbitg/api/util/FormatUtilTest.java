package com.goolbitg.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

/**
 * FormatUtilTest
 */
class FormatUtilTest {

    @Test
    void parse_time_from_string() {
        String source = "18:30:00";
        LocalTime time = FormatUtil.parseTime(source);

        assertEquals(time, LocalTime.of(18, 30, 0));
    }

    @Test
    void format_time_to_string() {
        LocalTime time = LocalTime.of(18, 30, 0);
        String string = FormatUtil.formatTime(time);

        assertEquals(string, "18:30:00");
    }
}
