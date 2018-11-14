package com.sly.app.activity.sly.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.activity.mine.wallet.WithdrawActivity;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.UserBalanceBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class SlyMineBalanceActivity extends BaseActivity {


    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout llCommLayout;
    @BindView(R.id.tv_ok_balance)
    TextView tvOkBalance;
    @BindView(R.id.rl_business)
    RelativeLayout rlBusiness;
    @BindView(R.id.rl_recharge)
    RelativeLayout rlRecharge;
    @BindView(R.id.rl_withdraw)
    RelativeLayout rlWithdraw;
    @BindView(R.id.rl_recharge_record)
    RelativeLayout rlRechargeRecord;
    @BindView(R.id.ll_miner_view)
    LinearLayout llMinerView;
    private String User, Token, FrSysCode, FMasterCode, Key, LoginType, mRounter;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(llCommLayout).init();
    }

    protected void initView() {
        tvMainTitle.setText("我的余额");
    }

    @Override
    protected void setListener() {

    }

    protected void initData() {
        User = SharedPreferencesUtil.getString(this, "User");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key");
        if ("Miner".equals(LoginType)) {
            llMinerView.setVisibility(View.VISIBLE);
        } else {
            llMinerView.setVisibility(View.GONE);
        }
        getBalance(this, User, Token);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_sly_mine_balance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getBalance(mContext, User, Token);
    }

    private void getBalance(Context mContext, String memberCode, String token) {
        Map map = new HashMap();

        if (LoginType.equals("Miner")) {
            mRounter = "Miner.022";
            map.put("sys", FrSysCode);
        } else {
            mRounter = "MineMaster.008";
            map.put("sys", FMasterCode);
        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", mRounter);
        map.put("User", User);
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        Gson gson = new Gson();
        final String json = gson.toJson(mapJson);
        Logcat.i("提交的参数：" + json + " 地址 :" + NetWorkCons.BASE_URL);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public boolean onSuccess(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        Logcat.e("返回码:" + statusCode + "返回参数:" + content + " headers :" + headers);
                        String backlogJsonStr = content;
                        backlogJsonStr = backlogJsonStr.replace("\\", "");
                        backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                        Logcat.i("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);
                        try {
                            ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                UserBalanceBean userBalanceBean = JSON.parseObject(returnBean.getData(), UserBalanceBean.class);

                                String formatBalance = String.format("%.2f", Double.valueOf(userBalanceBean.getPay01_Balance()));
                                tvOkBalance.setText(formatBalance);
                            }
                        } catch (Exception e) {

                        }
                        return false;
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                    }
                }
        );

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }


    @OnClick({R.id.btn_main_back, R.id.rl_business, R.id.rl_recharge, R.id.rl_withdraw,R.id.rl_recharge_record})
    public void onViewClicked(View view) {
        Bundle mBundle = null;
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_business:
                mBundle = new Bundle();
                mBundle.putString("marks", "YB");
                startActivityWithExtras(SlyRecordActivity.class, mBundle);
                break;
            case R.id.rl_recharge:
                startActivityWithoutExtras(SlyRechargeActivity.class);
                break;
            case R.id.rl_withdraw:
                checkRzStatus();
                break;
            case R.id.rl_recharge_record:
                CommonUtil2.goActivity(mContext,SlyRechargeRecordActivity.class);
                break;
        }
    }

    private void checkRzStatus() {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Sly.031");

        if (LoginType.equals("MinerMaster")) {
            map.put("sysCode", FMasterCode);
        } else {
            map.put("sysCode", FrSysCode);
        }
        map.put("User", User);

        Logcat.i("获取信息");
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("提交的参数：" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.i("API 地址：" + NetWorkCons.BASE_URL + "\n返回码:" + statusCode + "\n返回参数:" + CommonUtils.proStr(content) + "\n提交的内容：" + json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(CommonUtils.proStr(content));
                            if (jsonObject.optString("status").equals("1")) {
                                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                                if (mReturnBean.getStatus().equals("1")) {
                                    if(mReturnBean.getData().equals("false")){
                                        ToastUtils.showToast("请先实名认证再来提现哦");
                                    }else{
                                        CommonUtil2.goActivity(mContext, WithdrawActivity.class);
                                    }
                                }
                                /*if (LoginType.equals("Miner")) {
                                    SlyReturnListBean slyReturnListBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                                    List<ManagerRzStatusBean> managerRzStatusBeans = JSON.parseArray(slyReturnListBean.getData().getRows().toString(), ManagerRzStatusBean.class);
                                    String status = "";
                                    if (managerRzStatusBeans.size() == 0) {
                                        ToastUtils.showToast("请先实名认证再来提现哦");
                                        Intent intent = new Intent();
                                        intent.setClass(SlyMineBalanceActivity.this, IdCardActivity.class);
                                        *//**实名认证**//*
                                        intent.putExtra("tag", 02);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        status = managerRzStatusBeans.get(0).getMine72_AuditStatusCode();
                                        SharedPreferencesUtil.putString(SlyMineBalanceActivity.this, "IdCardName", managerRzStatusBeans.get(0).getMine72_Name());

                                        if ("Waitting".equals(status.trim())) {
                                            ToastUtils.showToast("您的实名认证还在审核中哦，暂时不能提现");
                                        } else if ("Pass".equals(status.trim())) {
                                            CommonUtil2.goActivity(mContext, WithdrawActivity.class);
                                        } else if ("Back".equals(status.trim()) || "Refuse".equals(status.trim())) {
                                            ToastUtils.showToast("您的实名认证没有通过哦，重新申请才能提现");
                                            Intent intent = new Intent();
                                            intent.setClass(SlyMineBalanceActivity.this, IdCardActivity.class);
                                            *//**实名认证**//*
                                            intent.putExtra("tag", 02);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                } else if (LoginType.equals("MinerMaster")) {
                                    SlyReturnListBean slyReturnListBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                                    List<ManagerRzStatusBean> managerRzStatusBeans = JSON.parseArray(slyReturnListBean.getData().getRows().toString(), ManagerRzStatusBean.class);
                                    String statusName = "";
                                    if (managerRzStatusBeans.size() == 0) {
                                        ToastUtils.showToast("请先实名认证再来提现哦");
                                        Intent intent = new Intent();
                                        intent.setClass(SlyMineBalanceActivity.this, IdCardActivity.class);
                                        *//**实名认证**//*
                                        intent.putExtra("tag", 02);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        statusName = managerRzStatusBeans.get(0).getMine60_AuditStatusName();
                                        SharedPreferencesUtil.putString(SlyMineBalanceActivity.this, "IdCardName", managerRzStatusBeans.get(0).getMine58_Name());

                                        if ("等待审核".equals(statusName)) {
                                            ToastUtils.showToast("您的实名认证还在审核中哦，暂时不能提现");
                                        } else if ("审核通过".equals(statusName)) {
                                            startActivityWithoutExtras(WithdrawActivity.class);
                                        } else if ("审核拒绝".equals(statusName) || "发回修改".equals(statusName)) {
                                            ToastUtils.showToast("您的实名认证没有通过哦，重新申请才能提现");
                                            Intent intent = new Intent();
                                            intent.setClass(SlyMineBalanceActivity.this, IdCardActivity.class);
                                            *//**实名认证**//*
                                            intent.putExtra("tag", 02);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }*/
                            } else {
                                ToastUtils.showToast(jsonObject.optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

