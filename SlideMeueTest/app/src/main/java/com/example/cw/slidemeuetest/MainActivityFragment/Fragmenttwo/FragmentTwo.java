package com.example.cw.slidemeuetest.MainActivityFragment.Fragmenttwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cw.slidemeuetest.PostContent.PostActivity;
import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.util.DisscussConst;
import com.example.cw.slidemeuetest.util.MyDividerItemDecoration;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

    //FAB 按钮
    private FloatingActionButton floatingActionButton;

    private List<ItemBean> itemBeen = new ArrayList<>();

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager mLayoutManager;

    private DiscussAdapter mAdapter;

    private Handler myHandler;

    private OkHttpClient client = new OkHttpClient();

    private RefreshLayout refreshLayout;

    private int pageNum;

    private int totalPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabtwo_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview();
        myHandler = new MyHandler(this);

        itemBeen = new ArrayList<>();

        getAllPost();

        //刷新控件初始化
        reFreshItemListener();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(getContext())) {
                    Intent intent = new Intent(getActivity(), NewPostActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void reFreshItemListener() {
        refreshLayout = (RefreshLayout) getActivity().findViewById(R.id.id_discussRefreshLayout);
        //设置 Header 为 BezierCircleHeader
        refreshLayout.setRefreshHeader(new BezierCircleHeader(getActivity()));
        refreshLayout.setPrimaryColors(getActivity().getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                reFreshPost();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getMorePost();
            }
        });

    }

    private void initview() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.id_discussRecyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new DiscussAdapter(itemBeen);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DiscussAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ItemBean itemBean, int position) {
                saveItemInfo(view, itemBean);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.id_FABonepost);
        floatingActionButton.attachToRecyclerView(recyclerView); // or attachToRecyclerView

        //初始化页数为1
        pageNum = 2;
        totalPage = 2;
        itemBeen.clear();
    }

    private void saveItemInfo(View view, ItemBean bean) {
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("postInfo",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("maintitle", bean.ItemTitle);
        editor.putString("postone", bean.RawConten);
        editor.putInt("postid", bean.getId());
        editor.putString("userheadimg", bean.getUserImgUrl());
        editor.putString("username", bean.ItemName);
        editor.putString("creattime", bean.ItemCreatTime);
        editor.commit();

        Intent intent = new Intent(view.getContext(), PostActivity.class);
        view.getContext().startActivity(intent);
    }

    static class MyHandler extends Handler {
        WeakReference<FragmentTwo> mActivityReference;

        MyHandler(FragmentTwo activity) {
            mActivityReference = new WeakReference<FragmentTwo>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentTwo activity = mActivityReference.get();
            try {
                switch (msg.what) {
                    case 1:
                        activity.itemBeen.clear();
                        showPage(msg, activity);
                        break;
                    case 2:
                        activity.itemBeen.clear();
                        activity.pageNum = 1;
                        showPage(msg, activity);
                        Toast.makeText(activity.getContext(), "刷新完成", LENGTH_SHORT).show();
                        break;
                    case 3:
                        showPage(msg, activity);
                        //获取更多帖子后 在这里计数器加一较好
                        ++activity.pageNum;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                activity.refreshLayout.finishRefresh();
                activity.refreshLayout.finishLoadmore();
            }
        }

        private void showPage(Message msg, FragmentTwo activity) throws JSONException {
            List<ItemBean> itemBeanList = formatPostList(activity, new JSONObject(msg.obj.toString()));
            activity.mAdapter.updateData(itemBeanList);
        }

        private List<ItemBean> formatPostList(FragmentTwo activity, JSONObject jsonObject) throws JSONException {
            JSONObject dataObj = (JSONObject) jsonObject.getJSONObject("data");
            //获取最大页数
            activity.totalPage = dataObj.getInt("last_page");
            //获取评论数组
            JSONArray dataArray = (JSONArray) dataObj.getJSONArray("data");
            for (int i = 0, count = 0; i < dataArray.length(); i++, count++) {
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
                        "创建于" + created_at
                ));
            }
            return activity.itemBeen;
        }

    }

    private void getAllPost() {
        Request request = new Request.Builder().url(DisscussConst.GET_ALL_POST + "1").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = response.body().string();
                myHandler.sendMessage(msg);
            }
        });
    }

    private void reFreshPost() {
        Request request = new Request.Builder().url(DisscussConst.GET_ALL_POST + "1").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = response.body().string();
                myHandler.sendMessage(msg);
            }
        });
    }

    private void getMorePost() {
        if (totalPage < pageNum) {
            Toast.makeText(getActivity(), "没有啦 m(｡≧ｴ≦｡)m ", LENGTH_SHORT).show();
            refreshLayout.finishLoadmore();
            return;
        }
        //已经有一页了 所以从2开始
        Request request = new Request.Builder().url(DisscussConst.GET_ALL_POST + pageNum).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what = 3;
                msg.obj = response.body().string();
                myHandler.sendMessage(msg);
            }
        });
    }

    private String haveImg(String content) {
        String contentImg = null;
        if (content.contains(imgfind)) {
            Log.e("errssimg", content);

            int first = content.indexOf(imgfind);
            contentImg = content.substring(first, content.length());

            if (contentImg.contains(".jpg")) {
                first = 0;
                contentImg = contentImg.substring(first, contentImg.indexOf(".jpeg") + 5);
            } else if (contentImg.contains(".jpeg")) {
                first = 0;
                contentImg = contentImg.substring(first, contentImg.indexOf(".jpg") + 4);
            } else if (contentImg.contains(".png")) {
                first = 0;
                contentImg = contentImg.substring(first, contentImg.indexOf(".png") + 4);
            }
            return contentImg;
        } else {
            return "";
        }

    }

    //判断当前网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

}

