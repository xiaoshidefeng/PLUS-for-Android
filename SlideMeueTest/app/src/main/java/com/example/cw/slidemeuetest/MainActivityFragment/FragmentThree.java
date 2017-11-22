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
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.util.MainConst;

/**
 * Created by cw on 2016/11/21.
 */

public class FragmentThree extends Fragment {
    //讨论
    private static WebView webView;
    private TextView textView;

    //刷新
    private SwipeRefreshLayout refreshthree = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        textView= (TextView)getActivity().findViewById(R.id.id_TVtabtwo);
        return inflater.inflate(R.layout.tabthree_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview();

        //进度条颜色
        refreshthree.setColorSchemeResources(R.color.colorAccent);

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

        ItemListener();


    }

    private void ItemListener() {
        //刷新进度条
        refreshthree.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(webView.getUrl());
                new Thread(new Runnable() {//下拉触发的函数，这里是谁1s然后加入一个数据，然后更新界面
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            Message message = new Message();
                            message.what=2;
                            myhandler.sendMessage(message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
    }

    private void initview() {
        webView=(WebView)getView().findViewById(R.id.id_webViewThree);
        refreshthree = (SwipeRefreshLayout)getActivity().findViewById(R.id.id_refreshthree);
    }

    //停止刷新
    Handler myhandler = new Handler(){
        public  void handleMessage(Message message){
            switch (message.what) {
                case 2:
                    refreshthree.setRefreshing(false);
                    Toast.makeText(getContext(),"刷新完成",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


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
