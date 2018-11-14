package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sly.app.R;
import com.sly.app.adapter.viewHolder.EmptyViewHolder;
import com.sly.app.model.GoodsBean;
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
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<GoodsBean> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public HomeRecyclerViewAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public HomeRecyclerViewAdapter(Context context, List<GoodsBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public HomeRecyclerViewAdapter(Context context, List<GoodsBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public HomeRecyclerViewAdapter(Context context, List<GoodsBean> beanList, boolean isAddFootView) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isAddFootView = isAddFootView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_list_footer, parent, false);
            if (isAddFootView == false) {
                EmptyViewHolder holder = new EmptyViewHolder(view, false);
                return holder;
            } else {
                EmptyViewHolder holder = new EmptyViewHolder(view, true);
                return holder;
            }
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_home_hot, parent, false);
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
            if (!CommonUtils.isBlank(gcBean.getDescript())) {
                Glide.with(context)
                        .load(gcBean.getDescript().replace("40-40", "400-400"))
                        .error(R.drawable.common_details_carousel_placeholder)
                        .into(eholder.mImg);
            } else if (gcBean.getImgList().size()>0){
                Glide.with(context)
                        .load(gcBean.getImgList().get(0).replace("40-40", "400-400"))
                        .error(R.drawable.common_details_carousel_placeholder)
                        .into(eholder.mImg);
            }else {
                Glide.with(context)
                        .load("")
                        .error(R.drawable.common_details_carousel_placeholder)
                        .into(eholder.mImg);
            }
            if (!CommonUtils.isBlank(gcBean.getMallType()) && gcBean.getMallType().equals("JF")) {
                eholder.mType.setText("积分：");
            } else {
                eholder.mType.setText("价格：");
            }
            eholder.mPrice.setText(CommonUtils.getDoubleStr(Double.valueOf(gcBean.getMartPrice())));
            eholder.mTitle.setText(gcBean.getComTitle());
            //将position保存在itemView的Tag中，以便点击时进行获取
            eholder.itemView.setTag(position);
        }
    }


    @Override
    public int getItemCount() {
        return beanList.size() > 0 ? beanList.size() + 1 : 1;
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


    @Override
    public int getItemViewType(int position) {
        if (beanList.size() == 0 || beanList.size() < position + 1) {
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView mTitle;
        TextView mPrice;
        TextView mType;

        public ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.iv_home_mall);
            mTitle = view.findViewById(R.id.tv_title);
            mPrice = view.findViewById(R.id.tv_price);
            mType = view.findViewById(R.id.tv_type);
        }
    }
}
