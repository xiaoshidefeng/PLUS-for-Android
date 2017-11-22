package com.example.cw.slidemeuetest.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asus on 2017/11/22.
 */

public class TokenUtil {
    /**
     * 获取token
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");
        return token;
    }

    /**
     * 设置token
     * @param token
     * @param context
     */
    public static void setToken(String token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }
}
