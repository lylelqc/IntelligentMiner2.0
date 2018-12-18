package com.sly.app.activity.yunw.machine;

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
import com.github.mikephil.charting.charts.LineChart;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.yunw.repair.RepairHistoryActivity;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.yunw.machine.MachineDetailsAllCalBean;
import com.sly.app.model.yunw.machine.MachineDetailsHeaderBean;
import com.sly.app.model.yunw.machine.MachineDetailsPicBean;
import com.sly.app.model.yunw.machine.MachineDetailsPoolBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.ChartUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MachineDetailsActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.tv_machine_details_status)
    TextView tvDetailsStatus;
    @BindView(R.id.tv_machine_details_ip)
    TextView tvDetailsIP;
    @BindView(R.id.tv_machine_details_start_stop)
    TextView tvDetailsStartStop;
    @BindView(R.id.tv_machine_details_area)
    TextView tvDetailsArea;
    @BindView(R.id.tv_details_month_rate)
    TextView tvDetailsMonthRateText;
    @BindView(R.id.tv_machine_details_monthrate)
    TextView tvDetailsMonthRate;
    @BindView(R.id.tv_machine_details_vip_code)
    TextView tvDetailsVIPCode;
    @BindView(R.id.tv_machine_details_btc)
    TextView tvDetailsBtc;

    @BindView(R.id.tv_machine_details_suanli)
    TextView tvDetailsSuanli;
    @BindView(R.id.btn_machine_details_hours)
    TextView btnDetailsHours;
    @BindView(R.id.btn_machine_details_month)
    TextView btnDetailsMonth;
    @BindView(R.id.lc_cal_power_pic)
    LineChart lcCalPowerPic;
    @BindView(R.id.tv_begintime)
    TextView tvBeginTime;
    @BindView(R.id.tv_endtime)
    TextView tvEndTime;

    @BindView(R.id.rl_machine_details_history)
    RelativeLayout rlDetailsHistory;
    @BindView(R.id.tv_shadow)
    View tvShadow;

    @BindView(R.id.rl_machine_details_change)
    RelativeLayout rlDeatailsChange;


    @BindView(R.id.tv_machine_details_mine1)
    TextView tvDeatailsMine1;
    @BindView(R.id.tv_machine_details_miner1_code)
    TextView tvDetailsMiner1Code;
    @BindView(R.id.tv_machine_details_pwd1)
    TextView tvDeatailsPwd1;

    @BindView(R.id.tv_machine_details_mine2)
    TextView tvDeatailsMine2;
    @BindView(R.id.tv_machine_details_miner2_code)
    TextView tvDetailsMiner2Code;
    @BindView(R.id.tv_machine_details_pwd2)
    TextView tvDeatailsPwd2;

    @BindView(R.id.tv_machine_details_mine3)
    TextView tvDeatailsMine3;
    @BindView(R.id.tv_machine_details_miner3_code)
    TextView tvDetailsMiner3Code;
    @BindView(R.id.tv_machine_details_pwd3)
    TextView tvDeatailsPwd3;

    //    private List<RepairBillDetailsBean> mResultList = new ArrayList<RepairBillDetailsBean>();
    ICommonRequestPresenter iCommonRequestPresenter;

    private String User, LoginType, MineCode, PersonSysCode, Token, Key, machineSysCode;
    private String reason = "";
    private MachineDetailsAllCalBean allCalbean;
    private List<MachineDetailsPicBean> mPic24ListBean = new ArrayList<>();
    private List<MachineDetailsPicBean> mPic30ListBean = new ArrayList<>();
    private boolean isMaster = false;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_machine_details;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        machineSysCode = getIntent().getExtras().getString("machineSysCode");
        isMaster = getIntent().getExtras().getBoolean("isMaster");

        if(isMaster){
            tvDetailsMonthRateText.setText(getString(R.string.machine_ip));
            tvDetailsStartStop.setVisibility(View.GONE);
            rlDeatailsChange.setVisibility(View.GONE);
            rlDetailsHistory.setVisibility(View.GONE);
            tvShadow.setVisibility(View.GONE);
        }

        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");

        intitNewsCount();

        toRequest(NetConstant.EventTags.GET_MACHINE_DEATAILS_HEADER);
        toRequest(NetConstant.EventTags.GET_MACHINE_DEATAILS_MINE_POOL);
        toRequest(NetConstant.EventTags.GET_MACHINE_DETAILS_ALL_SUANLI);
        toRequest(NetConstant.EventTags.GET_MACHINE_DETAILS_24_SUANLI);
        toRequest(NetConstant.EventTags.GET_MACHINE_DETAILS_30_SUANLI);
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
    protected void onResume() {
        super.onResume();
        toRequest(NetConstant.EventTags.GET_MACHINE_DEATAILS_MINE_POOL);
    }

    @Override
    public void toRequest(int eventTag) {

        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        if (eventTag == NetConstant.EventTags.GET_MACHINE_DEATAILS_HEADER) {
            // 头部信息
            map.put("Rounter", NetConstant.GET_MACHINE_DEATAILS_HEADER);
            map.put("machineSysCode", machineSysCode);
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DEATAILS_MINE_POOL) {
            // 矿池
            map.put("Rounter", NetConstant.GET_MACHINE_DEATAILS_MINE_POOL);
            map.put("machineSysCode", machineSysCode);
        } else if (eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE) {
            //重启设备
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_START_MACHINE);
            map.put("personSysCode", PersonSysCode);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", machineSysCode + ",");
            map.put("reason", getString(R.string.machine_start));
        } else if (eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE) {
            //设备停机
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_STOP_MACHINE);
            map.put("personSysCode", PersonSysCode);
            map.put("machineSysCode", machineSysCode + ",");
            map.put("mineCode", MineCode);
            map.put("reason", reason);
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DETAILS_ALL_SUANLI) {
            //所有算力
            map.put("Rounter", NetConstant.GET_MACHINE_DETAILS_ALL_SUANLI);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", machineSysCode);
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DETAILS_24_SUANLI) {
            //24小时算力
            map.put("Rounter", NetConstant.GET_MACHINE_DETAILS_24_SUANLI);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", machineSysCode);
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DETAILS_30_SUANLI) {
            //30天算力
            map.put("Rounter", NetConstant.GET_MACHINE_DETAILS_30_SUANLI);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", machineSysCode);
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
        if (eventTag == NetConstant.EventTags.GET_MACHINE_DEATAILS_HEADER) {
            // 头部信息
            List<MachineDetailsHeaderBean> bean =
                    (List<MachineDetailsHeaderBean>) AppUtils.parseRowsResult(result, MachineDetailsHeaderBean.class);

            setDataToHeader(bean.get(0));
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DEATAILS_MINE_POOL) {
            // 矿池
            List<MachineDetailsPoolBean> poolList =
                    (List<MachineDetailsPoolBean>) AppUtils.parseRowsResult(result, MachineDetailsPoolBean.class);
            setDataToPool(poolList);
        } else if (eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE
                || eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE) {
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                ToastUtils.showToast(getString(R.string.comfirm_success));
                toRequest(NetConstant.EventTags.GET_MACHINE_DEATAILS_HEADER);
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DETAILS_ALL_SUANLI) {
            //所有算力
            allCalbean =
                    ((List<MachineDetailsAllCalBean>) AppUtils.parseRowsResult(result, MachineDetailsAllCalBean.class)).get(0);
            tvDetailsSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getHoursCalcForce()) ? "0.0" : allCalbean.getHoursCalcForce()) + "T)");
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DETAILS_24_SUANLI) {
            //24小时算力
            mPic24ListBean =
                    (List<MachineDetailsPicBean>) AppUtils.parseRowsResult(result, MachineDetailsPicBean.class);
            Collections.reverse(mPic24ListBean);
            setCalPowerPicData(lcCalPowerPic, mPic24ListBean, 24);
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DETAILS_30_SUANLI) {
            //30天算力
            mPic30ListBean =
                    (List<MachineDetailsPicBean>) AppUtils.parseRowsResult(result, MachineDetailsPicBean.class);
            Collections.reverse(mPic30ListBean);
        }
    }

    private void setDataToHeader(MachineDetailsHeaderBean bean) {
        tvMainTitle.setText(bean.getModel());
        if (bean.getStatusName().contains("离线")) {
            tvDetailsStatus.setText(bean.getStatusName().substring(0, 2));
            tvDetailsStatus.setTextColor(getResources().getColor(R.color.color_fb3a2d));
            tvDetailsStartStop.setText(getString(R.string.machine_stop));
        } else if (bean.getStatusName().contains("在线")) {
            tvDetailsStatus.setText(bean.getStatusName());
            tvDetailsStatus.setTextColor(getResources().getColor(R.color.color_27ae0c));
            tvDetailsStartStop.setText(getString(R.string.machine_stop));
        } else if (bean.getStatusName().contains("算力异常")) {
            tvDetailsStatus.setText(bean.getStatusName());
            tvDetailsStatus.setTextColor(getResources().getColor(R.color.color_f6a800));
            tvDetailsStartStop.setText(getString(R.string.machine_stop));
        } else if (bean.getStatusName().contains("停机")) {
            tvDetailsStatus.setText(bean.getStatusName());
            tvDetailsStatus.setTextColor(getResources().getColor(R.color.color_777777));
            tvDetailsStartStop.setText(getString(R.string.machine_start));
        }

        double rate = Double.parseDouble(bean.getRunRate()) * 100;
        if (isMaster){
            tvDetailsIP.setText(getString(R.string.machine_month_rate) + String.format("%.2f", rate) + "%");
            tvDetailsMonthRate.setText(bean.getIP());
        }else{
            tvDetailsIP.setText(getString(R.string.machine_ip) + bean.getIP());
            tvDetailsMonthRate.setText(String.format("%.2f", rate) + "%");
        }

        tvDetailsArea.setText(bean.getAreaName());
        tvDetailsVIPCode.setText(bean.getMinerSysCode());
        tvDetailsBtc.setText(bean.getCoin());
    }

    private void setDataToPool(List<MachineDetailsPoolBean> poolList) {
        MachineDetailsPoolBean bean1 = poolList.get(0);
        tvDeatailsMine1.setText(bean1.getURL());
        tvDetailsMiner1Code.setText(bean1.getWorker());
        tvDeatailsPwd1.setText(bean1.getPasswords());

        MachineDetailsPoolBean bean2 = poolList.get(1);
        tvDeatailsMine2.setText(bean2.getURL());
        tvDetailsMiner2Code.setText(bean2.getWorker());
        tvDeatailsPwd2.setText(bean2.getPasswords());

        MachineDetailsPoolBean bean3 = poolList.get(2);
        tvDeatailsMine3.setText(bean3.getURL());
        tvDetailsMiner3Code.setText(bean3.getWorker());
        tvDeatailsPwd3.setText(bean3.getPasswords());
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

    @OnClick({R.id.btn_main_back, R.id.tv_machine_details_start_stop, R.id.rl_machine_details_change,
            R.id.rl_machine_details_history, R.id.rl_notice, R.id.btn_machine_details_hours, R.id.btn_machine_details_month})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.rl_machine_details_history:
                Bundle bundle1 = new Bundle();
                bundle1.putString("MachineSysCode", machineSysCode);
                AppUtils.goActivity(this, RepairHistoryActivity.class, bundle1);
                break;
            case R.id.tv_machine_details_start_stop:
                String btnStatus = tvDetailsStartStop.getText().toString().trim();
                if (btnStatus.equals(getString(R.string.machine_stop))) {
                    showCustomDialog(this, R.layout.dialog_stop_machine, 2, getString(R.string.request_stop_machine), 2);
                } else {
                    showCustomDialog(this, R.layout.dialog_general_style, 2, getString(R.string.request_start_machine), 1);
                }
                break;
            case R.id.btn_machine_details_hours:
                tvDetailsSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getHoursCalcForce()) ? "0.0" : allCalbean.getHoursCalcForce()) + "T)");
                setBgAndTextColor(1);
                setCalPowerPicData(lcCalPowerPic, mPic24ListBean, 24);
                break;
            case R.id.btn_machine_details_month:
                tvDetailsSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getMonthCalcForce()) ? "0.0" : allCalbean.getMonthCalcForce()) + "T)");
                setBgAndTextColor(2);
                setCalPowerPicData(lcCalPowerPic, mPic30ListBean, 30);
                break;
            case R.id.rl_machine_details_change:
                // 注意 - s
                Bundle bundle2 = new Bundle();
                bundle2.putString("machineSysCodes", machineSysCode + ",");
                AppUtils.goActivity(this, MachineChangePoolActivity.class, bundle2);
                break;
        }
    }

    private void setCalPowerPicData(LineChart lineChart, List<MachineDetailsPicBean> list, int tag) {
        lineChart.clear();
        ChartUtil.initCalPowerPic(this, lineChart, list);
        formatXValues(list, tag);
        ChartUtil.showLineChart(list, lineChart, this.getResources().getColor(R.color.sly_text_4a96f2),
                this.getResources().getColor(R.color.sly_bg_f5fbff));
    }

    private void formatXValues(List<MachineDetailsPicBean> list, int tag) {
        if (tag == 24) {
            tvBeginTime.setText("00:00");
            tvEndTime.setText("24:00");
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    String beginTime = list.get(i).getDataTime();
                    String btyear = beginTime.split(" ")[0];

                    String begin = btyear.substring(5, btyear.length());
                    tvBeginTime.setText(begin);
                }
                if (i == (list.size() - 1)) {
                    String endTime = list.get(i).getDataTime();
                    String etyear = endTime.split(" ")[0];
                    String end = etyear.substring(5, etyear.length());
                    tvEndTime.setText(end);
                }
            }

        }

    }

    private void setBgAndTextColor(int btnTag) {
        if (btnTag == 1) {
            btnDetailsHours.setBackground(getResources().getDrawable(R.drawable.layer_left_circle_blue_15dp));
            btnDetailsHours.setTextColor(getResources().getColor(R.color.white));
            btnDetailsMonth.setBackground(getResources().getDrawable(R.drawable.shape_right_circle_white_15dp));
            btnDetailsMonth.setTextColor(getResources().getColor(R.color.sly_text_666666));
        } else {
            btnDetailsHours.setBackground(getResources().getDrawable(R.drawable.layer_left_circle_white_15dp));
            btnDetailsHours.setTextColor(getResources().getColor(R.color.sly_text_666666));
            btnDetailsMonth.setBackground(getResources().getDrawable(R.drawable.shape_right_circle_blue_15dp));
            btnDetailsMonth.setTextColor(getResources().getColor(R.color.white));
        }
    }

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

        //获取控件
        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        if (layout == R.layout.dialog_general_style) {
            TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
            tvDetails.setText(content);
        }
        else{
            title.setText(content);
        }
        final EditText etDescriptions = dialog.findViewById(R.id.et_dialog_content);

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
                        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE);
                    } else if (tag == 2) {
                        reason = etDescriptions.getText().toString().trim();
                        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE);
                    }
                }
            }
        });
    }
}
