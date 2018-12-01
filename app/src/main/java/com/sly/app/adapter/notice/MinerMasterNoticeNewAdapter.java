package com.sly.app.adapter.notice;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.sly.mine.notice.NoticeNewDetailsActivity;
import com.sly.app.model.notice.YunwNoticeNewListBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerMasterNoticeNewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String type;
//    private List<MinerNoticeListBean> list;
//    private List<MinerMasterNoticeListBean> notes;
    private List<YunwNoticeNewListBean> queues;

//    public MinerMasterNoticeNewAdapter(Context Context, String loginType, List<MinerMasterNoticeListBean> mResultList) {
//        mContext = Context;
//        notes = mResultList;
//        type = loginType;
//    }
//
//    public MinerMasterNoticeNewAdapter(Context Context, List<MinerNoticeListBean> mResultList, String loginType) {
//        mContext = Context;
//        list = mResultList;
//        type = loginType;
//    }

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
//        if ("Miner".equals(type)) { } else if("MinerMaster".equals(type)){ }else{ }
        ViewHolder viewHolder = (ViewHolder) holder;
        final YunwNoticeNewListBean bean = queues.get(position);
        final String messageID = bean.getF48_MessageID();

        viewHolder.tvNoticeNewName.setText(bean.getF48_Title());
        viewHolder.tvNoticeNewReason.setText(bean.getF48_Message());
        viewHolder.tvNoticeNewTime.setText(bean.getF48_Time());
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
//        if ("Miner".equals(type)) {
//            return list.size();
//        } else if("MinerMaster".equals(type)){
//            return notes.size();
//        }else{
//        }
        return queues.size();
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
