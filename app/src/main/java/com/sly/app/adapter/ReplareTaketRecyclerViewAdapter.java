package com.sly.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.mine.TaketDetailActivity;
import com.sly.app.adapter.viewHolder.EmptyViewHolder;
import com.sly.app.adapter.viewHolder.LoadMoreViewHolder;
import com.sly.app.model.ReplaceListBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ReplareTaketRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private static final int LOAD_MORE_VIEW = 6;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<ReplaceListBean> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public ReplareTaketRecyclerViewAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public ReplareTaketRecyclerViewAdapter(Context context, List<ReplaceListBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public ReplareTaketRecyclerViewAdapter(Context context, List<ReplaceListBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public ReplareTaketRecyclerViewAdapter(Context context, List<ReplaceListBean> beanList, boolean isAddFootView) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isAddFootView = isAddFootView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_view_fragment, parent, false);
            if (beanList.size() > 14) {
                LoadMoreViewHolder holder = new LoadMoreViewHolder(view, LOAD_MORE_VIEW);
                view.setVisibility(View.VISIBLE);
                return holder;
            } else if(beanList.size() == 0){
                LoadMoreViewHolder holder = new LoadMoreViewHolder(view, 0);
                view.setVisibility(View.GONE);
                return holder;
            } else {
                LoadMoreViewHolder holder = new LoadMoreViewHolder(view, EMPTY_VIEW);
                view.setVisibility(View.VISIBLE);
                return holder;
            }
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_replare_taket, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(this);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        } else {
            final ReplaceListBean gcBean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
            eholder.mRepairID.setText(gcBean.getBillNo());
            eholder.mStatus.setText(gcBean.getBillStatus());
            if("未处理".equals(gcBean.getBillStatus())){
                eholder.mStatus.setTextColor(context.getResources().getColor(R.color.sly_text_blue2));
            }else{
                eholder.mStatus.setTextColor(Color.parseColor("#828a8c"));
            }
            eholder.mTime.setText(gcBean.getBillTime());
            eholder.mDesciption.setText(gcBean.getReason());
            eholder.mRlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, MachineSeatDetailActivity.class);
//                    context.startActivity(intent);
                        Intent intent = new Intent(context, TaketDetailActivity.class);
                        intent.putExtra("bean",gcBean);
                        context.startActivity(intent);
                }
            });
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
        if (beanList.size() < position + 1) {
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
        TextView mRepairID;
        TextView mStatus;
        TextView mTime;
        TextView mDesciption;
        RelativeLayout mRlItem;

        public ViewHolder(View view) {
            super(view);
            mRepairID = view.findViewById(R.id.tv_repair_id);
            mStatus = view.findViewById(R.id.tv_repair_status);
            mTime = view.findViewById(R.id.tv_begin_time);
            mDesciption = view.findViewById(R.id.tv_question_desciption);
            mRlItem = view.findViewById(R.id.rl_details);
        }
    }
}
