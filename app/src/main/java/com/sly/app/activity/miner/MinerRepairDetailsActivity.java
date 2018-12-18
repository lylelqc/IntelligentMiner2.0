package com.sly.app.activity.miner;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.miner.MinerRepairDetailsBean;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import vip.devkit.library.Logcat;

public class MinerRepairDetailsActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.miner_cost_details_money)
    TextView tvDetailsMoney;
    @BindView(R.id.miner_cost_details_model)
    TextView tvDetailsModel;
    @BindView(R.id.miner_cost_details_repair)
    TextView tvDetailsRepair;

    @BindView(R.id.miner_cost_details_code)
    TextView tvDetailsBillCode;
    @BindView(R.id.rl_miner_details_ptime)
    RelativeLayout rlDetailsPtime;
    @BindView(R.id.miner_cost_details_ptime)
    TextView tvDetailsPtime;
    @BindView(R.id.rl_miner_details_etime)
    RelativeLayout rlDetailsEtime;
    @BindView(R.id.miner_cost_details_etime)
    TextView tvDetailsEtime;
    @BindView(R.id.rl_miner_details_during)
    RelativeLayout rlDetailsDuring;
    @BindView(R.id.miner_cost_details_during_time)
    TextView tvDetailsDuringTime;
    @BindView(R.id.miner_cost_details_machine_code)
    TextView tvDetailsMachineCode;
    @BindView(R.id.miner_cost_details_reason)
    TextView tvDetailsMachineReason;
    @BindView(R.id.rl_miner_details_descripted)
    RelativeLayout rlDetailsDescripted;
    @BindView(R.id.miner_cost_details_descriped)
    TextView tvDetailsRepairDescriped;

    @BindView(R.id.rl_repair_details_history)
    RelativeLayout rlDetailsHistory;

    @BindView(R.id.tv_miner_details_reminder)
    TextView tvDetailsRepairReminder;
    @BindView(R.id.ll_miner_repair_details_btn)
    LinearLayout llDetailsRepairBtn;
    @BindView(R.id.tv_miner_repair_details_cancel)
    TextView tvDetailsRepairCancel;
    @BindView(R.id.tv_miner_repair_details_confirm)
    TextView tvDetailsRepairConfirm;

    private String User, LoginType, FrSysCode, FMasterCode, MineCode, Token, Key, machineSysCode;
    private String BillNo;
    ICommonRequestPresenter iCommonRequestPresenter;
    private List<MinerRepairDetailsBean> mResultList = new ArrayList<>();
    private boolean isReminder;
    private boolean isHistory;


    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_miner_repair_details;
    }

    @Override
    protected void initViewsAndEvents() {

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        BillNo = getIntent().getExtras().getString("BillNo");
        isHistory = getIntent().getExtras().getBoolean("isHistory");

        if(isHistory){
            rlDetailsHistory.setVisibility(View.GONE);
        }

        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        tvMainTitle.setText(getResources().getString(R.string.miner_repair_free_details));

        intitNewsCount();

        toRequest(NetConstant.EventTags.GET_MINER_IS_REMINDER);
        toRequest(NetConstant.EventTags.GET_MINER_REPAIR_DETAILS);
    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if ("0".equals(count)) {
            tvRedNum.setVisibility(View.GONE);
        } else {
            tvRedNum.setVisibility(View.VISIBLE);
            tvRedNum.setText(count);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("BillNo", BillNo);

        if (eventTag == NetConstant.EventTags.GET_MINER_REPAIR_DETAILS) {
            map.put("Rounter", NetConstant.GET_MINER_REPAIR_DETAILS);

        }
        else if(eventTag == NetConstant.EventTags.CONFRIM_MINER_REPAIR){
            map.put("Rounter", NetConstant.CONFRIM_MINER_REPAIR);

        }
        else if(eventTag == NetConstant.EventTags.CANCEL_MINER_REPAIR){
            map.put("Rounter", NetConstant.CANCEL_MINER_REPAIR);

        }
        else if(eventTag == NetConstant.EventTags.GET_MINER_IS_REMINDER){
            map.put("Rounter", NetConstant.GET_MINER_IS_REMINDER);

        }
        else if(eventTag == NetConstant.EventTags.GET_MINER_REMINDER){
            map.put("Rounter", NetConstant.GET_MINER_REMINDER);
            map.put("roleClass", LoginType);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 = " + result);
        if (eventTag == NetConstant.EventTags.GET_MINER_REPAIR_DETAILS) {
            mResultList =
                    (List<MinerRepairDetailsBean>) AppUtils.parseRowsResult(result, MinerRepairDetailsBean.class);
            refreshListView();
        }
        else if(eventTag == NetConstant.EventTags.CONFRIM_MINER_REPAIR){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                ToastUtils.showToast(getString(R.string.comfirm_success));
                EventBus.getDefault().post(new PostResult(EventBusTags.JUMP_MIENR_REPAIR_BILL_TREATED));
                finish();
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
        else if(eventTag == NetConstant.EventTags.CANCEL_MINER_REPAIR){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                ToastUtils.showToast(getString(R.string.cancel_success));
                EventBus.getDefault().post(new PostResult(EventBusTags.JUMP_MIENR_REPAIR_BILL_TREATED));
                finish();
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_MINER_IS_REMINDER){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                if("true".equals(returnBean.getData())){
                    isReminder = true;
                    tvDetailsRepairReminder.setClickable(true);
                    tvDetailsRepairReminder.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn_gradient_3dp));
                }else{
                    isReminder = false;
                    tvDetailsRepairReminder.setClickable(false);
                    tvDetailsRepairReminder.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_all_3dp));
                }
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_MINER_REMINDER){
            tvDetailsRepairReminder.setClickable(false);
            tvDetailsRepairReminder.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_all_3dp));
            ToastUtils.showToast(getString(R.string.no_reminder));
            toRequest(NetConstant.EventTags.GET_MINER_REPAIR_DETAILS);
        }
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

    private void refreshListView() {
        MinerRepairDetailsBean bean = mResultList.get(0);

        machineSysCode = bean.getMachineSysCode();

        tvDetailsMoney.setText(bean.getRepairSum()+"");
        tvDetailsModel.setText(bean.getModel());
        tvDetailsRepair.setText(bean.getResultName());
        tvDetailsBillCode.setText(bean.getBillNo());
        tvDetailsMachineCode.setText(bean.getMachineCode());
        tvDetailsMachineReason.setText(bean.getInfo());

//        reason = bean.getRepairInfo();
        String BillStatus = bean.getResultCode();
        if("00".equals(BillStatus)
                || "02".equals(BillStatus) || "03".equals(BillStatus)){

            rlDetailsEtime.setVisibility(View.GONE);
            rlDetailsDuring.setVisibility(View.GONE);

            if("00".equals(BillStatus)){
                rlDetailsPtime.setVisibility(View.GONE);
                rlDetailsDescripted.setVisibility(View.GONE);
                tvDetailsRepairReminder.setVisibility(View.VISIBLE);
                llDetailsRepairBtn.setVisibility(View.GONE);
            }
            else if("02".equals(BillStatus)){
                rlDetailsPtime.setVisibility(View.GONE);
                tvDetailsPtime.setText(bean.getPtime());
                tvDetailsRepairDescriped.setText(bean.getRepairInfo());
                tvDetailsRepairReminder.setVisibility(View.GONE);
                llDetailsRepairBtn.setVisibility(View.VISIBLE);
            }
            else if("03".equals(BillStatus)){
                rlDetailsPtime.setVisibility(View.GONE);
                tvDetailsPtime.setText(bean.getPtime());
                tvDetailsRepairDescriped.setText(bean.getRepairInfo());
                tvDetailsRepairReminder.setVisibility(View.VISIBLE);
                llDetailsRepairBtn.setVisibility(View.GONE);
            }

        }
        else if("04".equals(BillStatus) || "06".equals(BillStatus)){
            tvDetailsPtime.setText(bean.getPtime());
            tvDetailsEtime.setText(bean.getEndTime());
            tvDetailsDuringTime.setText(bean.getRepairHours());
            tvDetailsRepairDescriped.setText(bean.getRepairInfo());
            rlDetailsHistory.setVisibility(View.GONE);
            tvDetailsRepairReminder.setVisibility(View.GONE);
            llDetailsRepairBtn.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btn_main_back, R.id.rl_repair_details_history, R.id.tv_miner_details_reminder,
            R.id.tv_miner_repair_details_cancel, R.id.tv_miner_repair_details_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_repair_details_history:
                Bundle bundle = new Bundle();
                bundle.putString("machineSysCode", mResultList.get(0).getMachineSysCode());
                AppUtils.goActivity(this, MinerHistoryRepairActivity.class, bundle);
                break;
            case R.id.tv_miner_details_reminder:
                toRequest(NetConstant.EventTags.GET_MINER_REMINDER);
                break;
            case R.id.tv_miner_repair_details_cancel:
                showCustomDialog(this, R.layout.dialog_general_style, 2,
                        getString(R.string.request_cancel_repair), 1);
                break;
            case R.id.tv_miner_repair_details_confirm:
                showCustomDialog(this, R.layout.dialog_general_style, 2,
                        getString(R.string.request_confirm_repair), 2);
                break;
        }
    }

    // 操作提示框
    private void showCustomDialog(Context context, int layout, final int btnType, String content, final int tag) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 获取控件
        if (layout == R.layout.dialog_general_style) {
            TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
            tvDetails.setText(content);
        }

        TextView cancel = dialog.findViewById(R.id.cancel_action);
        TextView confirm = dialog.findViewById(R.id.confirm_action);
        View line = dialog.findViewById(R.id.view_line);
        if (btnType == 1) {
            cancel.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (btnType != 1) {
                    if (tag == 1) {
                        toRequest(NetConstant.EventTags.CANCEL_MINER_REPAIR);
                    }else{
                        toRequest(NetConstant.EventTags.CONFRIM_MINER_REPAIR);
                    }
                }
            }
        });
    }
}
