package com.example.cw.slidemeuetest.util.myOkHttp;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by asus on 2017/11/21.
 */

public class Callback<T> implements okhttp3.Callback {

    private static final int CALLBACK_SUCCESSFUL=0x01;
    private static final int CALLBACK_FAILED=0x02;

    private Parser<T> mParser;

    public Callback(Parser<T> mParser) {
        if (mParser == null) {
            throw new IllegalArgumentException("Parser can't be null");
        }
        this.mParser = mParser;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Message message=Message.obtain();
        message.what=CALLBACK_FAILED;
        message.obj=e;
        mHandler.sendMessage(message);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            T parseResult = mParser.parse(response);
            Message message=Message.obtain();
            message.what=CALLBACK_SUCCESSFUL;
            message.obj=parseResult;
            mHandler.sendMessage(message);
        } else {
            Message message=Message.obtain();
            message.what=CALLBACK_FAILED;
            mHandler.sendMessage(message);
        }
    }

    static class UIHandler<T> extends Handler {
        private WeakReference mWeakReference;
        public UIHandler(com.example.cw.slidemeuetest.util.myOkHttp.Callback<T> callback){
//            super(Looper.getMainLooper());
            mWeakReference=new WeakReference(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CALLBACK_SUCCESSFUL: {
                    T t = (T) msg.obj;
                    com.example.cw.slidemeuetest.util.myOkHttp.Callback callback = (com.example.cw.slidemeuetest.util.myOkHttp.Callback) mWeakReference.get();
                    if (callback != null) {
                        callback.onResponse(t);
                    }
                    break;
                }
                case CALLBACK_FAILED: {
                    IOException e = (IOException) msg.obj;
                    com.example.cw.slidemeuetest.util.myOkHttp.Callback callback = (com.example.cw.slidemeuetest.util.myOkHttp.Callback) mWeakReference.get();
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                    break;
                }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    public void onFailure(IOException e) {
    }

    public void onResponse(T t) {
    }


    private Handler mHandler=new UIHandler(this);
}
