package com.sly.app.adapter.notice;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.sly.mine.notice.NoticeNewDetailsActivity;
import com.sly.app.model.MinerNoticeListBean;
import com.sly.app.model.notice.MasterNoticeNewListBean;
import com.sly.app.model.notice.MinerNoticeNewListBean;
import com.sly.app.model.notice.YunwNoticeNewListBean;
import com.sly.app.model.sly.MinerMasterNoticeListBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerMasterNoticeNewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String type;
    private List<MinerNoticeNewListBean> list;
    private List<MasterNoticeNewListBean> notes;
    private List<YunwNoticeNewListBean> queues;

    public MinerMasterNoticeNewAdapter(Context Context, List<MinerNoticeNewListBean> mResultList, String loginType) {
        mContext = Context;
        list = mResultList;
        type = loginType;
    }

    public MinerMasterNoticeNewAdapter(Context Context, String loginType, List<MasterNoticeNewListBean> mResultList) {
        mContext = Context;
        notes = mResultList;
        type = loginType;
    }

    public MinerMasterNoticeNewAdapter(List<YunwNoticeNewListBean> mResultList, Context Context, String loginType) {
        mContext = Context;
        queues = mResultList;
        type = loginType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_notice_new, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String MessageID = "";
        if ("Miner".equals(type)) {
            final MinerNoticeNewListBean bean = list.get(position);
            MessageID = bean.getMine34_MessageID();
            viewHolder.tvNoticeNewName.setText(bean.getMine34_Title());
            viewHolder.tvNoticeNewReason.setText(bean.getMine34_Message());
            viewHolder.tvNoticeNewTime.setText(bean.getMine34_Time());
        }
        else if("MinerMaster".equals(type)){
            final MasterNoticeNewListBean bean = notes.get(position);
            MessageID = bean.getMine48_MessageID();
            viewHolder.tvNoticeNewName.setText(bean.getMine48_Title());
            viewHolder.tvNoticeNewReason.setText(bean.getMine48_Message());
            viewHolder.tvNoticeNewTime.setText(bean.getMine48_Time());
        }
        else{
            final YunwNoticeNewListBean bean = queues.get(position);
            MessageID = bean.getF48_MessageID();
            viewHolder.tvNoticeNewName.setText(bean.getF48_Title());
            viewHolder.tvNoticeNewReason.setText(bean.getF48_Message());
            viewHolder.tvNoticeNewTime.setText(bean.getF48_Time());
        }

        final String messageID = MessageID;
        viewHolder.tvNoticeNewCheckDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("MessageID", messageID);
                AppUtils.goActivity(mContext, NoticeNewDetailsActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        if ("Miner".equals(type)) {
            return list.size();
        }
        else if("MinerMaster".equals(type)){
            return notes.size();
        }
        else{
            return queues.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_notice_new_name)
        TextView tvNoticeNewName;
        @BindView(R.id.tv_notice_new_reason)
        TextView tvNoticeNewReason;
        @BindView(R.id.tv_notice_new_time)
        TextView tvNoticeNewTime;
        @BindView(R.id.tv_notice_new_check_details)
        TextView tvNoticeNewCheckDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
