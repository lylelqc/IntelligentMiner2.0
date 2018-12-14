package com.sly.app.activity.master;

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
import com.sly.app.adapter.master.MasterSpareRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.PostResult;
import com.sly.app.model.master.MasterSpareListBean;
import com.sly.app.model.yunw.machine.MachineStatusBean;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.model.yunw.repair.RepairSparesBean;
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
import com.sly.app.view.PopupView.Master.MasterSpareCheckPopView;
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

public class MasterSpareListActivity extends BaseActivity implements ICommonViewUi, IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener, MasterSpareCheckPopView.OnSearchClickListener{

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

    @BindView(R.id.ll_machine_spare_icon)
    LinearLayout llListSpareIcon;
    @BindView(R.id.tv_machine_spare_name_up)
    TextView tvListSpareUp;
    @BindView(R.id.tv_machine_spare_name_low)
    TextView tvListSpareLow;

    @BindView(R.id.ll_machine_num_icon)
    LinearLayout llListNumIcon;
    @BindView(R.id.tv_machine_num_up)
    TextView tvListNumUp;
    @BindView(R.id.tv_machine_num_low)
    TextView tvListNumLow;

    @BindView(R.id.ll_machine_date_icon)
    LinearLayout llListDateIcon;
    @BindView(R.id.tv_machine_date_up)
    TextView tvListDateUp;
    @BindView(R.id.tv_machine_date_low)
    TextView tvListDateLow;

    @BindView(R.id.ll_machine_worker_icon)
    LinearLayout llListWorkerIcon;
    @BindView(R.id.tv_machine_worker_up)
    TextView tvListWorkerUp;
    @BindView(R.id.tv_machine_worker_low)
    TextView tvListWorkerLow;

