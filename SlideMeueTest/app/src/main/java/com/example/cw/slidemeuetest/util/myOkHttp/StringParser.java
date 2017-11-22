package com.example.cw.slidemeuetest.util.myOkHttp;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by asus on 2017/11/21.
 */

public class StringParser implements Parser<String> {
    @Override
    public String parse(Response response) {
        String result=null;
        try {
            result=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}