package com.sly.app.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.yunw.ClockWorkActivity;
import com.sly.app.activity.yunw.changepool.MachineSetPoolActivity;
import com.sly.app.activity.yunw.goline.MachineGolineActivity;
import com.sly.app.activity.yunw.machine.MachineListActivity;
import com.sly.app.activity.yunw.machine.MachineManageActivity;
import com.sly.app.activity.yunw.offline.MachineOfflineActivity;
import com.sly.app.activity.yunw.repair.RepairBillActivity;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
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

public class Sly2YunwFragment extends BaseFragment implements ICommonViewUi{

    @BindView(R.id.rl_user_type)
    RelativeLayout rlUserType;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;

    //运维导航栏
    @BindView(R.id.tv_operation_manage)
    TextView tvOperationManage;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    //离线、维修单消息数量
    @BindView(R.id.rl_offline_num)
    RelativeLayout rlOfflineNum;
    @BindView(R.id.tv_offline_num)
    TextView tvOfflineNum;
    @BindView(R.id.rl_repair_num)
    RelativeLayout rlRepairNum;
    @BindView(R.id.tv_repair_num)
    TextView tvRepairNum;

    //可点击按钮
    @BindView(R.id.ll_offline_machine)
    LinearLayout llOfflineMachine;
    @BindView(R.id.ll_repair_bill)
    LinearLayout llRepairBill;
    @BindView(R.id.ll_downline_machine)
    LinearLayout llDownlineMachine;
    @BindView(R.id.ll_modify_mine)
    LinearLayout llModifyMine;
    @BindView(R.id.ll_goline_machine)
    LinearLayout llOnlineMachine;

    //总台数
    @BindView(R.id.rl_all_machine_count)
    RelativeLayout rlAllMachineCount;
    @BindView(R.id.tv_all_num)
    TextView tvAllNum;

    //各种机器状态
    @BindView(R.id.rl_online_machine)
    RelativeLayout rlOnlineMachine;
    @BindView(R.id.tv_online_num)
    TextView tvOnlineNum;
    @BindView(R.id.Progress_1)
    CustomCircleProgressBar mProgressBar1;

    @BindView(R.id.rl_offline_machine)
    RelativeLayout rlOfflineMachine;
    @BindView(R.id.tv_offline_num2)
    TextView tvOfflineNum2;
    @BindView(R.id.Progress_2)
    CustomCircleProgressBar mProgressBar2;

    @BindView(R.id.rl_exception_machine)
    RelativeLayout rlExceptionMachine;
    @BindView(R.id.tv_exception_num)
    TextView tvExceptionNum;
    @BindView(R.id.Progress_3)
    CustomCircleProgressBar mProgressBar3;

    @BindView(R.id.rl_stop_machine)
    RelativeLayout rlStopMachine;
    @BindView(R.id.tv_stop_num)
    TextView tvStopNum;
    @BindView(R.id.Progress_4)
    CustomCircleProgressBar mProgressBar4;

    //矿机监控
    @BindView(R.id.ll_monitor_area)
    LinearLayout llMonitorArea;
    //打卡
    @BindView(R.id.tv_home_clock_work)
    TextView tvClockWork;

    public static String mContent = "???";
    private String User,LoginType, MineCode, PersonSysCode, Token, Key, PlanID;
    ICommonRequestPresenter iCommonRequestPresenter;
    private List<MachineNumRateInfo> machineNumRateList = new ArrayList<>();

