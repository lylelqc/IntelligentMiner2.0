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
package com.sly.app.activity.mine.wallet;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.point.PointsRecordAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.PointRecordBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.UserInfoBean;
import com.sly.app.model.balance.BalanceInfo;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.Request;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 文 件 名: UserPointActivity
 * 创 建 人: By k
 * 创建日期: 2017/10/11 15:55
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class UserPointActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.iv_img)
    ImageView mIvImg;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_user_points)
    TextView mTvUserPoints;
    @BindView(R.id.iv_point_rule)
    ImageView mIvPointRule;
    @BindView(R.id.lv_list)
    ListView mLvList;
    private Realm realm = Realm.getDefaultInstance();
    private String MemberCode, Token;
    private PointsRecordAdapter mAdapter;
    private List<PointRecordBean> mList = new ArrayList<>();
    private int pageIndex = 1;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("我的积分");
        UserInfoBean mUser = realm.where(UserInfoBean.class).equalTo("MemberCode", MemberCode).findFirst();
            mTvUserName.setText(mUser.getName());
        GlideImgManager.glideLoader(this, mUser.getImageURl().replace("9897", "9896"), R.drawable.h_portrait, R.drawable.h_portrait, mIvImg);
        mAdapter = new PointsRecordAdapter(this, mList, R.layout.item_point_record);
        mLvList.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        getBalance(this, MemberCode, Token);
        getPointsRecord(this, pageIndex);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_user_point;
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btn_main_back, R.id.iv_point_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.iv_point_rule:
                startActivityWithoutExtras(PointRuleActivity.class);
                break;
        }
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
                    mTvUserPoints.setText(balanceInfo.get(0).getBalance() + "");
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }

    private void getPointsRecord(Context mContext, int pageIndex) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("PruseCode", MemberCode);
        map.put("CurrencyCode", "Gift");
        map.put("PageSize", "30");
        map.put("PageNo", pageIndex + "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.getBusiness, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                LogCatW(NetWorkConstant.getBusiness, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    List<PointRecordBean> list = JSON.parseArray(mReturnBean.getData(), PointRecordBean.class);
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }

}
