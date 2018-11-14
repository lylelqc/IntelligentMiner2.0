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
package com.sly.app.activity.qrc;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.ShareRecordAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ShareRecord;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;

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
 * 文 件 名: ShareRecordActivity
 * 创 建 人: By k
 * 创建日期: 2017/11/4 15:54
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class ShareRecordActivity extends BaseActivity {
    @BindView(R.id.btn_title_back)
    LinearLayout mBtnTitleBack;
    @BindView(R.id.btn_title_text)
    TextView mBtnTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout mLlTitleRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.lv_list)
    ListView mLvList;
    @BindView(R.id.tv_null)
    TextView mTvNull;
    private String MemberCode, Token;
    private ShareRecordAdapter mAdapter;
    List<ShareRecord> mList = new ArrayList<>();

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mBtnTitleText.setText("邀请记录");
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        getData(this, MemberCode, Token);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_share_record;
    }

    @Override
    protected void setListener() {
        mBtnTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData(ShareRecordActivity shareRecordActivity, String memberCode, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_INVITE, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                NetLogCat.W(NetWorkConstant.USER_INVITE, json, statusCode, content);
                try {
                    JSONObject jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("msg").equals("成功")) {
                        mList = JSON.parseArray(jsonObject.optString("data"), ShareRecord.class);
                        if (mList.size() > 0) {
                            mAdapter = new ShareRecordAdapter(ShareRecordActivity.this, mList, R.layout.item_share_record);
                            mLvList.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            mLvList.setVisibility(View.VISIBLE);
                            mTvNull.setVisibility(View.GONE);
                        } else {
                            mLvList.setVisibility(View.GONE);
                            mTvNull.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logcat.i("josn 异常："+e);
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }
}
