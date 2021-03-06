package com.example.cw.slidemeuetest.PostContent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.util.HeadImgUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import java.util.List;

/**
 * Created by asus on 2017/11/27.
 */

public class PAdapter extends RecyclerView.Adapter<PAdapter.ViewHolder> {

    private List<ItemBeanPost> mList;

    //点击事件回调
    private PAdapter.OnItemClickListener onItemClickListener;

    private int position;

    public PAdapter(List<ItemBeanPost> mList) {
        this.mList = mList;
    }

    public void updateData(List<ItemBeanPost> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    //  添加数据
    public void addData(int position, ItemBeanPost itemBeanPost) {
        mList.add(position, itemBeanPost);
        notifyItemInserted(position);
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(PAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_listview_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemBeanPost bean = mList.get(position);

        holder.name.setText(bean.ItemNamepost);

        holder.imageView.setImageURI(bean.getItemUserImgpost());
        holder.times.setText(bean.ItemCreatTimepost);

        //防止为空时报错
        if(!bean.ItemContentpost.equals("")){
            RichText.fromMarkdown(bean.ItemContentpost).autoFix(false).fix(new ImageFixCallback() {
                @Override
                public void onFix(ImageHolder holder) {
                    if (holder.getImageType() != ImageHolder.ImageType.GIF) {
                        holder.setAutoFix(true);
                    } else {
                        holder.setHeight(200 + PAdapter.this.position * 10);
                        holder.setWidth(200 + PAdapter.this.position * 10);
                    }
                    if (PAdapter.this.position == 0) {
                        holder.setAutoPlay(true);
                    } else {
                        holder.setAutoPlay(false);
                    }
                }


            }).into(holder.content);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView content;
        public SimpleDraweeView imageView;
        public TextView times;

        public ViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.id_TvPostName);
            content = (TextView)view.findViewById(R.id.id_TvPostContent);
            imageView = (SimpleDraweeView)view.findViewById(R.id.id_IMGheadPost);
            times = (TextView)view.findViewById(R.id.id_TvPostTime);

            //设置头像为圆
            imageView.getHierarchy().setRoundingParams(HeadImgUtil.getRoundingParams(5f, 1));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ItemBeanPost itemBean, int position);
        void onItemLongClick(View view, int position);
    }
}
