package com.sly.app.fragment.sly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.sly.mine.yunw.SlyMachineManagerActivity;
import com.sly.app.adapter.MineMachineAdapter;
import com.sly.app.adapter.MyMachineAdapter;
import com.sly.app.adapter.ReplareTaketRecyclerViewAdapter;
import com.sly.app.comm.EventBusTags;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.MachineRunRateBean;
import com.sly.app.model.MineAreaInfoBean;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReplaceListBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.MasterMineBean;
import com.sly.app.model.sly.MyMachineBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.LoadingDialog;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;
import vip.devkit.library.Logcat;
import vip.devkit.library.StringUtil;

import static com.sly.app.utils.AppLog.LogCatW;

public class SlyHostingFragment extends BaseFragment {
    public static String mContent = "???";
    @BindView(R.id.heath_tab)
    TabLayout heathTab;
    @BindView(R.id.viewpager_heath_tab)
    ViewPager viewpagerHeathTab;
    @BindView(R.id.top)
    LinearLayout top;

    private LayoutInflater mInflater;
    private View repareRecode, equirementRecode, machineRecode, machineMonitor, footView;
    MyPagerAdapter mAdapter;
    private List<View> mViewList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private RecyclerView rv1, rv2, rv3;
    private LinearLayout llTpyeName;
//    AssociationAdapter associationAdapter;
//    private Subscription mRxSubscribe;
//    private int pageShow = 0;
//    private RepaireRecyclerViewAdapter mReaireAdapter;
//    private EquipmentListAdapter mEquipmentListAdapter;
//    private List<GoodsBean> mMallRight = new ArrayList<>();
//    private List<RepairBean> mRepaire = new ArrayList<>();
//    private List<EquipmentBean> mMall = new ArrayList<>();
//    private String ProvinceCode = "None";
//    private String CityCode = "None";
//    private String CountryCode = "None";

    private int pageNo = 1;
    private int pageNoRight = 1;
    private String pageSize = "15";
    private int index = 0;
    private String User, Token, Key, LoginType, SysNumber, mineType;

    //矿工维修单
    private List<ReplaceListBean> minerRepairTaketList = new ArrayList<>();
    private ReplareTaketRecyclerViewAdapter minerRepairTaketAdapter;

    //矿工设备
    private List<MyMachineBean> minerMachineList = new ArrayList<>();
    private MyMachineAdapter minerMachineAdapter;

    //矿场主维修单
    private List<ReplaceListBean> masterRepairTaketList = new ArrayList<>();
    private ReplareTaketRecyclerViewAdapter masterRepairTaketAdapter;

    //矿场主设备
    private List<MasterMineBean> masterMachineList = new ArrayList<>();
    private MineMachineAdapter masterMachineAdapter;
    private boolean isRefresh = true;

    //矿机监控
    private List<MachineRunRateBean> mMachineRateList = new ArrayList<>();
    private List<MineAreaInfoBean> mAreaInfoList = new ArrayList<>();
    private TextView tvOnlineMachine1;
    private TextView tvOnlineMachine2;
    private TextView tvOnlineMachine3;
    private TextView tvMonitorRate1;
    private TextView tvMonitorRate2;
    private TextView tvMonitorRate3;
    private LinearLayout llMineAreaInfo;

    private String MineCode;
    private String PersonSysCode;
    private TextView tvPageStatus1;
    private TextView tvPageStatus2;
    private TextView tvPageStatus3;
    private LinearLayout llLoadMore1;
    private LinearLayout llLoadMore2;

    private String eventBusName = "";
    private LoadingDialog loadingDialog;