    public static Sly2YunwFragment newInstance(String content) {
        Sly2YunwFragment fragment = new Sly2YunwFragment();
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
        toRequest(NetConstant.EventTags.GET_YUNW_NEWS_COUNT);
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(mContext, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "None");

        AppUtils.setStatusBarColor(getActivity(), getResources().getColor(R.color.sly_status_bar));

        toRequest(NetConstant.EventTags.GET_NEW_REPAIR_NUM);
        toRequest(NetConstant.EventTags.GET_YUNW_ALL_NUM);
        toRequest(NetConstant.EventTags.GET_YUNW_MACHINE_NUM_RATE);
        toRequest(NetConstant.EventTags.GET_MINE_AREA_INFO);
        toRequest(NetConstant.EventTags.GET_YUNW_NEWS_COUNT);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_sly2_mine;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        if(eventTag == NetConstant.EventTags.GET_MINE_AREA_INFO){
            map.put("Rounter", NetConstant.GET_MINE_AREA_INFO);
            map.put("personSysCode", PersonSysCode);
        }
        else if(eventTag == NetConstant.EventTags.GET_NEW_REPAIR_NUM){
            map.put("Rounter", NetConstant.GET_NEW_REPAIR_NUM);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_ALL_NUM){
            map.put("Rounter", NetConstant.GET_YUNW_ALL_NUM);
            map.put("personSysCode", PersonSysCode);
            map.put("areacode", "");
            map.put("Model", "");
            map.put("Worker", "");
            map.put("minerSysCode", "");
            map.put("statusCode", "");
            map.put("machineCode", "");
            map.put("ip", "");
            map.put("beginip", "");
            map.put("endip", "");
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_MACHINE_NUM_RATE){
            map.put("Rounter", NetConstant.GET_YUNW_MACHINE_NUM_RATE);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_NEWS_COUNT){
            map.put("Rounter", NetConstant.GET_YUNW_NEWS_COUNT);
            map.put("personSysCode", PersonSysCode);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 = " + result);
        if(eventTag == NetConstant.EventTags.GET_MINE_AREA_INFO){
            List<MineAreaInfo> infoList =
                    (List<MineAreaInfo>)AppUtils.parseResult(result, MineAreaInfo.class);
            if(infoList != null && infoList.size() > 0){
                for (MineAreaInfo info : infoList){
                    addViewItem(info);
                }
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_NEW_REPAIR_NUM){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                rlRepairNum.setVisibility(View.VISIBLE);
                if(Integer.parseInt(returnBean.getData()) == 0){
                    rlRepairNum.setVisibility(View.GONE);
                    return ;
                }

                if(returnBean.getData().length() == 1){
                    tvRepairNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble1));
                    tvRepairNum.setText(returnBean.getData());
                }
                else if(returnBean.getData().length() == 2){
                    tvRepairNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble2));
                    tvRepairNum.setText(returnBean.getData());
                }
                else if(returnBean.getData().length() >= 3){
                    tvRepairNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble3));
                    tvRepairNum.setText("99+");
                }
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_ALL_NUM){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                tvAllNum.setText(returnBean.getData());
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_MACHINE_NUM_RATE){
            machineNumRateList = (List<MachineNumRateInfo>) AppUtils.parseResult(result, MachineNumRateInfo.class);
            if(!AppUtils.isListBlank(machineNumRateList)){
                setPorgressInfo();
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_NEWS_COUNT){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {

                int count = Integer.parseInt(returnBean.getData());
                if(count == 0){
                    tvRedNum.setVisibility(View.GONE);
                }
                else if(count > 99){
                    tvRedNum.setText("99+");
                    tvRedNum.setVisibility(View.VISIBLE);
                }
                else{
                    tvRedNum.setText(returnBean.getData());
                    tvRedNum.setVisibility(View.VISIBLE);
                }
                SharedPreferencesUtil.putString(mContext, "NewsCount", returnBean.getData());
            }
        }
    }

    private void setPorgressInfo() {

        for(int i = 0; i < machineNumRateList.size(); i++){
            MachineNumRateInfo info = machineNumRateList.get(i);
            // 在线
            if("00".equals(info.getStatusCode())){
                tvOnlineNum.setText(info.getMachineCount()+"");

                double rate = info.getRate()*100;
                if (Math.round(rate) - rate == 0) {
                    mProgressBar1.setProgress((int)rate);
                }else{
                    DecimalFormat df = new DecimalFormat(".#");
                    String runrate = df.format(rate);
                    mProgressBar1.setProgress(Float.parseFloat(runrate));
                }
            }
            //离线
            else if("01".equals(info.getStatusCode())){
                rlOfflineNum.setVisibility(View.VISIBLE);
                if(info.getMachineCount() == 0){
                    rlOfflineNum.setVisibility(View.GONE);
                }

                if(info.getMachineCount() < 10){
                    tvOfflineNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble1));
                    tvOfflineNum.setText(info.getMachineCount()+"");
                }
                else if(info.getMachineCount() > 9 && info.getMachineCount() < 100){
                    tvOfflineNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble2));
                    tvOfflineNum.setText(info.getMachineCount()+"");
                }
                else if(info.getMachineCount() > 99 ){
                    tvOfflineNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble3));
                    tvOfflineNum.setText("99+");
                }

                tvOfflineNum2.setText(info.getMachineCount()+"");
                double rate = info.getRate()*100;
                if (Math.round(rate) - rate == 0) {
                    mProgressBar2.setProgress((int)rate);
                }else{
                    DecimalFormat df = new DecimalFormat(".#");
                    String runrate = df.format(rate);
                    mProgressBar2.setProgress(Float.parseFloat(runrate));
                }
            }
            // 算力异常
            else if("02".equals(info.getStatusCode())){
                tvExceptionNum.setText(info.getMachineCount()+"");
                double rate = info.getRate()*100;
                if (Math.round(rate) - rate == 0) {
                    mProgressBar3.setProgress((int)rate);
                }else{
                    DecimalFormat df = new DecimalFormat(".#");
                    String runrate = df.format(rate);
                    mProgressBar3.setProgress(Float.parseFloat(runrate));
                }
            }
            // 停机
            else if("05".equals(info.getStatusCode())){
                tvStopNum.setText(info.getMachineCount()+"");
                double rate = info.getRate()*100;
                if (Math.round(rate) - rate == 0) {
                    mProgressBar4.setProgress((int)rate);
                }else{
                    DecimalFormat df = new DecimalFormat(".#");
                    String runrate = df.format(rate);
                    mProgressBar4.setProgress(Float.parseFloat(runrate));
                }
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

    private void addViewItem(final MineAreaInfo bean) {
        View itemView = View.inflate(mContext, R.layout.item_monitor_area, null);
        RelativeLayout runStatus = itemView.findViewById(R.id.rl_status);
        TextView areaName = itemView.findViewById(R.id.tv_area_name);
        TextView allNum = itemView.findViewById(R.id.tv_all_num);
        TextView normalNum = itemView.findViewById(R.id.tv_normal_num);
        TextView runRate = itemView.findViewById(R.id.tv_run_rate);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("areaCode", bean.getCode());
                AppUtils.goActivity(mContext, MachineListActivity.class, bundle);
            }
        });

        areaName.setText(bean.getName());
        allNum.setText(bean.getTotalCount()+"");
        normalNum.setText(bean.getNormalCount()+"");

        double rate = bean.getRunRate();
        runRate.setText(rate+"");
        if(rate == 100){
            runStatus.setBackgroundColor(Color.parseColor("#90d262"));
        }else if(rate == 0){
            runStatus.setBackgroundColor(Color.parseColor("#ff6e6e"));
        }
        llMonitorArea.addView(itemView);
    }

    @OnClick({R.id.tv_operation_manage,R.id.rl_notice, R.id.tv_offline_num,R.id.tv_repair_num,
            R.id.ll_offline_machine, R.id.ll_repair_bill, R.id.ll_downline_machine,
            R.id.ll_modify_mine, R.id.ll_goline_machine, R.id.rl_online_machine,
            R.id.rl_offline_machine,R.id.rl_exception_machine,R.id.rl_stop_machine,
            R.id.rl_all_machine_count, R.id.tv_home_clock_work})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_operation_manage:
                AppUtils.goActivity(mContext, MachineManageActivity.class);
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(mContext, Sly2NoticeActivity.class);
                break;
            case R.id.tv_offline_num:
            case R.id.ll_offline_machine:
                Bundle bundleOff = new Bundle();
                bundleOff.putString("statusCode", "01");
                AppUtils.goActivity(mContext, MachineListActivity.class, bundleOff);
                break;
            case R.id.tv_repair_num:
            case R.id.ll_repair_bill:
                AppUtils.goActivity(mContext, RepairBillActivity.class);
                break;
            case R.id.ll_downline_machine:
                AppUtils.goActivity(mContext, MachineOfflineActivity.class);
                break;
            case R.id.ll_modify_mine:
                AppUtils.goActivity(mContext, MachineSetPoolActivity.class);
                break;
            case R.id.ll_goline_machine:
                AppUtils.goActivity(mContext, MachineGolineActivity.class);
                break;
            case R.id.rl_all_machine_count:
                AppUtils.goActivity(mContext, MachineListActivity.class, new Bundle());
                break;
                //百分比状态点击
            case R.id.rl_online_machine:
                Bundle bundle1 = new Bundle();
                bundle1.putString("statusCode", "00");
                AppUtils.goActivity(mContext, MachineListActivity.class, bundle1);
                break;
            case R.id.rl_offline_machine:
                Bundle bundle2 = new Bundle();
                bundle2.putString("statusCode", "01");
                AppUtils.goActivity(mContext, MachineListActivity.class, bundle2);
                break;
            case R.id.rl_exception_machine:
                Bundle bundle3 = new Bundle();
                bundle3.putString("statusCode", "02");
                AppUtils.goActivity(mContext, MachineListActivity.class, bundle3);
                break;
            case R.id.rl_stop_machine:
                Bundle bundle4 = new Bundle();
                bundle4.putString("statusCode", "05");
                AppUtils.goActivity(mContext, MachineListActivity.class, bundle4);
                break;
            case R.id.tv_home_clock_work:
                AppUtils.goActivity(mContext, ClockWorkActivity.class);
                break;
        }
    }
}
