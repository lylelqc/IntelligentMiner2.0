package com.sly.app.fragment.notice;

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
import com.sly.app.adapter.notice.MinerMasterNoticeOldAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.NetConstant;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.notice.YunwNoticeOldListBean;
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

public class NoticeOldFragment extends BaseFragment implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;

    private String User, Token, FrSysCode, FMasterCode, PersonSysCode, Key, LoginType, mineType;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;

    IRecyclerViewPresenter iRecyclerViewPresenter;
    //    private List<MinerNoticeListBean> mResultList = new ArrayList<>();
//    private List<MinerMasterNoticeListBean> mMinerMasterList = new ArrayList<>();
    private List<YunwNoticeOldListBean> mResultList = new ArrayList<>();

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
        return R.layout.fragment_notice_new;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
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
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "None");

        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        mineType = SharedPreferencesUtil.getString(mContext, "mineType", "None");

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
//        if ("Miner".equals(mineType)) {
//            map.put("Rounter", NetWorkCons.GET_MINER_NOTICE_LIST);
//            map.put("minerSysCode", FrSysCode);
//        }
//        else if ("MinerMaster".equals(mineType)){
//            map.put("Rounter", NetWorkCons.GET_MINEMASTER_NOTICE_LIST);
//            map.put("minerMasterCode", FMasterCode);
//        }else {
        map.put("Rounter", NetConstant.GET_COMMON_NOTICE);
//        }
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
//        if ("Miner".equals(mineType)) {
//            map.put("Rounter", NetWorkCons.GET_MINER_NOTICE_LIST);
//            map.put("minerSysCode", FrSysCode);
//        } else if ("MinerMaster".equals(mineType)){
//            map.put("Rounter", NetWorkCons.GET_MINEMASTER_NOTICE_LIST);
//            map.put("minerMasterCode", FMasterCode);
//        }else {
        map.put("Rounter", NetConstant.GET_COMMON_NOTICE);
//        }
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

        List<YunwNoticeOldListBean> resultList =
                (List<YunwNoticeOldListBean>) AppUtils.parseRowsResult(result, YunwNoticeOldListBean.class);

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
        List<YunwNoticeOldListBean> resultList =
                (List<YunwNoticeOldListBean>) AppUtils.parseRowsResult(result, YunwNoticeOldListBean.class);

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
        toRefreshRequest();
    }

    public void refreshListView() {

        MinerMasterNoticeOldAdapter mIntermediary = new MinerMasterNoticeOldAdapter(mContext, mResultList);
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
