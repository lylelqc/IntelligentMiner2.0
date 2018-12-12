package com.sly.app.activity.yunw.machine;

import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.yunw.machine.MachineListRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.PostResult;
import com.sly.app.model.yunw.machine.MachineListBean;
import com.sly.app.model.yunw.machine.MachineStatusBean;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.LoginMsgHelper;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.utils.http.HttpStatusUtil;
import com.sly.app.view.PopupView.Yunw.MachineCheckPopView;
import com.sly.app.view.iviews.ICommonViewUi;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class MachineListActivity extends BaseActivity implements ICommonViewUi, IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener, MachineCheckPopView.OnSearchClickListener{

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

    @BindView(R.id.ll_machine_list_ip_icon)
    LinearLayout llListIpIcon;
    @BindView(R.id.tv_machine_list_ip_up)
    TextView tvListIpUp;
    @BindView(R.id.tv_machine_list_ip_low)
    TextView tvListIpLow;

    @BindView(R.id.ll_machine_list_status_icon)
    LinearLayout llListStatusIcon;
    @BindView(R.id.tv_machine_list_status_up)
    TextView tvListStatusUp;
    @BindView(R.id.tv_machine_list_status_low)
    TextView tvListStatusLow;

    @BindView(R.id.ll_machine_list_suanli_icon)
    LinearLayout llListSuanliIcon;
    @BindView(R.id.tv_machine_list_suanli_up)
    TextView tvListSuanliUp;
    @BindView(R.id.tv_machine_list_suanli_low)
    TextView tvListSuanliLow;

    @BindView(R.id.ll_machine_list_area_icon)
    LinearLayout llListAreaIcon;
    @BindView(R.id.tv_machine_list_area_up)
    TextView tvListAreaUp;
    @BindView(R.id.tv_machine_list_area_low)
    TextView tvListAreaLow;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_shadow)
    LinearLayout llShadow;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;

    private String User, Token, FrSysCode, FMasterCode, PersonSysCode, Key, LoginType;
    private String areaCode = "";
    private String Model = "";
    private String Worker = "";
    private String minerSysCode = "";
    private String statusCode = "";
    private String machineCode = "";
    private String ip = "";
    private String beginip = "";
    private String endip = "";
    private String isstop = "";
    private String orderField = "IP";
    private String orderBy = "ASC";

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
    private CommonRequestPresenterImpl iCommonRequestPresenter;
    private List<MachineListBean> mResultList = new ArrayList<>();

    private boolean isAreaTransfer = false;
    private boolean isStatusTransfer = false;
    private MachineCheckPopView mMachineCheckView;
    private List<MachineTypeBean> machineTypeList = new ArrayList<>();
    private List<MachineStatusBean> machineStatusList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sly2_machine_list;
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
        areaCode = getIntent().getExtras().getString("areaCode");
        statusCode = getIntent().getExtras().getString("statusCode");

        // 点击区域进来
        if(AppUtils.isBlank(areaCode)){
            areaCode = "";
        }else{
            isAreaTransfer = true;
            llListAreaIcon.setClickable(false);
            tvListAreaUp.setVisibility(View.GONE);
            tvListAreaLow.setVisibility(View.GONE);
        }
        // 点击状态进来的
        if(AppUtils.isBlank(statusCode)){
            statusCode = "";
        }else{
            isStatusTransfer = true;
            llListStatusIcon.setClickable(false);
            tvListStatusUp.setVisibility(View.GONE);
            tvListStatusLow.setVisibility(View.GONE);
        }

        tvMainTitle.setText(getString(R.string.machine_list));
        tvMainRightLeft.setText(getString(R.string.repair_check));
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new MyScrollListener());

        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "None");

        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        swipeRefreshLayout.setVisibility(View.GONE);

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_MACHINE_STATUS);
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
        map.put("isstop", "");
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
        map.put("isstop", "");
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
        adapter.notifyDataSetChanged();

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
        areaCode = isAreaTransfer ? areaCode : "";
        statusCode = isStatusTransfer ? statusCode : "";
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
        MachineListRecyclerViewAdapter mIntermediary = new MachineListRecyclerViewAdapter(mContext, mResultList);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        recyclerView.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @OnClick({R.id.btn_main_back, R.id.tv_main_right_left, R.id.ll_machine_list_ip_icon, R.id.ll_machine_list_status_icon,
            R.id.ll_machine_list_suanli_icon, R.id.ll_machine_list_area_icon, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_main_right_left:
                mMachineCheckView = new MachineCheckPopView(this, machineTypeList);
                mMachineCheckView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        llShadow.setVisibility(View.GONE);
                    }
                });
                mMachineCheckView.showFilterPopup(llComLayout);
                mMachineCheckView.setSearchClickListener(this);

                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(170);
                llShadow.setAnimation(appearAnimation);
                llShadow.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_machine_list_ip_icon:
                mIpCount ++;
                setListHeaderIcon("IP", mIpCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_list_status_icon:
                mStatusCount ++;
                setListHeaderIcon("StatusCode", mStatusCount % machineStatusList.size());
                firstRefresh();
                break;
            case R.id.ll_machine_list_suanli_icon:
                mSualiCount ++;
                setListHeaderIcon("NowCal", mSualiCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_list_area_icon:
                mAreaCount ++;
                setListHeaderIcon("AreaName", mAreaCount % 2);
                firstRefresh();
                break;
        }
    }

    @Override
    public void onSearchClick(View view, int position) {
        if(mMachineCheckView != null ){
            String[] info = mMachineCheckView.getTextInfo();
            Model = AppUtils.isBlank(info[0]) ? "" : info[0];
            Worker = AppUtils.isBlank(info[1]) ? "" : info[1];
            minerSysCode = AppUtils.isBlank(info[2]) ? "" : info[2];
            beginip = AppUtils.isBlank(info[3]) ? "" : info[3];
            endip = AppUtils.isBlank(info[4]) ? "" : info[4];

            if(info[5].equals("true")){
                statusCode = "00";
            }else if(info[6].equals("true")){
                statusCode = "01";
            }else if(info[7].equals("true")){
                statusCode = "02";
            }else if(info[8].equals("true")){
                statusCode = "05";
            }else {
                statusCode = "";
            }
            firstRefresh();
        }
    }

    private void setListHeaderIcon(String tag, int count) {
        Drawable drawableUp = getResources().getDrawable(R.drawable.monitor_sort_upper);
        Drawable drawableUp1 = getResources().getDrawable(R.drawable.monitor_sort_upper1);
        Drawable drawableLow = getResources().getDrawable(R.drawable.monitor_sort_lower);
        Drawable drawableLow1 = getResources().getDrawable(R.drawable.monitor_sort_lower1);
        if(tag.equals("IP")){
            orderField = tag;
            if(count == 1){
                tvListIpUp.setBackground(drawableUp1);
                tvListIpLow.setBackground(drawableLow);
                orderBy = "ASC";
            }else if(count == 0){
                tvListIpUp.setBackground(drawableUp);
                tvListIpLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvListStatusUp.setBackground(drawableUp);
            tvListStatusLow.setBackground(drawableLow);
            tvListSuanliUp.setBackground(drawableUp);
            tvListSuanliLow.setBackground(drawableLow);
            tvListAreaUp.setBackground(drawableUp);
            tvListAreaLow.setBackground(drawableLow);
        }else if(tag.equals("StatusCode")){
            orderField = tag;
            setOrderBy(count);
            if(count == 1 || count == 3){
                tvListStatusUp.setBackground(drawableUp1);
                tvListStatusLow.setBackground(drawableLow);
            }else if(count == 0 || count == 2) {
                tvListStatusUp.setBackground(drawableUp);
                tvListStatusLow.setBackground(drawableLow1);
            }
            tvListIpUp.setBackground(drawableUp);
            tvListIpLow.setBackground(drawableLow);
            tvListSuanliUp.setBackground(drawableUp);
            tvListSuanliLow.setBackground(drawableLow);
            tvListAreaUp.setBackground(drawableUp);
            tvListAreaLow.setBackground(drawableLow);
        }else if(tag.equals("NowCal")){
            orderField = tag;
            if(count == 1){
                tvListSuanliUp.setBackground(drawableUp1);
                tvListSuanliLow.setBackground(drawableLow);
                orderBy = "ASC";
            }else if(count == 0){
                tvListSuanliUp.setBackground(drawableUp);
                tvListSuanliLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvListIpUp.setBackground(drawableUp);
            tvListIpLow.setBackground(drawableLow);
            tvListStatusUp.setBackground(drawableUp);
            tvListStatusLow.setBackground(drawableLow);
            tvListAreaUp.setBackground(drawableUp);
            tvListAreaLow.setBackground(drawableLow);
        }else if(tag.equals("AreaName")) {
            orderField = tag;
            if (count == 1) {
                tvListAreaUp.setBackground(drawableUp1);
                tvListAreaLow.setBackground(drawableLow);
                orderBy = "ASC";
            } else if (count == 0) {
                tvListAreaUp.setBackground(drawableUp);
                tvListAreaLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvListIpUp.setBackground(drawableUp);
            tvListIpLow.setBackground(drawableLow);
            tvListStatusUp.setBackground(drawableUp);
            tvListStatusLow.setBackground(drawableLow);
            tvListSuanliUp.setBackground(drawableUp);
            tvListSuanliLow.setBackground(drawableLow);
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

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        if(eventTag == NetConstant.EventTags.GET_MACHINE_TYPE){
            map.put("Rounter", NetConstant.GET_MACHINE_TYPE);
            map.put("personSysCode", PersonSysCode);
        }else {
            map.put("Rounter", NetConstant.GET_MACHINE_STATUS);
        }

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, jsonMap);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(eventTag == NetConstant.EventTags.GET_MACHINE_TYPE){
            machineTypeList =
                    (List<MachineTypeBean>) AppUtils.parseRowsResult(result, MachineTypeBean.class);
        }else {
            machineStatusList =
                    (List<MachineStatusBean>) AppUtils.parseRowsResult(result, MachineStatusBean.class);
        }
    }

    private void toRequestMachineType() {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", NetConstant.GET_MACHINE_TYPE);
        map.put("User", User);
        map.put("personSysCode", PersonSysCode);

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.e("返回参数 - "+content);
                        if(mContext.getResources().getString(R.string.out_of_time).equals(HttpStatusUtil.getStatusMsg(content))){
                            LoginMsgHelper.reLogin(mContext); // 重启到登录页面
                            ToastUtils.showToast(mContext.getResources().getString(R.string.out_of_time));
//                            EventBus.getDefault().post(new PostResult(EventBusTags.LOGOUT));
                        }else{
                            content = content.substring(1, content.length() - 1);
                            content = content.replace("\\", "");
                            machineTypeList =
                                    (List<MachineTypeBean>) AppUtils.parseRowsResult(content, MachineTypeBean.class);
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );
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
