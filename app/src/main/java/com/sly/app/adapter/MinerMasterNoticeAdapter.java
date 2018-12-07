package com.sly.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.sly.NoticeDetailActivity;
import com.sly.app.model.MinerNoticeListBean;
import com.sly.app.model.OperationBean;
import com.sly.app.model.sly.MinerMasterNoticeListBean;
import com.sly.app.utils.CommonUtil2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerMasterNoticeAdapter extends RecyclerView.Adapter {

    private List<MinerNoticeListBean> list;
    private Context mContext;
    private List<MinerMasterNoticeListBean> notes;
    private String type;
    private List<OperationBean> queues;

    public MinerMasterNoticeAdapter(Context Context, String loginType, List<MinerMasterNoticeListBean> mResultList) {
        mContext = Context;
        notes = mResultList;
        type = loginType;
    }

    public MinerMasterNoticeAdapter(Context Context, List<MinerNoticeListBean> mResultList, String loginType) {
        mContext = Context;
        list = mResultList;
        type = loginType;
    }

    public MinerMasterNoticeAdapter(List<OperationBean> mResultList,  Context Context, String loginType) {
        mContext = Context;
        queues = mResultList;
        type = loginType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_notice_b, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String MessageID = "";
        if ("Miner".equals(type)) {
            final MinerNoticeListBean bean = list.get(position);
            String time = bean.getMine34_Time();
            String[] data = time.split(" ");
            viewHolder.tvMsgDate.setText(data[0]);
            viewHolder.tvMsgDate.setText(data[1]);
            viewHolder.tvMsgDate.setText(bean.getMine34_Time());

            viewHolder.tvMsgStatus.setText(bean.getMine34_MessageClass());

            viewHolder.tvMsgNo.setText("消息编号：" + bean.getMine34_MessageID());
            viewHolder.tvMsgBillno.setText("单据号" + bean.getMine34_BillNo());
            viewHolder.tvMsgPeopleNo.setVisibility(View.GONE);

            viewHolder.tvMsgTitle.setText(bean.getMine34_Title());
            viewHolder.tvMsgData.setText(bean.getMine34_Message());

            MessageID = bean.getMine34_MessageID();

            boolean isRead = bean.isMine34_IsRead();
            if (isRead) {
                viewHolder.tvWattingRead.setVisibility(View.GONE);
            }else{
                viewHolder.tvWattingRead.setVisibility(View.VISIBLE);
            }
        } else if("MinerMaster".equals(type)){
            final MinerMasterNoticeListBean bean = notes.get(position);
            String time = bean.getMine48_Time();
            String[] data = time.split(" ");
            viewHolder.tvMsgDate.setText(data[0]);
            viewHolder.tvMsgDate.setText(data[1]);
            viewHolder.tvMsgDate.setText(bean.getMine48_Time());

            viewHolder.tvMsgStatus.setText(bean.getMine48_MessageClass());

            viewHolder.tvMsgNo.setText(bean.getMine48_MessageID());
            viewHolder.tvMsgBillno.setText(bean.getMine48_BillNo());
            viewHolder.tvMsgPeopleNo.setText(bean.getMine48_MinePersonSysCode());

            viewHolder.tvMsgTitle.setText(bean.getMine48_Title());
            viewHolder.tvMsgData.setText(bean.getMine48_Message());

            MessageID = bean.getMine48_MessageID();
            boolean isRead = bean.isMine48_IsRead();
            if (isRead) {
                viewHolder.tvWattingRead.setVisibility(View.GONE);
            }else{
                viewHolder.tvWattingRead.setVisibility(View.VISIBLE);
            }
        }else{
            final OperationBean bean = queues.get(position);
            String time = bean.getF48_Time();
//            String[] data = time.split(" ");
//            viewHolder.tvMsgDate.setText(data[0]);
//            viewHolder.tvMsgDate.setText(data[1]);
            viewHolder.tvMsgDate.setText(bean.getF48_Time());

            viewHolder.tvMsgStatus.setText(bean.getF48_MessageClass());

//            viewHolder.tvMsgNo.setText(bean.getF48_MessageID());
//            viewHolder.tvMsgBillno.setText(bean.getF48_BillNo());
//            viewHolder.tvMsgPeopleNo.setText(bean.getF48_MinePersonSysCode());

            viewHolder.tvMsgTitle.setText(bean.getF48_Title());
            viewHolder.tvMsgData.setText(bean.getF48_Message());

            MessageID = bean.getF48_MessageID();
            boolean isRead = bean.isF48_IsRead();
            if (isRead) {
                viewHolder.tvWattingRead.setVisibility(View.GONE);
            }else{
                viewHolder.tvWattingRead.setVisibility(View.VISIBLE);
            }
        }


        final String finalMessageID = MessageID;
        viewHolder.ll_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("MessageID", finalMessageID + "");
                CommonUtil2.goActivity(mContext, NoticeDetailActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        if ("Miner".equals(type)) {
            return list.size();
        } else if("MinerMaster".equals(type)){
            return notes.size();
        }else{
            return queues.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.tv_msg_title)
        TextView tvMsgTitle;
        @BindView(R.id.tv_watting_read)
        CardView tvWattingRead;
        @BindView(R.id.tv_msg_date)
        TextView tvMsgDate;
        @BindView(R.id.tv_msg_no)
        TextView tvMsgNo;
        @BindView(R.id.tv_msg_billno)
        TextView tvMsgBillno;
        @BindView(R.id.tv_msg_people_no)
        TextView tvMsgPeopleNo;
        @BindView(R.id.iv_msg_img)
        ImageView ivMsgImg;
        @BindView(R.id.tv_msg_status)
        TextView tvMsgStatus;
        @BindView(R.id.tv_msg_data)
        TextView tvMsgData;
        @BindView(R.id.ll_notice)
        LinearLayout ll_notice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }

}
