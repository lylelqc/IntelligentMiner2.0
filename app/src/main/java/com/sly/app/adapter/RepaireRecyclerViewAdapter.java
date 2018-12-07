package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.viewHolder.EmptyViewHolder;
import com.sly.app.model.sly.RepairBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class RepaireRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<RepairBean> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public RepaireRecyclerViewAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public RepaireRecyclerViewAdapter(Context context, List<RepairBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public RepaireRecyclerViewAdapter(Context context, List<RepairBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public RepaireRecyclerViewAdapter(Context context, List<RepairBean> beanList, boolean isAddFootView) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.m_hosting, parent, false);
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
            final RepairBean gcBean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
            String[] time = gcBean.getRepairDate().split(" ");
            eholder.mNumber.setText(gcBean.getErrorName());
            eholder.mTyep.setText(time[0]+"\n"+time[1]);
            eholder.mTime.setText(String.format("%.2f", gcBean.getCost()));


            String ProcessHours = String.format("%.2f",gcBean.getProcessHours());
//            eholder.mP.setText(ProcessHours+"小时");
            eholder.mP.setText(gcBean.getTimeShow());
            eholder.mPalce.setText(gcBean.getProcessResult());

            eholder.mLlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, MachineSeatDetailActivity.class);
//                    context.startActivity(intent);
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
        if (beanList.size() == 0 || beanList.size() < position + 1) {
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView mTyep;
        TextView mNumber;
        TextView mTime;
        TextView mP;
        TextView mPalce;
        LinearLayout mLlItem;

        public ViewHolder(View view) {
            super(view);
            mTyep = (TextView) view.findViewById(R.id.tv_1);
            mNumber = (TextView) view.findViewById(R.id.tv_2);
            mTime = (TextView) view.findViewById(R.id.tv_3);
            mP = (TextView) view.findViewById(R.id.tv_4);
            mPalce = (TextView) view.findViewById(R.id.tv_5);
            mLlItem = (LinearLayout) view.findViewById(R.id.ll_item_rapare);
        }
    }
}
