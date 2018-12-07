package com.sly.app.adapter;

import android.content.Context;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.GoodsBean;
import com.sly.app.utils.CommonUtils;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/11 15:40
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class MicroMallAdapter extends CommonAdapter<GoodsBean> {
    public MicroMallAdapter(Context context, List<GoodsBean> mDatas, int layoutId) {
        super(context, mDatas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, GoodsBean mBean, int i) {
        holder.setImageURL(R.id.iv_m_mall_img, mBean.getImgList().get(0));

        holder.setText(R.id.tv_m_mall_title, mBean.getComTitle()).setText(R.id.tv_m_mall_price, "￥"+ CommonUtils.getDoubleStr(Double.valueOf(mBean.getDefaultPrice())));
    }
}
