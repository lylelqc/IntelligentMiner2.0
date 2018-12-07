package com.sly.app.adapter.notice;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.notice.AllNoticeBean;
import com.sly.app.model.notice.LogisticsMsgBean;
import com.sly.app.view.SwipeListLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者 by K
 * 时间：on 2017/9/23 17:22
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class LogisticsMsgAdapter extends CommonAdapter<AllNoticeBean> {
   private List<AllNoticeBean> mBeanList;
    public LogisticsMsgAdapter(Context context, List<AllNoticeBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
        this.mBeanList=mBeanList;
    }

    @Override
    public void convert(ViewHolder holder, AllNoticeBean logisticsNotice, final int i) {
        final SwipeListLayout swipeListLayout = holder.getView(R.id.sll_main);
        TextView tvDelete = holder.getView(R.id.tv_delete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeListLayout.setStatus(SwipeListLayout.Status.Close, true);
                notifyDataSetChanged();}
        });
    }

    private void delNotice(LogisticsMsgBean logisticsMsgBean) {
    }


    public Set<SwipeListLayout> sets = new HashSet();

    public Set<SwipeListLayout> SwipeListLayoutCount() {
        return sets;
    }
}
