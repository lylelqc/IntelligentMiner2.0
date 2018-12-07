package com.sly.app.activity.yunw.offline;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.yunw.offline.MachineOfflineRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.PostResult;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.model.yunw.machine.MachineListBean;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;
import com.sly.app.model.yunw.machine.MachineStatusBean;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.Yunw.OfflineCheckPopView;
import com.sly.app.view.iviews.ICommonViewUi;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MachineOfflineActivity extends BaseActivity implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener, ICommonViewUi, OfflineCheckPopView.OnSearchClickListener,
        MachineOfflineRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.ll_comm_layout)
    LinearLayout llComLayout;
    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;
    @BindView(R.id.tv_main_right_left)
    TextView tvMainRightLeft;

    @BindView(R.id.ll_machine_offline_ip_icon)
    LinearLayout llOfflineIpIcon;
    @BindView(R.id.tv_machine_offline_ip_up)
    TextView tvOfflineIpUp;
    @BindView(R.id.tv_machine_offline_ip_low)
    TextView tvOfflineIpLow;

    @BindView(R.id.ll_machine_offline_type_icon)
    LinearLayout llOfflineTypeIcon;
    @BindView(R.id.tv_machine_offline_type_up)
    TextView tvOfflineTypeUp;
    @BindView(R.id.tv_machine_offline_type_low)
    TextView tvOfflineTypeLow;

    @BindView(R.id.ll_machine_offline_status_icon)
    LinearLayout llOfflineStatusIcon;
    @BindView(R.id.tv_machine_offline_status_up)
    TextView tvOfflineStatusUp;
    @BindView(R.id.tv_machine_offline_status_low)
    TextView tvOfflineStatusLow;

    @BindView(R.id.ll_machine_offline_area_icon)
    LinearLayout llOfflineAreaIcon;
    @BindView(R.id.tv_machine_offline_area_up)
    TextView tvOfflineAreaUp;
    @BindView(R.id.tv_machine_offline_area_low)
    TextView tvOfflineAreaLow;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_shadow)
    LinearLayout llShadow;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;

    @BindView(R.id.cb_chose_all)
    CheckBox cbChoseAll;
    @BindView(R.id.tv_offline_count)
    TextView tvOfflineCount;
    @BindView(R.id.tv_machine_offline_btn)
    TextView tvOfflineBtn;

    private String User, Token, FrSysCode, FMasterCode, PersonSysCode, Key, LoginType, MineCode;
    private String areaCode = "";
    private String Model = "";
    private String Worker = "";
    private String minerSysCode = "";
    private String statusCode = "";
    private String machineCode = "";
    private String ip = "";
    private String beginip = "";
    private String endip = "";
    private String orderField = "IP";
    private String orderBy = "ASC";
    private String machineSysCode = "";
    private String reason = "";

    private int mIpCount = 1;
    private int mStatusCount = 0;
    private int mSualiCount = 0;
    private int mAreaCount = 0;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    IRecyclerViewPresenter iRecyclerViewPresenter;
    private List<MachineListBean> mResultList = new ArrayList<>();
    private Set<Integer> indexSet = new TreeSet<>();
    private CommonRequestPresenterImpl iCommonRequestPresenter;

    private List<MachineManageAreaBean> areaList = new ArrayList<>();
    private List<MachineTypeBean> machineTypeList = new ArrayList<>();
    private List<MachineStatusBean> machineStatusList = new ArrayList<>();

    private OfflineCheckPopView mOfflineCheckPopView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_machine_offline;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (EventBusTags.LOGIN_SUCCESS.equals(postResult.getTag())) {

        }
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText(getString(R.string.machine_offline));
        tvMainRightLeft.setText(getString(R.string.repair_check));
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new MyScrollListener());

        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        MineCode = SharedPreferencesUtil.getString(mContext, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "None");

        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        swipeRefreshLayout.setVisibility(View.GONE);

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_YUNW_MACHINE_LIST_STATUS);
        toRequest(NetConstant.EventTags.GET_YUNW_MANAGE_AREA);
        toRequest(NetConstant.EventTags.GET_MACHINE_TYPE);
        firstRefresh();
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

    private void firstRefresh() {

        if (NetUtils.isNetworkConnected(mContext)) {

            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);

                        toRefreshRequest();

                    }
                }, NetWorkCons.Integers.PAGE_LAZY_LOAD_DELAY_TIME_MS);
            }
        } else {
            if (mResultList != null && mResultList.size() > 0) {
                return;
            }
            showRefreshRetry(Contants.NetStatus.NETWORK_MAYBE_DISABLE);
        }
    }

    @Override
    public void clickLoadMoreData() {
        toLoadMoreRequest();
    }

    @Override
    public void toRefreshRequest() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        mCurrentPage = 1;

        Map map = new HashMap();

        map.put("Rounter", NetConstant.GET_MACHINE_LIST);
        map.put("personSysCode", PersonSysCode);
        map.put("areaCode", areaCode);
        map.put("Model", Model);
        map.put("Worker", Worker);
        map.put("ip", ip);
        map.put("minerSysCode", minerSysCode);
        map.put("statusCode", statusCode);
        map.put("machineCode", machineCode);
        map.put("beginip", beginip);
        map.put("endip", endip);
        map.put("orderField", orderField);
        map.put("orderBy", orderBy);

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("pageSize", NetConstant.Request.MACHINE_LIST_PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, NetConstant.BASE_URL, jsonMap);
        Logcat.e("提交参数 - " + jsonMap);
    }

    @Override
    public void toLoadMoreRequest() {
        if (isRequesting)
            return;

        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            iLoadView.showErrorView(loadMoreView);
            return;
        }

        if (mResultList.size() < NetConstant.Request.PAGE_NUMBER) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map map = new HashMap();

        map.put("Rounter", NetConstant.GET_MACHINE_LIST);
        map.put("personSysCode", PersonSysCode);
        map.put("areaCode", areaCode);
        map.put("Model", Model);
        map.put("Worker", Worker);
        map.put("ip", ip);
        map.put("minerSysCode", minerSysCode);
        map.put("statusCode", statusCode);
        map.put("machineCode", machineCode);
        map.put("beginip", beginip);
        map.put("endip", endip);
        map.put("orderField", orderField);
        map.put("orderBy", orderBy);

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("pageSize", NetConstant.Request.MACHINE_LIST_PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, NetConstant.BASE_URL, jsonMap);
        Logcat.e("提交参数 - " + jsonMap);

    }

    @Override
    public void getRefreshData(int eventTag, String result) {
        Logcat.e("返回参数 - " + result);
        List<MachineListBean> resultList =
                (List<MachineListBean>) AppUtils.parseRowsResult(result, MachineListBean.class);

        mResultList.clear();
        if (resultList != null && resultList.size() != 0) {
            mResultList.addAll(resultList);
            pageStatusTextTv.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            showPageStatusView(getString(R.string.request_data));
            cbChoseAll.setChecked(false);
            refreshListView();
        } else {
            pageStatusTextTv.setText(getString(R.string.no_data));
            pageStatusTextTv.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {
        List<MachineListBean> resultList =
                (List<MachineListBean>) AppUtils.parseRowsResult(result, MachineListBean.class);

        if (resultList.size() == 0) {
            iLoadView.showFinishView(loadMoreView);
        }
        mResultList.addAll(resultList);
        cbChoseAll.setChecked(resultList.size() == 0 ? true : false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("personSysCode", PersonSysCode);

        if (eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE) {
            //设备停机
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_STOP_MACHINE);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", getAllMachineSysCode());
            map.put("reason", reason);
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_MANAGE_AREA){
            //负责区域列表
            map.put("Rounter", NetConstant.GET_YUNW_MANAGE_AREA);
        }
        else if(eventTag == NetConstant.EventTags.GET_MACHINE_TYPE){
            //设备型号
            map.put("Rounter", NetConstant.GET_MACHINE_TYPE);
        }else{
            //运维负责的区域状态
            map.put("Rounter", NetConstant.GET_YUNW_MACHINE_LIST_STATUS);
        }
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数 - " + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 - " + result);
        ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
        if (eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE) {
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                ToastUtils.showToast(getString(R.string.comfirm_success));
                indexSet.clear();
                firstRefresh();
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_MANAGE_AREA){
            areaList = (List<MachineManageAreaBean>) AppUtils.parseRowsResult(result, MachineManageAreaBean.class);
        }
        else if(eventTag == NetConstant.EventTags.GET_MACHINE_TYPE){
            machineTypeList = (List<MachineTypeBean>) AppUtils.parseRowsResult(result, MachineTypeBean.class);
        }
        else{
            machineStatusList =
                    (List<MachineStatusBean>) AppUtils.parseRowsResult(result, MachineStatusBean.class);
        }
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        showToastShort(msg);
        if (NetWorkCons.EventTags.HOMEADV == eventTag) {
            return;
        }
        if (mCurrentPage > 1)
            mCurrentPage--;

        if (eventTag == Contants.HttpStatus.loadmore_data) {
            iLoadView.showErrorView(loadMoreView);
        }
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
        showToastShort(msg);
        if (NetWorkCons.EventTags.HOMEADV == eventTag) {
            return;
        }
        if (mCurrentPage > 1)
            mCurrentPage--;

        if (eventTag == Contants.HttpStatus.loadmore_data) {
            iLoadView.showErrorView(loadMoreView);
        }
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        if (NetWorkCons.EventTags.HOMEADV != eventTag && !status) {
            isRequesting = status;
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            ToastUtils.showToast(Contants.NetStatus.NETDISABLE);
            return;
        }
        areaCode = "";
        statusCode = "";
        Model = "";
        Worker = "";
        minerSysCode = "";
        machineCode = "";
        ip = "";
        beginip = "";
        endip = "";

        mIpCount = 1;
        mStatusCount = 0;
        mSualiCount = 0;
        mAreaCount = 0;

        setListHeaderIcon("IP", mIpCount % 2);
        toRefreshRequest();
    }

    public void refreshListView() {
        MachineOfflineRecyclerViewAdapter mIntermediary = new MachineOfflineRecyclerViewAdapter(mContext, mResultList, indexSet);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        recyclerView.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        mIntermediary.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @OnClick({R.id.btn_main_back, R.id.tv_main_right_left, R.id.ll_machine_offline_ip_icon,
            R.id.ll_machine_offline_type_icon, R.id.ll_machine_offline_status_icon, R.id.ll_machine_offline_area_icon,
            R.id.cb_chose_all, R.id.tv_machine_offline_btn, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_main_right_left:
                mOfflineCheckPopView = new OfflineCheckPopView(this, machineTypeList, areaList);
                mOfflineCheckPopView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        llShadow.setVisibility(View.GONE);
                    }
                });
                mOfflineCheckPopView.showFilterPopup(llComLayout);
                mOfflineCheckPopView.setSearchClickListener(this);

                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(170);
                llShadow.setAnimation(appearAnimation);
                llShadow.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_machine_offline_ip_icon:
                mIpCount++;
                setListHeaderIcon("IP", mIpCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_offline_type_icon:
                mSualiCount++;
                setListHeaderIcon("Model", mSualiCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_offline_status_icon:
                mStatusCount++;
                setListHeaderIcon("StatusCode", mStatusCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_offline_area_icon:
                mAreaCount++;
                setListHeaderIcon("AreaName", mAreaCount % 2);
                firstRefresh();
                break;
            case R.id.cb_chose_all:
                checkAll();
                break;
            case R.id.tv_machine_offline_btn:

                if (indexSet.size() > 0) {
                    showCustomDialog(this, R.layout.dialog_stop_machine,
                            2, getString(R.string.request_stop_machine));
                } else {
                    ToastUtils.showToast(getString(R.string.manage_no_chose));
                }
                break;
        }
    }

    private String getAllMachineSysCode() {
        StringBuilder builder = new StringBuilder();
        for (Integer in : indexSet) {
            builder.append(mResultList.get(in).getMachineSysCode() + ",");
        }
        return builder.toString();
    }

    @Override
    public void onItemClick(View view, int position) {
        if(((CheckBox)view).isChecked()){
            if(!indexSet.contains(position)){
                indexSet.add(position);
            }
        }else{
            if(indexSet.contains(position)){
                indexSet.remove(position);
            }
        }
        tvOfflineCount.setText("("+ indexSet.size() + ")");
    }

    // 全选按钮操作
    private void checkAll() {
        indexSet.clear();
        if (cbChoseAll.isChecked()) {
            for (int i = 0; i < mResultList.size(); i++) {
                indexSet.add(i);
            }
        }
        tvOfflineCount.setText("("+ indexSet.size() + ")");
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchClick(View view, int position) {
        if (mOfflineCheckPopView != null) {
            String[] info = mOfflineCheckPopView.getTextInfo();
            machineCode = AppUtils.isBlank(info[0]) ? "" : info[0];
            Worker = AppUtils.isBlank(info[1]) ? "" : info[1];
            ip = AppUtils.isBlank(info[2]) ? "" : info[2];
            beginip = AppUtils.isBlank(info[3]) ? "" : info[3];
            endip = AppUtils.isBlank(info[4]) ? "" : info[4];
            Model = AppUtils.isBlank(info[5]) ? "" : info[5];
            areaCode = AppUtils.isBlank(info[6]) ? "" : info[6];

            firstRefresh();
        }
    }

    private void setListHeaderIcon(String tag, int count) {
        Drawable drawableUp = getResources().getDrawable(R.drawable.monitor_sort_upper);
        Drawable drawableUp1 = getResources().getDrawable(R.drawable.monitor_sort_upper1);
        Drawable drawableLow = getResources().getDrawable(R.drawable.monitor_sort_lower);
        Drawable drawableLow1 = getResources().getDrawable(R.drawable.monitor_sort_lower1);
        if (tag.equals("IP")) {
            orderField = tag;
            if (count == 1) {
                tvOfflineIpUp.setBackground(drawableUp1);
                tvOfflineIpLow.setBackground(drawableLow);
                orderBy = "ASC";
            } else if (count == 0) {
                tvOfflineIpUp.setBackground(drawableUp);
                tvOfflineIpLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvOfflineTypeUp.setBackground(drawableUp);
            tvOfflineTypeLow.setBackground(drawableLow);
            tvOfflineStatusUp.setBackground(drawableUp);
            tvOfflineStatusLow.setBackground(drawableLow);
            tvOfflineAreaUp.setBackground(drawableUp);
            tvOfflineAreaLow.setBackground(drawableLow);
        } else if (tag.equals("Model")) {
            orderField = tag;
            if (count == 1) {
                tvOfflineTypeUp.setBackground(drawableUp1);
                tvOfflineTypeLow.setBackground(drawableLow);
                orderBy = "ASC";
            } else if (count == 0) {
                tvOfflineTypeUp.setBackground(drawableUp);
                tvOfflineTypeLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvOfflineIpUp.setBackground(drawableUp);
            tvOfflineIpLow.setBackground(drawableLow);
            tvOfflineStatusUp.setBackground(drawableUp);
            tvOfflineStatusLow.setBackground(drawableLow);
            tvOfflineAreaUp.setBackground(drawableUp);
            tvOfflineAreaLow.setBackground(drawableLow);
        } else if (tag.equals("StatusCode")) {
            orderField = tag;
            setOrderBy(count);
            if(count == 1 || count == 3){
                tvOfflineStatusUp.setBackground(drawableUp1);
                tvOfflineStatusLow.setBackground(drawableLow);
            } else if (count == 0 || count == 2) {
                tvOfflineStatusUp.setBackground(drawableUp);
                tvOfflineStatusLow.setBackground(drawableLow1);
            }
            tvOfflineIpUp.setBackground(drawableUp);
            tvOfflineIpLow.setBackground(drawableLow);
            tvOfflineTypeUp.setBackground(drawableUp);
            tvOfflineTypeLow.setBackground(drawableLow);
            tvOfflineAreaUp.setBackground(drawableUp);
            tvOfflineAreaLow.setBackground(drawableLow);
        } else if (tag.equals("AreaName")) {
            orderField = tag;
            if (count == 1) {
                tvOfflineAreaUp.setBackground(drawableUp1);
                tvOfflineAreaLow.setBackground(drawableLow);
                orderBy = "ASC";
            } else if (count == 0) {
                tvOfflineAreaUp.setBackground(drawableUp);
                tvOfflineAreaLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvOfflineIpUp.setBackground(drawableUp);
            tvOfflineIpLow.setBackground(drawableLow);
            tvOfflineTypeUp.setBackground(drawableUp);
            tvOfflineTypeLow.setBackground(drawableLow);
            tvOfflineStatusUp.setBackground(drawableUp);
            tvOfflineStatusLow.setBackground(drawableLow);
        }
    }

    private void setOrderBy(int count) {
        if(machineStatusList.size() == 1){
            orderBy = machineStatusList.get(0).getStatusCode();
        }
        else if(machineStatusList.size() == 2){
            if(count == 1){
                orderBy = machineStatusList.get(0).getStatusCode();
            }else{
                orderBy = machineStatusList.get(1).getStatusCode();
            }
        }
        else if(machineStatusList.size() == 3){
            if(count == 1){
                orderBy = machineStatusList.get(0).getStatusCode();
            }else if(count == 2){
                orderBy = machineStatusList.get(1).getStatusCode();
            }else{
                orderBy = machineStatusList.get(2).getStatusCode();
            }
        }
        else if(machineStatusList.size() == 4){
            if(count == 1){
                orderBy = machineStatusList.get(0).getStatusCode();
            }else if(count == 2){
                orderBy = machineStatusList.get(1).getStatusCode();
            }else if(count == 3){
                orderBy = machineStatusList.get(2).getStatusCode();
            }else{
                orderBy = machineStatusList.get(3).getStatusCode();
            }
        }
    }

    // 底部按钮操作提示框
    private void showCustomDialog(Context context, int layout, final int btnType, String content) {
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
        if (layout == R.layout.dialog_general_style) {
            TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
            tvDetails.setText(content);
        }
        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText(content);
        final EditText etContent = dialog.findViewById(R.id.et_dialog_content);

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
                    reason = etContent.getText().toString().trim();
                    toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE);
                }
            }
        });
    }

    public class MyScrollListener extends OnRecyclerViewListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onBottom() {
            toLoadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }

        @Override
        public void getScrollY(int y) {

        }
    }
}
