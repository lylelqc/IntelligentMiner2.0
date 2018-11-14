package com.sly.app.activity.sly.mine.notice;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.MinerMasterNoticeAdapter;
import com.sly.app.base.Contants;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.model.MinerNoticeListBean;
import com.sly.app.model.OperationBean;
import com.sly.app.model.sly.MinerMasterNoticeListBean;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.OnRecyclerViewListener;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class SlyNoticeActivity extends BaseActivity implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreClickListener {
    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout llCommLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.page_status_icon_iv)
    ImageView pageStatusIconIv;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;
    @BindView(R.id.refresh_again_tv)
    TextView refreshAgainTv;
    @BindView(R.id.refresh_again_btn)
    CardView refreshAgainBtn;

//    private String User, Token, SysCode, CusCode, Key, LoginType, mCurrencyCode;
    private String User, Token, FrSysCode, FMasterCode, Key, LoginType, mCurrencyCode, mineType;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;


    IRecyclerViewPresenter iRecyclerViewPresenter;
    private List<MinerNoticeListBean> mResultList = new ArrayList<>();
    private List<MinerMasterNoticeListBean> mMinerMasterList = new ArrayList<>();
    private List<OperationBean> mOperationList = new ArrayList<>();
    private String PersonSysCode;

    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sly_business;
    }

    @Override
    protected void initViewsAndEvents() {
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);

        iLoadView = new ILoadViewImpl(mContext, this);

        loadMoreView = iLoadView.inflate();

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.addOnScrollListener(new MyScrollListener());

        tvMainTitle.setText("消息列表");
        Bundle bundle = getIntent().getExtras();
        User = SharedPreferencesUtil.getString(this, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this,"PersonSysCode","None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        mineType = SharedPreferencesUtil.getString(this, "mineType", "None");

        firstRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            if (mMinerMasterList != null && mMinerMasterList.size() > 0) {
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
        if ("Miner".equals(mineType)) {
            map.put("Rounter", NetWorkCons.GET_MINER_NOTICE_LIST);
            map.put("minerSysCode", FrSysCode);
        } else if ("MinerMaster".equals(mineType)){
            map.put("Rounter", NetWorkCons.GET_MINEMASTER_NOTICE_LIST);
            map.put("minerMasterCode", FMasterCode);
        }else {
            map.put("Rounter", NetWorkCons.GET_OPERATION_NOTICE_LIST);
            map.put("personSysCode", PersonSysCode);
        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

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

        if (mResultList.size() < NetWorkCons.Request.PAGE_NUMBER && mMinerMasterList.size() < NetWorkCons.Request.PAGE_NUMBER) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map map = new HashMap();
        if ("Miner".equals(mineType)) {
            map.put("Rounter", NetWorkCons.GET_MINER_NOTICE_LIST);
            map.put("minerSysCode", FrSysCode);
        } else if ("MinerMaster".equals(mineType)){
            map.put("Rounter", NetWorkCons.GET_MINEMASTER_NOTICE_LIST);
            map.put("minerMasterCode", FMasterCode);
        }else {
            map.put("Rounter", NetWorkCons.GET_OPERATION_NOTICE_LIST);
            map.put("personSysCode", PersonSysCode);
        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

//        Map map = new HashMap();
//        map.put("Token", Token);
//        map.put("LoginType", LoginType);
//        map.put("Rounter", NetWorkCons.GET_RECHARGE_LIST);
//        map.put("User", User);
//        map.put("minerSysCode", LoginType.equals("Miner") ? FrSysCode : FMasterCode);
//        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
//        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, NetWorkCons.BASE_URL, jsonMap);
        Logcat.e("提交参数 - " + jsonMap);

    }

    @Override
    public void getRefreshData(int eventTag, String result) {
        Logcat.e("返回参数 - " + result);
        if ("Miner".equals(mineType)) {
            List<MinerNoticeListBean> resultList = parseResult(result);
            mResultList.clear();
            if (resultList != null && resultList.size() != 0) {
                mResultList.addAll(resultList);
                pageStatusTextTv.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                showPageStatusView("请求数据...");
                refreshListView();
            } else {
                pageStatusTextTv.setText("暂时木有数据哦！");
                pageStatusTextTv.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        } else if("MinerMaster".equals(mineType)){
            List<MinerMasterNoticeListBean> resultList = managerParseResult(result);

            mMinerMasterList.clear();
            if (resultList != null && resultList.size() != 0) {
                mMinerMasterList.addAll(resultList);
                pageStatusTextTv.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                showPageStatusView("请求数据...");
                refreshListView();
            } else {
                pageStatusTextTv.setText("暂时木有数据哦！");
                pageStatusTextTv.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        }else {
            List<OperationBean> resultList = operationParseResult(result);

            mOperationList.clear();
            if (resultList != null && resultList.size() != 0) {
                mOperationList.addAll(resultList);
                pageStatusTextTv.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                showPageStatusView("请求数据...");
                refreshListView();
            } else {
                pageStatusTextTv.setText("暂时木有数据哦！");
                pageStatusTextTv.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {
        if ("Miner".equals(mineType)) {
            List<MinerNoticeListBean> resultList = parseResult(result);

            if (resultList.size() == 0) {
                iLoadView.showFinishView(loadMoreView);
            }
            mResultList.addAll(resultList);
            adapter.notifyDataSetChanged();
        } else if ("MinerMaster".equals(mineType)){
            List<MinerMasterNoticeListBean> resultList = managerParseResult(result);

            if (resultList.size() == 0) {
                iLoadView.showFinishView(loadMoreView);
            }
            mMinerMasterList.addAll(resultList);
            adapter.notifyDataSetChanged();
        }else{
            List<OperationBean> resultList = operationParseResult(result);

            if (resultList.size() == 0) {
                iLoadView.showFinishView(loadMoreView);
            }
            mOperationList.addAll(resultList);
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

        if ("Miner".equals(mineType)) {
            MinerMasterNoticeAdapter mIntermediary = new MinerMasterNoticeAdapter(mContext, mResultList, mineType);
            adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
            recyclerView.setAdapter(adapter);
            if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
                RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        } else if ("MinerMaster".equals(mineType)){
            MinerMasterNoticeAdapter mIntermediary = new MinerMasterNoticeAdapter(mContext, mineType, mMinerMasterList);
            adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
            recyclerView.setAdapter(adapter);
            if (mMinerMasterList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
                RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }else{
            MinerMasterNoticeAdapter mIntermediary = new MinerMasterNoticeAdapter(mOperationList, mContext, mineType);
            adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
            recyclerView.setAdapter(adapter);
            if (mMinerMasterList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
                RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<MinerNoticeListBean> parseResult(String result) {
        JsonHelper<MinerNoticeListBean> dataParser = new JsonHelper<MinerNoticeListBean>(
                MinerNoticeListBean.class);

        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
//            String searchResult = jsonObject.getString("data");
            String searchResult = jsonObject.getString("Rows");
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<MinerMasterNoticeListBean> managerParseResult(String result) {
        JsonHelper<MinerMasterNoticeListBean> dataParser = new JsonHelper<MinerMasterNoticeListBean>(
                MinerMasterNoticeListBean.class);

        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
            String searchResult = jsonObject.getString("Rows");
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<OperationBean> operationParseResult(String result) {
        try {
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            return JSON.parseArray(returnBean.getData(), OperationBean.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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
