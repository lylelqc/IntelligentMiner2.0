package com.sly.app.adapter;

import android.content.Context;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.AssociationBean;

import java.util.List;

public class AssociationAdapter extends CommonAdapter<AssociationBean> {
    private Context mContext;

    public AssociationAdapter(Context context, List<AssociationBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, AssociationBean associationBean, int i) {
        holder.setText(R.id.tv_name,associationBean.getNickName()).setText(R.id.tv_data,associationBean.getEndDate()).setText(R.id.tv_sum,associationBean.getBuySum());
        holder.setImageURLHP(R.id.iv_hp,associationBean.getImageurl());
    }


}
