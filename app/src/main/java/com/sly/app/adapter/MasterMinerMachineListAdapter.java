package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.viewHolder.LoadMoreViewHolder;
import com.sly.app.model.ReplaceListBean;

import java.util.List;


public class MasterMinerMachineListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
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
    private boolean isAddList = true;

    public MasterMinerMachineListAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public MasterMinerMachineListAdapter(Context context, List<ReplaceListBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public MasterMinerMachineListAdapter(Context context, List<ReplaceListBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public MasterMinerMachineListAdapter(Context context, List<ReplaceListBean> beanList, boolean isAddFootView) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isAddFootView = isAddFootView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_view_fragment, parent, false);
            if (isAddList == true) {
                LoadMoreViewHolder holder = new LoadMoreViewHolder(view, LOAD_MORE_VIEW);
                return holder;
            } else {
                LoadMoreViewHolder holder = new LoadMoreViewHolder(view, EMPTY_VIEW);
                return holder;
            }
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_master_machine, parent, false);
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
            eholder.mMineName.setText(gcBean.getBillNo());
            eholder.mMachineModel.setText(gcBean.getBillStatus());
            eholder.mOperationRate.setText(gcBean.getBillTime());
            eholder.mMachineNum.setText(gcBean.getBillTime());
            eholder.mCalForce.setText(gcBean.getBillTime());
            eholder.mBadCount.setText(gcBean.getBillTime());
            eholder.mMachineList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, TaketDetailActivity.class);
//                    intent.putExtra("bean", gcBean);
//                    context.startActivity(intent);
                }
            });

            eholder.mBadList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, TaketDetailActivity.class);
//                    intent.putExtra("bean", gcBean);
//                    context.startActivity(intent);
                }
            });
            //将position保存在itemView的Tag中，以便点击时进行获取
//            eholder.itemView.setTag(position);
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

    public void wtihNoListToSetting(boolean flag) {
        isAddList = flag;
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mMasterMinerIcon;
        TextView mMineName;
        TextView mMachineModel;
        TextView mOperationRate;
        TextView mMachineNum;
        TextView mCalForce;
        TextView mBadCount;
        TextView mMachineList;
        TextView mBadList;

        public ViewHolder(View view) {
            super(view);
            mMasterMinerIcon = (ImageView) view.findViewById(R.id.iv_head_img);
            mMineName = (TextView) view.findViewById(R.id.tv_mine_name_1);
            mMachineModel = (TextView) view.findViewById(R.id.tv_machine_model);
            mOperationRate = (TextView) view.findViewById(R.id.tv_operation_rate);
            mMachineNum = (TextView) view.findViewById(R.id.tv_machine_num);
            mCalForce = (TextView) view.findViewById(R.id.tv_cal_force);
            mBadCount = (TextView) view.findViewById(R.id.tv_bad_count);
            mMachineList = (TextView) view.findViewById(R.id.tv_machine_list);
            mBadList = (TextView) view.findViewById(R.id.tv_bad_list);
        }
    }
}

