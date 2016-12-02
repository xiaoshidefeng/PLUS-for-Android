package com.example.cw.slidemeuetest.MainActivityFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

import java.util.List;

/**
 * Created by cw on 2016/11/28.
 */

public class MyAdapter extends BaseAdapter {

    private List<ItemBean> mList;

    private LayoutInflater mInflater;

    public MyAdapter(Context context,List<ItemBean> list){
        mList = list;
        mInflater = LayoutInflater.from(context);
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
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.discuss_listview_item,null);

            viewHolder.imageView = (ImageView)view.findViewById(R.id.id_IMGhead);
            viewHolder.title = (TextView)view.findViewById(R.id.id_TvDiscussTitle);
            viewHolder.content = (TextView)view.findViewById(R.id.id_TvDiscussContent);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        ItemBean bean = mList.get(i);
        viewHolder.imageView.setImageResource(bean.ItemImageResid);
        viewHolder.title.setText(bean.ItemTitle);
        viewHolder.content.setText(bean.ItemContent);
        return view;
    }

    class ViewHolder{
        public ImageView imageView;
        public TextView title;
        public TextView content;

    }
}
