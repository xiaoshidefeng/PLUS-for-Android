package com.example.cw.slidemeuetest.MainActivityFragment.Fragmenttwo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by cw on 2016/11/21.
 */

public class FragmentTwo extends Fragment {
    //讨论
    //private static WebView webView;

    //刷新
    private SwipeRefreshLayout refreshtwo = null;

    private String GetAllPostUrl = "http://lsuplus.top/api/discuss";

    //plus网址
    private String plus = "http://lsuplus.top";

    //图片检测
    private String imgfind = "http://lsuplus.top/uploads";

    private TextView tvnull;

    //FAB 按钮
    private FloatingActionButton floatingActionButton;

    private String content;

    //private MyAdapter myAdapter;

    private List<ItemBean> itemBeen = new ArrayList<>();

    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sendHttpURLConnectionGETuserInfo();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabtwo_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview();


        //进度条颜色
        refreshtwo.setColorSchemeResources(R.color.colorAccent);

        itemBeen = new ArrayList<>();


//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://lsuplus.top/discuss");
//        webView.setWebViewClient(new WebViewClient(){
//            //重写加载方法 不跳转浏览器
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//        });
//        ListView listView = (ListView) getActivity().findViewById(R.id.id_Discusslistview);
//        List<ItemBean> dataList = new ArrayList<>();
//        // 创建假数据
//        for (int i = 0; i < 20; i++) {
//            dataList.add(new ItemBean(
//                    R.mipmap.ic_launcher,
//                    "我是标题" + i,
//                    "我是内容" + i));
//        }
//        // 设置适配器
//        listView.setAdapter(new MyAdapter(this.getActivity(), dataList));

        sendHttpURLConnectionGETuserInfo();

        ItemListener();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable(getContext())){
                    Intent intent = new Intent(getActivity(),NewPostActivity.class);
                    startActivity(intent);
                }

            }
        });
//        if(listView!=null){
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    //Log.e("errss", String.valueOf(i));
//
//                    Toast.makeText(getActivity(),i+"test"+l,Toast.LENGTH_LONG).show();
//
//                }
//            });
//        }


    }

    private void ItemListener() {
        //刷新进度条
        refreshtwo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //webView.loadUrl(webView.getUrl());
                new Thread(new Runnable() {//下拉触发的函数，这里是谁1s然后加入一个数据，然后更新界面
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            Message message = new Message();
                            message.what=1;
                            myhandler.sendMessage(message);
                            itemBeen.clear();
                            sendHttpURLConnectionGETuserInfo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });



//        listView.getOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //int id = itemBeen.get(l).getId();
//                Toast.makeText(getContext(),"test",Toast.LENGTH_LONG).show();
//            }
//        });

    }

    private void initview() {
        //webView=(WebView)getView().findViewById(R.id.id_webViewTwo);
        refreshtwo = (SwipeRefreshLayout)getActivity().findViewById(R.id.id_refreshtwo);
        listView = (ListView)getActivity().findViewById(R.id.id_Discusslistview);

        //listView.setAdapter(myAdapter);
                floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.id_FABonepost);
        floatingActionButton.attachToListView(listView); // or attachToRecyclerView

        tvnull = (TextView)getActivity().findViewById(R.id.id_Tvpostnull);
        listView.setEmptyView(tvnull);

    }

    //停止刷新
    Handler myhandler = new Handler(){
        public  void handleMessage(Message message){
            switch (message.what) {
                case 1:
                    //sendHttpURLConnectionGETuserInfo();
                    refreshtwo.setRefreshing(false);
                    Toast.makeText(getContext(),"刷新完成", LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //通过GET方法 获取所有贴子信息
    private void sendHttpURLConnectionGETuserInfo() {
        //开启子线程访问网络 获取所有贴子信息模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {

                    URL url = new URL(GetAllPostUrl);

                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    //连接超时设置
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //获取输入流
                    InputStream in = connection.getInputStream();

                    //对获取的流进行读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }


                    //创建JSON对象
                    JSONObject AllPostJson = new JSONObject(response.toString());
                    Log.e("errss", response.toString());
                    if(AllPostJson.has("data")){
                        //创建JSON数组
                        JSONArray dataArray = AllPostJson.getJSONArray("data");
                        for(int i = 0;i<dataArray.length();i++){
                            JSONObject OnePostJson = dataArray.getJSONObject(i);
                            String title = OnePostJson.getString("title");
                            String created_at = OnePostJson.getString("created_at");
                            String userimgurl = OnePostJson.getString("avatar");
                            String username = OnePostJson.getString("name");
                            int id = OnePostJson.getInt("id");
                            content = OnePostJson.getString("body");
                            String allpostcontent = content;
//                            String contentimgurl = haveImg(content);

//                            if(contentimgurl!=null&&(!contentimgurl.equals(""))){
//                                //删文字
//                                int fir = content.indexOf("![\\");
//                                //Log.e("errssss", String.valueOf(fir));
//
//                                int last1 = content.indexOf(contentimgurl);
//                                int last2 = 0;
//                                if(contentimgurl.contains(".jpg")){
//                                    last2 = contentimgurl.indexOf(".jpg");
//                                }else if(contentimgurl.contains(".jpeg")){
//                                    last2 = contentimgurl.indexOf(".jpeg");
//                                }else if(contentimgurl.contains(".png")){
//                                    last2 = contentimgurl.indexOf(".png");
//                                }
//                                content = content.substring(0,fir)+"[[图片]]"+
//                                        content.substring(last2+last1+4,content.length());
//
//                            }

                            if(created_at.length()>10){
                                //时间过久
                                JSONObject creatat = new JSONObject(created_at.toString());
                                    created_at = creatat.getString("date");
                                    created_at = created_at.substring(0,19);
                            }

                            //Log.e("errss", created_at.toString());


                            itemBeen.add(new ItemBean(
                                    id,
                                    username,
                                    content,
                                    allpostcontent,
                                    plus+userimgurl,
                                    title,
//                                    contentimgurl,
                                    "创建于"+created_at
                            ));


                        }

                        //开启ui线程
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 MyAdapter myAdapter = new MyAdapter(getContext(),itemBeen);
                                listView.setAdapter(myAdapter);
                                //myAdapter.notifyDataSetChanged();

                            }
                        });


                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                itemBeen.clear();
                                listView.setAdapter(null);
                                //myAdapter.notifyDataSetChanged();

                            }
                        });
                        return;
                    }

                }   catch (Exception e) {
                    Log.e("errss catch", e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(null);

                        }
                    });
                }
            }
        }).start();
    }

    private String haveImg(String content) {
        String contentImg = null;
        if(content.contains(imgfind)){
            Log.e("errssimg", content);


            int first = content.indexOf(imgfind);
            contentImg = content.substring(first,content.length());

            if(contentImg.contains(".jpg")){
                first=0;
                contentImg = contentImg.substring(first,contentImg.indexOf(".jpeg")+5);
            }else if(contentImg.contains(".jpeg")){
                first=0;
                contentImg = contentImg.substring(first,contentImg.indexOf(".jpg")+4);
            }else if(contentImg.contains(".png")){
                first=0;
                contentImg = contentImg.substring(first,contentImg.indexOf(".png")+4);

            }

            return contentImg;
        }else {
            return "";
        }

    }

    //判断当前网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    //go back
//    public static boolean goback() {
//        if (webView.canGoBack()) {
//            //网页能返回则优先返回网页
//            webView.goBack();
//            return true;
//        }
//        return false;
//
//    }
}

