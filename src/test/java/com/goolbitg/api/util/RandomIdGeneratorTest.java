package com.goolbitg.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * RandomIdGeneratorTest
 */
public class RandomIdGeneratorTest {

    @Test
    void get_random_id() {
        int src = 26;
        String randomId = RandomIdGenerator.generate(src);

        assertEquals("abaaaaaaaa", randomId);
    }

}
