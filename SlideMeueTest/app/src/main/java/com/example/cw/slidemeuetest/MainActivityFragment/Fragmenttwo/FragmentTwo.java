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
import com.example.cw.slidemeuetest.util.DisscussConst;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by cw on 2016/11/21.
 */

public class FragmentTwo extends Fragment {

    //刷新
    private SwipeRefreshLayout refreshtwo = null;

    //图片检测
    private String imgfind = "http://lsuplus.top/uploads";

    private TextView tvnull;

    //FAB 按钮
    private FloatingActionButton floatingActionButton;

    private List<ItemBean> itemBeen = new ArrayList<>();

    private ListView listView;

    private Handler myHandler;

    private OkHttpClient client = new OkHttpClient();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        myHandler = new MyHandler(this);

        //进度条颜色
        refreshtwo.setColorSchemeResources(R.color.colorAccent);

        itemBeen = new ArrayList<>();

        getAllPost();

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

    }

    private void ItemListener() {
        //刷新进度条
        refreshtwo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFreshPost();
            }
        });
    }

    private void initview() {
        refreshtwo = (SwipeRefreshLayout)getActivity().findViewById(R.id.id_refreshtwo);
        listView = (ListView)getActivity().findViewById(R.id.id_Discusslistview);

        floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.id_FABonepost);
        floatingActionButton.attachToListView(listView); // or attachToRecyclerView

        tvnull = (TextView)getActivity().findViewById(R.id.id_Tvpostnull);
        listView.setEmptyView(tvnull);

    }

    static class MyHandler extends Handler {
        WeakReference<FragmentTwo> mActivityReference;

        MyHandler(FragmentTwo activity) {
            mActivityReference= new WeakReference<FragmentTwo>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentTwo activity = mActivityReference.get();
            activity.itemBeen.clear();
            switch (msg.what) {
                case 1:
                    try {
                        formatPostList(activity, new JSONObject(msg.obj.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        formatPostList(activity, new JSONObject(msg.obj.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        activity.refreshtwo.setRefreshing(false);
                        Toast.makeText(activity.getContext(),"刷新完成", LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
        private void formatPostList(FragmentTwo activity, JSONObject jsonObject) throws JSONException {
            JSONObject dataObj = (JSONObject) jsonObject.getJSONObject("data");
            JSONArray dataArray = (JSONArray) dataObj.getJSONArray("data");
            for (int i = 0,count = 0; i < dataArray.length(); i++, count++) {
                JSONObject post = (JSONObject) dataArray.getJSONObject(i);
                JSONObject OnePostJson = dataArray.getJSONObject(i);
                int id = post.getInt("id");
                String title = post.getString("title");
                String created_at = post.getString("created_at");
                String body = post.getString("body");

                JSONObject user = (JSONObject) post.getJSONObject("user");
                String username = user.getString("name");
                String avatar = user.getString("avatar");

                activity.itemBeen.add(new ItemBean(
                        id,
                        username,
                        body,
                        body,
                        avatar,
                        title,
                        "创建于"+created_at
                ));
                MyAdapter myAdapter = new MyAdapter(activity.getContext(), activity.itemBeen);
                activity.listView.setAdapter(myAdapter);
            }

        }
    }

    private void getAllPost() {
        Request request = new Request.Builder().url(DisscussConst.GET_ALL_POST).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what=1;
                msg.obj = response.body().string();
                myHandler.sendMessage(msg);
            }
        });
    }

    private void reFreshPost() {
        Request request = new Request.Builder().url(DisscussConst.GET_ALL_POST).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what=2;
                msg.obj = response.body().string();
                myHandler.sendMessage(msg);
            }
        });
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

}

