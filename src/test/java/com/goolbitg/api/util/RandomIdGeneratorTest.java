package com.goolbitg.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.goolbitg.api.v1.util.RandomIdGenerator;
import org.junit.jupiter.api.Test;

/**
 * RandomIdGeneratorTest
 */
public class RandomIdGeneratorTest {

    @Test
    void get_random_id() {
        String randomId = RandomIdGenerator.generate();

        assertEquals(10, randomId.length());
    }

}
