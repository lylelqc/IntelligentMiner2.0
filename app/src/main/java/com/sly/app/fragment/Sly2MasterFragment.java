package com.sly.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.LineChart;
import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.yunw.ClockWorkActivity;
import com.sly.app.activity.yunw.changepool.MachineSetPoolActivity;
import com.sly.app.activity.yunw.goline.MachineGolineActivity;
import com.sly.app.activity.yunw.machine.MachineManageActivity;
import com.sly.app.activity.yunw.offline.MachineOfflineActivity;
import com.sly.app.activity.yunw.repair.RepairBillActivity;
import com.sly.app.adapter.master.MasterAllDataBean;
import com.sly.app.adapter.master.MasterHomeRecyclerViewAdapter;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.master.MasterMineBean;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.model.yunw.home.MachineNumRateInfo;
import com.sly.app.model.yunw.home.MineAreaInfo;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.CustomCircleProgressBar;
import com.sly.app.view.iviews.ICommonViewUi;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class Sly2MasterFragment extends BaseFragment implements ICommonViewUi, MasterHomeRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.rl_user_type)
    RelativeLayout rlUserType;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.tv_user_account)
    TextView tvUserAccount;

    //矿场主导航栏
    @BindView(R.id.tv_master_add)
    TextView tvMasterAdd;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.recycler_view)
    RecyclerView rvMasterMineArea;
    @BindView(R.id.tv_master_all_free)
    TextView tvMasterAllFree;
    @BindView(R.id.tv_master_all_power_consumption)
    TextView tvMasterAllpowerConsumption;
    @BindView(R.id.tv_master_all_run_rate)
    TextView tvMasterAllRunRate;

    @BindView(R.id.tv_master_all_num)
    TextView tvMasterAllMachineNum;

    @BindView(R.id.tv_master_online_num)
    TextView tvMasterOnlineNum;
    @BindView(R.id.Progress_1)
    CustomCircleProgressBar mProgressBar1;
    @BindView(R.id.tv_master_offline_num)
    TextView tvMasterOfflineNum;
    @BindView(R.id.Progress_2)
    CustomCircleProgressBar mProgressBar2;
    @BindView(R.id.tv_master_exception_num)
    TextView tvMasterExceptionNum;
    @BindView(R.id.Progress_3)
    CustomCircleProgressBar mProgressBar3;
    @BindView(R.id.tv_master_stop_num)
    TextView tvMasterStopNum;
    @BindView(R.id.Progress_4)
    CustomCircleProgressBar mProgressBar4;

    @BindView(R.id.tv_master_suanli)
    TextView tvMasterSuanli;
    @BindView(R.id.tv_master_month)
    TextView tvMasterMonth;
    @BindView(R.id.tv_master_hours)
    TextView tvMasterHours;
    @BindView(R.id.lc_cal_power_pic)
    LineChart lcCalPowerPic;
    @BindView(R.id.tv_begintime)
    TextView tvBeginTime;
    @BindView(R.id.tv_endtime)
    TextView tvEndTime;

    @BindView(R.id.rl_master_person_manage)
    RelativeLayout rlMasterPersonManage;
    @BindView(R.id.rl_master_parts)
    RelativeLayout rlMasterParts;

    public static String mContent = "???";
    private String User, LoginType, FrSysCode, FMasterCode, MineCode, PersonSysCode, Token, Key;
    ICommonRequestPresenter iCommonRequestPresenter;

    private List<MasterMineBean> masterMineList = new ArrayList<>();
    private MasterHomeRecyclerViewAdapter adapter;

    private List<MasterAllDataBean> masterAllDataList = new ArrayList<>();
    private List<MachineNumRateInfo> machineStatusList = new ArrayList<>();


    public static Sly2MasterFragment newInstance(String content) {
        Sly2MasterFragment fragment = new Sly2MasterFragment();
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

        return R.layout.fragment_sly2_master;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void initViewsAndEvents() {

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");

        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "None");

        toRequest(NetConstant.EventTags.GET_MASTER_MOBILE);
        toRequest(NetConstant.EventTags.GET_MASTER_MINE_LIST);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        if (eventTag == NetConstant.EventTags.GET_MASTER_MINE_LIST) {
            map.put("Rounter", NetConstant.GET_MASTER_MINE_LIST);
            map.put("masterSysCode", FMasterCode);
        }
        else if (eventTag == NetConstant.EventTags.GET_MASTER_ALL_DATA) {
            map.put("Rounter", NetConstant.GET_MASTER_ALL_DATA);
            map.put("mineCode", MineCode);
        }
        else if (eventTag == NetConstant.EventTags.GET_MASTER_NUM_RATE) {
            map.put("Rounter", NetConstant.GET_MASTER_NUM_RATE);
            map.put("mineCode", MineCode);
        }
        else if (eventTag == NetConstant.EventTags.GET_MASTER_MOBILE) {
            map.put("Rounter", NetConstant.GET_MASTER_MOBILE);
            map.put("masterSysCode", FMasterCode);
        }
        /*else if (eventTag == NetConstant.EventTags.GET_YUNW_NEWS_COUNT) {
            map.put("Rounter", NetConstant.GET_YUNW_NEWS_COUNT);
            map.put("personSysCode", PersonSysCode);
        }*/

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 = " + result);
        if (eventTag == NetConstant.EventTags.GET_MASTER_MINE_LIST) {
            masterMineList =
                    (List<MasterMineBean>) AppUtils.parseRowsResult(result, MasterMineBean.class);
            refreshListView();
            if(masterMineList != null && masterMineList.size() > 0){
                MineCode = masterMineList.get(0).getMineCode();
                toRequest(NetConstant.EventTags.GET_MASTER_ALL_DATA);
                toRequest(NetConstant.EventTags.GET_MASTER_NUM_RATE);
            }
        }
        else if (eventTag == NetConstant.EventTags.GET_MASTER_ALL_DATA) {
            masterAllDataList =
                    (List<MasterAllDataBean>) AppUtils.parseResult(result, MasterAllDataBean.class);
            MasterAllDataBean bean = masterAllDataList.get(0);
            tvMasterAllFree.setText(bean.getMonthExpenses());
            tvMasterAllpowerConsumption.setText(bean.getMonthPower());
            double rate = Double.parseDouble(bean.getMonthRunRate()) * 100;
            tvMasterAllRunRate.setText(rate + "");
            tvMasterAllMachineNum.setText(bean.getMachineCount());
        }
        else if (eventTag == NetConstant.EventTags.GET_MASTER_NUM_RATE) {
            machineStatusList = (List<MachineNumRateInfo>) AppUtils.parseResult(result, MachineNumRateInfo.class);
            if(machineStatusList != null && machineStatusList.size() > 0){
                setPorgressInfo();
            }
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_MOBILE) {
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                String mobile = returnBean.getData();
                String maskNumber = mobile.substring(0, 3)+"****"+mobile.substring(7, mobile.length());
                tvUserAccount.setText(maskNumber);
            }

        } /*else if (eventTag == NetConstant.EventTags.GET_MASTER_MOBILE) {
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {

                int count = Integer.parseInt(returnBean.getData());
                if (count == 0) {
                    tvRedNum.setVisibility(View.GONE);
                } else if (count > 99) {
                    tvRedNum.setText("99+");
                    tvRedNum.setVisibility(View.VISIBLE);
                } else {
                    tvRedNum.setText(returnBean.getData());
                    tvRedNum.setVisibility(View.VISIBLE);
                }
                SharedPreferencesUtil.putString(mContext, "NewsCount", returnBean.getData());
            }
        }*/
    }

    public void refreshListView() {
        adapter = new MasterHomeRecyclerViewAdapter(mContext, masterMineList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        rvMasterMineArea.setLayoutManager(layoutManager);
    }

    private void setPorgressInfo() {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_DOWN);

        for(int i = 0; i < machineStatusList.size(); i++){
            MachineNumRateInfo info = machineStatusList.get(i);
            // 在线
            if("00".equals(info.getStatusCode())){
                tvMasterOnlineNum.setText(info.getMachineCount()+"");
                String rate1 = df.format(info.getRate()*100);
                mProgressBar1.setProgress(rate1.contains(".") ? Float.parseFloat(rate1) : Integer.parseInt(rate1));
            }
            //离线
            else if("01".equals(info.getStatusCode())){
                tvMasterOfflineNum.setText(info.getMachineCount()+"");
                String rate2 = df.format(info.getRate()*100);
                mProgressBar2.setProgress(rate2.contains(".") ? Float.parseFloat(rate2) : Integer.parseInt(rate2));
            }
            // 算力异常
            else if("02".equals(info.getStatusCode())){
                tvMasterExceptionNum.setText(info.getMachineCount()+"");
                String rate3 = df.format(info.getRate()*100);
                mProgressBar3.setProgress(rate3.contains(".") ? Float.parseFloat(rate3) : Integer.parseInt(rate3));
            }
            // 停机
            else if("05".equals(info.getStatusCode())){
                tvMasterStopNum.setText(info.getMachineCount()+"");
                String rate4 = df.format(info.getRate()*100);
                mProgressBar4.setProgress(rate4.contains(".") ? Float.parseFloat(rate4) : Integer.parseInt(rate4));
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

    @OnClick({R.id.tv_master_add,R.id.rl_notice, R.id.rl_online_machine,
            R.id.rl_offline_machine,R.id.rl_exception_machine,R.id.rl_stop_machine,
            R.id.rl_all_machine_count, R.id.rl_master_person_manage, R.id.rl_master_parts})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_operation_manage:
                AppUtils.goActivity(mContext, MachineManageActivity.class);
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(mContext, Sly2NoticeActivity.class);
                break;
            case R.id.rl_all_machine_count:
//                AppUtils.goActivity(mContext, MachineListActivity.class, new Bundle());
                break;
            //百分比状态点击
            case R.id.rl_online_machine:
                Bundle bundle1 = new Bundle();
                bundle1.putString("statusCode", "00");
//                AppUtils.goActivity(mContext, MachineListActivity.class, bundle1);
                break;
            case R.id.rl_offline_machine:
                Bundle bundle2 = new Bundle();
                bundle2.putString("statusCode", "01");
//                AppUtils.goActivity(mContext, MachineListActivity.class, bundle2);
                break;
            case R.id.rl_exception_machine:
                Bundle bundle3 = new Bundle();
                bundle3.putString("statusCode", "02");
//                AppUtils.goActivity(mContext, MachineListActivity.class, bundle3);
                break;
            case R.id.rl_stop_machine:
                Bundle bundle4 = new Bundle();
                bundle4.putString("statusCode", "05");
//                AppUtils.goActivity(mContext, MachineListActivity.class, bundle4);
                break;
            case R.id.rl_master_person_manage:
//                AppUtils.goActivity(mContext, MachineListActivity.class, bundle4);
                break;
            case R.id.rl_master_parts:
//                AppUtils.goActivity(mContext, MachineListActivity.class, bundle4);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        MasterMineBean bean = masterMineList.get(position);
        MineCode = bean.getMineCode();
        toRequest(NetConstant.EventTags.GET_MASTER_ALL_DATA);
        toRequest(NetConstant.EventTags.GET_MASTER_NUM_RATE);
    }
}
