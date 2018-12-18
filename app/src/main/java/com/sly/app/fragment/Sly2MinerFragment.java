package com.sly.app.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.LineChart;
import com.sly.app.R;
import com.sly.app.activity.master.MasterAccountExecActivity;
import com.sly.app.activity.master.MasterAllFreeActivity;
import com.sly.app.activity.master.MasterAllPowerActivity;
import com.sly.app.activity.master.MasterMachineListActivity;
import com.sly.app.activity.master.MasterPersonManageActivity;
import com.sly.app.activity.miner.MinerFreeActivity;
import com.sly.app.activity.miner.MinerMachineListActivity;
import com.sly.app.activity.miner.MinerRepairBillActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.master.MasterHomeRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.master.MasterAccountPermissionBean;
import com.sly.app.model.master.MasterAllDataBean;
import com.sly.app.model.master.MasterMineBean;
import com.sly.app.model.miner.MinerMachineNumBean;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.model.yunw.home.MachineNumRateInfo;
import com.sly.app.model.yunw.machine.MachineDetailsAllCalBean;
import com.sly.app.model.yunw.machine.MachineDetailsPicBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.ChartUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.CustomCircleProgressBar;
import com.sly.app.view.iviews.ICommonViewUi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

import static de.greenrobot.event.EventBus.TAG;

