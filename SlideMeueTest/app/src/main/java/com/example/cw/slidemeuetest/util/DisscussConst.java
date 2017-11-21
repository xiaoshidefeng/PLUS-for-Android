package com.example.cw.slidemeuetest.util;

/**
 * Created by asus on 2017/11/21.
 */

public class DisscussConst {
    /**
     * 新建帖子
     * post
     */
    public static final String NEW_POST = "http://" + MainConst.HOST + "/api/discussions";

    /**
     * 列出所有帖子
     * get
     */
    public static final String GET_ALL_POST = "http://" + MainConst.HOST + "/api/discussions";

    /**
     * 帖子详情
     * get
     */
    public static final String POST_DETAIL = "http://" + MainConst.HOST + "/api/discussions/";

    /**
     * 发表评论
     * post
     */
    public static final String SEND_COMMENTS = "http://" + MainConst.HOST + "/api/comments";

    /**
     * 更新帖子
     * post
     */
    public static final String UPDATE_POST = "http://" + MainConst.HOST + "/api/topics/update";




}
