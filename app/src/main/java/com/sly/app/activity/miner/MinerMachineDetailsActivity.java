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
import com.github.mikephil.charting.charts.LineChart;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.yunw.machine.MachineChangePoolActivity;
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

public class MinerMachineDetailsActivity extends BaseActivity implements ICommonViewUi{

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.ll_miner_details_history_free)
    LinearLayout llHistoryFree;

    @BindView(R.id.miner_model)
    TextView tvDetailsMachineModel;
    @BindView(R.id.miner_machine_code)
    TextView tvDetailsMachineCode;
    @BindView(R.id.miner_details_status)
    TextView tvDetailsStatus;
    @BindView(R.id.miner_details_mine_name)
    TextView tvDetailsMineName;
    @BindView(R.id.miner_details_month_rate)
    TextView tvDetailsMonthRate;

    @BindView(R.id.tv_miner_suanli)
    TextView tvDetailsAllSuanli;
    @BindView(R.id.tv_miner_hours)
    TextView btnDetailsHours;
    @BindView(R.id.tv_miner_month)
    TextView btnDetailsMonth;
    @BindView(R.id.lc_cal_power_pic)
    LineChart lcCalPowerPic;
    @BindView(R.id.tv_begintime)
    TextView tvBeginTime;
    @BindView(R.id.tv_endtime)
    TextView tvEndTime;

    @BindView(R.id.tv_machine_details_mine1)
    TextView tvDeatailsMine1;
    @BindView(R.id.tv_machine_details_miner1_code)
    TextView tvDetailsMiner1Code;

    @BindView(R.id.tv_machine_details_mine2)
    TextView tvDeatailsMine2;
    @BindView(R.id.tv_machine_details_miner2_code)
    TextView tvDetailsMiner2Code;

    @BindView(R.id.tv_machine_details_mine3)
    TextView tvDeatailsMine3;
    @BindView(R.id.tv_machine_details_miner3_code)
    TextView tvDetailsMiner3Code;

    //    private List<RepairBillDetailsBean> mResultList = new ArrayList<RepairBillDetailsBean>();
    ICommonRequestPresenter iCommonRequestPresenter;

    private String User, LoginType, MineCode, PersonSysCode, Token, Key, machineSysCode;
    private String reason = "";
    private MachineDetailsAllCalBean allCalbean;
    private List<MachineDetailsPicBean> mPic24ListBean = new ArrayList<>();
    private List<MachineDetailsPicBean> mPic30ListBean = new ArrayList<>();
    private boolean isMaster = false;
    private List<MachineDetailsHeaderBean> HeaderBean;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_miner_details;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        MineCode = getIntent().getExtras().getString("MineCode");
        machineSysCode = getIntent().getExtras().getString("machineSysCode");
//        isMaster = getIntent().getExtras().getBoolean("isMaster");

        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
//        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
//        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");

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
            HeaderBean =
                    (List<MachineDetailsHeaderBean>) AppUtils.parseRowsResult(result, MachineDetailsHeaderBean.class);

            setDataToHeader(HeaderBean.get(0));
        } else if (eventTag == NetConstant.EventTags.GET_MACHINE_DEATAILS_MINE_POOL) {
            // 矿池
            List<MachineDetailsPoolBean> poolList =
                    (List<MachineDetailsPoolBean>) AppUtils.parseRowsResult(result, MachineDetailsPoolBean.class);
            setDataToPool(poolList);
        }  else if (eventTag == NetConstant.EventTags.GET_MACHINE_DETAILS_ALL_SUANLI) {
            //所有算力
            allCalbean =
                    ((List<MachineDetailsAllCalBean>) AppUtils.parseRowsResult(result, MachineDetailsAllCalBean.class)).get(0);
            tvDetailsAllSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getHoursCalcForce()) ? "0.0" : allCalbean.getHoursCalcForce()) + "T)");
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

        tvDetailsMachineModel.setText(bean.getModel());
        tvDetailsMachineCode.setText(bean.getMachineCode());
        tvDetailsMineName.setText(bean.getMineName());
        tvMainTitle.setText(bean.getModel());
        if (bean.getStatusName().contains("离线")) {
            tvDetailsStatus.setText(bean.getStatusName().substring(0, 2));
            tvDetailsStatus.setTextColor(getResources().getColor(R.color.color_fe7ea8));
            tvDetailsStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_5dp));
        } else if (bean.getStatusName().contains("在线")) {
            tvDetailsStatus.setText(bean.getStatusName());
            tvDetailsStatus.setTextColor(getResources().getColor(R.color.color_27ae0c));
            tvDetailsStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_green_5dp));
        } else if (bean.getStatusName().contains("算力异常")) {
            tvDetailsStatus.setText(bean.getStatusName());
            tvDetailsStatus.setTextColor(getResources().getColor(R.color.color_57c2ff));
            tvDetailsStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_blues_5dp));
        } else if (bean.getStatusName().contains("停机")) {
            tvDetailsStatus.setText(bean.getStatusName());
            tvDetailsStatus.setTextColor(getResources().getColor(R.color.color_57c2ff));
            tvDetailsStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_blues_5dp));

        }
        double rate = Double.parseDouble(bean.getRunRate()) * 100;
        tvDetailsMonthRate.setText(String.format("%.2f", rate) + "%");
    }

    private void setDataToPool(List<MachineDetailsPoolBean> poolList) {
        MachineDetailsPoolBean bean1 = poolList.get(0);
        tvDeatailsMine1.setText(bean1.getURL());
        tvDetailsMiner1Code.setText(bean1.getWorker());

        MachineDetailsPoolBean bean2 = poolList.get(1);
        tvDeatailsMine2.setText(bean2.getURL());
        tvDetailsMiner2Code.setText(bean2.getWorker());

        MachineDetailsPoolBean bean3 = poolList.get(2);
        tvDeatailsMine3.setText(bean3.getURL());
        tvDetailsMiner3Code.setText(bean3.getWorker());
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

    @OnClick({R.id.btn_main_back, R.id.rl_notice, R.id.tv_miner_hours,
            R.id.tv_miner_month, R.id.ll_miner_details_history_free})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.ll_miner_details_history_free:
                Bundle bundle = new Bundle();
                bundle.putString("machineSysCode", HeaderBean.get(0).getMachineSysCode());
                bundle.putString("machineCode", HeaderBean.get(0).getMachineCode());
                bundle.putString("Model", HeaderBean.get(0).getModel());
                AppUtils.goActivity(this, MinerHistoryFreeActivity.class, bundle);
                break;
            case R.id.tv_miner_hours:
                tvDetailsAllSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getHoursCalcForce()) ? "0.0" : allCalbean.getHoursCalcForce()) + "T)");
                setBgAndTextColor(1);
                setCalPowerPicData(lcCalPowerPic, mPic24ListBean, 24);
                break;
            case R.id.tv_miner_month:
                tvDetailsAllSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getMonthCalcForce()) ? "0.0" : allCalbean.getMonthCalcForce()) + "T)");
                setBgAndTextColor(2);
                setCalPowerPicData(lcCalPowerPic, mPic30ListBean, 30);
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
}
