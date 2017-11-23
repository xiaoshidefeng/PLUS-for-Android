package com.example.cw.slidemeuetest.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by asus on 2017/11/22.
 */

public class TokenUtil {

    private static OkHttpClient client = new OkHttpClient();

    private static Context context;

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

    public static void reFreshToken(Context context) throws IOException, JSONException {
        RequestBody formBody = new FormBody.Builder()
                .add("", "")
                .build();
        Request request = new Request.Builder()
                .url(UserConst.REFRESH_TOKEN)
                .addHeader("Authorization", "Bearer " + getToken(context))
                .post(formBody)
                .build();
        TokenUtil.context = context;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.has("result")) {
                        setToken(jsonObject.getString("result"), TokenUtil.context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
