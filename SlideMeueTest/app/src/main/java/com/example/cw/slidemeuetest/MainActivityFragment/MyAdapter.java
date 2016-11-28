package com.example.cw.slidemeuetest.MainActivityFragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cw on 2016/11/28.
 */

public class MyAdapter extends BaseAdapter {

    private List<ItemBean> mList;

    public MyAdapter(List<ItemBean> list){
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){

        }
        return null;
    }

    class ViewHolder{
        public ImageView imageView;
        public TextView title;
        public TextView content;

    }
}
