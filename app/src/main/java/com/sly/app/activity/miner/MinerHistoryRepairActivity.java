package com.sly.app.activity.miner;

import android.graphics.Color;
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
import com.sly.app.adapter.miner.MinerRepairBillRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.yunw.repair.RepairBillBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.Yunw.RepairCheckPopView;
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

public class MinerHistoryRepairActivity extends BaseActivity implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener, RepairCheckPopView.OnSearchClickListener {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.rl_repair_history_check)
    RelativeLayout rlRepairHistoryCheck;
    @BindView(R.id.tv_repair_check)
    TextView tvRepairCheck;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;
    @BindView(R.id.tv_shadow)
    TextView tvShadow;

    private String User, Token, FrSysCode, FMasterCode, PersonSysCode, Key, LoginType, mineType;
    private String machineSysCode = "";
    private String MachineCode = "";
    private String BillNo = "";
    private String begintime1 = "";
    private String begintime2 = "";
    private String endtime1 = "";
    private String endtime2 = "";
    private String status = "";

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;

    IRecyclerViewPresenter iRecyclerViewPresenter;
    private List<RepairBillBean> mResultList = new ArrayList<>();
    private RepairCheckPopView mRepairCheckView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_miner_history_repair;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void initViewsAndEvents() {

        machineSysCode = getIntent().getExtras().getString("machineSysCode");

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
        mineType = SharedPreferencesUtil.getString(mContext, "mineType", "None");
        swipeRefreshLayout.setVisibility(View.GONE);
        tvMainTitle.setText(getString(R.string.repair_history));

        firstRefresh();
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

        map.put("Rounter", NetConstant.GET_MINER_REPAIR_BILL_BY_MACHINE);
        map.put("minerSysCode", FrSysCode);
        map.put("machineSysCode", machineSysCode);
        map.put("machineCode", MachineCode);
        map.put("BillNo", BillNo);
        map.put("begintime1", begintime1);
        map.put("begintime2", begintime2);
        map.put("endtime1", endtime1);
        map.put("endtime2", endtime2);
        map.put("status", status);

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("pageSize", NetConstant.Request.PAGE_NUMBER + "");
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

        if (mResultList.size() < NetWorkCons.Request.PAGE_NUMBER) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map map = new HashMap();

        map.put("Rounter", NetConstant.GET_MINER_REPAIR_BILL_BY_MACHINE);
        map.put("minerSysCode", FrSysCode);
        map.put("machineSysCode", machineSysCode);
        map.put("machineCode", MachineCode);
        map.put("BillNo", BillNo);
        map.put("begintime1", begintime1);
        map.put("begintime2", begintime2);
        map.put("endtime1", endtime1);
        map.put("endtime2", endtime2);
        map.put("status", status);

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("pageSize", NetConstant.Request.PAGE_NUMBER + "");
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
        List<RepairBillBean> resultList =
                (List<RepairBillBean>) AppUtils.parseRowsResult(result, RepairBillBean.class);

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
        List<RepairBillBean> resultList =
                (List<RepairBillBean>) AppUtils.parseRowsResult(result, RepairBillBean.class);

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
        BillNo = "";
        MachineCode = "";
        begintime1 = "";
        begintime2 = "";
        endtime1 = "";
        endtime2 = "";
        status = "";

        toRefreshRequest();
    }

    @OnClick({R.id.btn_main_back, R.id.rl_repair_history_check, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.rl_repair_history_check:
                tvRepairCheck.setTextColor(Color.parseColor("#4789f0"));
                tvRepairCheck.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sousuocolor_weixiudan_icon),
                        null, null, null);
                tvRepairCheck.setCompoundDrawablePadding(AppUtils.dp2px(this, 5));

                mRepairCheckView = new RepairCheckPopView(this, 3, 3);
                mRepairCheckView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        tvShadow.setVisibility(View.GONE);
                        tvRepairCheck.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.sousuo_weixiudan_icon),
                                null, null, null);
                        tvRepairCheck.setCompoundDrawablePadding(AppUtils.dp2px(mContext, 5));
                        tvRepairCheck.setTextColor(getResources().getColor(R.color.sly_text_244153));
                    }
                });
                mRepairCheckView.showFilterPopup(rlRepairHistoryCheck);
                mRepairCheckView.setSearchClickListener(this);

                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(170);
                tvShadow.setAnimation(appearAnimation);
                tvShadow.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onSearchClick(View view, int position) {
        if(mRepairCheckView != null ){
            String[] info = mRepairCheckView.getTextInfo();
            String[] info2 = mRepairCheckView.getTextInfo2();
            BillNo = AppUtils.isBlank(info[0]) ? "" : info[0];
            MachineCode = AppUtils.isBlank(info[1]) ? "" : info[1];
            begintime1 = AppUtils.isBlank(info[2]) ? "" : info[2];
            begintime2 = AppUtils.isBlank(info[3]) ? "" : info[3];
            endtime1 = AppUtils.isBlank(info[4]) ? "" : info[4];
            endtime2 = AppUtils.isBlank(info[5]) ? "" : info[5];

            //状态判断
            if(info[6].equals("true")){
                status = "00";
            }
            if(info[7].equals("true")){
                status = "02";
            }
            if(info2[0].equals("true")){
                status = "03";
            }
            if(info2[1].equals("true")){
                status = "04";
            }
            if(info[3].equals("true")){
                status = "06";
            }

            firstRefresh();
        }
    }

    public void refreshListView() {
        MinerRepairBillRecyclerViewAdapter mIntermediary = new MinerRepairBillRecyclerViewAdapter(mContext, mResultList, true);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        recyclerView.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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