    public static SlyHostingFragment newInstance(String content) {
        SlyHostingFragment fragment = new SlyHostingFragment();
        mContent = content;
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (!mRxSubscribe.isUnsubscribed()) {
//            mRxSubscribe.unsubscribe();
//        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                update();
                init();
            }
        }
    };

    @Override
    protected void onFirstUserVisible() {
        initProgressDialog("正在加载...", true);
        update();
        init();
    }

    private void initProgressDialog(String str, boolean isOnTouch) {
        if (!getActivity().isFinishing()) {
            loadingDialog = new LoadingDialog(mContext, R.style.hosting_loading_dialog, R.color.white);
            loadingDialog.setCanceledOnTouchOutside(isOnTouch);
            if (StringUtil.isEmpty(str)) {
                loadingDialog.setText("加载中，请稍等...");
            } else {
                loadingDialog.setText(str);
            }
            loadingDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViewsAndEvents() {
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        equirementRecode = mInflater.inflate(R.layout.equirement_recode, null); // 设备
        repareRecode = mInflater.inflate(R.layout.repair_recode, null); // 维修
        machineRecode = mInflater.inflate(R.layout.machine_recode, null);// 矿主设备列表
        machineMonitor = mInflater.inflate(R.layout.activity_monitor, null);

        llTpyeName = equirementRecode.findViewById(R.id.ll_type_name);
        rv1 = equirementRecode.findViewById(R.id.equirement_rv);
        rv2 = repareRecode.findViewById(R.id.tuoguan_repair_rv);
        rv3 = machineRecode.findViewById(R.id.tuoguan_machine_rv);

        tvPageStatus1 = repareRecode.findViewById(R.id.page_status_text_tv);
        tvPageStatus2 = equirementRecode.findViewById(R.id.page_status_text_tv);
        tvPageStatus3 = machineRecode.findViewById(R.id.page_status_text_tv);
        tvPageStatus1.setVisibility(View.GONE);
        tvPageStatus2.setVisibility(View.GONE);
        tvPageStatus3.setVisibility(View.GONE);

        llLoadMore1 = repareRecode.findViewById(R.id.ll_load_more);
        llLoadMore2 = equirementRecode.findViewById(R.id.ll_load_more);
        llLoadMore1.setVisibility(View.GONE);
        llLoadMore2.setVisibility(View.GONE);

        initMonitor();

//        update();
//        init();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_hosting;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (EventBusTags.UPDATE_HOSTING_OPERATION_DATA.equals(postResult.getTag())) {
            startUpdateInfo(EventBusTags.UPDATE_HOSTING_OPERATION_DATA, postResult.getTag());
        }
        else if (EventBusTags.UPDATE_HOSTING_MASTER_DATA.equals(postResult.getTag())) {
            startUpdateInfo(EventBusTags.UPDATE_HOSTING_MASTER_DATA, postResult.getTag());
        }
        else if(EventBusTags.UPDATE_HOSTING_MINER_DATA.equals(postResult.getTag())){
            startUpdateInfo(EventBusTags.UPDATE_HOSTING_MINER_DATA, postResult.getTag());
        }
    }

    private void startUpdateInfo(String eventType, String eventName){
        if(eventBusName.equals(eventType)){
            return;
        }else{
            eventBusName = eventName;
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        update();
    }

    private void initMonitor() {
        tvOnlineMachine1 = machineMonitor.findViewById(R.id.tv_online_machine1);
        tvOnlineMachine2 = machineMonitor.findViewById(R.id.tv_online_machine2);
        tvOnlineMachine3 = machineMonitor.findViewById(R.id.tv_online_machine3);
        tvMonitorRate1 = machineMonitor.findViewById(R.id.tv_monitor_rate1);
        tvMonitorRate2 = machineMonitor.findViewById(R.id.tv_monitor_rate2);
        tvMonitorRate3 = machineMonitor.findViewById(R.id.tv_monitor_rate3);
        llMineAreaInfo = machineMonitor.findViewById(R.id.ll_mine_area_info);
    }

    private void update() {
        User = SharedPreferencesUtil.getString(mContext, "User");
        Token = SharedPreferencesUtil.getString(mContext, "Token");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        if (LoginType.equals("Miner")) {
            SysNumber = SharedPreferencesUtil.getString(mContext, "FrSysCode");
        } else {
            SysNumber = SharedPreferencesUtil.getString(mContext, "FMasterCode");
        }

        mineType = SharedPreferencesUtil.getString(mContext, "mineType");
        mViewList.clear();
        if (CommonUtils.isBlank(User) || CommonUtils.isBlank(Token)) {
//            ToastUtils.showToast("请先登录");
//            CommonUtil2.goActivity(mContext,LoginActivity.class);
            return;
        } else if (mineType.equals("Miner")) {
            mTitleList.clear();
            mTitleList.add("设备记录");
            mTitleList.add("维修记录");
            mViewList.clear();
            mViewList.add(equirementRecode);
            mViewList.add(repareRecode);
            heathTab.removeAllTabs();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            heathTab.addTab(heathTab.newTab().setText(mTitleList.get(0)));
//            heathTab.addTab(heathTab.newTab().setText(mTitleList.get(1)));
            addCustomTab(1);
            initRvAll();
            getEquipmentList(1, rv1);
            getMaintenanceList(1, rv2);
            initScroll();
            initScrollRight();
        } else if (mineType.equals("MinerMaster")) {
            mTitleList.clear();
            mTitleList.add("设备记录");
            mTitleList.add("维修记录");
            mViewList.clear();
            mViewList.add(machineRecode);
            mViewList.add(repareRecode);
            heathTab.removeAllTabs();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            heathTab.addTab(heathTab.newTab().setText(mTitleList.get(0)));
            addCustomTab(1);
            initRvAll();
            getMinerMasterEquipmentList(rv1);
            getMinerMasterMaintenanceList(1, rv2);
            initScroll();
            initScrollRight();
        } else {
            mTitleList.clear();
            mTitleList.add("矿机监控");
            mViewList.clear();
            mViewList.add(machineMonitor);
            heathTab.removeAllTabs();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
                isRefresh = false;
            }
            heathTab.addTab(heathTab.newTab().setText(mTitleList.get(0)));
            getMachineStatus();
            getMineAreaInfo();
        }
    }

    private void addCustomTab(int i) {
        TabLayout.Tab tab = heathTab.newTab();
        //加载自定义的布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_custom_view, null);
        TextView tv1 = view.findViewById(R.id.choose_icon_tab_tv1);
        TextView tv2 = view.findViewById(R.id.choose_icon_tab_tv2);
        tv1.setText(mTitleList.get(i));
        tv2.setText("查询");
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logcat.e("haha, 我被点击了。。。");
            }
        });
        tab.setCustomView(view);
        heathTab.addTab(tab);
    }

    private void init() {
        heathTab.setTabMode(TabLayout.MODE_FIXED);
        //设置分割线
        if (mTitleList.size() > 1) {
            LinearLayout linearLayout = (LinearLayout) heathTab.getChildAt(0);
            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            linearLayout.setDividerDrawable(ContextCompat.getDrawable(mContext, R.drawable.layout_divider_vertical));
            linearLayout.setDividerPadding(CommonUtils.dip2px(mContext, 13));
        }

        heathTab.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        mAdapter = new MyPagerAdapter(mViewList);
        viewpagerHeathTab.setAdapter(mAdapter);
        heathTab.setupWithViewPager(viewpagerHeathTab);
        mAdapter.notifyDataSetChanged();
        viewpagerHeathTab.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab1 = heathTab.getTabAt(1);
                TextView tv2;
                if (tab1 != null) {
                    View view = tab1.getCustomView();
                    if (view != null) {
                        tv2 = view.findViewById(R.id.choose_icon_tab_tv2);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                switch (position) {
                    case 0:
                        //设备列表
                        if (mineType.equals("Miner")) {
                            pageNo = 1;
                            minerMachineList.clear();
                            getEquipmentList(1, rv1);
                            tv2.setVisibility(View.GONE);
                        } else if (mineType.equals("MinerMaster")) {
                            pageNo = 1;
                            masterMachineList.clear();
                            getMinerMasterEquipmentList(rv2);
                            tv2.setVisibility(View.GONE);
                        } else {
                            getMachineStatus();
                            getMineAreaInfo();
                        }
                        break;
                    case 1:
                        //维修单列表
                        if (LoginType.equals("Miner")) {
                            pageNoRight = 1;
                            minerRepairTaketList.clear();
                            getMaintenanceList(1, rv2);
                            tv2.setVisibility(View.VISIBLE);
                        } else if (LoginType.equals("MinerMaster")) {
                            pageNoRight = 1;
                            masterRepairTaketList.clear();
                            getMinerMasterMaintenanceList(1, rv2);
                            tv2.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    //右边tab加载更多
    private void initScrollRight() {
        NestedScrollView scrollViewright = repareRecode.findViewById(R.id.sc_view);
        scrollViewright.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    Logcat.i("------------------" + "Scroll DOWN");
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Logcat.i("------------------" + "BOTTOM SCROLL");
                    pageNoRight += 1;
                    if (LoginType.equals("Miner")) {
                        getMaintenanceList(pageNoRight, rv2);

                    } else {
                        getMinerMasterMaintenanceList(pageNoRight, rv2);
                    }

                }
                if (scrollY <= 0) {
                    Logcat.i("------------------" + "TOP SCROLL");
                } else {
//                    ImmersionBar.with(mActivity).fitsSystemWindows(false).transparentStatusBar().init();
                }
            }
        });
    }

    //左边tab加载更多
    private void initScroll() {
        NestedScrollView scrollView = equirementRecode.findViewById(R.id.sc_view);
        //加载更多
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    Logcat.i("------------------" + "Scroll DOWN");
                } else {
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Logcat.i("------------------" + "BOTTOM SCROLL");
//                    获取数据
                    pageNo += 1;
                    if (LoginType.equals("Miner")) {
                        getEquipmentList(pageNo, rv2);
                    } else {
                        getMinerMasterEquipmentList(rv2);
                    }
//                    getMachineSeatData(SysNumber, Key, Token, LoginType, User, ProvinceCode, CityCode, CountryCode, pageSize, pageNo++);
                }
                // TODO Auto-generated method stub
                if (scrollY <= 0) {
                    Logcat.i("------------------" + "TOP SCROLL");
//                    llCommLayout.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
                }
//                else if (scrollY > 0 && scrollY <= ViewUtils.getViewHeight(llCommLayout)) {
//                    float scale = (float) scrollY / ViewUtils.getViewHeight(llCommLayout);
//                    float alpha = (255 * scale);
////                    llCommLayout.setBackgroundColor(Color.argb((int) alpha, 23, 170, 221));
//                }
                else {
//                    llCommLayout.setBackgroundColor(Color.argb((int) 255, 41, 161, 247));
//                    mRlLayout.setBackground(getResources().getDrawable(R.drawable.home_nav_img_bg));
//                    ImmersionBar.with(mActivity).fitsSystemWindows(false).transparentStatusBar().init();
                }
            }
        });

    }

    //获取矿工设备列表
    private void getEquipmentList(final int pageNo, RecyclerView rv) {

        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", NetWorkCons.GET_MY_MACHINE);
        map.put("User", User);
        map.put("minerSysCode", SysNumber);
        map.put("pageSize", pageSize);
        map.put("pageNo", pageNo + "");
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);

        if(minerMachineList.size() > 15){
            llLoadMore2.setVisibility(View.VISIBLE);
        }

        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                try {
                    LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() != 0) {
                        llTpyeName.setVisibility(View.VISIBLE);
                        tvPageStatus2.setVisibility(View.GONE);
                        List<MyMachineBean> equipmentBeans = JSON.parseArray(mReturnBean.getData().getRows().toString(), MyMachineBean.class);
                        if (pageNo == 1) {
                            minerMachineList.clear();
                        }
                        if(equipmentBeans.size() == 0){
                            minerMachineAdapter.wtihNoListToSetting(false);
                        }
                        minerMachineList.addAll(equipmentBeans);
                        minerMachineAdapter.notifyDataSetChanged();
                    } else {
                        llTpyeName.setVisibility(View.GONE);
                        tvPageStatus2.setVisibility(View.VISIBLE);
                        tvPageStatus2.setText("暂时木有数据哦!");
//                        ToastUtils.showToast(mReturnBean.getMsg());
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                    Logcat.e(e.toString());
                }
                llLoadMore2.setVisibility(View.GONE);
            }
        });
        minerMachineAdapter.notifyDataSetChanged();

    }

    //获取矿工维修单列表
    private void getMaintenanceList(final int pageNo, RecyclerView rv) {

        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Miner.009");
        map.put("User", User);
        map.put("minerSysCode", SysNumber);
        map.put("pageSize", pageSize);
        map.put("pageNo", pageNo + "");

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);

        if(minerRepairTaketList.size() > 15){
            llLoadMore2.setVisibility(View.VISIBLE);
        }

        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                try {
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() != 0) {
                        tvPageStatus1.setVisibility(View.GONE);

                        List<ReplaceListBean> replaceTaketBeans = JSON.parseArray(mReturnBean.getData().getRows().toString(), ReplaceListBean.class);
                        if (pageNo == 1) {
                            minerRepairTaketList.clear();
                        }
                        minerRepairTaketList.addAll(replaceTaketBeans);
                        minerRepairTaketAdapter.notifyDataSetChanged();
                    } else {
                        tvPageStatus1.setVisibility(View.VISIBLE);
                        tvPageStatus1.setText("暂时木有数据哦!");
//                        ToastUtils.showToast(mReturnBean.getMsg());
                    }
                } catch (Exception e) {
                    Logcat.e(e.toString());
                }
                llLoadMore2.setVisibility(View.GONE);
            }
        });
        minerRepairTaketAdapter.notifyDataSetChanged();
    }

    //获取矿场主设备列表
    private void getMinerMasterEquipmentList(RecyclerView rv) {

        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Sly.028");
        map.put("User", User);
        map.put("minerMasterCode", SysNumber);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                try {
                    LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() != 0) {
                        List<MasterMineBean> equipmentBeans = JSON.parseArray(mReturnBean.getData().getRows().toString(), MasterMineBean.class);
                        tvPageStatus3.setVisibility(View.GONE);
                        masterMachineList.clear();
                        masterMachineList.addAll(equipmentBeans);
                        masterMachineAdapter.notifyDataSetChanged();
                    } else {
                        tvPageStatus3.setVisibility(View.VISIBLE);
                        tvPageStatus3.setText("暂时木有数据哦!");
                        ToastUtils.showToast(mReturnBean.getMsg());
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                    Logcat.e(e.toString());
                }
            }
        });
        masterMachineAdapter.notifyDataSetChanged();

    }

    //获取矿场主维修单列表
    private void getMinerMasterMaintenanceList(final int pageNo, RecyclerView rv) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "MineMaster.011");
        map.put("User", User);
        map.put("mineMasterCode", SysNumber);
        map.put("pageSize", pageSize);
        map.put("pageNo", pageNo + "");

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);

        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                LogCatW(this + "---" + NetWorkCons.BASE_URL, json, statusCode, content);
                try {
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() != 0) {
                        tvPageStatus1.setVisibility(View.GONE);
                        List<ReplaceListBean> equipmentBeans = JSON.parseArray(mReturnBean.getData().getRows().toString(), ReplaceListBean.class);
                        if (pageNo == 1) {
                            masterRepairTaketList.clear();
                        }
                        masterRepairTaketList.addAll(equipmentBeans);
                        masterRepairTaketAdapter.notifyDataSetChanged();
                    } else {
                        tvPageStatus1.setVisibility(View.VISIBLE);
                        tvPageStatus1.setText("暂时木有数据哦!");
                        masterRepairTaketAdapter.notifyDataSetChanged();
//                        ToastUtils.showToast(mReturnBean.getMsg());
                    }
                } catch (Exception e) {
                    Logcat.e(e.toString());
                }
                llLoadMore2.setVisibility(View.GONE);
            }
        });
        masterRepairTaketAdapter.notifyDataSetChanged();
    }

    //获取矿机运行状态
    private void getMachineStatus() {
        Map map = new HashMap();

        MineCode = SharedPreferencesUtil.getString(mContext, "MineCode", "");
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "");
        map.put("Rounter", NetWorkCons.GET_MONITOR_RATE);
        map.put("mineCode", MineCode);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("personSysCode", PersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                Logcat.e("获取报单返回码:" + statusCode + "提交参数" + json + "返回参数:" + content);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
//                Logcat.i("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);

                final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                    mMachineRateList = JSON.parseArray(returnBean.getData().toString(), MachineRunRateBean.class);
                    for (MachineRunRateBean bean : mMachineRateList) {
//                            Logcat.e(bean.getStatusCode()+" code");
                        if (bean.getStatusCode().equals("00")) {
                            tvOnlineMachine1.setText(bean.getMachineCount() + "");
                            double rate = bean.getRate() * 100;
                            if (rate == 100) {
                                tvMonitorRate1.setText("100");
                            } else {
                                tvMonitorRate1.setText(String.format("%.2f", rate));
                            }
                        } else if (bean.getStatusCode().equals("01")) {
                            tvOnlineMachine2.setText(bean.getMachineCount() + "");
                            double rate = bean.getRate() * 100;
                            if (rate == 100) {
                                tvMonitorRate2.setText("100");
                            } else {
                                tvMonitorRate2.setText(String.format("%.2f", rate));
                            }
                        } else if (bean.getStatusCode().equals("02")) { //bean.getStatusCode().equals("02") ||
                            tvOnlineMachine3.setText(bean.getMachineCount() + "");
                            double rate = bean.getRate() * 100;
                            if (rate == 100) {
                                tvMonitorRate3.setText("100");
                            } else {
                                tvMonitorRate3.setText(String.format("%.2f", rate));
                            }
                        }
                    }
                } else {
//                    ToastUtils.showToast(returnBean.getMsg());
                    Logcat.e(returnBean.getMsg());
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                dismissProgressDialog();
                Logcat.e("错误信息：" + request.toString() + "/" + e);
            }
        });
    }

    //获取矿机区域信息
    private void getMineAreaInfo() {

        Map map = new HashMap();
        map.put("Rounter", NetWorkCons.GET_MONITOR_AREA_INFO);

        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "");
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("personSysCode", PersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("获取报单返回码:" + statusCode + "提交参数" + json + "返回参数:" + content);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
//                Logcat.i("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);

                final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                    mAreaInfoList = JSON.parseArray(returnBean.getData().toString(), MineAreaInfoBean.class);
                    for (MineAreaInfoBean bean : mAreaInfoList) {
                        addViewItem(bean);
                    }
                } else {
                    ToastUtils.showToast(returnBean.getMsg());
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("错误信息：" + request.toString() + "/" + e);
            }
        });

    }

    private void addViewItem(final MineAreaInfoBean bean) {
        View itemView = View.inflate(mContext, R.layout.item_mine_info, null);
        TextView areaName = itemView.findViewById(R.id.tv_area_name);
        TextView allNum = itemView.findViewById(R.id.tv_all_num);
        TextView normalNum = itemView.findViewById(R.id.tv_normal_num);
        TextView runRate = itemView.findViewById(R.id.tv_run_rate1);
        TextView status = itemView.findViewById(R.id.tv_status);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, SlyMachineManagerActivity.class);
                intent.putExtra("areaCode", bean.getCode());
                startActivity(intent);
            }
        });

        areaName.setText(bean.getName());
        allNum.setText(bean.getTotalCount() + "");
        normalNum.setText(bean.getNormalCount() + "");

        double rate = bean.getRunRate();
        if (rate == 100) {
            runRate.setText("100%");
            status.setBackgroundColor(Color.parseColor("#90d262"));
        } else if (rate == 0) {
            runRate.setText("0%");
            status.setBackgroundColor(Color.parseColor("#ff6e6e"));
        } else {
            runRate.setText(String.format("%.2f", rate) + "%");
        }
        llMineAreaInfo.addView(itemView);
    }

    // 左右添加 recyclerview 适配器
    private void initRvAll() {
        if (mineType.equals("Miner")) {
            MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
            rv1.setLayoutManager(mLayoutManager);
            rv1.setItemAnimator(new DefaultItemAnimator());
            MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
//        lineVertical.setSize(5);
//        lineVertical.setColor(0xFFf4f4f4);

            rv1.addItemDecoration(lineVertical);
            minerMachineAdapter = new MyMachineAdapter(mContext, minerMachineList);
            rv1.setAdapter(minerMachineAdapter);
            minerMachineAdapter.notifyDataSetChanged();

            MyStaggeredGridLayoutManager mLayoutManager2 = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
            rv2.setLayoutManager(mLayoutManager2);
            rv2.setItemAnimator(new DefaultItemAnimator());
            MyGridItemDecoration lineVertical1 = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
            rv2.addItemDecoration(lineVertical1);
            minerRepairTaketAdapter = new ReplareTaketRecyclerViewAdapter(mContext, minerRepairTaketList);
            rv2.setAdapter(minerRepairTaketAdapter);
            minerRepairTaketAdapter.notifyDataSetChanged();
        } else if (mineType.equals("MinerMaster")) {
            MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
            rv3.setLayoutManager(mLayoutManager);
            rv3.setItemAnimator(new DefaultItemAnimator());
            MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
            rv3.addItemDecoration(lineVertical);
            masterMachineAdapter = new MineMachineAdapter(mContext, masterMachineList);
            rv3.setAdapter(masterMachineAdapter);
            masterMachineAdapter.notifyDataSetChanged();

            MyStaggeredGridLayoutManager mLayoutManager2 = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
            rv2.setLayoutManager(mLayoutManager2);
            rv2.setItemAnimator(new DefaultItemAnimator());
            MyGridItemDecoration lineVertical1 = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
            rv2.addItemDecoration(lineVertical1);
            masterRepairTaketAdapter = new ReplareTaketRecyclerViewAdapter(mContext, masterRepairTaketList);
            rv2.setAdapter(masterRepairTaketAdapter);
            masterRepairTaketAdapter.notifyDataSetChanged();
        } else {

        }

        viewpagerHeathTab.setCurrentItem(index);
    }

    //ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = mViewList.get(position);
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position > mViewList.size() - 1)
                return;
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (mineType.equals("Miner") || mineType.equals("MinerMaster")) {
                return mTitleList.get(position);
            } else {
                return mTitleList.get(0);
            }
        }
    }
}
