package com.example.cw.slidemeuetest.util.myOkHttp;

import okhttp3.Response;

/**
 * Created by asus on 2017/11/21.
 */

public interface Parser<T> {
    T parse(Response response);
}
