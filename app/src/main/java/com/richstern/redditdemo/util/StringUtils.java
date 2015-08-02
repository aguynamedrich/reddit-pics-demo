package com.richstern.redditdemo.util;

import java.util.regex.Pattern;

public class StringUtils {

    public final static String URL_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    private final static Pattern URL_REGEX_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().length() == 0;
    }

    public static boolean isValidUrl(String url) {
        return !isNullOrEmpty(url) && URL_REGEX_PATTERN.matcher(url).matches();
    }
}
