package com.goolbitg.api.util;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.goolbitg.api.v1.util.StringUtils;

public class StringUtilsTest {

    @Test
    void isNullOrBlank_true() {
        String nullString = null;
        String emptyString = "";
        String blankString = "   ";

        assertThat(StringUtils.isNullOrBlank(nullString)).isTrue();
        assertThat(StringUtils.isNullOrBlank(emptyString)).isTrue();
        assertThat(StringUtils.isNullOrBlank(blankString)).isTrue();
    }

    @Test
    void isNullOrBlank_false() {
        String str1 = "helo";
        String str2 = "string with space\nand newline";

        assertThat(StringUtils.isNullOrBlank(str1)).isFalse();
        assertThat(StringUtils.isNullOrBlank(str2)).isFalse();
    }
}
