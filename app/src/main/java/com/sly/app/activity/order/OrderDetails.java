package com.sly.app.activity.order;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.adapter.order.OrderDetailAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.OrderDetailBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ThreadUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.OneListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;


/**
 * 订单详情
 */
public class OrderDetails extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.tv_d_no)
    TextView mTvDNo;
    @BindView(R.id.tv_d_state)
    TextView mTvDState;
    @BindView(R.id.tv_d_name)
    TextView mTvDName;
    @BindView(R.id.tv_d_tell)
    TextView mTvDTell;
    @BindView(R.id.tv_d_address)
    TextView mTvDAddress;
    @BindView(R.id.rl_data)
    LinearLayout mRlData;
    @BindView(R.id.lv)
    OneListView mLv;
    @BindView(R.id.yunfei)
    TextView mYunfei;
    @BindView(R.id.chengyun)
    TextView mChengyun;
    @BindView(R.id.wuliudanhao)
    TextView mWuliudanhao;
    @BindView(R.id.peisong_day)
    TextView mPeisongDay;
    @BindView(R.id.ll)
    LinearLayout mLl;
    @BindView(R.id.tv_pay)
    TextView mTvPay;
    @BindView(R.id.tv_payType)
    TextView mTvPayType;
    @BindView(R.id.tv_orderDate)
    TextView mTvPayDate;
    @BindView(R.id.btn_right)
    Button mBtnRight;
    @BindView(R.id.btn_left)
    Button mBtnLeft;
    @BindView(R.id.linearLayout2)
    RelativeLayout mLinearLayout2;
    @BindView(R.id.sv)
    ScrollView mSv;
    private String MemberCode, Token;
    private Dialog dialog;
    private String OrderNo;
    private OrderDetailAdapter mAdapter;

    private List<OrderDetailBean.OrderDetailListBean> mBeanList = new ArrayList<>();
    OrderDetailBean mOrderInfoBean;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initData() {
        OrderNo = getIntent().getExtras().getString("OrderNo");
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        GetOrderDetails(OrderNo, MemberCode, Token);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("订单详情");
    }


    /**
     * @param orderNo
     * @param memberCode
     * @param token
     */
    private void GetOrderDetails(final String orderNo, String memberCode, String token) {
        final Map<String, String> map = new HashMap<>();
        map.put("OrderNo", orderNo);
        map.put("D021_OrderNo", orderNo);
        map.put("Token", token);
        map.put("MemberCode", memberCode);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_GET_DETAILS, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.ORDER_GET_DETAILS, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    mOrderInfoBean = JSON.parseObject(mReturnBean.getData(), OrderDetailBean.class);
                    mBeanList.addAll(mOrderInfoBean.getOrderDetailList());
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvDState.setText(mOrderInfoBean.getOrderStatus());
                            mTvDName.setText(mOrderInfoBean.getName());
                            mTvDAddress.setText(mOrderInfoBean.getOrderAdress());
                            mTvDTell.setText(mOrderInfoBean.getPhone());
                            mTvDNo.setText(mOrderInfoBean.getOrderDetailList().get(0).getD021_OrderNo());
                            mYunfei.setText(mOrderInfoBean.getPostage());
                            mTvPayDate.setText(mOrderInfoBean.getOrderDetailList().get(0).getOrderDateTime());
                            mTvPayType.setText(mOrderInfoBean.getPayType());
                            mTvPay.setText("¥ " + mOrderInfoBean.getCountSum());
                            mAdapter = new OrderDetailAdapter(OrderDetails.this, mBeanList, R.layout.item_com_info, mOrderInfoBean.getOrderClassCode());
                            mLv.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            if (mOrderInfoBean.getOrderStatus().equals("待付款")) {
                                mTvPayType.setText("待支付");
                                mBtnLeft.setText("取消订单");
                                mBtnRight.setText("去付款");
                            } else if (mOrderInfoBean.getOrderStatus().equals("已付款")) {
                                mBtnLeft.setVisibility(View.INVISIBLE);
                                mBtnRight.setText("提醒发货");
                            } else if (mOrderInfoBean.getOrderStatus().equals("已发货")) {
                                mBtnLeft.setText("查看物流");
                                mBtnRight.setText("确认收货");
                            } else if (mOrderInfoBean.getOrderStatus().equals("交易成功")) {
                                mBtnLeft.setText("删除订单");
                                mBtnRight.setText("再次购买");
                            } else if (mOrderInfoBean.getOrderStatus().equals("已取消")) {
                                mTvPayType.setText("未支付");
                                mBtnLeft.setText("删除订单");
                                mBtnRight.setText("再次购买");
                            }
                            if (mOrderInfoBean.getOrderDeliveryList().size() != 0) {
                                 mChengyun.setText("承运单位：" + mOrderInfoBean.getOrderDeliveryList().get(0).getD027_CompanyName());
                                  mWuliudanhao.setText("物流单号：" + mOrderInfoBean.getOrderDeliveryList().get(0).getD027_TransportNo());
                            }
                        }
                    });
                }
                return false;
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络异常：" + e);
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivityWithoutExtras(ShopOrder.class);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 确认取消订单的对话框
     *
     * @param type 1 取消 2 删除
     */
    private void showConfirmDialog(final int type) {
        dialog = new Dialog(OrderDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.clear_history_alert);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        title.setVisibility(View.VISIBLE);
        Button cancelButton = (Button) dialog.findViewById(R.id.confirm_action);
        if (type == 1) {
            ((TextView) dialog.findViewById(R.id.textView5)).setText("确定要取消当前订单？");
        } else {
            ((TextView) dialog.findViewById(R.id.textView5)).setText("确定要删除当前订单？");
        }
        dialog.findViewById(R.id.tv_dialog_detail).setVisibility(View.GONE);
        dialog.show();
        dialog.findViewById(R.id.confirm_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    cancelOrder(OrderNo); //取消订单
                } else {
                    delOrder(OrderNo); //删除订单
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });
    }


    @OnClick({R.id.btn_main_back, R.id.btn_right, R.id.btn_left})
    public void onViewClicked(View view) {
        String state;
        switch (view.getId()) {
            case R.id.btn_main_back:
                startActivityWithoutExtras(ShopOrder.class);
                finish();
                break;
            case R.id.btn_left:
                state = mTvDState.getText().toString().trim();
                if (mOrderInfoBean.getOrderStatus().equals("待付款")) {
                    showConfirmDialog(1);//取消订单
                } else if (state.equals("已发货")) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("OrderNo", OrderNo);
                    ToastUtils.showToast("待更新");
//                    startActivityWithExtras();//查看物流
                } else if (mOrderInfoBean.getOrderStatus().equals("已取消") || mOrderInfoBean.getOrderStatus().equals("交易成功")) {
                    showConfirmDialog(2);//删除订单
                }
                break;
            case R.id.btn_right:
                state = mTvDState.getText().toString().trim();
                if (state.equals("待付款")) {
                    if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                        ToastUtils.showToast("请先登录");
                        startActivityWithoutExtras(LoginActivity.class);
                    } else {
                        goOrderPay(
                                OrderNo,
                                mOrderInfoBean.getOrderClassCode(),
                                "",
                                "",
                                mOrderInfoBean.getPoints() + "",
                                mOrderInfoBean.getCountSum() + "",
                                mOrderInfoBean.getPostage() + "",
                                mOrderInfoBean.getOrderDetailList().get(0).getD021_Quantity() + "");
                    }
                } else if (state.equals("待发货")) {
                    //提醒发货
                } else if (state.equals("已发货")) {
                    if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                        ToastUtils.showToast("请先登录");
                        startActivityWithoutExtras(LoginActivity.class);
                    } else {
                        OrderConfirm(OrderNo, MemberCode, Token);
                    }
                } else if (state.equals("交易成功") || state.equals("已取消")) {
                    againBuy(mOrderInfoBean.getOrderDetailList().get(0).getD021_ProductCode(), mOrderInfoBean.getOrderClassCode());
                }
                break;
        }
    }

    /**
     * 取消订单
     *
     * @param orderNo
     */
    private void cancelOrder(String orderNo) {
        initProgressDialog("系统处理中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("OrderNo", orderNo);
        map.put("D020_OrderNo", orderNo);
        map.put("MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_CANCEL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                NetLogCat.I(NetWorkConstant.ORDER_CANCEL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("取消成功");
                    recreate();
                    dismissProgressDialog();
                    mOrderInfoBean.setOrderStatus("已取消");
                    mTvDState.setText("已取消");
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
        dismissProgressDialog();
    }

    /**
     * 删除订单
     *
     * @param orderNo
     */
    private void delOrder(String orderNo) {
        initProgressDialog("系统处理中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("OrderNo", orderNo);
        map.put("D020_OrderNo", orderNo);
        map.put("MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_DELETE, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                NetLogCat.I(NetWorkConstant.ORDER_DELETE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("删除成功");
                    startActivityWithoutExtras(ShopOrder.class);
                    finish();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                dismissProgressDialog();
                Logcat.e("网络错误：" + e);
            }
        });
    }

    /**
     * 确认收货
     *
     * @param orderNo
     */
    private void OrderConfirm(final String orderNo, final String memberCode, final String token) {
        final Dialog lDialog = new Dialog(this, R.style.Translucent_NoTitle);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(R.layout.dialog_orderconfirm);
        lDialog.findViewById(R.id.cancel_action)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lDialog.dismiss();
                    }
                });
        lDialog.findViewById(R.id.confirm_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, String> map = new HashMap<>();
                map.put("OrderNo", orderNo);
                map.put("Token", token);
                map.put("D020_Buyer", memberCode);
                map.put("D020_OrderNo", orderNo);
                map.put("MemberCode", memberCode);
                final String json = CommonUtils.GsonToJson(map);
                HttpClient.postJson(NetWorkConstant.ORDER_AFFIRM_R, json, new HttpResponseHandler() {
                    @Override
                    public void handleSuccessMessage(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        NetLogCat.W(NetWorkConstant.ORDER_AFFIRM_R, json, statusCode, content);
                        ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                        if (mReturnBean.getStatus().equals("1")) {
                            ToastUtils.showToast(mReturnBean.getData());
                            startActivityWithoutExtras(ShopOrder.class);
                            finish();
                        } else {
                            ToastUtils.showToast(mReturnBean.getMsg());
                        }
                        dismissProgressDialog();
                    }
                });
                lDialog.dismiss();
            }
        });
        lDialog.show();
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
//                    } else if (mallType.equals("DK")) {//微商城
//                        intent = new Intent(mContext, MComDetailActivity.class);
//                    } else if (mallType.equals(Constants.MAIL_TYPE_WS)) {//会员
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
     * @param orderNo
     * @param postage
     * @param saler
     * @param points
     * @param sum
     * @param total
     * @param totalQuantity
     */
    private void goOrderPay(String orderNo, String mallType, String postage, String saler, String points, String sum, String total, String totalQuantity) {
        Bundle mBundle = new Bundle();
        mBundle.putString("MallType", mallType);
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
}
