package com.sly.app.activity.yunw.changepool;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.yunw.machine.MachineChangePoolActivity;
import com.sly.app.adapter.yunw.changepool.MachineSetPoolRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.PostResult;
import com.sly.app.model.yunw.machine.MachineListBean;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.SetPoolCheckPopView;
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

public class MachineSetPoolActivity extends BaseActivity implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener, ICommonViewUi, SetPoolCheckPopView.OnSearchClickListener {

    @BindView(R.id.ll_comm_layout)
    LinearLayout llComLayout;
    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right_left)
    TextView tvMainRightLeft;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

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
    @BindView(R.id.tv_set_pool_count)
    TextView tvSetPoolCount;
    @BindView(R.id.tv_machine_set_pool_btn)
    TextView tvSetPoolBtn;

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
    private String orderField = "MinerSysCode";
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
    private SetPoolCheckPopView mSetPoolCheckPopView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_machine_set_pool;
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
        tvMainTitle.setText(getString(R.string.machine_change_pool));
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

        toRequest(NetConstant.EventTags.GET_YUNW_MANAGE_AREA);
        firstRefresh();
    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if("0".equals(count)){
            tvRedNum.setVisibility(View.GONE);
        }else{
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

        if(eventTag == NetConstant.EventTags.GET_YUNW_MANAGE_AREA){
            //负责区域列表
            map.put("Rounter", NetConstant.GET_YUNW_MANAGE_AREA);
        }
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(eventTag == NetConstant.EventTags.GET_YUNW_MANAGE_AREA){
            areaList = (List<MachineManageAreaBean>) AppUtils.parseRowsResult(result, MachineManageAreaBean.class);
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

        setListHeaderIcon("MinerSysCode", mIpCount % 2);
        toRefreshRequest();
    }

    public void refreshListView() {
        MachineSetPoolRecyclerViewAdapter mIntermediary = new MachineSetPoolRecyclerViewAdapter(mContext, mResultList, indexSet);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        recyclerView.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @OnClick({R.id.btn_main_back, R.id.tv_main_right_left, R.id.ll_machine_offline_ip_icon,
            R.id.ll_machine_offline_type_icon, R.id.ll_machine_offline_status_icon, R.id.ll_machine_offline_area_icon,
            R.id.cb_chose_all, R.id.tv_machine_set_pool_btn, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_main_right_left:
                mSetPoolCheckPopView = new SetPoolCheckPopView(this, areaList);
                mSetPoolCheckPopView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        llShadow.setVisibility(View.GONE);
                    }
                });
                mSetPoolCheckPopView.showFilterPopup(llComLayout);
                mSetPoolCheckPopView.setSearchClickListener(this);

                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(170);
                llShadow.setAnimation(appearAnimation);
                llShadow.setVisibility(View.VISIBLE);
                break;
                //会员号
            case R.id.ll_machine_offline_ip_icon:
                mIpCount++;
                setListHeaderIcon("MinerSysCode", mIpCount % 2);
                firstRefresh();
                break;
                // ip
            case R.id.ll_machine_offline_type_icon:
                mSualiCount++;
                setListHeaderIcon("IP", mSualiCount % 2);
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
            case R.id.tv_machine_set_pool_btn:
                if (indexSet.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("machineSysCodes", getAllMachineSysCodes());
                    AppUtils.goActivity(this, MachineChangePoolActivity.class, bundle);
                } else {
                    ToastUtils.showToast(getString(R.string.manage_no_chose));
                }
                break;
        }
    }

    private String getAllMachineSysCodes() {
        StringBuilder builder = new StringBuilder();
        for (Integer in : indexSet) {
            builder.append(mResultList.get(in).getMachineSysCode() + ",");
        }
        return builder.toString();
    }

    // 全选按钮操作
    private void checkAll() {
        indexSet.clear();
        if (cbChoseAll.isChecked()) {
            for (int i = 0; i < mResultList.size(); i++) {
                indexSet.add(i);
            }
        }
        tvSetPoolCount.setText(indexSet.size()+"");
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchClick(View view, int position) {
        if (mSetPoolCheckPopView != null) {
            String[] info = mSetPoolCheckPopView.getTextInfo();
            areaCode = AppUtils.isBlank(info[0]) ? "" : info[0];
            minerSysCode = AppUtils.isBlank(info[1]) ? "" : info[1];
            Worker = AppUtils.isBlank(info[2]) ? "" : info[2];
            ip = AppUtils.isBlank(info[3]) ? "" : info[3];

            firstRefresh();
        }
    }

    private void setListHeaderIcon(String tag, int count) {
        Drawable drawableUp = getResources().getDrawable(R.drawable.monitor_sort_upper);
        Drawable drawableUp1 = getResources().getDrawable(R.drawable.monitor_sort_upper1);
        Drawable drawableLow = getResources().getDrawable(R.drawable.monitor_sort_lower);
        Drawable drawableLow1 = getResources().getDrawable(R.drawable.monitor_sort_lower1);
        if (tag.equals("MinerSysCode")) {
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
        } else if (tag.equals("IP")) {
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
            if (count == 1) {
                tvOfflineStatusUp.setBackground(drawableUp1);
                tvOfflineStatusLow.setBackground(drawableLow);
                orderBy = "ASC";
            } else if (count == 0) {
                tvOfflineStatusUp.setBackground(drawableUp);
                tvOfflineStatusLow.setBackground(drawableLow1);
                orderBy = "DESC";
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
