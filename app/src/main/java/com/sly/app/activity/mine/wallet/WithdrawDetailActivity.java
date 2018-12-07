package com.sly.app.activity.mine.wallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class WithdrawDetailActivity extends BaseActivity {
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
    @BindView(R.id.tv_AccountName)
    TextView tvAccountName;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_applydata)
    TextView tvApplydata;
    @BindView(R.id.tv_handledata)
    TextView tvHandledata;
    @BindView(R.id.tv_BankName)
    TextView tvBankName;
    @BindView(R.id.tv_accounttime)
    TextView tvAccounttime;
    @BindView(R.id.tv_founddata)
    TextView tvFounddata;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    private String MemberCode, Token;
    private Bundle bundle;
    private Dialog dialog;
    InfoJson mInfo;
    InfoJson2 mInfo2;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(llCommLayout).init();
    }

    @Override
    protected void initView() {
        tvMainTitle.setText("提现详情");
        tvMainRight.setText("取消提现");
        tvMainRight.setTextColor(Color.parseColor("#ffffff"));
        if (bundle != null) {
            Logcat.i("json:" + bundle.getString("info"));
            String type = bundle.getString("type");
            mInfo2 = CommonUtils.GsonToObject(String.valueOf(bundle.getString("info")), InfoJson2.class);
            if (type.equals("1")) {
                tvMessage.setText("你已有提现在审核中");
            } else {
                tvMessage.setText("提现审核中");
            }
            tvMoney.setText("￥" + mInfo2.getP004_Sum() + "");
            tvApplydata.setText(mInfo2.getP004_Time() + "");
            tvHandledata.setText(mInfo2.getP004_Time() + "");
            tvFounddata.setText(mInfo2.getP004_Time() + "");
            tvBankName.setText(mInfo2.getP004_BankBranches());
            tvAccountName.setText(mInfo2.getP004_BankAccountName());
        } else {
            ToastUtils.showToast("请退出App之后重新打开");
        }
    }

    @Override
    protected void initData() {
        bundle = getIntent().getExtras();
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_withdrawdetails;
    }

    @Override
    protected void setListener() {

    }


    @OnClick({R.id.btn_main_back, R.id.ll_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                startActivityWithoutExtras(WithdrawActivity.class);
                finish();
                break;
            case R.id.ll_right:
                showCancelDialog();
                break;
        }
    }

    /**
     * 确认取消订单的对话框
     **/
    private void showCancelDialog() {
        dialog = new Dialog(WithdrawDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.clear_history_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) dialog.findViewById(R.id.tv_dialog_detail)).setVisibility(View.GONE);
        ((TextView) dialog.findViewById(R.id.textView5)).setText("确定要取消当前申请的提现吗？");
        dialog.show();
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_action);
        cancelButton.setText("取消");
        Button confirmButton = (Button) dialog.findViewById(R.id.confirm_action);
        confirmButton.setText("确认");
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProCancel(mInfo2.getP004_ID() + ""); //取消提现
                dialog.dismiss();
                return;
            }
        });
    }


    /**
     * 取消提现
     *
     * @pam id
     */
    private void ProCancel(String id) {
        initProgressDialog("系统处理中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("Pay09_ID", id);
        map.put("MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.CancelCash, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                NetLogCat.E(NetWorkConstant.CancelCash, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("取消成功");
                    startActivityWithoutExtras(WithdrawActivity.class);
                    finish();
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                dismissProgressDialog();
                Logcat.e("网络错误：" + e);
            }
        });
    }

    class InfoJson {
        /**
         * Message : 你已经有提现正在审核
         * Sum : 100.0
         * Time : 2017-11-07 16:16:32
         */
        private String Message;
        private double Sum;
        private String Time;

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public double getSum() {
            return Sum;
        }

        public void setSum(double Sum) {
            this.Sum = Sum;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }
    }

    class InfoJson2 {

        /**
         * P004_AuditStatusCode : Waitting
         * P004_BankAccountName : 测试
         * P004_BankBranches : 中国工商银行
         * P004_BankCardNo : 1234567899999999
         * P004_BankCode : 95588
         * P004_EnterSum : 100
         * P004_EventCode : C0F65E84-5427-46B9-B59E-98DEE9F1E46F
         * P004_ID : 6
         * P004_Mobile : 15772747952
         * P004_PapperNo : 522423199716274310
         * P004_Poundage : 2
         * P004_PurseCode : V8955979_YB
         * P004_Remark : V8955979申请提现
         * P004_Sum : 90.5
         * P004_SystemAuditStatusCode : Waitting
         * P004_Taxation : 7.5
         * P004_Time : 2017-12-14 17:52:12
         * P004_ToCashTypeCode : YB
         */

        private String P004_AuditStatusCode;
        private String P004_BankAccountName;
        private String P004_BankBranches;
        private String P004_BankCardNo;
        private String P004_BankCode;
        private int P004_EnterSum;
        private String P004_EventCode;
        private int P004_ID;
        private String P004_Mobile;
        private String P004_PapperNo;
        private int P004_Poundage;
        private String P004_PurseCode;
        private String P004_Remark;
        private double P004_Sum;
        private String P004_SystemAuditStatusCode;
        private double P004_Taxation;
        private String P004_Time;
        private String P004_ToCashTypeCode;

        public String getP004_AuditStatusCode() {
            return P004_AuditStatusCode;
        }

        public void setP004_AuditStatusCode(String P004_AuditStatusCode) {
            this.P004_AuditStatusCode = P004_AuditStatusCode;
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

        public String getP004_BankCode() {
            return P004_BankCode;
        }

        public void setP004_BankCode(String P004_BankCode) {
            this.P004_BankCode = P004_BankCode;
        }

        public int getP004_EnterSum() {
            return P004_EnterSum;
        }

        public void setP004_EnterSum(int P004_EnterSum) {
            this.P004_EnterSum = P004_EnterSum;
        }

        public String getP004_EventCode() {
            return P004_EventCode;
        }

        public void setP004_EventCode(String P004_EventCode) {
            this.P004_EventCode = P004_EventCode;
        }

        public int getP004_ID() {
            return P004_ID;
        }

        public void setP004_ID(int P004_ID) {
            this.P004_ID = P004_ID;
        }

        public String getP004_Mobile() {
            return P004_Mobile;
        }

        public void setP004_Mobile(String P004_Mobile) {
            this.P004_Mobile = P004_Mobile;
        }

        public String getP004_PapperNo() {
            return P004_PapperNo;
        }

        public void setP004_PapperNo(String P004_PapperNo) {
            this.P004_PapperNo = P004_PapperNo;
        }

        public int getP004_Poundage() {
            return P004_Poundage;
        }

        public void setP004_Poundage(int P004_Poundage) {
            this.P004_Poundage = P004_Poundage;
        }

        public String getP004_PurseCode() {
            return P004_PurseCode;
        }

        public void setP004_PurseCode(String P004_PurseCode) {
            this.P004_PurseCode = P004_PurseCode;
        }

        public String getP004_Remark() {
            return P004_Remark;
        }

        public void setP004_Remark(String P004_Remark) {
            this.P004_Remark = P004_Remark;
        }

        public double getP004_Sum() {
            return P004_Sum;
        }

        public void setP004_Sum(double P004_Sum) {
            this.P004_Sum = P004_Sum;
        }

        public String getP004_SystemAuditStatusCode() {
            return P004_SystemAuditStatusCode;
        }

        public void setP004_SystemAuditStatusCode(String P004_SystemAuditStatusCode) {
            this.P004_SystemAuditStatusCode = P004_SystemAuditStatusCode;
        }

        public double getP004_Taxation() {
            return P004_Taxation;
        }

        public void setP004_Taxation(double P004_Taxation) {
            this.P004_Taxation = P004_Taxation;
        }

        public String getP004_Time() {
            return P004_Time;
        }

        public void setP004_Time(String P004_Time) {
            this.P004_Time = P004_Time;
        }

        public String getP004_ToCashTypeCode() {
            return P004_ToCashTypeCode;
        }

        public void setP004_ToCashTypeCode(String P004_ToCashTypeCode) {
            this.P004_ToCashTypeCode = P004_ToCashTypeCode;
        }
    }
}
