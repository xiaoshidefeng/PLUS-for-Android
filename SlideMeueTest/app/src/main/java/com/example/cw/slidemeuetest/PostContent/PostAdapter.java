package com.example.cw.slidemeuetest.PostContent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import java.util.List;

/**
 * Created by cw on 2016/12/8.
 */

public class PostAdapter extends BaseAdapter {

    private List<ItemBeanPost> mList;

    private LayoutInflater mInflater;

    public PostAdapter(Context context, List<ItemBeanPost> list){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if(view == null){

            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.post_listview_item,null);

            viewHolder.name = (TextView)view.findViewById(R.id.id_TvPostName);
            viewHolder.content = (TextView)view.findViewById(R.id.id_TvPostContent);
            viewHolder.imageView = (SimpleDraweeView)view.findViewById(R.id.id_IMGheadPost);
            viewHolder.times = (TextView)view.findViewById(R.id.id_TvPostTime);
            view.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        Fresco.initialize(view.getContext());
        final ItemBeanPost bean = mList.get(i);

        viewHolder.name.setText(bean.ItemNamepost);

        viewHolder.imageView.setImageURI(bean.getItemUserImgpost());
        viewHolder.times.setText(bean.ItemCreatTimepost);

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(R.color.colorWhite, (float) 1.0);
        roundingParams.setRoundAsCircle(true);
        viewHolder.imageView.getHierarchy().setRoundingParams(roundingParams);


        Log.e("viewholder",bean.ItemContentpost);

        //防止为空时报错
        if(!bean.ItemContentpost.equals("")){
            //bean.ItemContentpost = " ";
            RichText.fromMarkdown(bean.ItemContentpost).autoFix(false).fix(new ImageFixCallback() {
                @Override
                public void onFix(ImageHolder holder) {
                    if (holder.getImageType() != ImageHolder.ImageType.GIF) {
                        holder.setAutoFix(true);
                    } else {
                        holder.setHeight(200 + i * 10);
                        holder.setWidth(200 + i * 10);
                    }
                    if (i == 0) {
                        holder.setAutoPlay(true);
                    } else {
                        holder.setAutoPlay(false);
                    }
                }


            }).into(viewHolder.content);
        }



        return view;
    }


    class ViewHolder{
        public TextView name;
        public TextView content;
        public SimpleDraweeView imageView;
//        public TextView title;
//        public SimpleDraweeView contentimg;
        public TextView times;

//        public LinearLayout linearLayout;
    }
}
