package com.example.cw.slidemeuetest.PostContent;

/**
 * Created by cw on 2016/12/5.
 */

public class ItemBeanPost {
    public String ItemNamepost;
    public String ItemContentpost;
    public String ItemCreatTimepost;

    public String getItemUserImgpost() {
        return ItemUserImgpost;
    }

    public String ItemUserImgpost;

    public ItemBeanPost(String itemNamepost, String itemContentpost,
                        String itemCreatTimepost, String itemUserImgpost) {
        ItemNamepost = itemNamepost;
        ItemContentpost = itemContentpost;
        ItemCreatTimepost = itemCreatTimepost;
        ItemUserImgpost = itemUserImgpost;
    }
}
