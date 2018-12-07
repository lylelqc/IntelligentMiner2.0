package com.sly.app.adapter.point;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sly.app.R;
import com.sly.app.adapter.viewHolder.EmptyViewHolder;
import com.sly.app.listener.MyItemClickListener;
import com.sly.app.model.GoodsBean;
import com.sly.app.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class PointMallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<GoodsBean> beanList;
    private MyItemClickListener mOnItemClickListener = null;

    public PointMallAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public PointMallAdapter(Context context, List<GoodsBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_list_footer, parent, false);
            EmptyViewHolder holder = new EmptyViewHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_point_mall, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(this);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        } else {
            final GoodsBean gcBean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
            Glide.with(context)
                    .load(gcBean.getImgList().get(0))
                    .placeholder(R.drawable.common_details_carousel_placeholder)
                    .error(R.drawable.common_details_carousel_placeholder)
                    .into(eholder.mReconmendImg);
            eholder.mRecommendPrice.setText(CommonUtils.getDoubleStr(Double.valueOf(gcBean.getMartPrice())));
            eholder.mRecommendName.setText(gcBean.getComTitle());
            eholder.itemView.setTag(position);
        }
    }


    @Override
    public int getItemCount() {
        return beanList.size() > 0 ? beanList.size() + 1 : 1;
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


    @Override
    public int getItemViewType(int position) {
        if (beanList.size() == 0 || beanList.size() < position + 1) {
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.reconmend_img)
        ImageView mReconmendImg;
        @BindView(R.id.recommend_name)
        TextView mRecommendName;
        @BindView(R.id.recommend_price)
        TextView mRecommendPrice;
        @BindView(R.id.recommend_lv)
        LinearLayout mRecommendLv;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