    @BindView(R.id.ll_machine_type_icon)
    LinearLayout llListTypeIcon;
    @BindView(R.id.tv_machine_type_up)
    TextView tvListTypeUp;
    @BindView(R.id.tv_machine_type_low)
    TextView tvListTypeLow;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_shadow)
    LinearLayout llShadow;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;

    private String User, Token, FrSysCode, FMasterCode, PersonSysCode, Key, LoginType;
    private String MineCode = "";
    private String time1 = "";
    private String time2 = "";
    private String PartID = "";
    private String orderField = "Times";
    private String orderBy = "DESC";

    private int mSpareNameCount = 0;
    private int mSpareNumCount = 0;
    private int mDateCount = 0;
    private int mWorkerCount = 0;
    private int mTypeCount = 0;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    IRecyclerViewPresenter iRecyclerViewPresenter;
    private CommonRequestPresenterImpl iCommonRequestPresenter;
    private List<MasterSpareListBean> mResultList = new ArrayList<>();

    private MasterSpareCheckPopView mMachineCheckView;
    private List<RepairSparesBean> mSpareList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_master_spare_list;
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
        MineCode = getIntent().getExtras().getString("MineCode");

        tvMainTitle.setText(getString(R.string.master_spare));
        tvMainRightLeft.setText(getString(R.string.repair_check));
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
        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_SPARES);
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

        map.put("Rounter", NetConstant.GET_MASTER_SPARE_LIST);
        map.put("mineCode", MineCode);
        map.put("time1", time1);
        map.put("time2", time2);
        map.put("PartID", PartID);
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

        map.put("Rounter", NetConstant.GET_MASTER_SPARE_LIST);
        map.put("mineCode", MineCode);
        map.put("time1", time1);
        map.put("time2", time2);
        map.put("PartID", PartID);
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
        List<MasterSpareListBean> resultList =
                (List<MasterSpareListBean>) AppUtils.parseRowsResult(result, MasterSpareListBean.class);

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
        List<MasterSpareListBean> resultList =
                (List<MasterSpareListBean>) AppUtils.parseRowsResult(result, MasterSpareListBean.class);

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

        time1 = "";
        time2 = "";
        PartID = "";

        mSpareNameCount = 0;
        mSpareNumCount = 0;
        mDateCount = 0;
        mWorkerCount = 0;
        mTypeCount = 0;

        setListHeaderIcon("Times", mSpareNameCount % 2);
        toRefreshRequest();
    }

    public void refreshListView() {
        MasterSpareRecyclerViewAdapter mIntermediary = new MasterSpareRecyclerViewAdapter(mContext, mResultList);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        recyclerView.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @OnClick({R.id.btn_main_back, R.id.tv_main_right_left, R.id.ll_machine_spare_icon, R.id.ll_machine_num_icon,
            R.id.ll_machine_date_icon, R.id.ll_machine_worker_icon, R.id.ll_machine_type_icon, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_main_right_left:
                mMachineCheckView = new MasterSpareCheckPopView(this, mSpareList);
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
            case R.id.ll_machine_spare_icon:
                mSpareNameCount ++;
                setListHeaderIcon("PartName", mSpareNameCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_num_icon:
                mSpareNumCount ++;
                setListHeaderIcon("UseCount", mSpareNumCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_date_icon:
                mDateCount ++;
                setListHeaderIcon("Times", mDateCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_worker_icon:
                mWorkerCount ++;
                setListHeaderIcon("Worker", mWorkerCount % 2);
                firstRefresh();
                break;
            case R.id.ll_machine_type_icon:
                mTypeCount ++;
                setListHeaderIcon("Model", mTypeCount % 2);
                firstRefresh();
                break;
        }
    }

    @Override
    public void onSearchClick(View view, int position) {
        if(mMachineCheckView != null ){
            String[] info = mMachineCheckView.getTextInfo();
            PartID = AppUtils.isBlank(info[0]) ? "" : info[0];
            time1 = AppUtils.isBlank(info[1]) ? "" : info[1];
            time2 = AppUtils.isBlank(info[2]) ? "" : info[2];
            firstRefresh();
        }
    }

    private void setListHeaderIcon(String tag, int count) {
        Drawable drawableUp = getResources().getDrawable(R.drawable.monitor_sort_upper);
        Drawable drawableUp1 = getResources().getDrawable(R.drawable.monitor_sort_upper1);
        Drawable drawableLow = getResources().getDrawable(R.drawable.monitor_sort_lower);
        Drawable drawableLow1 = getResources().getDrawable(R.drawable.monitor_sort_lower1);
        if(tag.equals("PartName")){
            orderField = tag;
            if(count == 1){
                tvListSpareUp.setBackground(drawableUp1);
                tvListSpareLow.setBackground(drawableLow);
                orderBy = "ASC";
            }else if(count == 0){
                tvListSpareUp.setBackground(drawableUp);
                tvListSpareLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvListNumUp.setBackground(drawableUp);
            tvListNumLow.setBackground(drawableLow);
            tvListDateUp.setBackground(drawableUp);
            tvListDateLow.setBackground(drawableLow);
            tvListWorkerUp.setBackground(drawableUp);
            tvListWorkerLow.setBackground(drawableLow);
            tvListTypeUp.setBackground(drawableUp);
            tvListTypeLow.setBackground(drawableLow);
        }else if(tag.equals("UseCount")){
            orderField = tag;
            if(count == 1 ){
                tvListNumUp.setBackground(drawableUp1);
                tvListNumLow.setBackground(drawableLow);
            }else if(count == 0) {
                tvListNumUp.setBackground(drawableUp);
                tvListNumLow.setBackground(drawableLow1);
            }
            tvListSpareUp.setBackground(drawableUp);
            tvListSpareLow.setBackground(drawableLow);
            tvListDateUp.setBackground(drawableUp);
            tvListDateLow.setBackground(drawableLow);
            tvListWorkerUp.setBackground(drawableUp);
            tvListWorkerLow.setBackground(drawableLow);
            tvListTypeUp.setBackground(drawableUp);
            tvListTypeLow.setBackground(drawableLow);
        }else if(tag.equals("Times")){
            orderField = tag;
            if(count == 1){
                tvListDateUp.setBackground(drawableUp1);
                tvListDateLow.setBackground(drawableLow);
                orderBy = "ASC";
            }else if(count == 0){
                tvListDateUp.setBackground(drawableUp);
                tvListDateLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvListSpareUp.setBackground(drawableUp);
            tvListSpareLow.setBackground(drawableLow);
            tvListNumUp.setBackground(drawableUp);
            tvListNumLow.setBackground(drawableLow);
            tvListWorkerUp.setBackground(drawableUp);
            tvListWorkerLow.setBackground(drawableLow);
            tvListTypeUp.setBackground(drawableUp);
            tvListTypeLow.setBackground(drawableLow);
        }else if(tag.equals("Worker")) {
            orderField = tag;
            if (count == 1) {
                tvListWorkerUp.setBackground(drawableUp1);
                tvListWorkerLow.setBackground(drawableLow);
                orderBy = "ASC";
            } else if (count == 0) {
                tvListWorkerUp.setBackground(drawableUp);
                tvListWorkerLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvListSpareUp.setBackground(drawableUp);
            tvListSpareLow.setBackground(drawableLow);
            tvListNumUp.setBackground(drawableUp);
            tvListNumLow.setBackground(drawableLow);
            tvListDateUp.setBackground(drawableUp);
            tvListDateLow.setBackground(drawableLow);
            tvListTypeUp.setBackground(drawableUp);
            tvListTypeLow.setBackground(drawableLow);
        }else if(tag.equals("Model")) {
            orderField = tag;
            if (count == 1) {
                tvListTypeUp.setBackground(drawableUp1);
                tvListTypeLow.setBackground(drawableLow);
                orderBy = "ASC";
            } else if (count == 0) {
                tvListTypeUp.setBackground(drawableUp);
                tvListTypeLow.setBackground(drawableLow1);
                orderBy = "DESC";
            }
            tvListSpareUp.setBackground(drawableUp);
            tvListSpareLow.setBackground(drawableLow);
            tvListNumUp.setBackground(drawableUp);
            tvListNumLow.setBackground(drawableLow);
            tvListDateUp.setBackground(drawableUp);
            tvListDateLow.setBackground(drawableLow);
            tvListWorkerUp.setBackground(drawableUp);
            tvListWorkerLow.setBackground(drawableLow);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("mineCode", MineCode);

        if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_SPARES){
            map.put("Rounter", NetConstant.GET_YUNW_REPAIR_SPARES);
        }

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, jsonMap);
        Logcat.e("提交参数 - "+ jsonMap);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 - "+ result);
        if(eventTag == NetConstant.EventTags.GET_YUNW_REPAIR_SPARES){
            mSpareList =
                    (List<RepairSparesBean>) AppUtils.parseRowsResult(result, RepairSparesBean.class);
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
