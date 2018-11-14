package com.sly.app.activity.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.SlyRechargeActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.ReplaceListBean;
import com.sly.app.model.TaketDetailBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.devkit.library.Logcat;


public class TaketDetailActivity extends BaseActivity implements ICommonViewUi {
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
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.tv_taket_time)
    TextView tvTaketTime;
    @BindView(R.id.rl_item_rapare)
    RelativeLayout rlItemRapare;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_taket_num)
    TextView tvTaketNum;
    @BindView(R.id.tv_taket_name)
    TextView tvTaketName;
    @BindView(R.id.tv_time_now)
    TextView tvTimeNow;
    @BindView(R.id.tv_taket_think)
    TextView tvTaketThink;
    @BindView(R.id.tv_waise_time)
    TextView tvWaiseTime;
    @BindView(R.id.tv_run_msg)
    TextView tvRunMsg;
    @BindView(R.id.tv_taket_people)
    TextView tvTaketPeople;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.ll_L1)
    LinearLayout llL1;
    @BindView(R.id.ll_L2)
    LinearLayout llL2;
    @BindView(R.id.ll_L8)
    LinearLayout llL8;
    @BindView(R.id.tv_taket_repair_pj)
    TextView tvTaketRepairPj;
    @BindView(R.id.ll_L9)
    LinearLayout llL9;
    @BindView(R.id.tv_no_pay)
    TextView tvNoPay;
    @BindView(R.id.tv_taket_repair_fee)
    TextView tvTaketRepairFee;
    @BindView(R.id.ll_fee)
    LinearLayout llFee;
    @BindView(R.id.tv_taket_code)
    TextView tvTaketCode;

    private String User, FrSysCode, FMasterCode, Token, Key;
    private String LoginType = "None";
    private String mRounter = "";
    private ReplaceListBean bean;
    private String mBillNo = "";
    ICommonRequestPresenter iCommonRequestPresenter;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_taket_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        initData();
    }

    private void initData() {
        bean = getIntent().getParcelableExtra("bean");
        mBillNo = bean.getBillNo();
        tvMainTitle.setText("维修记录");
//        tvMainTitle.setText(bean.getBillStatus().equals("处理中")?"维修单":"维修记录");
        User = SharedPreferencesUtil.getString(this, "User");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        if (LoginType.equals("Miner")) {
            llL1.setVisibility(View.GONE);
            llL2.setVisibility(View.GONE);
            llL8.setVisibility(View.GONE);
            tvNext.setText("我要催单");
        } else {
            llL1.setVisibility(View.VISIBLE);
            llL2.setVisibility(View.VISIBLE);
            llL8.setVisibility(View.VISIBLE);
            tvNext.setText("通知处理");
        }
        toRequest(NetWorkCons.EventTags.GET_REPAIR_TAKET_DETAIL);
        toRequest(NetWorkCons.EventTags.GET_REPAIR_FEE_DETAIL);
    }

    private void setData(List<TaketDetailBean> taketDetailBean) {
        if (LoginType.equals("MinerMaster")) {
            tvPlace.setText(taketDetailBean.get(0).getMineName() + "");
            tvTaketNum.setText(taketDetailBean.get(0).getMachineCode() + "");
            tvTaketPeople.setText(taketDetailBean.get(0).getRepairPerson() + "");
        }
        tvNum.setText(bean.getBillNo());
        tvTaketTime.setText(bean.getBillTime());

        tvTaketName.setText(taketDetailBean.get(0).getMachineName());
        tvTaketCode.setText(taketDetailBean.get(0).getMachineCode());
        tvTaketThink.setText(taketDetailBean.get(0).getAnalyse());
        tvTimeNow.setText(taketDetailBean.get(0).getBeginTime());
        tvWaiseTime.setText(taketDetailBean.get(0).getRepairHours() + "h");
        tvRunMsg.setText(taketDetailBean.get(0).getRunStatus());
        tvTaketRepairFee.setText(taketDetailBean.get(0).getRepairSum() + "元");

        if (("处理中,等待付款").equals(taketDetailBean.get(0).getBillStatus())) {
            if (LoginType.equals("Miner")) {
                tvNext.setVisibility(View.VISIBLE);
                tvNext.setText("确认维修");
                tvNoPay.setVisibility(View.VISIBLE);
            }
        } else {
            tvNoPay.setVisibility(View.GONE);
            toRequest(NetWorkCons.EventTags.IS_REMINDER_STATUS);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        if (NetWorkCons.EventTags.GET_REPAIR_TAKET_DETAIL == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_REPAIR_TAKET_DETAIL);
            map.put("User", User);
            map.put("billNo", mBillNo);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_REPAIR_TAKET_DETAIL, mContext, NetWorkCons.BASE_URL, mapJson);
        } else if (NetWorkCons.EventTags.GET_REPAIR_FEE_DETAIL == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_REPAIR_FEE_DETAIL);
            map.put("User", User);
            map.put("billNo", mBillNo);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_REPAIR_FEE_DETAIL, mContext, NetWorkCons.BASE_URL, mapJson);
        } else if (NetWorkCons.EventTags.PAY_REPAIR_FEE == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.PAY_REPAIR_FEE);
            map.put("User", User);
            map.put("billNo", mBillNo);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.PAY_REPAIR_FEE, mContext, NetWorkCons.BASE_URL, mapJson);
        } else if (NetWorkCons.EventTags.IS_REMINDER_STATUS == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.IS_REMINDER_STATUS);
            map.put("User", User);
            map.put("billNo", mBillNo);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.IS_REMINDER_STATUS, mContext, NetWorkCons.BASE_URL, mapJson);
        } else if (NetWorkCons.EventTags.REMINDER_REPAIR_TAKET == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.REMINDER_REPAIR_TAKET);
            map.put("User", User);
            map.put("billNo", mBillNo);
            if ("Miner".equals(LoginType)) {
                map.put("roleClass", "Miner");
            } else {
                map.put("roleClass", "MineMaster");
            }
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.REMINDER_REPAIR_TAKET, mContext, NetWorkCons.BASE_URL, mapJson);
        } else if (NetWorkCons.EventTags.NO_PAY_REPAIR_FEE == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.NO_PAY_REPAIR_FEE);
            map.put("User", User);
            map.put("billNo", mBillNo);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.NO_PAY_REPAIR_FEE, mContext, NetWorkCons.BASE_URL, mapJson);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if (NetWorkCons.EventTags.GET_REPAIR_TAKET_DETAIL == eventTag) {
            try {
                SlyReturnListBean mReturnBean = JSON.parseObject(result, SlyReturnListBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getMsg().equals("获取成功")) {
                    List<TaketDetailBean> taketDetailBean = JSON.parseArray(mReturnBean.getData().getRows().toString(), TaketDetailBean.class);
                    setData(taketDetailBean);
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
            } catch (Exception e) {
                Logcat.e(e.toString());
            }
        } else if (NetWorkCons.EventTags.GET_REPAIR_FEE_DETAIL == eventTag) {
            try {
                SlyReturnListBean mReturnBean = JSON.parseObject(result, SlyReturnListBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
                    JsonHelper<RepairPjBean> dataParser = new JsonHelper<RepairPjBean>(RepairPjBean.class);
                    List<RepairPjBean> replaceListBean = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                        String searchResult = jsonObject.getString("Rows");
                        replaceListBean = dataParser.getDatas(searchResult);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    llL9.setVisibility(View.VISIBLE);
                    StringBuffer beijian = new StringBuffer();
                    for (RepairPjBean bean : replaceListBean) {
                        beijian.append(bean.getMine17_PartName() + "  ");
                    }
                    tvTaketRepairPj.setText(beijian + "");
                    llFee.setVisibility(View.VISIBLE);
                } else {
                    llFee.setVisibility(View.GONE);
                    llL9.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                ToastUtils.showToast(e.toString());
            }
        } else if (NetWorkCons.EventTags.PAY_REPAIR_FEE == eventTag) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                ToastUtils.showToast(jsonObject.optString("msg"));
                finish();
            } catch (Exception e) {
                Logcat.e(e.toString());
            }

        } else if (NetWorkCons.EventTags.IS_REMINDER_STATUS == eventTag) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equals("1")) {
                    tvNext.setVisibility(jsonObject.optString("data").equals("true") ? View.VISIBLE : View.INVISIBLE);
                } else {
                    ToastUtils.showToast(jsonObject.optString("msg"));
                }

            } catch (Exception e) {
                Logcat.e(e.toString());
            }
        } else if (NetWorkCons.EventTags.REMINDER_REPAIR_TAKET == eventTag) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equals("1")) {
                    ToastUtils.showToast("催单成功");
                    toRequest(NetWorkCons.EventTags.IS_REMINDER_STATUS);
                }
            } catch (Exception e) {
                Logcat.e(e.toString());
            }
        } else if (NetWorkCons.EventTags.NO_PAY_REPAIR_FEE == eventTag) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                ToastUtils.showToast(jsonObject.optString("msg"));
                finish();
            } catch (Exception e) {
                Logcat.e(e.toString());
            }
        }
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        if (NetWorkCons.EventTags.PAY_REPAIR_FEE == eventTag) {
            new MaterialDialog.Builder(mContext)
                    .title("维修失败，是否充值")
                    .titleColor(Color.parseColor("#2F3F61"))
                    .content(msg)
                    .positiveText("确定")
                    .positiveColor(Color.parseColor("#2F3F61"))
                    .negativeText("取消")
                    .negativeColor(Color.parseColor("#999999"))
                    .cancelable(false)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            CommonUtil2.goActivity(mContext, SlyRechargeActivity.class);
                        }
                    })
                    .show();

        } else if (NetWorkCons.EventTags.NO_PAY_REPAIR_FEE == eventTag) {
            ToastUtils.showToast(msg);
        }

    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {

    }

    @Override
    public void isRequesting(int eventTag, boolean status) {

    }

    @OnClick({R.id.btn_main_back, R.id.tv_next, R.id.tv_no_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_next:
                if (tvNext.getText().toString().equals("确认维修")) {
                    new MaterialDialog.Builder(mContext)
                            .content("需要支付维修费用")
                            .positiveText("确定")
                            .negativeText("取消")
                            .cancelable(false)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    toRequest(NetWorkCons.EventTags.PAY_REPAIR_FEE);
                                }
                            })
                            .show();
                } else {
                    toRequest(NetWorkCons.EventTags.REMINDER_REPAIR_TAKET);
                }

                break;
            case R.id.tv_no_pay:
                new MaterialDialog.Builder(mContext)
                        .content("取消维修后，设备将下架")
                        .positiveText("确定")
                        .negativeText("取消")
                        .negativeColor(Color.parseColor("#999999"))
                        .cancelable(false)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                toRequest(NetWorkCons.EventTags.NO_PAY_REPAIR_FEE);
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class RepairPjBean {
        /**
         * Mine17_PartName : 备件002
         */

        private String Mine17_PartName;

        public String getMine17_PartName() {
            return Mine17_PartName;
        }

        public void setMine17_PartName(String Mine17_PartName) {
            this.Mine17_PartName = Mine17_PartName;
        }
    }
}
