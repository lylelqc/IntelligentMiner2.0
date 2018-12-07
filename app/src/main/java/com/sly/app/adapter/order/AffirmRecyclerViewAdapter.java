package com.sly.app.adapter.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sly.app.R;
import com.sly.app.listener.MyItemClickListener;
import com.sly.app.model.GoodsInfo;
import com.sly.app.utils.CommonUtils;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class AffirmRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private String storeType;
    private String typeName;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<GoodsInfo> beanList;
    private MyItemClickListener mOnItemClickListener = null;

    public AffirmRecyclerViewAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public AffirmRecyclerViewAdapter(Context context, List<GoodsInfo> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public AffirmRecyclerViewAdapter(Context context, List<GoodsInfo> beanList, String typeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.typeName = typeName;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_com_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GoodsInfo mBean = beanList.get(position);
        ViewHolder eholder = (ViewHolder) holder;
        eholder.title.setText(mBean.getComTitle());
        eholder.guige.setText(mBean.getOptionIDCombin());
        eholder.count.setText("X" + mBean.getCount());
        Glide.with(context).load(mBean.getComPicUrl().replace("40-40","400-400")).error(R.drawable.default_image).into(eholder.image);
        if (CommonUtils.isBlank(typeName)) {
            eholder.price.setText("价格 : " + mBean.getPrice());
        } else {
            eholder.price.setText(typeName+": " + mBean.getPrice());
        }
        eholder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return beanList.size() > 0 ? beanList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


//    @Override
//    public int getItemViewType(int position) {
//        if (beanList.size() == 0 || beanList.size() < position + 1) {
//            return EMPTY_VIEW;
//        } else {
//            return super.getItemViewType(position);
//        }
//    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout shopLl;
        public TextView title;
        public TextView price;
        public TextView count;
        public ImageView image;
        public TextView color;
        public TextView guige;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_item_title);
            price = (TextView) view.findViewById(R.id.tv_item_price);
            guige = (TextView) view.findViewById(R.id.tv_item_attrs);
            count = (TextView) view.findViewById(R.id.tv_item_count);
            image = (ImageView) view.findViewById(R.id.iv_item_img);
            shopLl = (RelativeLayout) view.findViewById(R.id.rl_item_layout);
        }
    }
}
