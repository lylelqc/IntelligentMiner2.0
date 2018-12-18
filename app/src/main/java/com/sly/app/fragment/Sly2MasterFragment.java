package com.sly.app.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.sly.app.R;
import com.sly.app.activity.master.MasterAccountExecActivity;
import com.sly.app.activity.master.MasterAllFreeActivity;
import com.sly.app.activity.master.MasterAllPowerActivity;
import com.sly.app.activity.master.MasterMachineListActivity;
import com.sly.app.activity.master.MasterPersonManageActivity;
import com.sly.app.activity.master.MasterSpareListActivity;
import com.sly.app.adapter.master.MasterHomeRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.master.MasterAccountPermissionBean;
import com.sly.app.model.master.MasterAllDataBean;
import com.sly.app.model.master.MasterMineBean;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class Sly2MasterFragment extends BaseFragment implements ICommonViewUi,
        MasterHomeRecyclerViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

//    @BindView(R.id.rl_user_type)
//    RelativeLayout rlUserType;
//    @BindView(R.id.tv_user_type)
//    TextView tvUserType;
//    @BindView(R.id.tv_user_account)
//    TextView tvUserAccount;
//
//    //矿场主导航栏
//    @BindView(R.id.tv_master_add)
//    TextView tvMasterAdd;
//    @BindView(R.id.rl_notice)
//    RelativeLayout rlNotice;
//    @BindView(R.id.tv_red_num)
//    TextView tvRedNum;

    @BindView(R.id.recycler_view)
    RecyclerView rvMasterMineArea;
    @BindView(R.id.ll_master_all_free)
    LinearLayout llMasterAllFree;
    @BindView(R.id.tv_master_all_free)
    TextView tvMasterAllFree;
    @BindView(R.id.ll_master_all_power)
    LinearLayout llMasterAllPower;
    @BindView(R.id.tv_master_all_power_consumption)
    TextView tvMasterAllpowerConsumption;
    @BindView(R.id.tv_master_all_run_rate)
    TextView tvMasterAllRunRate;


    @BindView(R.id.rl_all_machine_count)
    RelativeLayout rlMasterMachineCount;
    @BindView(R.id.tv_master_all_num)
    TextView tvMasterAllMachineNum;

    @BindView(R.id.ll_master_num_rate)
    LinearLayout llMasterNumRate;
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

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static String mContent = "???";
    private String User, LoginType, FrSysCode, FMasterCode, MineCode, PersonSysCode, Token, Key;
    private String ChildAccount;
    private String MineName;
    ICommonRequestPresenter iCommonRequestPresenter;

    private List<MasterMineBean> masterMineList = new ArrayList<>();
    private MasterHomeRecyclerViewAdapter adapter;

    private List<MasterAllDataBean> masterAllDataList = new ArrayList<>();
    private List<MachineNumRateInfo> machineStatusList = new ArrayList<>();
    private MachineDetailsAllCalBean allCalbean;
    private List<MachineDetailsPicBean> mPic24ListBean = new ArrayList<>();
    private List<MachineDetailsPicBean> mPic30ListBean = new ArrayList<>();

    private List<MasterAccountPermissionBean> mPermissionList = new ArrayList<>();
    private PopupWindow mPopupWindow;

    private boolean isFree = false;
    private boolean isPower = false;
    private boolean isRate = false;
    private boolean isParts = false;

    private boolean isMaster = true;

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
        return true;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        /*if(EventBusTags.CLICK_AUTH_ACCOUNT.equals(postResult.getTag())){
            getActivityResult();
        }
        else*/ if (EventBusTags.CLICK_MINE_MASTER.equals(postResult.getTag())) {
            rvMasterMineArea.setVisibility(View.VISIBLE);
            rlMasterPersonManage.setVisibility(View.VISIBLE);
            rlMasterParts.setVisibility(View.VISIBLE);
            rlMasterMachineCount.setClickable(true);
            llMasterNumRate.setClickable(true);
            toRequest(NetConstant.EventTags.GET_MASTER_MINE_LIST);
        }
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
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "None");
        ChildAccount = SharedPreferencesUtil.getString(mContext, "ChildAccount", "None");

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
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_ALL_DATA) {
            map.put("Rounter", NetConstant.GET_MASTER_ALL_DATA);
            map.put("mineCode", MineCode);
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_NUM_RATE) {
            map.put("Rounter", NetConstant.GET_MASTER_NUM_RATE);
            map.put("mineCode", MineCode);
//            map.put("masterSysCode", FMasterCode);
        } else if (eventTag == NetConstant.EventTags.GET_AUTH_ACCOUNT_PERMISSION) {
            map.put("Rounter", NetConstant.GET_AUTH_ACCOUNT_PERMISSION);
            map.put("Mobile", User);
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_ALL_SUANLI) {
            //所有算力
            map.put("Rounter", NetConstant.GET_MASTER_ALL_SUANLI);
            map.put("mineCode", MineCode);
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_24_SUANLI) {
            //24小时算力
            map.put("Rounter", NetConstant.GET_MASTER_24_SUANLI);
            map.put("mineCode", MineCode);
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_30_SUANLI) {
            //30天算力
            map.put("Rounter", NetConstant.GET_MASTER_30_SUANLI);
            map.put("mineCode", MineCode);
        }/*else if(eventTag == NetConstant.EventTags.GET_MASTER_NEW_COUNT){
            map.put("Rounter", NetConstant.GET_MASTER_NEW_COUNT);
            map.put("masterSysCode", FMasterCode);
        }*/

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
        if (eventTag == NetConstant.EventTags.GET_MASTER_MINE_LIST) {
            masterMineList =
                    (List<MasterMineBean>) AppUtils.parseRowsResult(result, MasterMineBean.class);
            refreshListView();
            if (!AppUtils.isListBlank(masterMineList)) {
                MineCode = masterMineList.get(0).getMineCode();
                MineName = masterMineList.get(0).getMineName();
                toRequest(NetConstant.EventTags.GET_MASTER_ALL_DATA);
                toRequest(NetConstant.EventTags.GET_MASTER_NUM_RATE);
                toRequest(NetConstant.EventTags.GET_MASTER_ALL_SUANLI);
                toRequest(NetConstant.EventTags.GET_MASTER_24_SUANLI);
                toRequest(NetConstant.EventTags.GET_MASTER_30_SUANLI);
            }
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_ALL_DATA) {
            masterAllDataList =
                    (List<MasterAllDataBean>) AppUtils.parseRowsResult(result, MasterAllDataBean.class);
            if (!AppUtils.isListBlank(masterAllDataList)) {
                MasterAllDataBean bean = masterAllDataList.get(0);
                if (isFree || isMaster) {
                    tvMasterAllFree.setText(bean.getMonthExpenses());
                    llMasterAllFree.setClickable(true);
                } else {
                    tvMasterAllFree.setText("*****");
                    llMasterAllFree.setClickable(false);
                }

                if (isPower || isMaster) {
                    double power = Double.parseDouble(bean.getMonthPower());
                    tvMasterAllpowerConsumption.setText(String.format("%.2f", power));
                    llMasterAllPower.setClickable(true);
                } else {
                    tvMasterAllpowerConsumption.setText("*****");
                    llMasterAllPower.setClickable(false);
                }

                if (isRate || isMaster) {
                    double rate = Double.parseDouble(bean.getMonthRunRate()) * 100;
                    tvMasterAllRunRate.setText(String.format("%.2f", rate));
                } else {
                    tvMasterAllRunRate.setText("*****");
                }
                tvMasterAllMachineNum.setText(bean.getMachineCount());
            }
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_NUM_RATE) {
            machineStatusList = (List<MachineNumRateInfo>) AppUtils.parseResult(result, MachineNumRateInfo.class);
            if (!AppUtils.isListBlank(machineStatusList)) {
                setPorgressInfo();
            }
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_ALL_SUANLI) {
            //所有算力
            allCalbean =
                    ((List<MachineDetailsAllCalBean>) AppUtils.parseRowsResult(result, MachineDetailsAllCalBean.class)).get(0);
            tvMasterSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getHoursCalcForce()) ? "0.0" : allCalbean.getHoursCalcForce()) + "T)");
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_24_SUANLI) {
            //24小时算力
            mPic24ListBean =
                    (List<MachineDetailsPicBean>) AppUtils.parseRowsResult(result, MachineDetailsPicBean.class);
            Collections.reverse(mPic24ListBean);
            setCalPowerPicData(lcCalPowerPic, mPic24ListBean, 24);
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_30_SUANLI) {
            //30天算力
            mPic30ListBean =
                    (List<MachineDetailsPicBean>) AppUtils.parseRowsResult(result, MachineDetailsPicBean.class);
            Collections.reverse(mPic30ListBean);
        }
        //授权账号获取权限
        else if (eventTag == NetConstant.EventTags.GET_AUTH_ACCOUNT_PERMISSION) {
            mPermissionList =
                    (List<MasterAccountPermissionBean>) AppUtils.parseRowsResult(result, MasterAccountPermissionBean.class);
            if (!AppUtils.isListBlank(mPermissionList)) {
                lcCalPowerPic.clear();
                rvMasterMineArea.setVisibility(View.GONE);
                rlMasterPersonManage.setVisibility(View.GONE);
                rlMasterMachineCount.setClickable(false);
                llMasterNumRate.setClickable(false);

                MasterAccountPermissionBean bean = mPermissionList.get(0);
                isParts = bean.isUsingPart();
                isFree = bean.isUsingFee();
                isPower = bean.isUsingPower();
                isRate = bean.isUsingRate();

                if (isRate || isMaster) {
                    rlMasterParts.setVisibility(View.VISIBLE);
                } else {
                    rlMasterParts.setVisibility(View.GONE);
                }
                toRequest(NetConstant.EventTags.GET_MASTER_ALL_DATA);
                toRequest(NetConstant.EventTags.GET_MASTER_NUM_RATE);
            }
        }
    }

    private void setCalPowerPicData(LineChart lineChart, List<MachineDetailsPicBean> list, int tag) {
        if (!AppUtils.isListBlank(list)) {
            lineChart.clear();
            ChartUtil.initCalPowerPic(mContext, lineChart, list);
            formatXValues(list, tag);
            ChartUtil.showLineChart(list, lineChart, this.getResources().getColor(R.color.sly_text_4a96f2),
                    this.getResources().getColor(R.color.sly_bg_f5fbff));
        }
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
        adapter = new MasterHomeRecyclerViewAdapter(mContext, masterMineList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        rvMasterMineArea.setLayoutManager(layoutManager);
        rvMasterMineArea.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    private void setPorgressInfo() {

        for (int i = 0; i < machineStatusList.size(); i++) {
            MachineNumRateInfo info = machineStatusList.get(i);
            // 在线
            if ("00".equals(info.getStatusCode())) {
                tvMasterOnlineNum.setText(info.getMachineCount() + "");
                double rate = info.getRate() * 100;
                if (Math.round(rate) - rate == 0) {
                    mProgressBar1.setProgress((int) rate);
                } else {
                    DecimalFormat df = new DecimalFormat(".#");
                    String runrate = df.format(rate);
                    mProgressBar1.setProgress(Float.parseFloat(runrate));
                }
            }
            //离线
            else if ("01".equals(info.getStatusCode())) {
                tvMasterOfflineNum.setText(info.getMachineCount() + "");
                double rate = info.getRate() * 100;
                if (Math.round(rate) - rate == 0) {
                    mProgressBar2.setProgress((int) rate);
                } else {
                    DecimalFormat df = new DecimalFormat(".#");
                    String runrate = df.format(rate);
                    mProgressBar2.setProgress(Float.parseFloat(runrate));
                }
            }
            // 算力异常
            else if ("02".equals(info.getStatusCode())) {
                tvMasterExceptionNum.setText(info.getMachineCount() + "");
                double rate = info.getRate() * 100;
                if (Math.round(rate) - rate == 0) {
                    mProgressBar3.setProgress((int) rate);
                } else {
                    DecimalFormat df = new DecimalFormat(".#");
                    String runrate = df.format(rate);
                    mProgressBar3.setProgress(Float.parseFloat(runrate));
                }
            }
            // 停机
            else if ("05".equals(info.getStatusCode())) {
                tvMasterStopNum.setText(info.getMachineCount() + "");
                double rate = info.getRate() * 100;
                if (Math.round(rate) - rate == 0) {
                    mProgressBar4.setProgress((int) rate);
                } else {
                    DecimalFormat df = new DecimalFormat(".#");
                    String runrate = df.format(rate);
                    mProgressBar4.setProgress(Float.parseFloat(runrate));
                }
            }
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

    @OnClick({/*R.id.rl_user_type, R.id.tv_master_add, R.id.rl_notice,*/ R.id.ll_master_all_free, R.id.ll_master_all_power,
            R.id.rl_online_machine, R.id.rl_offline_machine, R.id.rl_exception_machine, R.id.rl_stop_machine,
            R.id.rl_all_machine_count, R.id.rl_master_person_manage, R.id.rl_master_parts,
            R.id.tv_master_hours, R.id.tv_master_month})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.rl_user_type:
//                showPopupWindow();
//                break;
//            case R.id.tv_master_add:
//                Bundle addBun = new Bundle();
//                addBun.putString("MineCode", MineCode);
//                AppUtils.goActivity(mContext, MasterAccountExecActivity.class, addBun);
//                break;
//            case R.id.rl_notice:
//                AppUtils.goActivity(mContext, Sly2NoticeActivity.class);
//                break;
            case R.id.ll_master_all_free:
                Bundle freeBun = new Bundle();
                freeBun.putString("MineCode", MineCode);
                AppUtils.goActivity(mContext, MasterAllFreeActivity.class, freeBun);
                break;
            case R.id.ll_master_all_power:
                Bundle powerBun = new Bundle();
                powerBun.putString("MineCode", MineCode);
                AppUtils.goActivity(mContext, MasterAllPowerActivity.class, powerBun);
                break;
            case R.id.rl_all_machine_count:
                Bundle bundle = new Bundle();
                bundle.putString("MineCode", MineCode);
                Logcat.e("矿场 - " + MineCode);
                AppUtils.goActivity(mContext, MasterMachineListActivity.class, bundle);
                break;
            //百分比状态点击
            case R.id.rl_online_machine:
                Bundle bundle1 = new Bundle();
                bundle1.putString("MineCode", MineCode);
                bundle1.putString("statusCode", "00");
                AppUtils.goActivity(mContext, MasterMachineListActivity.class, bundle1);
                break;
            case R.id.rl_offline_machine:
                Bundle bundle2 = new Bundle();
                bundle2.putString("MineCode", MineCode);
                bundle2.putString("statusCode", "01");
                AppUtils.goActivity(mContext, MasterMachineListActivity.class, bundle2);
                break;
            case R.id.rl_exception_machine:
                Bundle bundle3 = new Bundle();
                bundle3.putString("MineCode", MineCode);
                bundle3.putString("statusCode", "02");
                AppUtils.goActivity(mContext, MasterMachineListActivity.class, bundle3);
                break;
            case R.id.rl_stop_machine:
                Bundle bundle4 = new Bundle();
                bundle4.putString("MineCode", MineCode);
                bundle4.putString("statusCode", "05");
                AppUtils.goActivity(mContext, MasterMachineListActivity.class, bundle4);
                break;
            case R.id.tv_master_hours:
                if(allCalbean != null) {
                    tvMasterSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getHoursCalcForce()) ? "0.0" : allCalbean.getHoursCalcForce()) + "T)");
                    setBgAndTextColor(1);
                    setCalPowerPicData(lcCalPowerPic, mPic24ListBean, 24);
                }
                break;
            case R.id.tv_master_month:
                if(allCalbean != null){
                    tvMasterSuanli.setText("(" + (AppUtils.isBlank(allCalbean.getMonthCalcForce()) ? "0.0" : allCalbean.getMonthCalcForce()) + "T)");
                    setBgAndTextColor(2);
                    setCalPowerPicData(lcCalPowerPic, mPic30ListBean, 30);
                }
                break;
            case R.id.rl_master_person_manage:
                Bundle bundle5 = new Bundle();
                bundle5.putString("MineCode", MineCode);
                bundle5.putString("MineName", MineName);
                AppUtils.goActivity(mContext, MasterPersonManageActivity.class, bundle5);
                break;
            case R.id.rl_master_parts:
                Bundle bundle6 = new Bundle();
                bundle6.putString("MineCode", MineCode);
                AppUtils.goActivity(mContext, MasterSpareListActivity.class, bundle6);
                break;
        }
    }

    public void startActivitys(){
        Bundle addBun = new Bundle();
        addBun.putString("MineCode", MineCode);
        AppUtils.goActivity(mContext, MasterAccountExecActivity.class, addBun);
    }

    private void setBgAndTextColor(int btnTag) {
        if (btnTag == 1) {
            tvMasterHours.setBackground(getResources().getDrawable(R.drawable.layer_left_circle_blue_15dp));
            tvMasterHours.setTextColor(getResources().getColor(R.color.white));
            tvMasterMonth.setBackground(getResources().getDrawable(R.drawable.shape_right_circle_white_15dp));
            tvMasterMonth.setTextColor(getResources().getColor(R.color.sly_text_666666));
        } else {
            tvMasterHours.setBackground(getResources().getDrawable(R.drawable.layer_left_circle_white_15dp));
            tvMasterHours.setTextColor(getResources().getColor(R.color.sly_text_666666));
            tvMasterMonth.setBackground(getResources().getDrawable(R.drawable.shape_right_circle_blue_15dp));
            tvMasterMonth.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        MasterMineBean bean = masterMineList.get(position);
        MineCode = bean.getMineCode();
        MineName = bean.getMineName();
        toRequest(NetConstant.EventTags.GET_MASTER_ALL_DATA);
        toRequest(NetConstant.EventTags.GET_MASTER_NUM_RATE);

    }

    public void getActivityResult() {
        MineCode = SharedPreferencesUtil.getString(mContext, "authMineCode");
        isMaster = SharedPreferencesUtil.getBoolean(mContext, "authIsMaster");
        toRequest(NetConstant.EventTags.GET_AUTH_ACCOUNT_PERMISSION);
    }

    @Override
    public void onRefresh() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            ToastUtils.showToast(Contants.NetStatus.NETDISABLE);
            return;
        }
        if(isMaster){
            toRequest(NetConstant.EventTags.GET_MASTER_MINE_LIST);
        }else{
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
