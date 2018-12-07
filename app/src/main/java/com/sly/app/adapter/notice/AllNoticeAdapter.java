package com.sly.app.adapter.notice;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.notice.AllNoticeBean;
import com.sly.app.view.SwipeListLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者 by K
 * 时间：on 2017/9/23 17:23
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class AllNoticeAdapter extends CommonAdapter<AllNoticeBean> {
    private List<AllNoticeBean> mBeanList;

    private OnItemDeleteListener mOnItemDeleteListener = null;
    public void setItemDeleteListener(OnItemDeleteListener itemDeleteListener) {
       this.mOnItemDeleteListener = itemDeleteListener;
    }
    public interface OnItemDeleteListener {
        void onItemDeleteClick(int position);
    }
    public AllNoticeAdapter(Context context, List<AllNoticeBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
        this.mBeanList = mBeanList;
    }

    @Override
    public void convert(ViewHolder holder, AllNoticeBean allNoticeBean, final int i) {
        switch (allNoticeBean.getMemberMessageClassCode()) {
            case "01":
                holder.setText(R.id.tv_msg_title, "提现审核通知");
                break;
            case "02":
                holder.setText(R.id.tv_msg_title, "个人订单物流信息");
                break;
            case "03":
                holder.setText(R.id.tv_msg_title, "加盟订单审核");
                break;
            case "04":
                holder.setText(R.id.tv_msg_title, "积分赠送");
                break;
        }
        if (allNoticeBean.getPicurl().contains("http://")||allNoticeBean.getPicurl().contains("https://")){
            holder.setImageURL(R.id.iv_msg_img,allNoticeBean.getPicurl());
        }else {
            holder.setImageURL(R.id.iv_msg_img, allNoticeBean.getPicurl());
        }
        String[] data = allNoticeBean.getTime().split(" ");
        holder.setText(R.id.tv_msg_data, allNoticeBean.getTitle()).setText(R.id.tv_msg_date, allNoticeBean.getTime());
        final SwipeListLayout swipeListLayout = holder.getView(R.id.sll_main);
        TextView tvDelete = holder.getView(R.id.tv_delete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeListLayout.setStatus(SwipeListLayout.Status.Close, true);
                mOnItemDeleteListener.onItemDeleteClick(i);
              //  mBeanList.remove(i); 使用接口回调
                notifyDataSetChanged();
            }
        });
    }

    public Set<SwipeListLayout> sets = new HashSet();

    public Set<SwipeListLayout> SwipeListLayoutCount() {
        return sets;
    }


}
