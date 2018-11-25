package com.sly.app.adapter.notice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinerMasterNoticeOldAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String type;
//    private List<MinerNoticeListBean> list;
//    private List<MinerMasterNoticeListBean> notes;
//    private List<OperationBean> queues;

//    public MinerMasterNoticeOldAdapter(Context Context, String loginType, List<MinerMasterNoticeListBean> mResultList) {
//        mContext = Context;
//        notes = mResultList;
//        type = loginType;
//    }
//
//    public MinerMasterNoticeOldAdapter(Context Context, List<MinerNoticeListBean> mResultList, String loginType) {
//        mContext = Context;
//        list = mResultList;
//        type = loginType;
//    }
//
//    public MinerMasterNoticeOldAdapter(List<OperationBean> mResultList, Context Context, String loginType) {
//        mContext = Context;
//        queues = mResultList;
//        type = loginType;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_notice_old, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        MinerMasterNoticeAdapter.ViewHolder viewHolder = (MinerMasterNoticeAdapter.ViewHolder) holder;
        String MessageID = "";
        if ("Miner".equals(type)) {


        } else if("MinerMaster".equals(type)){

        }else{

        }

        final String finalMessageID = MessageID;

    }

    @Override
    public int getItemCount() {
//        if ("Miner".equals(type)) {
//            return list.size();
//        } else if("MinerMaster".equals(type)){
//            return notes.size();
//        }else{
//            return queues.size();
//        }
        return 0;
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
