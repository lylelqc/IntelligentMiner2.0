package com.sly.app.fragment.orderState;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.order.OrderDetails;
import com.sly.app.adapter.order.OrderSAdapter;
import com.sly.app.base.BaseFragment;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.OrderInfoBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.LoadMoreListView;
import com.sly.app.view.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * 作者 by K
 * 时间：on 2017/8/29 14:36
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：待发货订单
 * 最后修改：
 */
public class StayDelivery extends BaseFragment {
    OrderSAdapter mAdapter;
    String MemberCode = null; //会员编号
    String Token = null;//会员TOKEN
    @BindView(R.id.tv_null)
    TextView mTvNull;
    @BindView(R.id.lv_odetails)
    LoadMoreListView mLvOdetails;
    private View FooterView;
    private List<OrderInfoBean> mBeanList = new ArrayList<>();
    private int PageIndex = 1;
    private int NOTIF = 1;
    private boolean isLoad=true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NOTIF) {
                if (mBeanList.size() > 0) {
                    if (mLvOdetails != null) {
                        mLvOdetails.setVisibility(View.VISIBLE);
                        mTvNull.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    @Nullable
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_list_null;
    }

    @Override
    protected void initData() {
        super.initData();
        getOrder(PageIndex);
    }

    @Override
    protected void initView(View view) {
        FooterView = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_list_footer, null, false);
        mAdapter = new OrderSAdapter(mActivity, mBeanList, R.layout.item_order_info);
        mLvOdetails.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mLvOdetails.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        OrderInfoBean dataBean = mBeanList.get(position);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("OrderNo", dataBean.getD020_OrderNo());
                        startActivityWithExtras(OrderDetails.class, mBundle);mActivity.finish();
                    }
                }
        );
        mLvOdetails.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mLvOdetails.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        Logcat.i("##### 滚动到顶部 #####");
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mLvOdetails.getChildAt(mLvOdetails.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mLvOdetails.getHeight()) {
                        Logcat.i("##### 滚动到底部 #####");
                        //加载更多
                        if (isLoad == true) {
                            getOrder(PageIndex++);
                        }
                    }
                }
            }
        });
    }


    public void getOrder(int pageIndex) {
        MemberCode = SharedPreferencesUtil.getString(getActivity(), "MemberCode");
        Token = SharedPreferencesUtil.getString(getActivity(), "Token");
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("PageSize", "30");
        map.put("PageIndex", pageIndex + "");
        map.put("D020_Buyer", MemberCode);
        map.put("D020_OrderClassCode", "");
        map.put("D020_OrderStatusCode", "Paid");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ORDER_GET_ALL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        NetLogCat.W(NetWorkConstant.ORDER_GET_ALL, json, statusCode, CommonUtils.proStr(content));
                        try {
                            JSONObject jsonObject = new JSONObject(CommonUtils.proStr(content));
                            if (jsonObject.optString("msg").equals("成功")) {
                                List<OrderInfoBean> list = JSON.parseArray(jsonObject.optString("data"), OrderInfoBean.class);
                                mBeanList.addAll(list);
                                Message msg = new Message();
                                msg.what = NOTIF;
                                msg.obj = mAdapter;
                                handler.sendMessage(msg);
                                if (PageIndex == 1 && list.size() > 0) {
                                    mTvNull.setVisibility(View.GONE);
                                    mLvOdetails.setVisibility(View.INVISIBLE);
                                } else {
                                    mTvNull.setText("你暂时没有任何订单\n快去下单吧");
                                    mTvNull.setVisibility(View.VISIBLE);
                                    mLvOdetails.setVisibility(View.GONE);

                                }
                                if (list.size() < 30) {
                                    isLoad = false;
                                    ViewUtils.addFooterViewForList(mLvOdetails, FooterView);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logcat.i("josn 异常：" + e);
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        mTvNull.setText("你暂时没有任何订单\n快去下单吧");
                        mTvNull.setVisibility(View.VISIBLE);
                        mLvOdetails.setVisibility(View.INVISIBLE);
                        System.out.println("全部订单列表失败");
                    }
                }
        );
    }
}
