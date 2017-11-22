package com.example.cw.slidemeuetest;

import com.example.cw.slidemeuetest.util.DisscussConst;
import com.example.cw.slidemeuetest.util.UserConst;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private OkHttpClient mOkHttpClient;


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void constTest() {
        System.out.println(UserConst.GET_ALL_POST);
    }

    @Test
    public void okHttpTest() throws IOException {
//        System.out.println(_getAsyn(DisscussConst.GET_ALL_POST).toString());
//        performSyncHttpRequest();


//        OkHttpClient okHttpClient=new OkHttpClient();
//        StringParser parser=new StringParser();
//        Request request = new Request.Builder().url("https://www.baidu.com").build();
//        okHttpClient.newCall(request).enqueue(new Callback<String>(parser) {
//            @Override
//            public void onResponse(String s) {
//                System.out.println(s);
//            }
//
//        });
    }


    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url) throws IOException
    {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    private void performSyncHttpRequest() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(DisscussConst.GET_ALL_POST)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        System.out.print(response.isSuccessful());
    }

}