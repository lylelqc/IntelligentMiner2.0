package com.sly.app.fragment.sly.hosting;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.adapter.MineMachineAdapter;
import com.sly.app.adapter.MyMachineAdapter;
import com.sly.app.adapter.ReplareTaketRecyclerViewAdapter;
import com.sly.app.adapter.notice.MinerMasterNoticeOldAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.BusEvent;
import com.sly.app.comm.NetConstant;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.fragment.notice.NoticeOldFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReplaceListBean;
import com.sly.app.model.notice.YunwNoticeOldListBean;
import com.sly.app.model.sly.MasterMineBean;
import com.sly.app.model.sly.MyMachineBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import vip.devkit.library.Logcat;

public class RepairBillFragment extends BaseFragment implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;

    private String User, Token, FrSysCode, FMasterCode, Key, LoginType;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;

    IRecyclerViewPresenter iRecyclerViewPresenter;

    //矿工维修单
    private List<ReplaceListBean> minerRepairTaketList = new ArrayList<>();

    //矿场主维修单
    private List<ReplaceListBean> masterRepairTaketList = new ArrayList<>();

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
    protected int getContentViewLayoutID() {
        return R.layout.fragment_repairbill;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (BusEvent.UPDATE_HOSTING_MASTER_DATA.equals(postResult.getTag())
                || BusEvent.UPDATE_HOSTING_MINER_DATA.equals(postResult.getTag())) {
            LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
            firstRefresh();
        }
    }

    @Override
    protected void initViewsAndEvents() {
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new MyScrollListener());

        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");

        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");

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
            if(LoginType.equals("Miner")){
                if (minerRepairTaketList != null && minerRepairTaketList.size() > 0) {
                    return;
                }
            }else{
                if (masterRepairTaketList != null && masterRepairTaketList.size() > 0) {
                    return;
                }
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
        if ("Miner".equals(LoginType)) {
            map.put("Rounter", NetWorkCons.GET_MINER_REPAIR_TAKET);
            map.put("minerSysCode", FrSysCode);
        }
        else {
            map.put("Rounter", NetWorkCons.GET_MASTER_REPAIR_TAKET);
            map.put("mineMasterCode", FMasterCode);
        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, NetWorkCons.BASE_URL, jsonMap);
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

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map map = new HashMap();
        if ("Miner".equals(LoginType)) {
            map.put("Rounter", NetWorkCons.GET_MINER_REPAIR_TAKET);
            map.put("minerSysCode", FrSysCode);
        }
        else {
            map.put("Rounter", NetWorkCons.GET_MASTER_REPAIR_TAKET);
            map.put("minerMasterCode", FMasterCode);
        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, NetWorkCons.BASE_URL, jsonMap);
        Logcat.e("提交参数 - " + jsonMap);

    }

    @Override
    public void getRefreshData(int eventTag, String result) {
        Logcat.e("返回参数 - " + result);
        if(("Miner").equals(LoginType)){
            List<ReplaceListBean> resultList =
                    (List<ReplaceListBean>) AppUtils.parseRowsResult(result, ReplaceListBean.class);

            minerRepairTaketList.clear();
            if (resultList != null && resultList.size() != 0) {
                minerRepairTaketList.addAll(resultList);
                pageStatusTextTv.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                showPageStatusView(getString(R.string.request_data));
                refreshListView();
            } else {
                pageStatusTextTv.setText(getString(R.string.no_data));
                pageStatusTextTv.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        }else{
            List<ReplaceListBean> resultList =
                    (List<ReplaceListBean>) AppUtils.parseRowsResult(result, ReplaceListBean.class);

            masterRepairTaketList.clear();
            if (resultList != null && resultList.size() != 0) {
                masterRepairTaketList.addAll(resultList);
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
    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {
        Logcat.e("返回参数 - " + result);
        if(("Miner").equals(LoginType)) {
            List<ReplaceListBean> resultList =
                    (List<ReplaceListBean>) AppUtils.parseRowsResult(result, ReplaceListBean.class);

            if (resultList.size() == 0) {
                iLoadView.showFinishView(loadMoreView);
            }
            minerRepairTaketList.addAll(resultList);
            adapter.notifyDataSetChanged();
        }
        else{
            List<ReplaceListBean> resultList =
                    (List<ReplaceListBean>) AppUtils.parseRowsResult(result, ReplaceListBean.class);

            if (resultList.size() == 0) {
                iLoadView.showFinishView(loadMoreView);
            }
            masterRepairTaketList.addAll(resultList);
            adapter.notifyDataSetChanged();
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
        toRefreshRequest();
    }

    public void refreshListView() {
        // 更新页面
        if(("Miner").equals(LoginType)){
            ReplareTaketRecyclerViewAdapter mIntermediary = new ReplareTaketRecyclerViewAdapter(mContext, minerRepairTaketList);
            adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
            recyclerView.setAdapter(adapter);
            if (minerRepairTaketList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
                RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }
        else {
            ReplareTaketRecyclerViewAdapter mIntermediary = new ReplareTaketRecyclerViewAdapter(mContext, masterRepairTaketList);
            adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
            recyclerView.setAdapter(adapter);
            if (masterRepairTaketList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
                RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
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
