package com.example.cw.slidemeuetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by cw on 2016/10/26.
 */
public class NetworkChangeReciver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable()){
            Toast.makeText(context,"网络已连接",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context,"网络未连接",Toast.LENGTH_LONG).show();
        }
    }
}
