package com.sly.app.adapter;

import android.content.Context;
import android.graphics.Paint;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.GoodsBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/12 17:26
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ZPMallAdapter extends CommonAdapter<GoodsBean> {

    public ZPMallAdapter(Context context, List<GoodsBean> mDatas, int layoutId) {
        super(context, mDatas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, GoodsBean homeHotComBean, int i) {
        holder.setText(R.id.tv_name, homeHotComBean.getComTitle()).setText(R.id.tv_price, homeHotComBean.getMartPrice() + "", Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setImageURL(R.id.iv_img,homeHotComBean.getImgList().get(0));
    }
}
