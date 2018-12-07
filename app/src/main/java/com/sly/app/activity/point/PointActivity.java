/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, DevKit.vip, china, qd. sd
**                          All Rights Reserved
**
**                           By(K)
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.sly.app.activity.point;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.activity.mine.wallet.UserPointActivity;
import com.sly.app.adapter.HomeRecyclerViewAdapter;
import com.sly.app.adapter.point.PointHotAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.HBannerBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.balance.BalanceInfo;
import com.sly.app.utils.BannerGlideImageLoader;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

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
import vip.devkit.view.common.banner.BannerConfig;
import vip.devkit.view.common.banner.BannerV;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 文 件 名: UserPointActivity
 * 创 建 人: By k
 * 创建日期: 2017/10/10 10:36
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class PointActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView mTvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout mLlRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.icon1)
    ImageView mIcon1;
    @BindView(R.id.rl_point_menu_1)
    RelativeLayout mRlPointMenu1;
    @BindView(R.id.icon2)
    ImageView mIcon2;
    @BindView(R.id.rl_point_menu_2)
    RelativeLayout mRlPointMenu2;
    @BindView(R.id.icon3)
    ImageView mIcon3;
    @BindView(R.id.rl_point_menu_3)
    RelativeLayout mRlPointMenu3;
    @BindView(R.id.icon4)
    ImageView mIcon4;
    @BindView(R.id.rl_point_menu_4)
    RelativeLayout mRlPointMenu4;
    @BindView(R.id.icon5)
    ImageView mIcon5;
    @BindView(R.id.rl_point_menu_5)
    RelativeLayout mRlPointMenu5;
    @BindView(R.id.icon6)
    ImageView mIcon6;
    @BindView(R.id.rl_point_menu_6)
    RelativeLayout mRlPointMenu6;
    @BindView(R.id.icon7)
    ImageView mIcon7;
    @BindView(R.id.rl_point_menu_7)
    RelativeLayout mRlPointMenu7;
    @BindView(R.id.icon8)
    ImageView mIcon8;
    @BindView(R.id.rl_point_menu_8)
    RelativeLayout mRlPointMenu8;
    @BindView(R.id.gv_integral_hot)
    GridView mGvIntegralHot;
    @BindView(R.id.integral_remen_vp)
    BannerV mBannerV2;
    @BindView(R.id.ll_layout_a)
    LinearLayout mLlLayoutA;
    @BindView(R.id.ll_layout_b)
    LinearLayout mLlLayoutB;
    @BindView(R.id.ll_layout_c)
    LinearLayout mLlLayoutC;
    @BindView(R.id.ll_layout_d)
    LinearLayout mLlLayoutD;
    @BindView(R.id.rv_point_list)
    RecyclerView mRvPointList;
    @BindView(R.id.banner_view)
    BannerV mBannerV1;
    @BindView(R.id.title_jifen)
    TextView mTitleJifen;
    @BindView(R.id.title_jifen_jl)
    TextView mTitleJifenJl;
    @BindView(R.id.tv_integral)
    TextView mTvIntegral;
    @BindView(R.id.ll_myintegral)
    RelativeLayout mLlMyintegral;
    @BindView(R.id.tv_convert_record)
    TextView mTvConvertRecord;
    @BindView(R.id.sc_view)
    NestedScrollView mScView;
    @BindView(R.id.iv_toTop)
    ImageView mIvTop;
    private int pageIndex = 1;
    private List<GoodsBean> mMall = new ArrayList<>();
    private List<GoodsBean> mHotBean = new ArrayList<>();
    private HomeRecyclerViewAdapter mRecyclerViewAdapter;
    private PointHotAdapter mHotAdapter;
    private String MemberCode, Token;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("积分商城");
        initBanner2();
        mLlMyintegral.clearFocus();
        mLlMyintegral.setFocusable(false);
        mGvIntegralHot.clearFocus();
        mGvIntegralHot.setFocusable(false);
        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(2, MyStaggeredGridLayoutManager.VERTICAL);
        mRvPointList.setLayoutManager(mLayoutManager);
        mRvPointList.setItemAnimator(new DefaultItemAnimator());
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        lineVertical.setSize(5);
        lineVertical.setColor(0xFFf2f2f2);
        MyGridItemDecoration lineVertica2 = new MyGridItemDecoration(MyGridItemDecoration.HORIZONTAL);
        lineVertica2.setSize(5);
        lineVertica2.setColor(0xFFf2f2f2);
        lineVertica2.setFootViewHight(100);
        mRvPointList.addItemDecoration(lineVertical);
        mRvPointList.addItemDecoration(lineVertica2);
        mRecyclerViewAdapter = new HomeRecyclerViewAdapter(this, mMall, "积分：");
        mRvPointList.setAdapter(mRecyclerViewAdapter);
        mHotAdapter = new PointHotAdapter(this, mHotBean, R.layout.item_point_hot);
        mGvIntegralHot.setAdapter(mHotAdapter);
    }


    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        GetTopBanner();
        GetPointHot();
        GetPointData(pageIndex);
        if (!CommonUtils.isBlank(MemberCode) && !CommonUtils.isBlank(Token)) {
            getBalance(this, MemberCode, Token);
        }else {
            mTvIntegral.setText("请先登录");
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_point;
    }

    @Override
    protected void setListener() {
        mScView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    //    Logcat.i("------------------" + "Scroll DOWN");
                    mIvTop.setVisibility(View.VISIBLE);
                }else {
                    mIvTop.setVisibility(View.GONE);
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Logcat.i("------------------" + "BOTTOM SCROLL");
                    GetPointData(pageIndex++);
                }
            }
        });
        mRecyclerViewAdapter.setOnItemClickListener(new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("ComBean", mMall.get(postion));
                startActivityWithExtras(PointComDetailActivity.class, mBundle);
            }
        });
        mGvIntegralHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("ComBean", mHotBean.get(i));
                startActivityWithExtras(PointComDetailActivity.class, mBundle);
            }
        });
    }

    @OnClick({R.id.btn_main_back, R.id.rl_point_menu_1, R.id.rl_point_menu_2, R.id.rl_point_menu_3, R.id.rl_point_menu_4, R.id.rl_point_menu_5, R.id.rl_point_menu_6, R.id.rl_point_menu_7, R.id.rl_point_menu_8, R.id.ll_layout_a, R.id.ll_layout_b, R.id.ll_layout_c, R.id.ll_layout_d, R.id.ll_myintegral, R.id.iv_toTop})
    public void onViewClicked(View view) {
        Bundle mBundle = new Bundle();
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_point_menu_1:
                mBundle.putString("TypeCode", "18");
                mBundle.putString("TypeName", "家居百货");
                startActivityWithExtras(PointMallTypeActivity.class, mBundle);
                break;
            case R.id.rl_point_menu_2:
                mBundle.putString("TypeCode", "19");
                mBundle.putString("TypeName", "时尚配饰");
                startActivityWithExtras(PointMallTypeActivity.class, mBundle);
                break;
            case R.id.rl_point_menu_3:
                mBundle.putString("TypeCode", "20");
                mBundle.putString("TypeName", "母婴玩具");
                startActivityWithExtras(PointMallTypeActivity.class, mBundle);
                break;
            case R.id.rl_point_menu_4:
                mBundle.putString("TypeCode", "21");
                mBundle.putString("TypeName", "个护美妆");
                startActivityWithExtras(PointMallTypeActivity.class, mBundle);
                break;
            case R.id.rl_point_menu_5:
                mBundle.putString("TypeCode", "22");
                mBundle.putString("TypeName", "手机数码");
                startActivityWithExtras(PointMallTypeActivity.class, mBundle);
                break;
            case R.id.rl_point_menu_6:
                mBundle.putString("TypeCode", "23");
                mBundle.putString("TypeName", "家用电器");
                startActivityWithExtras(PointMallTypeActivity.class, mBundle);
                break;
            case R.id.rl_point_menu_7:
                mBundle.putString("TypeCode", "24");
                mBundle.putString("TypeName", "户外运动");
                startActivityWithExtras(PointMallTypeActivity.class, mBundle);
                break;
            case R.id.rl_point_menu_8:
                mBundle.putString("TypeCode", "25");
                mBundle.putString("TypeName", "潮品兑换");
                startActivityWithExtras(PointMallTypeActivity.class, mBundle);
                break;
            case R.id.ll_layout_a:
                ToastUtils.showToast("暂未开放");
                break;
            case R.id.ll_layout_b:
                ToastUtils.showToast("暂未开放");
                break;
            case R.id.ll_layout_c:
                ToastUtils.showToast("暂未开放");
                break;
            case R.id.ll_layout_d:
                ToastUtils.showToast("暂未开放");
                break;
            case R.id.ll_myintegral:
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    startActivityWithoutExtras(UserPointActivity.class);
                    finish();
                }
                break;
            case R.id.iv_toTop:
                mScView.smoothScrollTo(0,0);
                break;
        }
    }
    public void GetTopBanner() {
        final Map<String, String> map = new HashMap<>();
        map.put("type", "积分");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.HOME_BANNER_TOP, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.I(NetWorkConstant.HOME_BANNER_TOP, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    List<HBannerBean> bannerList = JSON.parseArray(mReturnBean.getData(), HBannerBean.class);
                    List<String> listBean = new ArrayList<String>();
                    for (int i = 0; i < bannerList.size(); i++) {
                        listBean.add(bannerList.get(i).getM070_imageUrl());
                    }
                    setBannerView1(listBean);
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
    private void initBanner2() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.img_banner2_1);
        list.add(R.drawable.img_banner2_2);
        list.add(R.drawable.img_banner2_3);
        setBannerView2(list);
    }

    private void setBannerView2(List<Integer> data) {
        mBannerV2.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mBannerV2.setImageLoader(new BannerGlideImageLoader());
        mBannerV2.setImages(data);
        mBannerV2.isAutoPlay(false);
        mBannerV2.setDelayTime(5000);
        mBannerV2.setIndicatorGravity(BannerConfig.CENTER);
        mBannerV2.start();
    }


    private void setBannerView1(List<String> data) {
        mBannerV1.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBannerV1.setImageLoader(new BannerGlideImageLoader());
        mBannerV1.setImages(data);
        mBannerV1.isAutoPlay(true);
        mBannerV1.setDelayTime(5000);
        mBannerV1.setIndicatorGravity(BannerConfig.CENTER);
        mBannerV1.start();
    }

    private void GetPointHot() {
        final Map<String, String> map = new HashMap<>();
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", 6 + "");
        map.put("mallType", "JF");// 积分 JF 微商 V 积分JF
        Gson gson = new Gson();
        final String json = gson.toJson(map);
        HttpClient.postJson(NetWorkConstant.COM_GET_LIST, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.I(NetWorkConstant.COM_GET_LIST, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getMsg().equals("成功")) {
                    List<GoodsBean> mMalls = JSON.parseArray(mReturnBean.getData(), GoodsBean.class);
                    mHotBean.addAll(mMalls);
                    mHotAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("错误：" + e);
            }
        });
    }

    private void GetPointData(int pageIndex) {
        final Map<String, String> map = new HashMap<>();
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", 20 + "");
        map.put("mallType", "JF");// 积分 JF 微商 V 积分JF
        Gson gson = new Gson();
        final String json = gson.toJson(map);
        HttpClient.postJson(NetWorkConstant.COM_GET_LIST, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.I(NetWorkConstant.COM_GET_LIST, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getMsg().equals("成功")) {
                    List<GoodsBean> mMalls = JSON.parseArray(mReturnBean.getData(), GoodsBean.class);
                    mMall.addAll(mMalls);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("错误：" + e);
            }
        });
    }

    private void getBalance(Context mContext, String memberCode, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PruseCode", memberCode);
        map.put("CurrencyCode", "Gift");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.PURSER_DATE, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                LogCatW(NetWorkConstant.PURSER_DATE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    List<BalanceInfo> balanceInfo = JSON.parseArray(mReturnBean.getData(), BalanceInfo.class);
                    mTvIntegral.setText(balanceInfo.get(0).getBalance() + "");
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }

}
