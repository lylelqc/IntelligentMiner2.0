/**
 * ****************************** Copyright (c)*********************************\
 * *
 * *                 (c) Copyright 2017, DevKit.vip, china, qd. sd
 * *                          All Rights Reserved
 * *
 * *                           By(K)
 * *
 * *-----------------------------------版本信息------------------------------------
 * * 版    本: V0.1
 * *
 * *------------------------------------------------------------------------------
 * *******************************End of Head************************************\
 */
package com.sly.app.activity.mine;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.point.PointComDetailActivity;
import com.sly.app.adapter.FavouriteAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.comm.Constants;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.FavouriteBean;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * 文 件 名: FavouriteActivity
 * 创 建 人: By k
 * 创建日期: 2017/11/7 15:04
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class FavouriteActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.lv_list)
    ListView mLvList;
    @BindView(R.id.tv_null)
    TextView mTvNull;
    private String MemberCode, Token;
    public GoodsBean mGoodsBean;
    private FavouriteAdapter mAdapter;
    private  List<FavouriteBean> mBeanList;
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("我的收藏列表");

    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        getData(MemberCode, Token);
    }

    private void getData(String memberCode, String token) {
        initProgressDialog("加载中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.FAVOURITE_GET_ALL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                NetLogCat.I(NetWorkConstant.FAVOURITE_GET_ALL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    if (mReturnBean.getData().equals("未发现任何产品收藏")) {
                        mLvList.setVisibility(View.GONE);
                        mTvNull.setVisibility(View.VISIBLE);
                    } else {
                        mLvList.setVisibility(View.VISIBLE);
                        mTvNull.setVisibility(View.GONE);
                        mBeanList= JSON.parseArray(mReturnBean.getData(), FavouriteBean.class);
                        mAdapter = new FavouriteAdapter(FavouriteActivity.this, mBeanList, R.layout.item_com_info);
                        mLvList.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mLvList.setVisibility(View.GONE);
                    mTvNull.setVisibility(View.VISIBLE);
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.i("网络错误：" + e);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_favourite;
    }

    @Override
    protected void setListener() {
        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBeanList!=null&&mBeanList.size()>0){
                    GetComIDInfo(mBeanList.get(position).getComID(),mBeanList.get(position).getComTypeCode());
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }
    private void GetComIDInfo(String comID, final String mallType) {
        initProgressDialog("加载中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("ComID", comID);
        map.put("MemberCode", "");
        map.put("Token", "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_DETAIL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.I(NetWorkConstant.COM_DETAIL, json, statusCode, content);
                dismissProgressDialog();
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    mGoodsBean = JSON.parseObject(mReturnBean.getData(), GoodsBean.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("ComBean", mGoodsBean);
                    if (mGoodsBean.getMallType().equals("V")){
//                        startActivityWithExtras(MComDetailActivity.class, mBundle);
                    }else if (mGoodsBean.getMallType().equals(Constants.MAIL_TYPE_JF)){
                        startActivityWithExtras(PointComDetailActivity.class, mBundle);
                    }else if (mGoodsBean.getMallType().equals(Constants.MAIL_TYPE_WS)){
//                        startActivityWithExtras(UComDetailActivity.class, mBundle);
                    }
                }
                dismissProgressDialog();
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }
}
