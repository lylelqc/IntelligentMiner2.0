package com.sly.app.activity.yunw.repair;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.Helper.ActivityHelper;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.yunw.repair.RepairBillDetailsBean;
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

public class
RepairBillDetailsActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.tv_repair_details_type)
    TextView tvDetailsType;
    @BindView(R.id.tv_repair_details_status)
    TextView tvDetailsStatus;

    @BindView(R.id.ll_repair_details_startstop)
    LinearLayout llDetailsStartStop;
    @BindView(R.id.tv_repair_details_start_machine)
    TextView tvDetailsStart1;
    @BindView(R.id.tv_repair_details_stop_machine)
    TextView tvDetailsStop;

    @BindView(R.id.tv_repair_details_billno)
    TextView tvDetailsBillNo;
    @BindView(R.id.tv_repair_details_ptime)
    TextView tvDetailsPtime;

    @BindView(R.id.ll_repair_details_endtime)
    LinearLayout llDetailsEndTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndtime;
    @BindView(R.id.tv_repair_details_etime)
    TextView tvDetailsEtime;

    @BindView(R.id.ll_repair_details_allhours)
    LinearLayout llDetailsAllhours;
    @BindView(R.id.tv_repair_details_alltime)
    TextView tvDetailsAlltime;

    @BindView(R.id.ll_repair_details_allfree)
    LinearLayout llDetailsAllfree;
    @BindView(R.id.tv_repair_details_allfree)
    TextView tvDetailsAllfree;

    @BindView(R.id.tv_repair_details_IP)
    TextView tvDetailsIP;
    @BindView(R.id.tv_repair_details_area)
    TextView tvDetailsArea;
    @BindView(R.id.tv_repair_details_reason)
    TextView tvDetailsReason;

    @BindView(R.id.ll_repair_details_description)
    LinearLayout llDetailsDescription;
    @BindView(R.id.tv_repair_details_description)
    TextView tvDetailsDescription;

    @BindView(R.id.ll_repair_history)
    LinearLayout llDetailsHistory;


    @BindView(R.id.rl_repair_btn)
    RelativeLayout rlDetailsBtn;
    @BindView(R.id.tv_repair_details_start)
    TextView tvDetailStart2;

    @BindView(R.id.ll_repair_details_btn)
    LinearLayout llDetailsBtn;
    @BindView(R.id.tv_repair_details_form)
    TextView tvDetailsForm;
    @BindView(R.id.tv_repair_details_dealed)
    TextView tvDetailsDealed;

    @BindView(R.id.iv_repair_details_status_icon)
    ImageView ivDetailsStatusIcon;

    private List<RepairBillDetailsBean> mResultList = new ArrayList<RepairBillDetailsBean>();
    ICommonRequestPresenter iCommonRequestPresenter;

    private String User,LoginType, MineCode, PersonSysCode, Token, Key, BillNo;
    private String detailID = "";
    private String remark = "";
    private String machineSysCode = "";
    private String reason = "";

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_repair_details;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        tvMainTitle.setText(getString(R.string.repair_details));

        // 把当前activty加入压入栈中
        ActivityHelper.getInstance().pushOneActivity(this);
        BillNo = getIntent().getExtras().getString("BillNo");
        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_DETAILS);
    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if("0".equals(count)){
            tvRedNum.setVisibility(View.GONE);
        }else{
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

        if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_DEALING_BTN){
            //已解决（处理中）
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_DEALING_BTN);
            map.put("DetailID", detailID);
        }else if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_DEALED_BTN){
            //已解决（报单）
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_DEALED_BTN);
            map.put("DetailID", detailID);
            map.put("remark", remark);
        }else if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE){
            //重启设备
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_START_MACHINE);
            map.put("personSysCode", PersonSysCode);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", machineSysCode + ",");
            map.put("reason", getString(R.string.machine_start));
        }else if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE){
            //设备停机
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_STOP_MACHINE);
            map.put("personSysCode", PersonSysCode);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", machineSysCode + ",");
            map.put("reason", reason);
        }else{
            //维修单详情
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_DETAILS);
            map.put("BillNo", BillNo);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数" + result);
        if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_DEALING_BTN
                || eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_DEALED_BTN){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
//                showCustomDialog(this,R.layout.dialog_general_style, 1, getString(R.string.comfirm_success),0);
                toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_DETAILS);
                EventBus.getDefault().post(new PostResult(EventBusTags.JUMP_REPAIR_BILL_TREATED));
            }else{
                ToastUtils.showToast(returnBean.getMsg());
            }
        } else if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE
                || eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                ToastUtils.showToast(getString(R.string.comfirm_success));
            }else{
                ToastUtils.showToast(returnBean.getMsg());
            }
        } else{
            List<RepairBillDetailsBean> resultList =
                    (List<RepairBillDetailsBean>) AppUtils.parseRowsResult(result, RepairBillDetailsBean.class);

            mResultList.clear();
            if (resultList != null && resultList.size() != 0) {
                mResultList.addAll(resultList);
            }
            refreshListView();
        }
    }

    private void refreshListView() {
        RepairBillDetailsBean bean = mResultList.get(0);

        machineSysCode = bean.getMachineSysCode();
//        reason = bean.getRepairInfo();
        String BillStatus = bean.getResultCode();
        if("00".equals(BillStatus)
                || "02".equals(BillStatus) || "03".equals(BillStatus)){
            llDetailsEndTime.setVisibility(View.GONE);
            llDetailsAllhours.setVisibility(View.GONE);

            tvDetailsType.setText(bean.getModel());
            tvDetailsStatus.setText(bean.getResultName());
            tvDetailsBillNo.setText(bean.getBillNo());
            tvDetailsPtime.setText(bean.getPtime());
            tvDetailsIP.setText(bean.getIP());
            tvDetailsArea.setText(bean.getAreaName());
            tvDetailsReason.setText(bean.getInfo());

            if("00".equals(BillStatus)){
                detailID = bean.getDetailID();

                llDetailsAllfree.setVisibility(View.GONE);
                llDetailsDescription.setVisibility(View.GONE);
                tvDetailStart2.setVisibility(View.GONE);
                llDetailsBtn.setVisibility(View.VISIBLE);
                tvDetailsForm.setText(getString(R.string.repair_form));
                tvDetailsDealed.setText(getString(R.string.repair_deal));

                ivDetailsStatusIcon.setImageResource(R.drawable.nodelee_weixiudan_icon);
            }else if("02".equals(BillStatus)){
                rlDetailsBtn.setVisibility(View.GONE);

                tvDetailsAllfree.setText(bean.getRepairSum() + "");
                tvDetailsDescription.setText(bean.getRepairInfo());
                ivDetailsStatusIcon.setImageResource(R.drawable.nothave_weixiudan_icon);
            }else if("03".equals(BillStatus)){
                detailID = bean.getDetailID();

                llDetailsStartStop.setVisibility(View.VISIBLE);
                tvDetailStart2.setText(getString(R.string.repair_deal));

                tvDetailsAllfree.setText(bean.getRepairSum() + "");
                tvDetailsDescription.setText(bean.getRepairInfo());

                //设置控件距离top的高度
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ivDetailsStatusIcon.getLayoutParams());
                lp.setMargins(0, AppUtils.dp2px(this,100), 0, AppUtils.dp2px(this,15));
                ivDetailsStatusIcon.setLayoutParams(lp);
                ivDetailsStatusIcon.setImageResource(R.drawable.have_weixiudan_icon);
            }

        }else if("04".equals(BillStatus) || "06".equals(BillStatus)){
            rlDetailsBtn.setVisibility(View.GONE);

            tvDetailsType.setText(bean.getModel());
            tvDetailsStatus.setText(bean.getResultName());
            tvDetailsBillNo.setText(bean.getBillNo());
            tvDetailsPtime.setText(bean.getPtime());
            tvDetailsEtime.setText(bean.getEndTime());
            tvDetailsAllfree.setText(bean.getRepairSum() + "");
            tvDetailsIP.setText(bean.getIP());
            tvDetailsArea.setText(bean.getAreaName());
            tvDetailsReason.setText(bean.getInfo());
            tvDetailsDescription.setText(bean.getRepairInfo());

            if("04".equals(bean.getResultCode())){
                tvDetailsAlltime.setText(bean.getRepairHours());
                llDetailsEndTime.setVisibility(View.VISIBLE);
                llDetailsAllhours.setVisibility(View.VISIBLE);
                llDetailsAllfree.setVisibility(View.VISIBLE);
                llDetailsDescription.setVisibility(View.VISIBLE);
                ivDetailsStatusIcon.setImageResource(R.drawable.jieshu_weixiudan_icon);
            }else{
                llDetailsAllhours.setVisibility(View.GONE);
                tvEndtime.setText(getString(R.string.repair_refuse_time));
                ivDetailsStatusIcon.setImageResource(R.drawable.quxiao_weixiudan_icon);
            }

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

    @OnClick({R.id.btn_main_back, R.id.tv_repair_details_start_machine, R.id.tv_repair_details_stop_machine,
            R.id.ll_repair_history, R.id.tv_repair_details_start, R.id.tv_repair_details_form,
            R.id.tv_repair_details_dealed, R.id.rl_notice})
    public void onViewClicked(View view){
        switch(view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_repair_details_start_machine:
                showCustomDialog(this, R.layout.dialog_general_style, 2, getString(R.string.request_start_machine), 1);
                break;
            case R.id.tv_repair_details_stop_machine:
                showCustomDialog(this, R.layout.dialog_stop_machine, 2, getString(R.string.request_stop_machine), 2);
                break;
            case R.id.ll_repair_history:
                Bundle bundle = new Bundle();
                bundle.putString("MachineSysCode", mResultList.get(0).getMachineSysCode());
                AppUtils.goActivity(this, RepairHistoryActivity.class, bundle);
                break;
                //处理中，已付款 出现
            case R.id.tv_repair_details_start:
                toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_DEALING_BTN);
                break;
                //未处理 出现
            case R.id.tv_repair_details_form:
                Bundle bundle1 = new Bundle();
                bundle1.putString("DetailID", detailID);
                AppUtils.goActivity(this, RepairFormActivity.class, bundle1);
                break;
                //未处理 出现
            case R.id.tv_repair_details_dealed:
                showCustomDialog(this, R.layout.dialog_general_style2, 2, "", 3);
//                toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_DEALED_BTN);
                break;
        }
    }

    private void showCustomDialog(Context context, int layout, final int btnType, String content, final int tag){
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
        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        if(layout == R.layout.dialog_general_style){
            TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
            tvDetails.setText(content);
        }else{
            title.setText(content);
        }
        final EditText etDescriptions = dialog.findViewById(R.id.et_dialog_content);

        TextView cancel = dialog.findViewById(R.id.cancel_action);
        TextView confirm = dialog.findViewById(R.id.confirm_action);
        View line = dialog.findViewById(R.id.view_line);
        if(btnType == 1){
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
                if(btnType != 1){
                    if(tag == 1){
                        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE);
                    }else if(tag == 2){
                        reason = etDescriptions.getText().toString().trim();
                        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE);
                    }else if(tag == 3){
                        remark = etDescriptions.getText().toString().trim();
                        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_DEALED_BTN);
                    }
                }
            }
        });
    }

}
