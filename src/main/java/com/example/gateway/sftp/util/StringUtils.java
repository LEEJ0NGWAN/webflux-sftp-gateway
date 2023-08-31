package com.example.gateway.sftp.util;

public class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String string) {

        final int length = string==null? 0: string.length();

        if (length==0) return true;

        for (int index=0; index<length; index++)
        if (!Character.isWhitespace(string.charAt(index)))
        return false;
        return true;
    }
}
