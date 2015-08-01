package com.richstern.redditdemo.util;

public class StringUtils {
    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().length() == 0;
    }
}
