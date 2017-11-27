package com.example.cw.slidemeuetest.PostContent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.util.DisscussConst;
import com.example.cw.slidemeuetest.util.IsNull;
import com.example.cw.slidemeuetest.util.MyDividerItemDecoration;
import com.example.cw.slidemeuetest.util.TokenUtil;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostActivity extends AppCompatActivity {

    private List<ItemBeanPost> itembeanpost = new ArrayList<>();

    private ListView listviewpostone;

    private ProgressDialog progressDialog;

    private TextView tvnull;

    //返回
    private TextView Tvback;
    //标题
    private TextView maintitle;

    //显示状态
    private Boolean showedit = false;

    private ProgressBar progressBar;

    //编辑框与发送按钮
    private LinearLayout llEdit;

    //编辑框
    private EditText etreply;

    private ImageView imSend;

    //回复内容
    private String replystr;
    private String postid;
    private String token;
    private String userid;

    private int Id;
    private String PostOne;
    private String PostMainTitle;
    private String smalltail;

    private PostAdapter postAdapter;

    private Handler myHandler;

    private OkHttpClient client = new OkHttpClient();

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager mLayoutManager;

    private RefreshLayout refreshLayout;

    private PAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initview();
        myHandler = new MyHandler(this);
        //进度条开始转动
        progressBar.setVisibility(View.VISIBLE);
        getPostinfo();

        reFreshItemListener();

        try {
            TokenUtil.reFreshToken(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        etreply.clearFocus();

        //监控输入框状态 输入后变蓝
        etreply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0 && i1 == 0 && i2 != 0) {
                    imSend.setImageResource(R.drawable.ic_send_button_changed);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = etreply.getText().length();
                if (length == 0) {
                    imSend.setImageResource(R.drawable.ic_send_button);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length = etreply.getText().length();
                if (length == 0) {
                    return;
                } else if (IsNull.isNullField(TokenUtil.getToken(PostActivity.this))) {
                    Toast.makeText(PostActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SharedPreferences sharedPreferences2 = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    smalltail = sharedPreferences2.getString("smalltail", "");

                    replystr = etreply.getText().toString();

                    if (smalltail.equals("来自Plus客户端")) {
                        //彩蛋 特殊小尾巴
                        replystr = replystr + "\n\n" + "*" + "——————————" + smalltail + "*" + "\n" +
                                "\n\n一切伟大的行动和思想\n\n都有一个微不足道的开始";
                    } else if (!IsNull.isNullField(smalltail)) {
                        replystr = replystr + "\n\n" + "*" + "——————————" + smalltail + "*";
                    }

                    etreply.setText("");
                    //点击send按钮后 强制隐藏键盘
                    InputMethodManager immPw = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    immPw.hideSoftInputFromWindow(etreply.getWindowToken(), 0);
                    etreply.clearFocus();
                    progressDialog = new ProgressDialog(PostActivity.this);
                    progressDialog.setMessage("请稍后...");
                    //进度条周围不可点击
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    sendComment(replystr);
                }
            }
        });

    }

    private void reFreshItemListener() {
        refreshLayout = (RefreshLayout) findViewById(R.id.id_rvRefreshPost);
        //设置 Header 为 BezierCircleHeader
        refreshLayout.setRefreshHeader(new BezierCircleHeader(PostActivity.this));
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                reFreshPost();
            }
        });


    }

    /**
     * 内部静态Handler 类 防止内存泄漏
     */
    static class MyHandler extends Handler {
        WeakReference<PostActivity> mActivityReference;

        MyHandler(PostActivity activity) {
            mActivityReference = new WeakReference<PostActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PostActivity activity = mActivityReference.get();
            if (msg.what == 1) {
                //获取所有评论
                try {
                    showPage(msg, activity);
                    //formatPost(activity, new JSONObject(msg.obj.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    //隐藏进度条
                    activity.progressBar.setVisibility(View.GONE);
                }
            } else if (msg.what == 2) {
                //发送评论
                activity.getPostinfo();
                activity.progressDialog.cancel();
            }

        }

        private void showPage(Message msg, PostActivity activity) throws JSONException {
            List<ItemBeanPost> itemBeanList = formatPost(activity, new JSONObject(msg.obj.toString()));
            activity.pAdapter.updateData(itemBeanList);
        }

        private List<ItemBeanPost> formatPost(PostActivity activity, JSONObject jsonObject) throws JSONException {
            if (jsonObject.has("data")) {
                JSONObject data = jsonObject.getJSONObject("data");
                if (data.has("comments")) {
                    JSONArray dataArray = data.getJSONArray("comments");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject onePostJson = dataArray.getJSONObject(i);
                        String created_at = onePostJson.getString("created_at");
                        String content = onePostJson.getString("body");

                        JSONObject user = onePostJson.getJSONObject("user");
                        String username = user.getString("name");
                        String userImgUrl = user.getString("avatar");
                        activity.itembeanpost.add(new ItemBeanPost(
                                username,
                                content,
                                "回复于 " + created_at,
                                userImgUrl
                        ));
                    }
                }
            } else {
                Toast.makeText(activity, "获取贴子信息失败", Toast.LENGTH_SHORT).show();
            }
            return activity.itembeanpost;
        }
    }

    /**
     * 回帖
     *
     * @param content
     */
    private void sendComment(String content) {
        RequestBody body = new FormBody.Builder()
                .add("body", content)//添加键值对
                .add("discussion_id", Id + "")
                .build();
        Request request = new Request.Builder()
                .url(DisscussConst.SEND_COMMENTS)
                .addHeader("Authorization", "Bearer " + TokenUtil.getToken(getApplicationContext()))
                .post(body)
                .build();
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

    /**
     * 获取帖子信息2
     */
    private void getComments() {
        Request request = new Request.Builder().url(DisscussConst.POST_DETAIL + Id).build();
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


    /**
     * 获取帖子信息1 添加头部帖子
     */
    private void getPostinfo() {
        itembeanpost.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("postInfo", Context.MODE_PRIVATE);
        PostOne = sharedPreferences.getString("postone", "");
        PostMainTitle = sharedPreferences.getString("maintitle", "");
        Id = sharedPreferences.getInt("postid", 0);
        String firstpost = sharedPreferences.getString("postone", "");
        String userimg = sharedPreferences.getString("userheadimg", "");
        String usern = sharedPreferences.getString("username", "");
        String firsttime = sharedPreferences.getString("creattime", "");
        itembeanpost.add(new ItemBeanPost(
                usern,
                firstpost,
                firsttime,
                userimg
        ));
        maintitle.setText(PostMainTitle);
        getComments();
    }

    /**
     * 初始化组件
     */
    private void initview() {
        Tvback = (TextView) findViewById(R.id.id_registerBackText);
        maintitle = (TextView) findViewById(R.id.id_TvpostMaintitle);
        progressBar = (ProgressBar) findViewById(R.id.id_Pbnewreply);
        llEdit = (LinearLayout) findViewById(R.id.id_llEdit);
        imSend = (ImageView) findViewById(R.id.id_IMSendpost);
        etreply = (EditText) findViewById(R.id.id_etReply);
        tvnull = (TextView) findViewById(R.id.id_tvpostonenull);

        itembeanpost = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(PostActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.id_rvPost);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(PostActivity.this, LinearLayoutManager.VERTICAL));
        pAdapter = new PAdapter(itembeanpost);
        recyclerView.setAdapter(pAdapter);

        itembeanpost.clear();

        //listviewpostone = (ListView) findViewById(R.id.id_lvPostContent);
//        listviewpostone.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
//        postAdapter = new PostAdapter(getApplicationContext(),
//                itembeanpost);
//
//        View footerView = getLayoutInflater().inflate(R.layout.foot_layout, null, false);
//
//        listviewpostone.addFooterView(footerView);
//        listviewpostone.setEmptyView(tvnull);
    }

}