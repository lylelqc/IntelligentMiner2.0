package com.sly.app.activity.yunw.machine;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.sly.app.adapter.yunw.machine.MachineManageRecyclerViewAdapter;
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
import com.sly.app.view.PopupView.Yunw.ManageAllChoicePopView;
import com.sly.app.view.PopupView.Yunw.ManageAreaCheckPopView;
import com.sly.app.view.PopupView.Yunw.ManageStatusCheckPopView;
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

public class MachineManageActivity extends BaseActivity implements ICommonViewUi, IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener, ManageAreaCheckPopView.OnSearchClickListener, ManageStatusCheckPopView.OnSearchClickListener,
        ManageAllChoicePopView.OnSearchClickListener{

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

    @BindView(R.id.ll_manage_header)
    LinearLayout llManageHeader;
    @BindView(R.id.rl_manage_all)
    RelativeLayout rlManageAll;
    @BindView(R.id.tv_manage_all)
    TextView tvManageAll;

    @BindView(R.id.ll_manage_status)
    LinearLayout llManageStatus;
    @BindView(R.id.tv_manage_status)
    TextView tvManageStatus;
    @BindView(R.id.tv_manage_status_icon)
    TextView tvManageStatusIcon;

    @BindView(R.id.ll_manage_area)
    LinearLayout llManageArea;
    @BindView(R.id.tv_manage_area)
    TextView tvManageArea;
    @BindView(R.id.tv_manage_area_icon)
    TextView tvManageAreaIcon;

    @BindView(R.id.rl_manage_choose)
    RelativeLayout rlManageChoose;
    @BindView(R.id.tv_manage_choose)
    TextView tvManageChoose;
    @BindView(R.id.tv_manage_choose_icon)
    TextView tvManageChooseIcon;

    @BindView(R.id.tv_manage_all_count)
    TextView tvManageAllCount;

    @BindView(R.id.cb_chose_all)
    CheckBox cbChoseAll;
    @BindView(R.id.tv_manage_change_pool)
    TextView tvManageChangePool;
    @BindView(R.id.tv_manage_start_machine)
    TextView tvManageStartMachine;
    @BindView(R.id.tv_manage_stop_machine)
    TextView tvManageStopMachine;
    @BindView(R.id.tv_manage_cancel_machine)
    TextView tvManageCancelMachine;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_shadow)
    LinearLayout llShadow;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;

    private String User, Token, FrSysCode, FMasterCode, MineCode, PersonSysCode, Key, LoginType;
    private String Model = "";
    private String Worker = "";
    private String areaCode = "";
    private String minerSysCode = "";
    private String statusCode = "";
    private String machineCode = "";
    private String ip = "";
    private String beginip = "";
    private String endip = "";
    private String orderField = "IP";
    private String orderBy = "ASC";

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    IRecyclerViewPresenter iRecyclerViewPresenter;
    private List<MachineListBean> mResultList = new ArrayList<>();

    private MachineManageRecyclerViewAdapter mIntermediary;
    private Set<Integer> indexSet = new TreeSet<>();
    private CommonRequestPresenterImpl iCommonRequestPresenter;
    
    private List<MachineManageAreaBean> areaList = new ArrayList<>();
    private List<MachineTypeBean> machineTypeList = new ArrayList<>();
    private ManageAreaCheckPopView mAreaCheckPopView;
    private ManageStatusCheckPopView mStatusCheckPopView;
    private ManageAllChoicePopView mAllChoicePopView;
    private String reason = "";


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_machine_manage;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (EventBusTags.LOGIN_SUCCESS.equals(postResult.getTag())) {

        }
    }

    @Override
    protected void initViewsAndEvents() {
//        areaCode = getIntent().getExtras().getString("areaCode");
//        statusCode = getIntent().getExtras().getString("statusCode");
//        if(AppUtils.isBlank(areaCode)){
//            areaCode = "";
//        }
//        if(AppUtils.isBlank(statusCode)){
//            statusCode = "";
//        }

        tvMainTitle.setText(getString(R.string.manage_machine));
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
        toRequest(NetConstant.EventTags.GET_YUNW_MANAGE_AREA);
        toRequest(NetConstant.EventTags.GET_MACHINE_TYPE);
        toRequest(NetConstant.EventTags.GET_YUNW_ALL_NUM);

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

    // 列表请求数据
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
//            showPageStatusView(getString(R.string.request_data));
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

    // 非列表请求数据
    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("personSysCode", PersonSysCode);

        if(eventTag == NetConstant.EventTags.GET_YUNW_ALL_NUM){
            //获取总设备数
            map.put("Rounter", NetConstant.GET_YUNW_ALL_NUM);
            map.put("areacode", "");
            map.put("Model", "");
            map.put("Worker", "");
            map.put("minerSysCode", "");
            map.put("statusCode", "");
            map.put("machineCode", "");
            map.put("ip", "");
            map.put("beginip", "");
            map.put("endip", "");
        }else if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE){
            //重启设备
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_START_MACHINE);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", getAllMachineSysCode());
            map.put("reason", getString(R.string.machine_start));
        }else if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE){
            //设备停机
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_STOP_MACHINE);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", getAllMachineSysCode());
            map.put("reason", reason);
        }else if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_CANCEL_MACHINE){
            //设备注销
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_CANCEL_MACHINE);
            map.put("mineCode", MineCode);
            map.put("machineSysCode", getAllMachineSysCode());
            map.put("reason", getString(R.string.machine_cancel));
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_MANAGE_AREA){
            //负责区域列表
            map.put("Rounter", NetConstant.GET_YUNW_MANAGE_AREA);
        }
        else if(eventTag == NetConstant.EventTags.GET_MACHINE_TYPE){
            //设备型号
            map.put("Rounter", NetConstant.GET_MACHINE_TYPE);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
        if(eventTag == NetConstant.EventTags.GET_YUNW_ALL_NUM){
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                tvManageAllCount.setText(returnBean.getData());
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE
                || eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE
                || eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_CANCEL_MACHINE){
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                ToastUtils.showToast(getString(R.string.comfirm_success));
                indexSet.clear();
                firstRefresh();
            }else{
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_YUNW_MANAGE_AREA){
            areaList = (List<MachineManageAreaBean>) AppUtils.parseRowsResult(result, MachineManageAreaBean.class);
        }
        else if(eventTag == NetConstant.EventTags.GET_MACHINE_TYPE){
            machineTypeList = (List<MachineTypeBean>) AppUtils.parseRowsResult(result, MachineTypeBean.class);
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
        orderField = "IP";
        orderBy = "ASC";

        firstRefresh();
    }

    public void refreshListView() {
        mIntermediary = new MachineManageRecyclerViewAdapter(mContext, mResultList, indexSet);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        recyclerView.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @OnClick({R.id.btn_main_back, R.id.rl_manage_all, R.id.ll_manage_status, R.id.ll_manage_area,
            R.id.rl_manage_choose, R.id.cb_chose_all, R.id.tv_manage_change_pool, R.id.tv_manage_start_machine,
            R.id.tv_manage_stop_machine, R.id.tv_manage_cancel_machine, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.rl_manage_all:
                setListHeaderIcon(1);
                onRefresh();
                break;
            case R.id.ll_manage_status:
                setListHeaderIcon(2);
                mStatusCheckPopView = new ManageStatusCheckPopView(this);
                mStatusCheckPopView.setStatusSearchClickListener(this);
                mStatusCheckPopView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        llShadow.setVisibility(View.GONE);
                    }
                });
                mStatusCheckPopView.showFilterPopup(llManageHeader);

                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(170);
                llShadow.setAnimation(appearAnimation);
                llShadow.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_manage_area:
                setListHeaderIcon(3);
                mAreaCheckPopView = new ManageAreaCheckPopView(this, areaList);
                mAreaCheckPopView.setAreaSearchClickListener(this);
                mAreaCheckPopView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        llShadow.setVisibility(View.GONE);
                    }
                });
                mAreaCheckPopView.showFilterPopup(llManageHeader);

                AlphaAnimation appearAnimation1 = new AlphaAnimation(0, 1);
                appearAnimation1.setDuration(170);
                llShadow.setAnimation(appearAnimation1);
                llShadow.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_manage_choose:
                setListHeaderIcon(4);
                mAllChoicePopView = new ManageAllChoicePopView(this, areaList, machineTypeList);
                mAllChoicePopView.setAllChoiceSearchClickListener(this);
                mAllChoicePopView.showFilterPopup(llManageHeader);
                break;
            case R.id.cb_chose_all:
                checkAll();
                break;
            case R.id.tv_manage_change_pool:
                if(indexSet.size() > 0){
                    String machineSysCodes = getAllMachineSysCode();
                    Bundle bundle = new Bundle();
                    bundle.putString("machineSysCodes", machineSysCodes);
                    AppUtils.goActivity(this, MachineChangePoolActivity.class, bundle);
                }else{
                    ToastUtils.showToast(getString(R.string.manage_no_chose));
                }
                break;
            case R.id.tv_manage_start_machine:
                if(indexSet.size() > 0){
                    showCustomDialog(this, R.layout.dialog_general_style, 2, getString(R.string.request_start_machine), 1);
                }else{
                    ToastUtils.showToast(getString(R.string.manage_no_chose));
                }
                break;
            case R.id.tv_manage_stop_machine:
                if(indexSet.size() > 0){
                    showCustomDialog(this, R.layout.dialog_stop_machine, 2, getString(R.string.request_stop_machine), 2);
                }else{
                    ToastUtils.showToast(getString(R.string.manage_no_chose));
                }
                break;
            case R.id.tv_manage_cancel_machine:
                if(indexSet.size() > 0){
                    showCustomDialog(this, R.layout.dialog_general_style, 2, getString(R.string.request_cancel_machine), 3);
                }else{
                    ToastUtils.showToast(getString(R.string.manage_no_chose));
                }
                break;
        }
    }

    @Override
    public void onAreaSearchClick(View view, int position) {
        if(mAreaCheckPopView != null ){
            areaCode = mAreaCheckPopView.getManageAreaCode();
            statusCode = "";
            Model = "";
            Worker = "";
            minerSysCode = "";
            firstRefresh();
        }
    }

    @Override
    public void onStatusSearchClick(View view, int position) {
        if(mStatusCheckPopView != null ){
            String[] info = mStatusCheckPopView.getStatusInfo();
            if(info[0].equals("true")){
                statusCode = "01";
            }else if(info[1].equals("true")){
                statusCode = "02";
            }else if(info[2].equals("true")){
                statusCode = "00";
            }else if(info[3].equals("true")){
                statusCode = "05";
            }else {
                statusCode = "";
            }
            areaCode = "";
            Model = "";
            Worker = "";
            minerSysCode = "";
            firstRefresh();
        }
    }

    @Override
    public void onAllChoiceSearchClick(View view, int position) {
        if(mAllChoicePopView != null){
            areaCode = mAllChoicePopView.getAllAreaCode();
            Model = mAllChoicePopView.getAllTypeSet();

            String[] info = mAllChoicePopView.getTextInfo();
            Worker = AppUtils.isBlank(info[0]) ? "" : info[0];
            minerSysCode = AppUtils.isBlank(info[1]) ? "" : info[1];

            if(info[2].equals("true")){
                statusCode = "00";
            }else if(info[3].equals("true")){
                statusCode = "01";
            }else if(info[4].equals("true")){
                statusCode = "02";
            }else if(info[5].equals("true")){
                statusCode = "05";
            }else {
                statusCode = "";
            }
            firstRefresh();
        }
    }

    // 头部按钮点击替换图标颜色
    private void setListHeaderIcon(int tag) {
        Drawable drawableUp = getResources().getDrawable(R.drawable.jiantou_guanli_icon);
        Drawable drawableLow = getResources().getDrawable(R.drawable.nojiantou_guanli_icon);
        Drawable drawableChose = getResources().getDrawable(R.drawable.shaixuan_guanli_icon);
        Drawable drawableNochose = getResources().getDrawable(R.drawable.noshaixuan_guanli_icon);
        int defalutColor = getResources().getColor(R.color.sly_text_244153);
        int buleColor = getResources().getColor(R.color.sly_text_1890ff);

        if(tag == 1){
            tvManageAll.setTextColor(buleColor);
            tvManageStatus.setTextColor(defalutColor);
            tvManageStatusIcon.setBackground(drawableLow);
            tvManageArea.setTextColor(defalutColor);
            tvManageAreaIcon.setBackground(drawableLow);
            tvManageChoose.setTextColor(defalutColor);
            tvManageChooseIcon.setBackground(drawableNochose);
        }else if(tag == 2){
            tvManageAll.setTextColor(defalutColor);
            tvManageStatus.setTextColor(buleColor);
            tvManageStatusIcon.setBackground(drawableUp);
            tvManageArea.setTextColor(defalutColor);
            tvManageAreaIcon.setBackground(drawableLow);
            tvManageChoose.setTextColor(defalutColor);
            tvManageChooseIcon.setBackground(drawableNochose);
        }
        else if(tag == 3){
            tvManageAll.setTextColor(defalutColor);
            tvManageStatus.setTextColor(defalutColor);
            tvManageStatusIcon.setBackground(drawableLow);
            tvManageArea.setTextColor(buleColor);
            tvManageAreaIcon.setBackground(drawableUp);
            tvManageChoose.setTextColor(defalutColor);
            tvManageChooseIcon.setBackground(drawableNochose);
        }else if(tag == 4){
            tvManageAll.setTextColor(defalutColor);
            tvManageStatus.setTextColor(defalutColor);
            tvManageStatusIcon.setBackground(drawableLow);
            tvManageArea.setTextColor(defalutColor);
            tvManageAreaIcon.setBackground(drawableLow);
            tvManageChoose.setTextColor(buleColor);
            tvManageChooseIcon.setBackground(drawableChose);
        }else{
            tvManageAll.setTextColor(defalutColor);
            tvManageStatus.setTextColor(defalutColor);
            tvManageStatusIcon.setBackground(drawableLow);
            tvManageArea.setTextColor(defalutColor);
            tvManageAreaIcon.setBackground(drawableLow);
            tvManageChoose.setTextColor(defalutColor);
            tvManageChooseIcon.setBackground(drawableNochose);
        }
    }

    // 获取所有矿机系统编号
    private String getAllMachineSysCode() {
        StringBuilder builder = new StringBuilder();
        for (Integer in : indexSet){
            builder.append(mResultList.get(in).getMachineSysCode()+",");
        }
        return builder.toString();
    }

    // 全选按钮操作
    private void checkAll() {
        indexSet.clear();
        if(cbChoseAll.isChecked()){
            for(int i = 0; i < mResultList.size(); i++){
                indexSet.add(i);
            }
        }
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    // 底部按钮操作提示框
    private void showCustomDialog(Context context, int layout, final int btnType, String content, final int tag){
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
        // 获取控件
        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        if(layout == R.layout.dialog_general_style){
            TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
            tvDetails.setText(content);
        }
        else if(layout == R.layout.dialog_stop_machine){
            title.setText(content);
        }
        final EditText etDescriptions = dialog.findViewById(R.id.et_dialog_content);

        TextView cancel = dialog.findViewById(R.id.cancel_action);
        TextView confirm = dialog.findViewById(R.id.confirm_action);
        View line = dialog.findViewById(R.id.view_line);
        if(btnType == 1){
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
                if(btnType != 1){
                    if(tag == 1){
                        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_START_MACHINE);
                    }else if(tag == 2){
                        reason = etDescriptions.getText().toString().trim();
                        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_STOP_MACHINE);
                    }else if(tag == 3){
                        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_CANCEL_MACHINE);
                    }
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
