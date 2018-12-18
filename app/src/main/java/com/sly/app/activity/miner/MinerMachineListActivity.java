package com.sly.app.activity.miner;

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
import com.sly.app.adapter.master.MasterMachineListRecyclerViewAdapter;
import com.sly.app.adapter.miner.MinerMachineListRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.PostResult;
import com.sly.app.model.master.MasterMachineListBean;
import com.sly.app.model.master.MasterMachineTypeBean;
import com.sly.app.model.miner.MinerMachineListBean;
import com.sly.app.model.yunw.machine.MachineStatusBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.Master.MasterMachineCheckPopView;
import com.sly.app.view.PopupView.Miner.MinerTypePopView;
import com.sly.app.view.PopupView.Yunw.MachineCheckPopView;
import com.sly.app.view.iviews.ICommonViewUi;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MinerMachineListActivity extends BaseActivity implements ICommonViewUi, IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener, MinerTypePopView.OnTypeClickListener{

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

    @BindView(R.id.ll_mahcine_list_code)
    LinearLayout llListIpIcon;
    @BindView(R.id.tv_machine_list_code_up)
    TextView tvListIpUp;
    @BindView(R.id.tv_machine_list_code_low)
    TextView tvListIpLow;
    @BindView(R.id.l_texts)
    TextView ltexts;
    @BindView(R.id.l_texts2)
    TextView ltexts2;
    @BindView(R.id.l_texts3)
    TextView ltexts3;
    @BindView(R.id.l_texts4)
    TextView ltexts4;


    @BindView(R.id.ll_machcine_list_model)
    LinearLayout llListStatusIcon;
    @BindView(R.id.tv_machine_list_model_low)
    TextView tvListStatusLow;

    @BindView(R.id.ll_machcine_list_state)
    LinearLayout llListSuanliIcon;
    @BindView(R.id.tv_machine_list_state_up)
    TextView tvListSuanliUp;
    @BindView(R.id.tv_machine_list_state_low)
    TextView tvListSuanliLow;

    @BindView(R.id.ll_machcine_list_calc)
    LinearLayout llListAreaIcon;
    @BindView(R.id.tv_machine_list_calc_up)
    TextView tvListAreaUp;
    @BindView(R.id.tv_machine_list_calc_low)
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
    private String Model = "";
    private String machineCode = "";
    private String statusCode = "";
    private String orderField = "NowCal";
    private String orderBy = "ASC";

    private int mIpCount = 0;
    private int mStatusCount = 0;
    private int mSualiCount = 0;
    private int mAreaCount = 1;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    IRecyclerViewPresenter iRecyclerViewPresenter;
    private CommonRequestPresenterImpl iCommonRequestPresenter;
    private List<MinerMachineListBean> mResultList = new ArrayList<>();

    private boolean isStatusTransfer = false;

    private MinerTypePopView mTypeCheckView;
    private List<MasterMachineTypeBean> machineTypeList = new ArrayList<>();

    private List<MachineStatusBean> machineStatusList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_miner_machine_list;
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
        statusCode = getIntent().getExtras().getString("statusCode");

        // 点击状态进来的
        if(AppUtils.isBlank(statusCode)){
            statusCode = "";
        }else{
            isStatusTransfer = true;
            llListSuanliIcon.setClickable(false);
            tvListSuanliUp.setVisibility(View.GONE);
            tvListSuanliLow.setVisibility(View.GONE);
        }

        tvMainTitle.setText(getString(R.string.machine_list));
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new MyScrollListener());

        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");

        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        swipeRefreshLayout.setVisibility(View.GONE);

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_MINER_MACHINE_MODEL);
        toRequest(NetConstant.EventTags.GET_MINER_MACHINE_STATUS);
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

        map.put("Rounter", NetConstant.GET_MINER_MACHINE_LIST);
        map.put("minerSysCode", FrSysCode);
        map.put("Model", Model);
        map.put("machineCode", machineCode);
        map.put("statusCode", statusCode);
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

        map.put("Rounter", NetConstant.GET_MINER_MACHINE_LIST);
        map.put("minerSysCode", FrSysCode);
        map.put("Model", Model);
        map.put("machineCode", machineCode);
        map.put("statusCode", statusCode);
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
        List<MinerMachineListBean> resultList =
                (List<MinerMachineListBean>) AppUtils.parseRowsResult(result, MinerMachineListBean.class);

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
        List<MinerMachineListBean> resultList =
                (List<MinerMachineListBean>) AppUtils.parseRowsResult(result, MinerMachineListBean.class);

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

        statusCode = isStatusTransfer ? statusCode : "";
        Model = "";
        machineCode ="";

        mIpCount = 0;
        mStatusCount = 0;
        mSualiCount = 0;
        mAreaCount = 1;

        setListHeaderIcon("NowCal", mAreaCount % 2);
        toRefreshRequest();
    }

    public void refreshListView() {
        MinerMachineListRecyclerViewAdapter mIntermediary = new MinerMachineListRecyclerViewAdapter(mContext, mResultList);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        recyclerView.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @OnClick({R.id.btn_main_back, R.id.ll_mahcine_list_code, R.id.ll_machcine_list_model,
            R.id.ll_machcine_list_state, R.id.ll_machcine_list_calc, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.ll_mahcine_list_code:
                mIpCount ++;
                setListHeaderIcon("MachineCode", mIpCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machcine_list_model:
                mStatusCount ++;
                setListHeaderIcon("Model", mStatusCount % 2);

                mTypeCheckView = new MinerTypePopView(this, machineTypeList);
                mTypeCheckView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        llShadow.setVisibility(View.GONE);
                    }
                });
                mTypeCheckView.showFilterPopup(llComLayout);
                mTypeCheckView.setTypeSearchClickListener(this);

                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(170);
                llShadow.setAnimation(appearAnimation);
                llShadow.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_machcine_list_state:
                mSualiCount ++;
                setListHeaderIcon("StatusCode", mSualiCount % machineStatusList.size());
                firstRefresh();
                break;
            case R.id.ll_machcine_list_calc:
                mAreaCount ++;
                setListHeaderIcon("NowCal", mAreaCount % 2);
                firstRefresh();
                break;
        }
    }

    @Override
    public void onTypeSearchClick(View view, int position) {
        if(mTypeCheckView != null ){
            Model = mTypeCheckView.getMinerTypeCode();

            firstRefresh();
        }
    }

    private void setListHeaderIcon(String tag, int count) {
        Drawable drawableUp = getResources().getDrawable(R.drawable.monitor_sort_upper);
        Drawable drawableUp1 = getResources().getDrawable(R.drawable.monitor_sort_upper1);
        Drawable drawableLow = getResources().getDrawable(R.drawable.monitor_sort_lower);
        Drawable drawableLow1 = getResources().getDrawable(R.drawable.monitor_sort_lower1);
        int color1 = getResources().getColor(R.color.sly_text_848291);
        int color2 = getResources().getColor(R.color.sly_text_2e2d35);

        if(tag.equals("MachineCode")){
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
            ltexts.setTextColor(color2);
            ltexts2.setTextColor(color1);
            ltexts3.setTextColor(color1);
            ltexts4.setTextColor(color1);
            tvListStatusLow.setBackground(drawableLow);
            tvListSuanliUp.setBackground(drawableUp);
            tvListSuanliLow.setBackground(drawableLow);
            tvListAreaUp.setBackground(drawableUp);
            tvListAreaLow.setBackground(drawableLow);
        }else if(tag.equals("Model")){
            if(count == 1){
                tvListStatusLow.setBackground(drawableUp1);
            }else if(count == 0){
                tvListStatusLow.setBackground(drawableLow);
            }
            ltexts.setTextColor(color1);
            ltexts2.setTextColor(color2);
            ltexts3.setTextColor(color1);
            ltexts4.setTextColor(color1);
            tvListIpUp.setBackground(drawableUp);
            tvListIpLow.setBackground(drawableLow);
            tvListSuanliUp.setBackground(drawableUp);
            tvListSuanliLow.setBackground(drawableLow);
            tvListAreaUp.setBackground(drawableUp);
            tvListAreaLow.setBackground(drawableLow);
        }else if(tag.equals("StatusCode")){
            orderField = tag;
            setOrderBy(count);
            if(count == 1 || count == 3){
                tvListSuanliUp.setBackground(drawableUp1);
                tvListSuanliLow.setBackground(drawableLow);
            }else if(count == 0 || count == 2) {
                tvListSuanliUp.setBackground(drawableUp);
                tvListSuanliLow.setBackground(drawableLow1);
            }
            ltexts.setTextColor(color1);
            ltexts2.setTextColor(color1);
            ltexts3.setTextColor(color2);
            ltexts4.setTextColor(color1);
            tvListIpUp.setBackground(drawableUp);
            tvListIpLow.setBackground(drawableLow);
            tvListStatusLow.setBackground(drawableLow);
            tvListAreaUp.setBackground(drawableUp);
            tvListAreaLow.setBackground(drawableLow);
        }else if(tag.equals("NowCal")) {
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
            ltexts.setTextColor(color1);
            ltexts2.setTextColor(color1);
            ltexts3.setTextColor(color1);
            ltexts4.setTextColor(color2);
            tvListIpUp.setBackground(drawableUp);
            tvListIpLow.setBackground(drawableLow);
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
        map.put("minerSysCode", FrSysCode);

        if(eventTag == NetConstant.EventTags.GET_MINER_MACHINE_MODEL){
            map.put("Rounter", NetConstant.GET_MINER_MACHINE_MODEL);
        }else {
            map.put("Rounter", NetConstant.GET_MINER_MACHINE_STATUS);
        }

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, jsonMap);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(eventTag == NetConstant.EventTags.GET_MINER_MACHINE_MODEL){
            machineTypeList =
                    (List<MasterMachineTypeBean>) AppUtils.parseRowsResult(result, MasterMachineTypeBean.class);
        }
        else if(eventTag == NetConstant.EventTags.GET_MINER_MACHINE_STATUS){
            machineStatusList =
                    (List<MachineStatusBean>) AppUtils.parseRowsResult(result, MachineStatusBean.class);
        }
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
