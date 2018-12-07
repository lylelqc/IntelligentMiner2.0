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

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.HomeRecyclerViewAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * 文 件 名: PointMallTypeActivity
 * 创 建 人: By k
 * 创建日期: 2017/10/11 13:35
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class PointMallTypeActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.banner)
    ImageView mBanner;
    @BindView(R.id.iv_hot_img1)
    ImageView mIvHotImg1;
    @BindView(R.id.tv_hot_name1)
    TextView mTvHotName1;
    @BindView(R.id.tv_hot_price1)
    TextView mTvHotPrice1;
    @BindView(R.id.ll_hot_layout1)
    LinearLayout mLlHotLayout1;
    @BindView(R.id.iv_hot_img2)
    ImageView mIvHotImg2;
    @BindView(R.id.tv_hot_name2)
    TextView mTvHotName2;
    @BindView(R.id.tv_hot_price2)
    TextView mTvHotPrice2;
    @BindView(R.id.ll_hot_layout2)
    LinearLayout mLlHotLayout2;
    @BindView(R.id.iv_hot_img3)
    ImageView mIvHotImg3;
    @BindView(R.id.tv_hot_name3)
    TextView mTvHotName3;
    @BindView(R.id.tv_hot_price3)
    TextView mTvHotPrice3;
    @BindView(R.id.ll_hot_layout3)
    LinearLayout mLlHotLayout3;
    @BindView(R.id.shopcenter_rv)
    RecyclerView mRv;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.iv_toTop)
    ImageView mIvTop;
    private List<GoodsBean> mMall = new ArrayList<>();
    private List<GoodsBean> HotBean = new ArrayList<>();
    private HomeRecyclerViewAdapter mRecyclerViewAdapter;
    private int pageIndex = 1;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.actiivity_point_type;
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText((getIntent().getExtras().getString("TypeName")));
        initBanner((getIntent().getExtras().getString("TypeName")));
        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(2, MyStaggeredGridLayoutManager.VERTICAL);
        mRv.setLayoutManager(mLayoutManager);
        mRv.setItemAnimator(new DefaultItemAnimator());
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        lineVertical.setSize(5);
        lineVertical.setColor(0xFFf2f2f2);
        MyGridItemDecoration lineVertica2 = new MyGridItemDecoration(MyGridItemDecoration.HORIZONTAL);
        lineVertica2.setSize(5);
        lineVertica2.setColor(0xFFf2f2f2);
        lineVertica2.setFootViewHight(100);
        mRv.addItemDecoration(lineVertical);
        mRv.addItemDecoration(lineVertica2);
        mRecyclerViewAdapter = new HomeRecyclerViewAdapter(this, mMall, "积分：");
        mRv.setAdapter(mRecyclerViewAdapter);

    }

    private void initBanner(String typeName) {
        if (typeName.equals("家居百货")) {
            mBanner.setImageResource(R.drawable.img_banner_1);
        } else if (typeName.equals("时尚配饰")) {
            mBanner.setImageResource(R.drawable.img_banner_2);
        } else if (typeName.equals("母婴玩具")) {
            mBanner.setImageResource(R.drawable.img_banner_3);
        } else if (typeName.equals("个护美妆")) {
            mBanner.setImageResource(R.drawable.img_banner_4);
        } else if (typeName.equals("手机数码")) {
            mBanner.setImageResource(R.drawable.img_banner_5);
        } else if (typeName.equals("家用电器")) {
            mBanner.setImageResource(R.drawable.img_banner_6);
        } else if (typeName.equals("户外运动")) {
            mBanner.setImageResource(R.drawable.img_banner_7);
        } else if (typeName.equals("潮品兑换")) {
            mBanner.setImageResource(R.drawable.img_banner_8);
        }
    }

    @Override
    protected void initData() {
        GetPointHot(getIntent().getExtras().getString("TypeCode"));
        GetPointData(getIntent().getExtras().getString("TypeCode"), pageIndex);
    }

    @Override
    protected void setListener() {
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                    GetPointData((getIntent().getExtras().getString("TypeCode")), ++pageIndex);
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
    }

    private void GetPointData(String typeCode, int pageIndex) {
        final Map<String, String> map = new HashMap<>();
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", 20 + "");
        map.put("mallType", "JF");// 积分 JF 微商 V 积分JF
        map.put("orderclasscode", typeCode);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_GET_TYPE_LIST, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.I(NetWorkConstant.COM_GET_TYPE_LIST, json, statusCode, content);
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

    private void GetPointHot(String typeCode) {
        final Map<String, String> map = new HashMap<>();
        map.put("PageIndex", pageIndex + "");
        map.put("PageSize", 3 + "");
        map.put("mallType", "JF");// 积分 JF 微商 V 积分JF
        map.put("orderclasscode", typeCode);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_GET_TYPE_LIST, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.I(NetWorkConstant.COM_GET_TYPE_LIST, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getMsg().equals("成功")) {
                    if (mReturnBean.getMsg().equals("成功")) {
                        List<GoodsBean> mMalls = JSON.parseArray(mReturnBean.getData(), GoodsBean.class);
                        HotBean.addAll(mMalls);
                        if (HotBean.size() == 3) {
                            setHotUI(HotBean);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("错误：" + e);
            }
        });
    }

    private void setHotUI(List<GoodsBean> mMalls) {
        GlideImgManager.glideLoader(this, mMalls.get(0).getImgList().get(0).replace("40-40", "400-400"), R.drawable.common_details_carousel_placeholder, mIvHotImg1);
        GlideImgManager.glideLoader(this, mMalls.get(1).getImgList().get(0).replace("40-40", "400-400"), R.drawable.common_details_carousel_placeholder, mIvHotImg2);
        GlideImgManager.glideLoader(this, mMalls.get(2).getImgList().get(0).replace("40-40", "400-400"), R.drawable.common_details_carousel_placeholder, mIvHotImg3);
        mTvHotName1.setText(mMalls.get(0).getComTitle());
        mTvHotName2.setText(mMalls.get(1).getComTitle());
        mTvHotName3.setText(mMalls.get(2).getComTitle());
        mTvHotPrice1.setText("积分：" + mMalls.get(0).getDefaultPrice());
        mTvHotPrice2.setText("积分：" + mMalls.get(1).getDefaultPrice());
        mTvHotPrice3.setText("积分：" + mMalls.get(2).getDefaultPrice());
    }


    @OnClick({R.id.btn_main_back, R.id.ll_hot_layout1, R.id.ll_hot_layout2, R.id.ll_hot_layout3, R.id.iv_toTop})
    public void onViewClicked(View view) {
        Bundle mBundle;
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.ll_hot_layout1:
                if (HotBean != null && HotBean.size() != 0) {
                    mBundle = new Bundle();
                    mBundle.putParcelable("ComBean", HotBean.get(0));
                    startActivityWithExtras(PointComDetailActivity.class, mBundle);
                }
                break;
            case R.id.ll_hot_layout2:
                if (HotBean != null && HotBean.size() >= 1) {
                    mBundle = new Bundle();
                    mBundle.putParcelable("ComBean", HotBean.get(1));
                    startActivityWithExtras(PointComDetailActivity.class, mBundle);
                }
                break;
            case R.id.ll_hot_layout3:
                if (HotBean != null && HotBean.size() >= 2) {
                    mBundle = new Bundle();
                    mBundle.putParcelable("ComBean", HotBean.get(2));
                    startActivityWithExtras(PointComDetailActivity.class, mBundle);
                }
                break;
            case R.id.iv_toTop:
                mScrollView.smoothScrollTo(0,0);
                break;
        }
    }
}
