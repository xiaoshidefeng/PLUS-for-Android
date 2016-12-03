package com.example.cw.slidemeuetest.MainActivityFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

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

            viewHolder.name = (TextView)view.findViewById(R.id.id_TvDiscussName);
            viewHolder.content = (TextView)view.findViewById(R.id.id_TvDiscussContent);
            viewHolder.imageView = (SimpleDraweeView)view.findViewById(R.id.id_IMGhead);
            viewHolder.title = (TextView)view.findViewById(R.id.id_TvDiscussTitle);
            viewHolder.contentimg = (SimpleDraweeView)view.findViewById(R.id.id_IMGcontent);
            viewHolder.times = (TextView)view.findViewById(R.id.id_TvDiscussTime);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        Fresco.initialize(view.getContext());

        ItemBean bean = mList.get(i);
        viewHolder.name.setText(bean.ItemName);
        viewHolder.content.setText(bean.ItemContent);
        viewHolder.imageView.setImageURI(bean.getUserImgUrl());
        viewHolder.title.setText(bean.ItemTitle);
        viewHolder.times.setText(bean.ItemCreatTime);

        if(bean.getItemContentImg()!=null&&(!bean.getItemContentImg().equals(""))){
            viewHolder.contentimg.setImageURI(bean.getItemContentImg());
            viewHolder.contentimg.setAspectRatio(1.62f);

        }

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(R.color.colorWhite, (float) 1.0);
        roundingParams.setRoundAsCircle(true);
        viewHolder.imageView.getHierarchy().setRoundingParams(roundingParams);
        return view;
    }

    class ViewHolder{
        public TextView name;
        public TextView content;
        public SimpleDraweeView imageView;
        public TextView title;
        public SimpleDraweeView contentimg;
        public TextView times;

    }
}
