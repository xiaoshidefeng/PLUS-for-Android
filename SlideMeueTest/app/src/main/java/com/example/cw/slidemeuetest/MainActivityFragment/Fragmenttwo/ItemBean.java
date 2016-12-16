package com.example.cw.slidemeuetest.MainActivityFragment.Fragmenttwo;

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
    public String RawConten;
   // public String ItemContentImg;
    public String UserImgUrl;

    public int getId() {
        return Id;
    }

//    public String getItemContentImg() {
//        return ItemContentImg;
//    }

    public ItemBean(int id,String itemName, String itemContent,String rawConten, String userImgUrl,
                    String itemTitle, String itemCreatTime) {
        Id = id;
        ItemName = itemName;
        ItemContent = itemContent;
        RawConten = rawConten;
        UserImgUrl = userImgUrl;
        ItemTitle = itemTitle;
        ItemCreatTime = itemCreatTime;
    }

}
