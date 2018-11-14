package com.sly.app.activity.mine.wallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.SetPayPwdActivity;
import com.sly.app.comm.EventBusTags;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.model.sly.UserBalanceBean;
import com.sly.app.model.sly.WithdrawalsInfoBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;
import com.sly.app.view.pwd.SercurityDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;


/**
 * 提现
 */
public class WithdrawActivity extends BaseActivity implements ICommonViewUi{
    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.ll_nobind)
    LinearLayout llNobind;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_bankname)
    TextView tvBankname;
    @BindView(R.id.tv_bankcard)
    TextView tvBankcard;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.ll_bind)
    LinearLayout llBind;
    @BindView(R.id.bt_binding)
    Button btBinding;
    @BindView(R.id.iv_bank_icon)
    ImageView ivBankIcon;
    @BindView(R.id.tv_updata_bank)
    TextView tvUpdataBankInfo;

    private boolean isPassword = false;
    private String money = null;
    private String getBalances = "0";
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    private String User, Token, LoginType, SysNumber, Key;
    private String AccountName;
    private String WithdrawalsInfo;
    private String btnSate = "绑定";
    private String mRounter;
    private String mBankID;
    private String mCardId;

    @OnClick({R.id.btn_main_back, R.id.tv_notice, R.id.ll_bind, R.id.bt_binding, R.id.tv_updata_bank})
    public void onViewClicked(View view) {
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_notice:
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.notice);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;
            case R.id.tv_updata_bank:
                ToastUtils.showToast("换绑银行卡");
                bundle = new Bundle();
                bundle.putString("Tag", "updata");
                bundle.putString("CardId", mCardId);
                CommonUtil2.goActivity(mContext,WithdrawAccountActivity.class, bundle);
                finish();
                break;
            case R.id.ll_bind:
                break;
            case R.id.bt_binding:
                String btnType = btBinding.getText().toString();
                if (btnType.equals("绑定提现账户")) {
                    bundle = new Bundle();
                    bundle.putString("Tag", "set");
                    CommonUtil2.goActivity(mContext,WithdrawAccountActivity.class, bundle);
                    finish();
                } else if (btnType.equals("提现审核中")) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("info", WithdrawalsInfo);
                    mBundle.putString("BankName", tvBankname.getText().toString());
                    mBundle.putString("AccountName", AccountName);
                    mBundle.putString("type", "2");
                    CommonUtil2.goActivity(mContext,WithdrawDetailActivity.class, mBundle);
                } else if (btnType.equals("提现")) {
                    Logcat.i("------------------");
                    money = etMoney.getText().toString().trim();
                    getBalances = tvBalance.getText().toString().trim() + "";
                    if (getBalances.contains(".")) {
                        getBalances = getBalances.substring(0, getBalances.indexOf("."));
                    }
                    final Intent intent = new Intent();
                    if (!isPassword) {
                        ToastUtils.showToast("您还未设置支付密码");
                        try {
                            Thread.sleep(500);
                            intent.setClass(WithdrawActivity.this, SetPayPwdActivity.class);
                            intent.putExtra("pay", "pay");
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (CommonUtil2.isEmpty(money)) {
                            ToastUtils.showToast("请输入提现金额");
                            return;
                        } else {
                            if (Integer.valueOf(money) > Integer.valueOf(getBalances)) {
                                ToastUtils.showToast("输入的金额大于账户余额");
                                return;
                            }
//                            else if (Integer.valueOf(money) < 100) {
//                                ToastUtils.showToast("提现金额不能少于100元");
//                                return;
//                            }
                            final String Price = etMoney.getText().toString().trim();
                            SercurityDialog mDialog = new SercurityDialog(this, Price);
                            mDialog.setOnInputCompleteListener(new SercurityDialog.InputCompleteListener() {
                                @Override
                                public void inputComplete(String passWord) {
                                    juagePayPassword(passWord, Price);
                                }
                            });
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSetPassword();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (EventBusTags.UPDATE_BANK_LIST.equals(postResult.getTag())) {
            isSetBankInfo(User, Token, LoginType, SysNumber, Key);//是否绑定银行卡
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText("绑定提现账户");
        User = SharedPreferencesUtil.getString(this, "User","None");
        Token = SharedPreferencesUtil.getString(this, "Token","None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType","None");
        SysNumber = SharedPreferencesUtil.getString(this, LoginType.equals("Miner") ? "FrSysCode" : "FMasterCode","None");
        initView();
    }

    private void initView() {
        isSetBankInfo(User, Token, LoginType, SysNumber, Key);//是否绑定银行卡
        isHaveAudit(User, Token);//当前是否正有提现正在审核中
        getBalance(this, User, Token,LoginType,SysNumber);
        isSetPassword();
    }

    private void getBalance(Context mContext, String user, String token, String loginType, String sysNumber) {
        Map map = new HashMap();
        if (LoginType.equals("Miner")) {
            mRounter = "Miner.022";
        } else {
            mRounter = "MineMaster.008";
        }
        map.put("Token", token);
        map.put("LoginType", loginType);
        map.put("Rounter", mRounter);
        map.put("User", user);
        map.put("sys", sysNumber);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logcat.e("返回码:" + statusCode);
                super.onSuccess(statusCode, content);
                LogCatW("提现", json, statusCode, content);
                try {
                    ReturnBean returnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                    if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                        UserBalanceBean userBalanceBean = JSON.parseObject(returnBean.getData(), UserBalanceBean.class);
//                        tvBalance.setText(String.valueOf(userBalanceBean.getPay01_Balance()));
                        tvBalance.setText(String.format("%.2f", Double.valueOf(userBalanceBean.getPay01_Balance())));
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }

    private void isSetBankInfo(String user, String token, String loginType, String sysNumber, String key) {
        Map map = new HashMap();

        map.put("Token", token);
        map.put("LoginType", loginType);
        map.put("Rounter", "Member.007");
        map.put("User", user);
        map.put("sys", sysNumber);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);

                if (mReturnBean.getStatus().equals("1")&& mReturnBean.getMsg().equals("成功")) {
                    if (mReturnBean.getData().getRows().size() == 0) {
                        llNobind.setVisibility(View.VISIBLE);
                        llBind.setVisibility(View.GONE);
                        btBinding.setText("绑定提现账户");
                        tvMainTitle.setText("绑定提现账户");
                    } else {
                        String data = mReturnBean.getData().getRows().get(0).toString();
                        llNobind.setVisibility(View.GONE);
                        llBind.setVisibility(View.VISIBLE);
                        btBinding.setText("提现");
                        tvMainTitle.setText("提现");
                        WithdrawalsInfoBean withdrawDataBean = JSON.parseObject(data, WithdrawalsInfoBean.class);
                        tvBankname.setText(withdrawDataBean.getPay25_BankName());
                        mCardId = withdrawDataBean.getPay26_ID();
                        mBankID = withdrawDataBean.getPay26_AccountNo();
                        String accountNO = mBankID.substring(mBankID.length() - 4, mBankID.length());
                        tvBankcard.setText("**** **** **** " + accountNO);
                        AccountName = withdrawDataBean.getPay26_AccountName();
                        GlideImgManager.glideLoader(mContext, NetWorkCons.IMG_URL2 + withdrawDataBean.getPay25_Logo(), R.drawable.nong, ivBankIcon);
                    }
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
            }
        });

    }

    private void isHaveAudit(final String memberCode, final String token) {
        Map map = new HashMap();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PruseCode", memberCode);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.IsApply, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW(NetWorkConstant.IsApply, json, statusCode, content);
                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(CommonUtils.proStr(content));
//                    if (jsonObject.optString("status").equals("1")) {
//                        Rows mReturnBean = JSON.parseObject(CommonUtils.proStr(content), Rows.class);
//                        WithdrawalsInfo = CommonUtils.GsonToJson(mReturnBean.getData().getRows().get(0));
//                        ThreadUtils.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                btBinding.setText("提现审核中");
//                                btBinding.setBackgroundColor(Color.parseColor("#979B9A"));
//                            }
//                        });
//                    } else {
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    private void juagePayPassword(String password, final String price) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        if ("Miner".equals(LoginType)){
            map.put("Rounter", "Miner.021");
        }else{
            map.put("Rounter", "MineMaster.007");
        }
        map.put("User", User);
        map.put("sys", SysNumber);
        map.put("Sum", price);
        map.put("bankID", mBankID);
        map.put("password", password);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("status").equals("1")) {
                        ToastUtils.showToast("您的提现申请已发送，请耐心等候");
                        finish();
                    } else {
                        ToastUtils.showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void isSetPassword() {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Member.008");
        map.put("User", User);
        map.put("sys", SysNumber);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        Logcat.i("判断会员是否设置安全密码" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.W(NetWorkCons.BASE_URL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")&& mReturnBean.getMsg().equals("成功")) {
                    isPassword = mReturnBean.getData().equals("True");
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }
    @Override
    public void toRequest(int eventTag) {

    }

    @Override
    public void getRequestData(int eventTag, String result) {

    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {

    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {

    }

    @Override
    public void isRequesting(int eventTag, boolean status) {

    }

    static class Rows {

        /**
         * status : 1
         * msg : 成功
         * data : {"Rows":[{"P004_ID":6,"P004_PurseCode":"V8955979_YB","P004_ToCashTypeCode":"YB","P004_Time":"2017-12-14 17:52:12","P004_Sum":90.5,"P004_Poundage":2,"P004_Taxation":7.5,"P004_EnterSum":100,"P004_BankCode":"95588","P004_BankAccountName":"测试","P004_BankBranches":"中国工商银行","P004_BankCardNo":"1234567899999999","P004_PapperNo":"522423199716274310","P004_Mobile":"15772747952","P004_Remark":"V8955979申请提现","P004_AuditStatusCode":"Waitting","P004_Auditor":null,"P004_AuditTime":null,"P004_AuditInfo":null,"P004_EventCode":"C0F65E84-5427-46B9-B59E-98DEE9F1E46F","P004_SystemAuditStatusCode":"Waitting","P004_SystemAuditor":null,"P004_SystemAuditTime":null,"P004_SystemAuditInfo":null},{"P004_ID":7,"P004_PurseCode":"V8955979_YB","P004_ToCashTypeCode":"YB","P004_Time":"2017-12-14 17:56:46","P004_Sum":90.5,"P004_Poundage":2,"P004_Taxation":7.5,"P004_EnterSum":100,"P004_BankCode":"95588","P004_BankAccountName":"测试","P004_BankBranches":"中国工商银行","P004_BankCardNo":"1234567899999999","P004_PapperNo":"522423199716274310","P004_Mobile":"15772747952","P004_Remark":"V8955979申请提现","P004_AuditStatusCode":"Waitting","P004_Auditor":null,"P004_AuditTime":null,"P004_AuditInfo":null,"P004_EventCode":"07F82BFA-173C-4850-BABA-4B3A2EB74736","P004_SystemAuditStatusCode":"Waitting","P004_SystemAuditor":null,"P004_SystemAuditTime":null,"P004_SystemAuditInfo":null},{"P004_ID":8,"P004_PurseCode":"V8955979_YB","P004_ToCashTypeCode":"YB","P004_Time":"2017-12-14 17:57:52","P004_Sum":90.5,"P004_Poundage":2,"P004_Taxation":7.5,"P004_EnterSum":100,"P004_BankCode":"95588","P004_BankAccountName":"测试","P004_BankBranches":"中国工商银行","P004_BankCardNo":"1234567899999999","P004_PapperNo":"522423199716274310","P004_Mobile":"15772747952","P004_Remark":"V8955979申请提现","P004_AuditStatusCode":"Waitting","P004_Auditor":null,"P004_AuditTime":null,"P004_AuditInfo":null,"P004_EventCode":"04DD3634-A35E-417F-A45A-A85B54F76573","P004_SystemAuditStatusCode":"Waitting","P004_SystemAuditor":null,"P004_SystemAuditTime":null,"P004_SystemAuditInfo":null},{"P004_ID":9,"P004_PurseCode":"V8955979_YB","P004_ToCashTypeCode":"YB","P004_Time":"2017-12-14 17:57:59","P004_Sum":90.5,"P004_Poundage":2,"P004_Taxation":7.5,"P004_EnterSum":100,"P004_BankCode":"95588","P004_BankAccountName":"测试","P004_BankBranches":"中国工商银行","P004_BankCardNo":"1234567899999999","P004_PapperNo":"522423199716274310","P004_Mobile":"15772747952","P004_Remark":"V8955979申请提现","P004_AuditStatusCode":"Waitting","P004_Auditor":null,"P004_AuditTime":null,"P004_AuditInfo":null,"P004_EventCode":"342C8D33-7644-4790-A55B-C52EFA318F69","P004_SystemAuditStatusCode":"Waitting","P004_SystemAuditor":null,"P004_SystemAuditTime":null,"P004_SystemAuditInfo":null},{"P004_ID":10,"P004_PurseCode":"V8955979_YB","P004_ToCashTypeCode":"YB","P004_Time":"2017-12-14 17:59:16","P004_Sum":90.5,"P004_Poundage":2,"P004_Taxation":7.5,"P004_EnterSum":100,"P004_BankCode":"95588","P004_BankAccountName":"测试","P004_BankBranches":"中国工商银行","P004_BankCardNo":"1234567899999999","P004_PapperNo":"522423199716274310","P004_Mobile":"15772747952","P004_Remark":"V8955979申请提现","P004_AuditStatusCode":"Waitting","P004_Auditor":null,"P004_AuditTime":null,"P004_AuditInfo":null,"P004_EventCode":"95135E7B-EA36-490C-AD25-4C2845BF250B","P004_SystemAuditStatusCode":"Waitting","P004_SystemAuditor":null,"P004_SystemAuditTime":null,"P004_SystemAuditInfo":null}]}
         */

        private String status;
        private String msg;
        private DataBean data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private List<RowsBean> Rows;

            public List<RowsBean> getRows() {
                return Rows;
            }

            public void setRows(List<RowsBean> Rows) {
                this.Rows = Rows;
            }

            public static class RowsBean {
                /**
                 * P004_ID : 6
                 * P004_PurseCode : V8955979_YB
                 * P004_ToCashTypeCode : YB
                 * P004_Time : 2017-12-14 17:52:12
                 * P004_Sum : 90.5
                 * P004_Poundage : 2.0
                 * P004_Taxation : 7.5
                 * P004_EnterSum : 100.0
                 * P004_BankCode : 95588
                 * P004_BankAccountName : 测试
                 * P004_BankBranches : 中国工商银行
                 * P004_BankCardNo : 1234567899999999
                 * P004_PapperNo : 522423199716274310
                 * P004_Mobile : 15772747952
                 * P004_Remark : V8955979申请提现
                 * P004_AuditStatusCode : Waitting
                 * P004_Auditor : null
                 * P004_AuditTime : null
                 * P004_AuditInfo : null
                 * P004_EventCode : C0F65E84-5427-46B9-B59E-98DEE9F1E46F
                 * P004_SystemAuditStatusCode : Waitting
                 * P004_SystemAuditor : null
                 * P004_SystemAuditTime : null
                 * P004_SystemAuditInfo : null
                 */

                private int P004_ID;
                private String P004_PurseCode;
                private String P004_ToCashTypeCode;
                private String P004_Time;
                private double P004_Sum;
                private double P004_Poundage;
                private double P004_Taxation;
                private double P004_EnterSum;
                private String P004_BankCode;
                private String P004_BankAccountName;
                private String P004_BankBranches;
                private String P004_BankCardNo;
                private String P004_PapperNo;
                private String P004_Mobile;
                private String P004_Remark;
                private String P004_AuditStatusCode;
                private Object P004_Auditor;
                private Object P004_AuditTime;
                private Object P004_AuditInfo;
                private String P004_EventCode;
                private String P004_SystemAuditStatusCode;
                private Object P004_SystemAuditor;
                private Object P004_SystemAuditTime;
                private Object P004_SystemAuditInfo;

                public int getP004_ID() {
                    return P004_ID;
                }

                public void setP004_ID(int P004_ID) {
                    this.P004_ID = P004_ID;
                }

                public String getP004_PurseCode() {
                    return P004_PurseCode;
                }

                public void setP004_PurseCode(String P004_PurseCode) {
                    this.P004_PurseCode = P004_PurseCode;
                }

                public String getP004_ToCashTypeCode() {
                    return P004_ToCashTypeCode;
                }

                public void setP004_ToCashTypeCode(String P004_ToCashTypeCode) {
                    this.P004_ToCashTypeCode = P004_ToCashTypeCode;
                }

                public String getP004_Time() {
                    return P004_Time;
                }

                public void setP004_Time(String P004_Time) {
                    this.P004_Time = P004_Time;
                }

                public double getP004_Sum() {
                    return P004_Sum;
                }

                public void setP004_Sum(double P004_Sum) {
                    this.P004_Sum = P004_Sum;
                }

                public double getP004_Poundage() {
                    return P004_Poundage;
                }

                public void setP004_Poundage(double P004_Poundage) {
                    this.P004_Poundage = P004_Poundage;
                }

                public double getP004_Taxation() {
                    return P004_Taxation;
                }

                public void setP004_Taxation(double P004_Taxation) {
                    this.P004_Taxation = P004_Taxation;
                }

                public double getP004_EnterSum() {
                    return P004_EnterSum;
                }

                public void setP004_EnterSum(double P004_EnterSum) {
                    this.P004_EnterSum = P004_EnterSum;
                }

                public String getP004_BankCode() {
                    return P004_BankCode;
                }

                public void setP004_BankCode(String P004_BankCode) {
                    this.P004_BankCode = P004_BankCode;
                }

                public String getP004_BankAccountName() {
                    return P004_BankAccountName;
                }

                public void setP004_BankAccountName(String P004_BankAccountName) {
                    this.P004_BankAccountName = P004_BankAccountName;
                }

                public String getP004_BankBranches() {
                    return P004_BankBranches;
                }

                public void setP004_BankBranches(String P004_BankBranches) {
                    this.P004_BankBranches = P004_BankBranches;
                }

                public String getP004_BankCardNo() {
                    return P004_BankCardNo;
                }

                public void setP004_BankCardNo(String P004_BankCardNo) {
                    this.P004_BankCardNo = P004_BankCardNo;
                }

                public String getP004_PapperNo() {
                    return P004_PapperNo;
                }

                public void setP004_PapperNo(String P004_PapperNo) {
                    this.P004_PapperNo = P004_PapperNo;
                }

                public String getP004_Mobile() {
                    return P004_Mobile;
                }

                public void setP004_Mobile(String P004_Mobile) {
                    this.P004_Mobile = P004_Mobile;
                }

                public String getP004_Remark() {
                    return P004_Remark;
                }

                public void setP004_Remark(String P004_Remark) {
                    this.P004_Remark = P004_Remark;
                }

                public String getP004_AuditStatusCode() {
                    return P004_AuditStatusCode;
                }

                public void setP004_AuditStatusCode(String P004_AuditStatusCode) {
                    this.P004_AuditStatusCode = P004_AuditStatusCode;
                }

                public Object getP004_Auditor() {
                    return P004_Auditor;
                }

                public void setP004_Auditor(Object P004_Auditor) {
                    this.P004_Auditor = P004_Auditor;
                }

                public Object getP004_AuditTime() {
                    return P004_AuditTime;
                }

                public void setP004_AuditTime(Object P004_AuditTime) {
                    this.P004_AuditTime = P004_AuditTime;
                }

                public Object getP004_AuditInfo() {
                    return P004_AuditInfo;
                }

                public void setP004_AuditInfo(Object P004_AuditInfo) {
                    this.P004_AuditInfo = P004_AuditInfo;
                }

                public String getP004_EventCode() {
                    return P004_EventCode;
                }

                public void setP004_EventCode(String P004_EventCode) {
                    this.P004_EventCode = P004_EventCode;
                }

                public String getP004_SystemAuditStatusCode() {
                    return P004_SystemAuditStatusCode;
                }

                public void setP004_SystemAuditStatusCode(String P004_SystemAuditStatusCode) {
                    this.P004_SystemAuditStatusCode = P004_SystemAuditStatusCode;
                }

                public Object getP004_SystemAuditor() {
                    return P004_SystemAuditor;
                }

                public void setP004_SystemAuditor(Object P004_SystemAuditor) {
                    this.P004_SystemAuditor = P004_SystemAuditor;
                }

                public Object getP004_SystemAuditTime() {
                    return P004_SystemAuditTime;
                }

                public void setP004_SystemAuditTime(Object P004_SystemAuditTime) {
                    this.P004_SystemAuditTime = P004_SystemAuditTime;
                }

                public Object getP004_SystemAuditInfo() {
                    return P004_SystemAuditInfo;
                }

                public void setP004_SystemAuditInfo(Object P004_SystemAuditInfo) {
                    this.P004_SystemAuditInfo = P004_SystemAuditInfo;
                }
            }
        }
    }
}