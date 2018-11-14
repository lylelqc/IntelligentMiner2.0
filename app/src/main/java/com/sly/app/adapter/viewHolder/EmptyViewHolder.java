package com.sly.app.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:55
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public EmptyViewHolder(View itemView) {
        super(itemView);
//        LinearLayout mEmpty = (LinearLayout) itemView.findViewById(R.id.ll_nomore);
//        TextView no_sjop = (TextView) itemView.findViewById(R.id.no_meaage);
    }

    public EmptyViewHolder(View itemView, boolean isVisibility) {
        super(itemView);
        ImageView mEmpty = itemView.findViewById(R.id.img_foot);
        if (isVisibility == false) {
            mEmpty.setVisibility(View.GONE);
        } else {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }
}
