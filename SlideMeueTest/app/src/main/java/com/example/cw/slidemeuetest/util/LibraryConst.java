package com.example.cw.slidemeuetest.util;

/**
 * Created by asus on 2017/11/21.
 */

public class LibraryConst {
    /**
     * 放入书籍
     * post
     */
    public static final String ADD_BOOK = "http://" + MainConst.HOST + "/api/books";

    /**
     * 列出所有书
     * get
     */
    public static final String GET_ALL_BOOK = "http://" + MainConst.HOST + "/api/books";


    /**
     * 书籍详情
     * get
     */
    public static final String GET_BOOK = "http://" + MainConst.HOST + "/api/books/";

    /**
     * 借书
     * PAT
     */
    public static final String BORROW_BOOK = "http://" + MainConst.HOST + "/api/books/";

    /**
     * 更新书籍信息
     * put
     */
    public static final String UPDATE_BOOK = "http://" + MainConst.HOST + "/api/books/";

    /**
     * 删除书籍
     * del
     */
    public static final String DELETE_BOOK = "http://" + MainConst.HOST + "/api/books/";

}