public class Sly2MinerFragment extends BaseFragment implements ICommonViewUi, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.ll_miner_repair)
    LinearLayout llMinerRepair;
    @BindView(R.id.tv_miner_repair_num)
    TextView tvMinerRepairNum;
    @BindView(R.id.ll_miner_free)
    LinearLayout llMinerFree;

    @BindView(R.id.rl_all_machine_count)
    RelativeLayout rlMinerMachineCount;
    @BindView(R.id.tv_miner_all_num)
    TextView tvMinerAllMachineNum;

    @BindView(R.id.tv_miner_online_num)
    TextView tvMinerOnlineNum;
    @BindView(R.id.Progress_1)
    CustomCircleProgressBar mProgressBar1;
    @BindView(R.id.tv_miner_offline_num)
    TextView tvMinerOfflineNum;
    @BindView(R.id.Progress_2)
    CustomCircleProgressBar mProgressBar2;
    @BindView(R.id.tv_miner_exception_num)
    TextView tvMinerExceptionNum;
    @BindView(R.id.Progress_3)
    CustomCircleProgressBar mProgressBar3;
    @BindView(R.id.tv_miner_stop_num)
    TextView tvMinerStopNum;
    @BindView(R.id.Progress_4)
    CustomCircleProgressBar mProgressBar4;

    @BindView(R.id.tv_miner_suanli)
    TextView tvMinerSuanli;
    @BindView(R.id.tv_miner_month)
    TextView tvMinerMonth;
    @BindView(R.id.tv_miner_hours)
    TextView tvMinerHours;
    @BindView(R.id.lc_cal_power_pic)
    LineChart lcCalPowerPic;
    @BindView(R.id.tv_begintime)
    TextView tvBeginTime;
    @BindView(R.id.tv_endtime)
    TextView tvEndTime;

    @BindView(R.id.tv_miner_rate)
    TextView tvMinerRate;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static String mContent = "???";
    private String User, LoginType, FrSysCode, FMasterCode, MineCode, PersonSysCode, Token, Key;
    private String MineName;
    ICommonRequestPresenter iCommonRequestPresenter;

    private List<MinerMachineNumBean> minerMachineList = new ArrayList<>();
    private MasterHomeRecyclerViewAdapter adapter;

    private List<MasterAllDataBean> masterAllDataList = new ArrayList<>();
    private List<MachineNumRateInfo> machineStatusList = new ArrayList<>();
    private MachineDetailsAllCalBean allCalbean;
    private List<MachineDetailsPicBean> mPic24ListBean = new ArrayList<>();
    private List<MachineDetailsPicBean> mPic30ListBean = new ArrayList<>();

    private List<MasterAccountPermissionBean> mPermissionList = new ArrayList<>();
    private PopupWindow mPopupWindow;
    private boolean isMachineZero;

    public static Sly2MinerFragment newInstance(String content) {
        Sly2MinerFragment fragment = new Sly2MinerFragment();
        mContent = content;
        return fragment;
    }

    @Override
    protected void onFirstUserVisible() {
        AppUtils.setStatusBarColor(getActivity(), getResources().getColor(R.color.sly_status_bar));
    }

    @Override
    protected void onUserVisible() {
        AppUtils.setStatusBarColor(getActivity(), getResources().getColor(R.color.sly_status_bar));
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_sly2_miner;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void initViewsAndEvents() {

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        swipeRefreshLayout.setOnRefreshListener(this);

        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");

        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");

        toRequest(NetConstant.EventTags.GET_MINER_ALL_MACHINE_COUNT);
        toRequest(NetConstant.EventTags.GET_MINER_REPAIR_NUM);
        toRequest(NetConstant.EventTags.GET_MINER_MONTH_RATE);
        toRequest(NetConstant.EventTags.GET_MINER_ALL_SUALI);
        toRequest(NetConstant.EventTags.GET_MINER_24_SUALI);
        toRequest(NetConstant.EventTags.GET_MINER_30_SUALI);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("minerSysCode", FrSysCode);

        if (eventTag == NetConstant.EventTags.GET_MINER_ALL_MACHINE_COUNT) {
            map.put("Rounter", NetConstant.GET_MINER_ALL_MACHINE_COUNT);
        } else if (eventTag == NetConstant.EventTags.GET_MINER_REPAIR_NUM) {
            map.put("Rounter", NetConstant.GET_MINER_REPAIR_NUM);
        } else if (eventTag == NetConstant.EventTags.GET_MINER_MONTH_RATE) {
            map.put("Rounter", NetConstant.GET_MINER_MONTH_RATE);
        } else if (eventTag == NetConstant.EventTags.GET_MINER_ALL_SUALI) {
            //所有算力
            map.put("Rounter", NetConstant.GET_MINER_ALL_SUALI);
        } else if (eventTag == NetConstant.EventTags.GET_MINER_24_SUALI) {
            //24小时算力
            map.put("Rounter", NetConstant.GET_MINER_24_SUALI);
        } else if (eventTag == NetConstant.EventTags.GET_MINER_30_SUALI) {
            //30天算力
            map.put("Rounter", NetConstant.GET_MINER_30_SUALI);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数 = " + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 = " + result);
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }

        if (eventTag == NetConstant.EventTags.GET_MINER_ALL_MACHINE_COUNT) {
            minerMachineList =
                    (List<MinerMachineNumBean>) AppUtils.parseRowsResult(result, MinerMachineNumBean.class);
            if (!AppUtils.isListBlank(minerMachineList)) {
                refreshListView();
            }
        } else if (eventTag == NetConstant.EventTags.GET_MINER_REPAIR_NUM) {
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                if("0".equals(returnBean.getData())){
                    tvMinerRepairNum.setVisibility(View.GONE);
                }else{
                    tvMinerRepairNum.setVisibility(View.VISIBLE);
                    int newCount = Integer.parseInt(returnBean.getData());
                    if(newCount < 10 ){
                        tvMinerRepairNum.setBackgroundDrawable(getResources().getDrawable(R.drawable.xiaoxi_kuanggong_icon));
                    }
                    else{
                        tvMinerRepairNum.setBackgroundDrawable(getResources().getDrawable(R.drawable.bigxiaoxi_kuanggong_icon));
                    }
                    tvMinerRepairNum.setText(returnBean.getData());
                }
            }
        } else if (eventTag == NetConstant.EventTags.GET_MINER_MONTH_RATE) {
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                double rate = Double.parseDouble(returnBean.getData()) * 100;
                tvMinerRate.setText(String.format("%.2f", rate) + "%");
            }
        } else if (eventTag == NetConstant.EventTags.GET_MINER_ALL_SUALI) {
            //所有算力
            allCalbean =
                    ((List<MachineDetailsAllCalBean>) AppUtils.parseRowsResult(result, MachineDetailsAllCalBean.class)).get(0);
            tvMinerSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getHoursCalcForce()) ? "0.0" : allCalbean.getHoursCalcForce()) + "T)");
        } else if (eventTag == NetConstant.EventTags.GET_MINER_24_SUALI) {
            //24小时算力
            mPic24ListBean =
                    (List<MachineDetailsPicBean>) AppUtils.parseRowsResult(result, MachineDetailsPicBean.class);
            setCalPowerPicData(lcCalPowerPic, mPic24ListBean, 24);
        } else if (eventTag == NetConstant.EventTags.GET_MINER_30_SUALI) {
            //30天算力
            mPic30ListBean =
                    (List<MachineDetailsPicBean>) AppUtils.parseRowsResult(result, MachineDetailsPicBean.class);
            Collections.reverse(mPic30ListBean);
        }
    }

    private void setCalPowerPicData(LineChart lineChart, List<MachineDetailsPicBean> list, int tag) {
        lineChart.clear();
        ChartUtil.initCalPowerPic(mContext, lineChart, list);
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
                    tvEndTime.setText(begin);
                }
                if (i == (list.size() - 1)) {
                    String endTime = list.get(i).getDataTime();
                    String etyear = endTime.split(" ")[0];
                    String end = etyear.substring(5, etyear.length());
                    tvBeginTime.setText(end);
                }
            }
        }
    }

    public void refreshListView() {
        MinerMachineNumBean bean = minerMachineList.get(0);
        int allCount = bean.getCount();
        int onlineCount = bean.getOnLineCount();
        int offlineCount = bean.getOffLineCount();
        int exceptionCount = bean.getAbnormalCount();
        int stopCount = bean.getDowntimeCount();

        if(allCount == 0){
            isMachineZero = true;
        }else{
            isMachineZero = false;
        }

        tvMinerAllMachineNum.setText(allCount + "");
        tvMinerOnlineNum.setText(onlineCount + "");
        tvMinerOfflineNum.setText(offlineCount + "");
        tvMinerExceptionNum.setText(exceptionCount + "");
        tvMinerStopNum.setText(stopCount + "");


        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);

        String onlineRate = numberFormat.format((float) onlineCount / (float) allCount);
        String offlineRate = numberFormat.format((float) offlineCount / (float) allCount);
        String exceptionRate = numberFormat.format((float) exceptionCount / (float) allCount);
        String stopRate = numberFormat.format((float) stopCount / (float) allCount);

        setPorgressInfo(mProgressBar1, onlineRate);
        setPorgressInfo(mProgressBar2, offlineRate);
        setPorgressInfo(mProgressBar3, exceptionRate);
        setPorgressInfo(mProgressBar4, stopRate);
    }

    private void setPorgressInfo(CustomCircleProgressBar progressBar, String runRate) {

        double rate = Double.parseDouble(runRate) * 100;
        if (Math.round(rate) - rate == 0) {
            progressBar.setProgress((int) rate);
        } else {
            DecimalFormat df = new DecimalFormat(".#");
            String run = df.format(rate);
            progressBar.setProgress(Float.parseFloat(run));
        }
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {

    }

    @OnClick({R.id.ll_miner_repair, R.id.ll_miner_free, R.id.rl_online_machine, R.id.rl_offline_machine,
            R.id.rl_exception_machine, R.id.rl_stop_machine, R.id.rl_all_machine_count, R.id.tv_miner_hours, R.id.tv_miner_month})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_miner_repair:
                if(isMachineZero){
                    ToastUtils.showToast(getString(R.string.no_machine_num));
                }else{
                    AppUtils.goActivity(mContext, MinerRepairBillActivity.class);
                }
                break;
            case R.id.ll_miner_free:
                AppUtils.goActivity(mContext, MinerFreeActivity.class);
                break;
            case R.id.rl_all_machine_count:
                Bundle bundle = new Bundle();
                AppUtils.goActivity(mContext, MinerMachineListActivity.class, bundle);
                break;
            //百分比状态点击
            case R.id.rl_online_machine:
                Bundle bundle1 = new Bundle();
                bundle1.putString("statusCode", "00");
                AppUtils.goActivity(mContext, MinerMachineListActivity.class, bundle1);
                break;
            case R.id.rl_offline_machine:
                Bundle bundle2 = new Bundle();
                bundle2.putString("statusCode", "01");
                AppUtils.goActivity(mContext, MinerMachineListActivity.class, bundle2);
                break;
            case R.id.rl_exception_machine:
                Bundle bundle3 = new Bundle();
                bundle3.putString("statusCode", "02");
                AppUtils.goActivity(mContext, MinerMachineListActivity.class, bundle3);
                break;
            case R.id.rl_stop_machine:
                Bundle bundle4 = new Bundle();
                bundle4.putString("statusCode", "05");
                AppUtils.goActivity(mContext, MinerMachineListActivity.class, bundle4);
                break;
            case R.id.tv_miner_hours:
                tvMinerSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getHoursCalcForce()) ? "0.0" : allCalbean.getHoursCalcForce()) + "T)");
                setBgAndTextColor(1);
                setCalPowerPicData(lcCalPowerPic, mPic24ListBean, 24);
                break;
            case R.id.tv_miner_month:
                tvMinerSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getMonthCalcForce()) ? "0.0" : allCalbean.getMonthCalcForce()) + "T)");
                setBgAndTextColor(2);
                setCalPowerPicData(lcCalPowerPic, mPic30ListBean, 30);
                break;
        }
    }

    private void setBgAndTextColor(int btnTag) {
        if (btnTag == 1) {
            tvMinerHours.setBackground(getResources().getDrawable(R.drawable.layer_left_circle_blue_15dp));
            tvMinerHours.setTextColor(getResources().getColor(R.color.white));
            tvMinerMonth.setBackground(getResources().getDrawable(R.drawable.shape_right_circle_white_15dp));
            tvMinerMonth.setTextColor(getResources().getColor(R.color.sly_text_666666));
        } else {
            tvMinerHours.setBackground(getResources().getDrawable(R.drawable.layer_left_circle_white_15dp));
            tvMinerHours.setTextColor(getResources().getColor(R.color.sly_text_666666));
            tvMinerMonth.setBackground(getResources().getDrawable(R.drawable.shape_right_circle_blue_15dp));
            tvMinerMonth.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onRefresh() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            ToastUtils.showToast(Contants.NetStatus.NETDISABLE);
            return;
        }
        toRequest(NetConstant.EventTags.GET_MINER_ALL_MACHINE_COUNT);
        toRequest(NetConstant.EventTags.GET_MINER_REPAIR_NUM);
        toRequest(NetConstant.EventTags.GET_MINER_MONTH_RATE);
        toRequest(NetConstant.EventTags.GET_MINER_ALL_SUALI);
        toRequest(NetConstant.EventTags.GET_MINER_24_SUALI);
        toRequest(NetConstant.EventTags.GET_MINER_30_SUALI);
    }
}
