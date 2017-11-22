package com.example.cw.slidemeuetest.util;

/**
 * Created by asus on 2017/11/21.
 * 空判断
 */

public class IsNull {

    public static boolean isNullField(String string) {
        Boolean string_is_null = string == null || string.equals("");
        if (string_is_null) {
            return true;
        }
        return false;
    }

    public static boolean isNullField(String s1, String s2) {
        Boolean s1_is_null = s1 == null || s1.equals("");
        Boolean s2_is_null = s2 == null || s2.equals("");

        if (s1_is_null || s2_is_null) {
            return true;
        }
        return false;
    }

    public static boolean isNullField(String s1, String s2, String s3) {
        Boolean s1_is_null = s1 == null || s1.equals("");
        Boolean s2_is_null = s2 == null || s2.equals("");
        Boolean s3_is_null = s3 == null || s3.equals("");
        if (s1_is_null || s2_is_null || s3_is_null) {
            return true;
        }
        return false;
    }
}

