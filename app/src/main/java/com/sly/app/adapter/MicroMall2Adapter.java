package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sly.app.R;
import com.sly.app.model.HomeHotComBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/14 19:19
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class MicroMall2Adapter extends RecyclerView.Adapter<MicroMall2Adapter.ViewHolder> implements View.OnClickListener {
    private LayoutInflater mInflater;
    private List<HomeHotComBean> mDatas;
    private Context mContext;

    public MicroMall2Adapter(Context context, List<HomeHotComBean> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
        ImageView mImg;
        TextView mTitle;
        TextView mPrice;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        ViewHolder viewHolder;
        view = mInflater.inflate(R.layout.item_m_mall_hot, viewGroup, false);
        viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.iv_hot_img);
        viewHolder.mTitle = (TextView) view.findViewById(R.id.tv_hot_name);
        viewHolder.mPrice = (TextView) view.findViewById(R.id.tv_hot_price);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.mTitle.setText(mDatas.get(i).getTitle());
        viewHolder.mPrice.setText(mDatas.get(i).getPrice()+"");
        Glide.with(mContext)
                .load(mDatas.get(i).getFilePath())
                .placeholder(R.drawable.common_details_carousel_placeholder)
                .error(R.drawable.common_details_carousel_placeholder)
                .into(viewHolder.mImg);
        viewHolder.itemView.setTag(i);
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static interface loadMoreItemClickListener {
        void loadMoreData(View view, int position);
    }

    public void setloadMoreItemClickListener(loadMoreItemClickListener listener) {
        this.mloadMoreItemClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    private OnItemClickListener mOnItemClickListener = null;
    private loadMoreItemClickListener mloadMoreItemClickListener = null;

}
