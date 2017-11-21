package com.example.cw.slidemeuetest.util;

/**
 * Created by asus on 2017/11/21.
 * 用户相关常量类
 */

public class UserConst {
    /**
     * 登录获取token
     * post
     */
    public static final String LOGIN = "http://" + MainConst.HOST + "/api/login";

    /**
     * 刷新token
     * post
     */
    public static final String REFRESH_TOKEN = "http://" + MainConst.HOST + "/api/refresh";

    /**
     * 获取用户信息(所有人可见)
     * get
     */
    public static final String GET_USER_INFO = "http://" + MainConst.HOST + "/api/user/";

    /**
     * 修改密码
     * post
     */
    public static final String RESET_PASSWORD = "http://" + MainConst.HOST + "/api/user/reset";

    /**
     * 获取验证码
     * post
     */
    public static final String GET_CONFIRM_CODE = "http://" + MainConst.HOST + "/api/user/code";

    /**
     * 忘记密码
     * post
     */
    public static final String FORGET_PASSWORD = "http://" + MainConst.HOST + "/api/user/forget";

    /**
     * 获取用户信息(个人可见)
     * get
     */
    public static final String GET_USER_DETAILS = "http://" + MainConst.HOST + "/api/get_user_details";

    /**
     * 用户所有帖子
     * get
     */
    public static final String GET_ALL_POST = "http://" + MainConst.HOST + "/api/user/1/discussions";

    /**
     * 获取用户所有回复
     * get
     */
    public static final String GET_ALL_USER_REPLY = "http://" + MainConst.HOST + "/api/user/1/comments";

    /**
     * 获取所有用户(admin)
     * get
     */
    public static final String GET_ALL_USER = "http://" + MainConst.HOST + "/api/users/all";
}
