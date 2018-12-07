package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.viewHolder.EmptyViewHolder;
import com.sly.app.model.sly.EquipmentBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class EquipmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<EquipmentBean> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public EquipmentListAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public EquipmentListAdapter(Context context, List<EquipmentBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public EquipmentListAdapter(Context context, List<EquipmentBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public EquipmentListAdapter(Context context, List<EquipmentBean> beanList, boolean isAddFootView) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_hosting, parent, false);
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
            final EquipmentBean bean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;



            eholder.tv_model_name.setText(bean.getModelName());
            eholder.tv_machine_count.setText(bean.getMachineCount()+"");
//            eholder.mPalce.setText("电价：0.01-0.04元/千瓦时");

            String runHours = String.format("%.2f", bean.getRunHours());
//            int runHours = ((Number) (Float.parseFloat(bean.getRunHours()))).intValue();
            eholder.tv_run_hours.setText(bean.getTimeShow()+"");
            eholder.tv_run_rate.setText(String.format("%.2f", bean.getRunRate()*100)+"%");
            eholder.tv_mine_name.setText(bean.getMineName());
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
        TextView tv_model_name;
        TextView tv_machine_count;
        TextView tv_run_hours;
        TextView tv_run_rate;
        TextView tv_mine_name;
        LinearLayout mLlItem;

        public ViewHolder(View view) {
            super(view);
            tv_model_name = (TextView) view.findViewById(R.id.tv_model_name);
            tv_machine_count = (TextView) view.findViewById(R.id.tv_machine_count);
            tv_run_hours = (TextView) view.findViewById(R.id.tv_run_hours);
            tv_run_rate = (TextView) view.findViewById(R.id.tv_run_rate);
            tv_mine_name = (TextView) view.findViewById(R.id.tv_mine_name);
            mLlItem = (LinearLayout) view.findViewById(R.id.ll_item_rapare);
        }
    }
}
