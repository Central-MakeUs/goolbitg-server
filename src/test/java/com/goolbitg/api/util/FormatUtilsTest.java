package com.goolbitg.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import com.goolbitg.api.v1.util.FormatUtils;
import org.junit.jupiter.api.Test;

/**
 * FormatUtilTest
 */
class FormatUtilsTest {

    @Test
    void parse_time_from_string() {
        String source = "18:30:00";
        LocalTime time = FormatUtils.parseTime(source);

        assertEquals(time, LocalTime.of(18, 30, 0));
    }

    @Test
    void format_time_to_string() {
        LocalTime time = LocalTime.of(18, 30, 0);
        String string = FormatUtils.formatTime(time);

        assertEquals(string, "18:30:00");
    }
}
