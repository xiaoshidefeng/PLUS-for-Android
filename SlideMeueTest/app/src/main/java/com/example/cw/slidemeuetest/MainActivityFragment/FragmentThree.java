package com.example.cw.slidemeuetest.MainActivityFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.cw.slidemeuetest.R;

/**
 * Created by cw on 2016/11/21.
 */

public class FragmentThree extends Fragment {
    //讨论
    private WebView webView;
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
        return inflater.inflate(R.layout.tabthree_layout,container,false);
    }
}
