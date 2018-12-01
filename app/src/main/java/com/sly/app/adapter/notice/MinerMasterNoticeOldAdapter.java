package com.sly.app.adapter.notice;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.sly.mine.notice.NoticeNewDetailsActivity;
import com.sly.app.activity.sly.mine.notice.NoticeOldDetailsActivity;
import com.sly.app.model.notice.YunwNoticeNewDetailsBean;
import com.sly.app.model.notice.YunwNoticeOldListBean;
import com.sly.app.utils.AppUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerMasterNoticeOldAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<YunwNoticeOldListBean> list;

    public MinerMasterNoticeOldAdapter(Context Context, List<YunwNoticeOldListBean> mResultList) {
        mContext = Context;
        list = mResultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_notice_old, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final YunwNoticeOldListBean bean = list.get(position);
        viewHolder.tvNoticeOldTime.setText(bean.getMine85_Time());
        viewHolder.tvNoticeOldContent.setText(bean.getMine85_Content());
        viewHolder.llNoticeOldDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("CommonNoticeInfo", (Serializable) bean);
                AppUtils.goActivity(mContext, NoticeOldDetailsActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_notice_old_content)
        TextView tvNoticeOldContent;
        @BindView(R.id.tv_notice_old_time)
        TextView tvNoticeOldTime;
        @BindView(R.id.ll_notice_old_details)
        LinearLayout llNoticeOldDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
