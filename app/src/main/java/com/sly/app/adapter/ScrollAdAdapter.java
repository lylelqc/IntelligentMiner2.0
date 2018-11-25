package com.sly.app.adapter;

import android.content.Context;

import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.sly.SlyMsgBean;

import java.util.List;


public class ScrollAdAdapter extends CommonAdapter<SlyMsgBean> {
    int type;
    Context context;
    public ScrollAdAdapter(Context context, List<SlyMsgBean> mBeanList, int layoutId, int type) {
        super(context, mBeanList, layoutId);
        this.type=type;
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final SlyMsgBean msgBean, int i) {
//        holder.setText(R.id.tv_ad_type,msgBean.getClassName());
//        holder.setText(R.id.tv_scroll,msgBean.getMine66_Title());
//        holder.getView(R.id.ll_home_msg).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("MsgID",msgBean.getMine66_ID());
//                CommonUtil2.goActivity(context,SlyHomeMsgDetailActivity.class,bundle);
//            }
//        });
    }
}
