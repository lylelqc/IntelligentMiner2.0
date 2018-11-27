package com.sly.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.yunw.machine.MachineListActivity;
import com.sly.app.activity.yunw.machine.MachineManageActivity;
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
    @BindView(R.id.tv_offline_num)
    TextView tvOfflineNum;
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
    @BindView(R.id.ll_online_machine)
    LinearLayout llOnlineMachine;

    //总台数
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

    public static String mContent = "???";
    private String User,LoginType, MineCode, PersonSysCode, Token, Key, PlanID;
    ICommonRequestPresenter iCommonRequestPresenter;
    private List<MachineNumRateInfo> machineStatusList = new ArrayList<>();

    public static Sly2YunwFragment newInstance(String content) {
        Sly2YunwFragment fragment = new Sly2YunwFragment();
        mContent = content;
        return fragment;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

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

        toRequest(NetConstant.EventTags.GET_NEW_REPAIR_NUM);
        toRequest(NetConstant.EventTags.GET_YUNW_ALL_NUM);
        toRequest(NetConstant.EventTags.GET_YUNW_MACHINE_NUM_RATE);
        toRequest(NetConstant.EventTags.GET_MINE_AREA_INFO);
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
        if(eventTag == NetConstant.EventTags.GET_MINE_AREA_INFO){
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetConstant.GET_MINE_AREA_INFO);
            map.put("User", User);
            map.put("personSysCode", PersonSysCode);

            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
            iCommonRequestPresenter.request(NetConstant.EventTags.GET_MINE_AREA_INFO, mContext, NetWorkCons.BASE_URL, mapJson);
        }
        else if(eventTag == NetConstant.EventTags.GET_NEW_REPAIR_NUM){
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetConstant.GET_NEW_REPAIR_NUM);
            map.put("User", User);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);

            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
            iCommonRequestPresenter.request(NetConstant.EventTags.GET_NEW_REPAIR_NUM, mContext, NetWorkCons.BASE_URL, mapJson);
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_ALL_NUM){
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetConstant.GET_YUNW_ALL_NUM);
            map.put("User", User);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);

            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
            iCommonRequestPresenter.request(NetConstant.EventTags.GET_YUNW_ALL_NUM, mContext, NetWorkCons.BASE_URL, mapJson);
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_MACHINE_NUM_RATE){
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
                map.put("Rounter", NetConstant.GET_YUNW_MACHINE_NUM_RATE);
            map.put("User", User);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);

            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
            iCommonRequestPresenter.request(NetConstant.EventTags.GET_YUNW_MACHINE_NUM_RATE, mContext, NetWorkCons.BASE_URL, mapJson);
        }
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
                tvRepairNum.setVisibility(View.VISIBLE);
                if(returnBean.getData().length() == 2){
                    tvRepairNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble2));
                    tvRepairNum.setText(returnBean.getData());
                }else if(returnBean.getData().length() == 3){
                    tvRepairNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble3));
                    tvRepairNum.setText("99+");
                }else{
                    tvRepairNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble1));
                    tvRepairNum.setText(returnBean.getData());
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
            machineStatusList = (List<MachineNumRateInfo>) AppUtils.parseResult(result, MachineNumRateInfo.class);
            if(machineStatusList != null && machineStatusList.size() > 0){
                tvOfflineNum.setVisibility(View.VISIBLE);
                MachineNumRateInfo offlineInfo = machineStatusList.get(1);
                if(offlineInfo.getMachineCount() > 9){
                    tvOfflineNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble2));
                    tvOfflineNum.setText(offlineInfo.getMachineCount()+"");
                }else if(offlineInfo.getMachineCount() > 99){
                    tvOfflineNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble3));
                    tvOfflineNum.setText("99+");
                }else{
                    tvOfflineNum.setBackground(getResources().getDrawable(R.drawable.operation_bubble1));
                    tvOfflineNum.setText(offlineInfo.getMachineCount()+"");
                }

                DecimalFormat df = new DecimalFormat("#.#");
//                df.setRoundingMode(RoundingMode.HALF_DOWN);

                MachineNumRateInfo onlineInfo = machineStatusList.get(0);
                tvOnlineNum.setText(onlineInfo.getMachineCount()+"");
                String rate1 = df.format(onlineInfo.getRate()*100);
                mProgressBar1.setProgress(rate1.contains(".") ? Float.parseFloat(rate1) : Integer.parseInt(rate1));

                tvOfflineNum2.setText(offlineInfo.getMachineCount()+"");
                String rate2 = df.format(offlineInfo.getRate()*100);
                mProgressBar2.setProgress(rate2.contains(".") ? Float.parseFloat(rate2) : Integer.parseInt(rate2));

                MachineNumRateInfo exceptionInfo = machineStatusList.get(2);
                tvExceptionNum.setText(exceptionInfo.getMachineCount()+"");
                String rate3 = df.format(exceptionInfo.getRate()*100);
                mProgressBar3.setProgress(rate3.contains(".") ? Float.parseFloat(rate3) : Integer.parseInt(rate3));

                MachineNumRateInfo stopInfo = machineStatusList.get(3);
                tvStopNum.setText(stopInfo.getMachineCount()+"");
                String rate4 = df.format(stopInfo.getRate()*100);
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

    @OnClick({R.id.tv_operation_manage,R.id.tv_offline_num,R.id.tv_repair_num,
            R.id.ll_offline_machine, R.id.ll_repair_bill, R.id.ll_downline_machine,
            R.id.ll_modify_mine, R.id.ll_online_machine, R.id.rl_online_machine,
            R.id.rl_offline_machine,R.id.rl_exception_machine,R.id.rl_stop_machine})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_operation_manage:
                AppUtils.goActivity(mContext, MachineManageActivity.class);
                break;
            case R.id.tv_offline_num:
            case R.id.ll_offline_machine:
                break;
            case R.id.tv_repair_num:
            case R.id.ll_repair_bill:
                AppUtils.goActivity(mContext, RepairBillActivity.class);
                break;
            case R.id.ll_downline_machine:
                break;
            case R.id.ll_modify_mine:
                break;
            case R.id.ll_online_machine:
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
        }
    }
}
