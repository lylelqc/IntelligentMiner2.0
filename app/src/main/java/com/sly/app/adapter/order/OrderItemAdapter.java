package com.sly.app.adapter.order;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.OrderInfoBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/23 13:28
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class OrderItemAdapter extends CommonAdapter<OrderInfoBean.OrderDetailBean> {
    private String mallType;

    public OrderItemAdapter(Context context, List<OrderInfoBean.OrderDetailBean> mBeanList, int layoutId, String mallType) {
        super(context, mBeanList, layoutId);
        this.mallType = mallType;
    }

    @Override
    public void convert(ViewHolder holder, OrderInfoBean.OrderDetailBean orderDetailBean, int i) {
        RelativeLayout relativeLayout = holder.getView(R.id.rl_item_layout);
        relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.setText(R.id.tv_item_title, orderDetailBean.getD021_ProductName())
                .setText(R.id.tv_item_attrs, "规格：" + orderDetailBean.getD021_OptionName())
                .setText(R.id.tv_item_price, "价格：" + orderDetailBean.getD021_Price() + "")
                .setText(R.id.tv_item_count, "X " + orderDetailBean.getD021_Quantity() + "");
        holder.setImageURL(R.id.iv_item_img,  orderDetailBean.getComPic().replace("40-40","400-400"));
    }

}
