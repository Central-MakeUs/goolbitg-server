package com.goolbitg.api.util;

import java.util.Random;

public class RandomIdGenerator {

    private static final int ID_LENGTH = 10;

    public static String generate() {
        Random random = new Random();
        StringBuilder sb;
        String chars = "abcdefghijklmnopqrstuvwxyz";

        sb = new StringBuilder();
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(26);
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }
}

