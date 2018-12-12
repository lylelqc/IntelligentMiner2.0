package com.sly.app.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.sly.app.R;
import com.sly.app.activity.master.AuthAccountSetMineActivity;
import com.sly.app.activity.master.MasterAccountExecActivity;
import com.sly.app.activity.master.MasterAllFreeActivity;
import com.sly.app.activity.master.MasterAllPowerActivity;
import com.sly.app.activity.master.MasterMachineListActivity;
import com.sly.app.activity.master.MasterPersonManageActivity;
import com.sly.app.activity.master.MasterSpareListActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.master.MasterHomeRecyclerViewAdapter;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.master.MasterAccountPermissionBean;
import com.sly.app.model.master.MasterAllDataBean;
import com.sly.app.model.master.MasterMineBean;
import com.sly.app.model.yunw.home.MachineNumRateInfo;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.CustomCircleProgressBar;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;
import com.sly.app.view.iviews.ICommonViewUi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

import static de.greenrobot.event.EventBus.TAG;

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
    private String ChildAccount;
    private String MineName;
    ICommonRequestPresenter iCommonRequestPresenter;

    private List<MasterMineBean> masterMineList = new ArrayList<>();
    private MasterHomeRecyclerViewAdapter adapter;

    private List<MasterAllDataBean> masterAllDataList = new ArrayList<>();
    private List<MachineNumRateInfo> machineStatusList = new ArrayList<>();
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
        if (EventBusTags.CHOOSE_AUTH_MINE_AREA.equals(postResult.getTag())) {
            getActivityResult();
        }
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
        ChildAccount = SharedPreferencesUtil.getString(mContext, "ChildAccount", "None");

        String maskNumber = User.substring(0, 3) + "****" + User.substring(7, User.length());
        tvUserAccount.setText(maskNumber);
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
        }
        /*else if (eventTag == NetConstant.EventTags.GET_YUNW_NEWS_COUNT) {
            map.put("Rounter", NetConstant.GET_YUNW_NEWS_COUNT);
            map.put("personSysCode", PersonSysCode);
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
        if (eventTag == NetConstant.EventTags.GET_MASTER_MINE_LIST) {
            masterMineList =
                    (List<MasterMineBean>) AppUtils.parseRowsResult(result, MasterMineBean.class);
            refreshListView();
            if (!AppUtils.isListBlank(masterMineList)) {
                MineCode = masterMineList.get(0).getMineCode();
                MineName = masterMineList.get(0).getMineName();
                toRequest(NetConstant.EventTags.GET_MASTER_ALL_DATA);
                toRequest(NetConstant.EventTags.GET_MASTER_NUM_RATE);
            }
        } else if (eventTag == NetConstant.EventTags.GET_MASTER_ALL_DATA) {
            masterAllDataList =
                    (List<MasterAllDataBean>) AppUtils.parseRowsResult(result, MasterAllDataBean.class);
            if (!AppUtils.isListBlank(masterAllDataList)) {
                MasterAllDataBean bean = masterAllDataList.get(0);
                if (isFree || isMaster) {
                    tvMasterAllFree.setText(bean.getMonthExpenses());
                } else {
                    tvMasterAllFree.setText("*****");
                }

                if (isPower || isMaster) {
                    tvMasterAllpowerConsumption.setText(bean.getMonthPower());
                } else {
                    tvMasterAllpowerConsumption.setText("*****");
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
        } else if (eventTag == NetConstant.EventTags.GET_AUTH_ACCOUNT_PERMISSION) {
            mPermissionList =
                    (List<MasterAccountPermissionBean>) AppUtils.parseRowsResult(result, MasterAccountPermissionBean.class);
            if (!AppUtils.isListBlank(mPermissionList)) {
                rvMasterMineArea.setVisibility(View.GONE);
                rlMasterPersonManage.setVisibility(View.GONE);

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

    public void refreshListView() {
        adapter = new MasterHomeRecyclerViewAdapter(mContext, masterMineList);
        MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(3, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.HORIZONTAL);
        rvMasterMineArea.setLayoutManager(mLayoutManager);
        rvMasterMineArea.addItemDecoration(lineVertical);
        rvMasterMineArea.setItemAnimator(new DefaultItemAnimator());
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

    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {

    }

    @Override
    public void isRequesting(int eventTag, boolean status) {

    }

    @OnClick({R.id.rl_user_type, R.id.tv_master_add, R.id.rl_notice, R.id.ll_master_all_free, R.id.ll_master_all_power,
            R.id.rl_online_machine, R.id.rl_offline_machine, R.id.rl_exception_machine, R.id.rl_stop_machine,
            R.id.rl_all_machine_count, R.id.rl_master_person_manage, R.id.rl_master_parts})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user_type:
                showPopupWindow();
                break;
            case R.id.tv_master_add:
                Bundle addBun = new Bundle();
                addBun.putString("MineCode", MineCode);
                AppUtils.goActivity(mContext, MasterAccountExecActivity.class, addBun);
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(mContext, Sly2NoticeActivity.class);
                break;
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

    @Override
    public void onItemClick(View view, int position) {
        MasterMineBean bean = masterMineList.get(position);
        MineCode = bean.getMineCode();
        MineName = bean.getMineName();
        toRequest(NetConstant.EventTags.GET_MASTER_ALL_DATA);
        toRequest(NetConstant.EventTags.GET_MASTER_NUM_RATE);

    }

    private void showPopupWindow() {
        if (mPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View contentView = inflater.inflate(R.layout.mine_popupwindow, null, false);

            mPopupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.showAsDropDown(rlUserType, 10, 5);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(00000000));

            TextView miner = contentView.findViewById(R.id.miner);
            TextView mineMaster = contentView.findViewById(R.id.mineMaster);
            TextView operation = contentView.findViewById(R.id.operation);
            TextView authAccount = contentView.findViewById(R.id.auth_account);

            miner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "FrSysCode: ");
//                    mine.setText("矿工");
//                    SharedPreferencesUtil.putString(mContext, "LoginType", "Miner");
//                    SharedPreferencesUtil.putString(mContext, "mineType", "Miner");
//                    if(!LoginType.equals("Miner")){
//                        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType","None");
//                    }
//                    getUserInfo(mContext, LoginType, User, FrSysCode, Key, Token);//更新数据
//                    EventBus.getDefault().post(new PostResult(BusEvent.UPDATE_HOSTING_MINER_DATA));
//                    showMenuForRole();
//                    getNewsCount();
                }
            });

            mineMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "FMasterCode: ");
                    if (FMasterCode != null && !FMasterCode.isEmpty()) {
//                        mine.setText("矿场主");
//                        SharedPreferencesUtil.putString(mContext, "LoginType", "MinerMaster");
//                        SharedPreferencesUtil.putString(mContext, "mineType", "MinerMaster");
//                        if(!LoginType.equals("MinerMaster")){
//                            LoginType = SharedPreferencesUtil.getString(mContext, "LoginType","None");
//                        }
//                        getUserInfo(mContext, LoginType, User, FMasterCode, Key, Token);//更新数据
//                        EventBus.getDefault().post(new PostResult(BusEvent.UPDATE_HOSTING_MASTER_DATA));
//                        showMenuForRole();
//                        getNewsCount();
                    } else {
                        ToastUtils.showToast("抱歉！您当前没有此权限");
                    }
                }
            });

            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "PersonSysCode: ");
                    if (PersonSysCode != null && !PersonSysCode.isEmpty()) {
//                        mine.setText("运维");
//                        SharedPreferencesUtil.putString(mContext, "mineType", "Operation");
//                        String SysCode = LoginType.equals("Miner") ? FrSysCode : FMasterCode;
//                        getUserInfo(mContext, LoginType, User, SysCode, Key, Token);//更新数据
//                        EventBus.getDefault().post(new PostResult(BusEvent.UPDATE_HOSTING_OPERATION_DATA));
//                        showMenuForRole();
//                        getNewsCount();
                    } else {
                        ToastUtils.showToast("抱歉！您当前没有此权限");
                    }
                }
            });

            authAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!AppUtils.isBlank(ChildAccount) && !"None".equals(ChildAccount)) {
                        AppUtils.goActivity(mContext, AuthAccountSetMineActivity.class);
                        mPopupWindow.dismiss();
                    } else {
                        ToastUtils.showToast("抱歉！您当前没有此权限");
                    }
                }
            });
        } else {
            mPopupWindow.showAsDropDown(rlUserType, 10, 5);
        }
    }

    public void getActivityResult() {
        MineCode = SharedPreferencesUtil.getString(mContext, "authMineCode");
        isMaster = SharedPreferencesUtil.getBoolean(mContext, "authIsMaster");
        toRequest(NetConstant.EventTags.GET_AUTH_ACCOUNT_PERMISSION);
    }
}
