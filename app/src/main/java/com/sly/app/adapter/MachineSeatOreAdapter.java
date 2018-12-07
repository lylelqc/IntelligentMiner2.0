package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sly.app.R;
import com.sly.app.adapter.viewHolder.EmptyViewHolder;
import com.sly.app.utils.GlideImgManager;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class MachineSeatOreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<String> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public MachineSeatOreAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public MachineSeatOreAdapter(Context context, List<String> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_list_footer, parent, false);
//            if (isAddFootView == false) {
//                EmptyViewHolder holder = new EmptyViewHolder(view, false);
//                return holder;
//            } else {
//                EmptyViewHolder holder = new EmptyViewHolder(view, true);
//                return holder;
//            }
            EmptyViewHolder holder = new EmptyViewHolder(view, false);
            return holder;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_machine_seta_ore, parent, false);
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
            final String imgUrl = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
                GlideImgManager.glideLoader(context,imgUrl, R.drawable.seat_background,eholder.mImg);
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

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.iv_ore);
        }
    }
}
