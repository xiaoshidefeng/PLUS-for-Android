package com.example.cw.slidemeuetest.MainActivityFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

/**
 * Created by cw on 2016/11/21.
 */

public class FragmentThree extends Fragment {
    //讨论
    private static WebView webView;
    private TextView textView;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        webView=(WebView)getView().findViewById(R.id.id_webViewThree);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://lsuplus.top/library/index");
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        textView= (TextView)getActivity().findViewById(R.id.id_TVtabtwo);
        return inflater.inflate(R.layout.tabthree_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView=(WebView)getView().findViewById(R.id.id_webViewThree);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://lsuplus.top/library/index");
        webView.setWebViewClient(new WebViewClient(){
            //重写加载方法 不跳转浏览器

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
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
