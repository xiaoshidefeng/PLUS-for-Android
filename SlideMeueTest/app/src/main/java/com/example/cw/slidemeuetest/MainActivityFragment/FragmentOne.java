package com.example.cw.slidemeuetest.MainActivityFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.util.MainConst;

import java.lang.ref.WeakReference;

/**
 * Created by cw on 2016/11/21.
 */

public class FragmentOne extends Fragment {

    //官网
    private static WebView webView;

    //刷新
    private SwipeRefreshLayout refreshone = null;

    private Handler myHandler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tabone_layout,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initview();

        //进度条颜色
        refreshone.setColorSchemeResources(R.color.colorAccent);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://" + MainConst.HOST);
        webView.setWebViewClient(new WebViewClient(){
            //重写加载方法 不跳转浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

        });

        //监听
        ItemListener();

        super.onViewCreated(view, savedInstanceState);
    }

    private void ItemListener() {

        //刷新进度条
        refreshone.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(webView.getUrl());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            Message message = new Message();
                            message.what=0;
                            myHandler.sendMessage(message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });


    }

    //初始化
    private void initview() {
        webView=(WebView)getActivity().findViewById(R.id.id_webViewOne);
        refreshone = (SwipeRefreshLayout)getActivity().findViewById(R.id.id_refreshone);
    }

    static class MyHandler extends Handler {
        WeakReference<FragmentOne> mActivityReference;

        MyHandler(FragmentOne activity) {
            mActivityReference = new WeakReference<FragmentOne>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentOne activity = mActivityReference.get();
            if (msg.what == 1) {
                activity.refreshone.setRefreshing(false);
                Toast.makeText(activity.getContext(),"刷新完成",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //go back
    public static boolean goback() {
        if (webView.canGoBack()) {
            //网页能返回则优先返回网页
            webView.goBack();
            return true;
        }
        return false;
    }

}


