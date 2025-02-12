package com.goolbitg.api.util;

public class RandomIdGenerator {

    private static final int ID_LENGTH = 10;

    public static String generate(int input) {
        StringBuilder sb;
        String chars = "abcdefghijklmnopqrstuvwxyz";

        sb = new StringBuilder();
        int value = input;
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = value % chars.length();
            sb.append(chars.charAt(index));
            value /= chars.length();
        }

        return sb.toString();
    }
}

