package com.example.cw.slidemeuetest.MainActivityFragment.Fragmenttwo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.util.IsNull;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import java.util.List;

/**
 * Created by asus on 2017/11/24.
 */

public class DiscussAdapter extends RecyclerView.Adapter<DiscussAdapter.ViewHolder> {

    private List<ItemBean> mList;

    //点击事件回调
    private DiscussAdapter.OnItemClickListener onItemClickListener;

    private int position;

    public DiscussAdapter(List<ItemBean> mList) {
        this.mList = mList;
    }

    public void updateData(List<ItemBean> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(DiscussAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public DiscussAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discuss_listview_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DiscussAdapter.ViewHolder holder, int position) {
        // 绑定数据
        final ItemBean bean = mList.get(position);
        holder.name.setText(bean.ItemName);

        //防止为空时报错
        if(IsNull.isNullField(bean.ItemContent)){
            bean.ItemContent = " ";
        }
        bean.ItemContent = bean.ItemContent+" ";

        this.position = position;
        RichText.fromMarkdown(bean.ItemContent).autoFix(false).fix(new ImageFixCallback() {
            @Override
            public void onFix(ImageHolder holder) {
                if (holder.getImageType() != ImageHolder.ImageType.GIF) {
                    holder.setAutoFix(true);
                } else {
                    holder.setHeight(200 + DiscussAdapter.this.position * 10);
                    holder.setWidth(200 + DiscussAdapter.this.position * 10);
                }
                if (DiscussAdapter.this.position == 0) {
                    holder.setAutoPlay(true);
                } else {
                    holder.setAutoPlay(false);
                }
            }


        }).into(holder.content);

        holder.imageView.setImageURI(bean.getUserImgUrl());
        holder.title.setText(bean.ItemTitle);
        holder.times.setText(bean.ItemCreatTime);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"不给你看Ta的信息o(￣ヘ￣o＃)",Toast.LENGTH_SHORT).show();

            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, bean, pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView content;
        public SimpleDraweeView imageView;
        public TextView title;
        public TextView times;
        public LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.id_TvDiscussName);
            content = (TextView)view.findViewById(R.id.id_TvDiscussContent);
            imageView = (SimpleDraweeView)view.findViewById(R.id.id_IMGhead);
            title = (TextView)view.findViewById(R.id.id_TvDiscussTitle);
            times = (TextView)view.findViewById(R.id.id_TvDiscussTime);
            linearLayout = (LinearLayout)view.findViewById(R.id.id_lltoonepost);

            //设置头像为圆
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(R.color.colorWhite, (float) 1.0);
            roundingParams.setRoundAsCircle(true);
            imageView.getHierarchy().setRoundingParams(roundingParams);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ItemBean itemBean, int position);
        void onItemLongClick(View view, int position);
    }

}
