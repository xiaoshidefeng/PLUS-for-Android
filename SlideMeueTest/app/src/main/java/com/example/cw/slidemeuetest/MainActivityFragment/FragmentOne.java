package com.example.cw.slidemeuetest.MainActivityFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cw.slidemeuetest.R;

/**
 * Created by cw on 2016/11/21.
 */

public class FragmentOne extends Fragment {

    //官网
    private static WebView webView;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        webView=(WebView)getView().findViewById(R.id.id_webViewOne);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://lsuplus.top/");
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        webView=(WebView)getActivity().findViewById(R.id.id_webViewOne);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://lsuplus.top/");
        return inflater.inflate(R.layout.tabone_layout,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        webView=(WebView)getActivity().findViewById(R.id.id_webViewOne);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://lsuplus.top/");
        webView.setWebViewClient(new WebViewClient(){
            //重写加载方法 不跳转浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        super.onViewCreated(view, savedInstanceState);
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


