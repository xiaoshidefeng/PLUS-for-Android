package com.example.cw.slidemeuetest.MainActivityFragment;

/**
 * Created by cw on 2016/11/28.
 */

public class ItemBean {
    public int Id;
    public int ItemImageResid;
    public String ItemTitle;

    public String getUserImgUrl() {
        return UserImgUrl;
    }
    public String ItemName;
    public String ItemContent;
    public String ItemCreatTime;
    public String ItemContentImg;
    public String UserImgUrl;

    public int getId() {
        return Id;
    }

    public String getItemContentImg() {
        return ItemContentImg;
    }

    public ItemBean(int id,String itemName, String itemContent, String userImgUrl,
                    String itemTitle, String itemContentImg, String itemCreatTime) {
        Id = id;
        ItemName = itemName;
        ItemContent = itemContent;
        UserImgUrl = userImgUrl;
        ItemTitle = itemTitle;
        ItemContentImg = itemContentImg;
        ItemCreatTime = itemCreatTime;
    }

}
