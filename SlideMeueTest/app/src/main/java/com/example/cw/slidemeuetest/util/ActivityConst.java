package com.example.cw.slidemeuetest.util;

/**
 * Created by asus on 2017/11/21.
 */

public class ActivityConst {

    /**
     * 添加活动
     * post
     */
    public static final String ADD_ACTIVITY = "http://" + MainConst.HOST + "/api/activities";

    /**
     * 列出所有活动
     * get
     */
    public static final String GET_ALL_ACTIVITY = "http://" + MainConst.HOST + "/api/activities";

    /**
     * 活动详情
     * get
     */
    public static final String ACTIVITY_INFO = "http://" + MainConst.HOST + "/api/activities/";

    /**
     * 报名活动
     * post
     */
    public static final String SIGN_UP_ACTIVITY = "http://" + MainConst.HOST + "/api/activities/";

    /**
     * 更新活动信息
     * put
     */
    public static final String UPDATE_ACTIVITY = "http://" + MainConst.HOST + "/api/activities/";

    /**
     * 删除活动
     * del
     */
    public static final String DELETE_ACTIVITY = "http://" + MainConst.HOST + "/api/activities/";


}
