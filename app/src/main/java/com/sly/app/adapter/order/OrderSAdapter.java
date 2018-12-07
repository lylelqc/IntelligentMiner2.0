package com.sly.app.adapter.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.order.OrderPayActivity;
import com.sly.app.activity.order.ShopOrder;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.comm.Constants;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.OrderInfoBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * 作者 by K
 * 时间：on 2017/9/22 20:43
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class OrderSAdapter extends CommonAdapter<OrderInfoBean> {
    protected List<OrderInfoBean> mBeanList;//数据源

    public OrderSAdapter(Context context, List<OrderInfoBean> mBeanList, int layoutId) {
        super(context, mBeanList, layoutId);
        this.mBeanList = mBeanList;
    }


    @Override
    public void convert(ViewHolder holder, final OrderInfoBean orderInfoBean, final int i) {
        TextView tvBtnRight = holder.getView(R.id.tv_btn_right);
        TextView tvBtnLeft = holder.getView(R.id.tv_btn_left);
        if (orderInfoBean.getD020_OrderStatusCode().equals("Paid")) {
            holder.setText(R.id.tv_order_State, "已支付");
            tvBtnRight.setText("提醒发货");
            tvBtnLeft.setVisibility(View.GONE);
        } else if (orderInfoBean.getD020_OrderStatusCode().equals("WaitPay")) {
            holder.setText(R.id.tv_order_State, "待支付");
            tvBtnRight.setText("立即付款");
            tvBtnLeft.setText("取消订单");
        } else if (orderInfoBean.getD020_OrderStatusCode().equals("Success")) {
            holder.setText(R.id.tv_order_State, "已完成");
            tvBtnLeft.setText("删除订单");
            tvBtnRight.setText("再次购买");
        } else if (orderInfoBean.getD020_OrderStatusCode().equals("Send")) {
            holder.setText(R.id.tv_order_State, "待收货");
            tvBtnLeft.setVisibility(View.GONE);
            tvBtnRight.setText("确认收货");
        } else if (orderInfoBean.getD020_OrderStatusCode().equals("Cancel")) {
            holder.setText(R.id.tv_order_State, "已取消");
            tvBtnLeft.setText("删除订单");
            tvBtnRight.setText("在次购买");
        }
        holder.setText(R.id.tv_order_source_name, "美好地球村")
                .setText(R.id.tv_order_com_count, orderInfoBean.getTotalQuantity() + "");
        holder.setText(R.id.tv_order_price, orderInfoBean.getD020_Sum() + "");
        OrderItemAdapter itemAdapter = new OrderItemAdapter(mContext, orderInfoBean.getOrderDetail(), R.layout.item_com_info, orderInfoBean.getD020_OrderClassCode());
        ListView itemList = holder.getView(R.id.lv_order_item);
        itemList.setPressed(false);//去掉焦点
        itemList.setEnabled(false);//去掉焦点
        itemList.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        tvBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderInfoBean.getD020_OrderStatusCode().equals("WaitPay")) {
                    ToastUtils.showToast("去支付");
                    goOrderPay(
                            orderInfoBean.getD020_OrderNo(),
                            orderInfoBean.getD020_OrderClassCode(),
                            orderInfoBean.getPageIndex(),
                            orderInfoBean.getD020_Saler(),
                            orderInfoBean.getD020_Pv(),
                            orderInfoBean.getD020_Sum(),
                            orderInfoBean.getTotal(),
                            orderInfoBean.getTotalQuantity());
                } else if (orderInfoBean.getD020_OrderStatusCode().equals("Paid")) {
                    ToastUtils.showToast("提醒发货");
                } else if (orderInfoBean.getD020_OrderStatusCode().equals("Send")) {
                    ToastUtils.showToast("确认收货");
                    affirmReceive(orderInfoBean.getD020_OrderNo());
                    notifyDataSetChanged();
                } else if (orderInfoBean.getD020_OrderStatusCode().equals("Success") || orderInfoBean.getD020_OrderStatusCode().equals("Cancel")) {
                    againBuy(orderInfoBean.getOrderDetail().get(0).getD021_ProductCode(), orderInfoBean.getD003_MallCode());
                }
            }
        });
        tvBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderInfoBean.getD020_OrderStatusCode().equals("WaitPay")) {
                    CancelOrder(orderInfoBean.getD020_OrderNo(), i);//取消订单
                } else if (orderInfoBean.getD020_OrderStatusCode().equals("Success") || orderInfoBean.getD020_OrderStatusCode().equals("Cancel")) {
                    DeleteOrder(orderInfoBean.getD020_OrderNo(), i);//删除订单
                }
            }
        });
    }

    /**
     * @param orderNo
     * @param postage
     * @param saler
     * @param points
     * @param sum
     * @param total
     * @param totalQuantity
     */
    private void goOrderPay(String orderNo, String mallType, int postage, String saler, double points, double sum, String total, int totalQuantity) {
        Bundle mBundle = new Bundle();
        mBundle.putString("MallType", Constants.MAIL_TYPE_WS);
        mBundle.putString("OrderNo", orderNo);
        mBundle.putString("PostAge", postage + "");
        mBundle.putString("Saler", saler);
        mBundle.putString("points", points + "");
        mBundle.putString("total", total + "");
        mBundle.putString("PayPrice", sum + "");
        mBundle.putString("totalQuantity", totalQuantity + "");
        Intent intent = new Intent(mContext, OrderPayActivity.class);
        intent.putExtras(mBundle);
        mContext.startActivity(intent);
    }

    /**
     * 确认收货
     *
     * @param orderNo
     */
    private void affirmReceive(String orderNo) {
        Map<String, String> map = new HashMap<>();
        map.put("OrderNo", orderNo);
        map.put("D020_OrderNo", orderNo);
        map.put("D020_Buyer", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_AFFIRM_R, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.I(NetWorkConstant.ORDER_AFFIRM_R, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast(mReturnBean.getData());
                    ToastUtils.showToast("刷新数据");
                    mContext.startActivity(new Intent(mContext, ShopOrder.class));
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络错误：" + e);
            }
        });
    }

    /**
     * 再次购买
     *
     * @param comID
     * @param mallType
     */
    private void againBuy(String comID, final String mallType) {
        Map<String, String> map = new HashMap<>();
        map.put("ComID", comID + "");
        map.put("MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_DETAIL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.I(NetWorkConstant.COM_DETAIL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    GoodsBean mGoodsBean = JSON.parseObject(mReturnBean.getData(), GoodsBean.class);
                    Intent intent = null;
//                    if (mallType.equals("V")) {//微商
//                        intent = new Intent(mContext, MComDetailActivity.class);
//                    } else if (mallType.equals("JF")) {//积分
//                        intent = new Intent(mContext, PointComDetailActivity.class);
//                    } else if (mallType.equals("LY")) {//零元购
//                        intent = new Intent(mContext, ZPComDetailActivity.class);
//                    } else if (mallType.equals("DK")) {//微商兑换
//                        intent = new Intent(mContext, MComDetailActivity.class);
//                    } else if (mallType.equals(Constants.MAIL_TYPE_WS)) {//精选
//                        intent = new Intent(mContext, UComDetailActivity.class);
//                    }
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("ComBean", mGoodsBean);
                    intent.putExtras(mBundle);
                    mContext.startActivity(intent);
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }
        });
    }

    /**
     * 取消订单
     *
     * @param orderNo
     */
    private void CancelOrder(String orderNo, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("D020_OrderNo", orderNo);
        map.put("MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_CANCEL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.I(NetWorkConstant.ORDER_CANCEL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("取消成功");
                    mBeanList.get(i).setD020_OrderStatusCode("Cancel");
                    notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络错误：" + e);
            }
        });
    }

    /**
     * 取消订单
     *
     * @param orderNo
     */
    private void DeleteOrder(String orderNo, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("D020_OrderNo", orderNo);
        map.put("MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_DELETE, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.I(NetWorkConstant.ORDER_DELETE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("删除成功");
                    mBeanList.remove(i);
                    notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络错误：" + e);
            }
        });
    }
}
