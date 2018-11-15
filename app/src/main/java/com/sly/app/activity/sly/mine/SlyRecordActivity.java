package com.sly.app.activity.sly.mine;

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

import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.SlyRecordAdapter;
import com.sly.app.base.Contants;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.model.SlyRecordListBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.NetUtils;
import com.sly.app.listener.OnRecyclerViewListener;
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

public class SlyRecordActivity extends BaseActivity implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
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
    private String User, Token, FrSysCode, FMasterCode, PersonSysCode, Key, LoginType, mCurrencyCode;
    private int pageSize = 50;
    private int pageNo = 1;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    HeaderAndFooterRecyclerViewAdapter adapter;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;


    IRecyclerViewPresenter iRecyclerViewPresenter;
    private List<SlyRecordListBean> mResultList = new ArrayList<>();
    private String mRounter;


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

        tvMainTitle.setText("交易记录");
        Bundle bundle = getIntent().getExtras();
        User = SharedPreferencesUtil.getString(this, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
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

        /**mCurrencyCode 货币编号
         * 矿工：C01  矿场主：C02 **/
        if (LoginType.equals("Miner")) {
            mCurrencyCode = "C01";
            map.put("sys", FrSysCode);
        } else if (LoginType.equals("MinerMaster")) {
            mCurrencyCode = "C02";
            map.put("sys", FMasterCode);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", NetWorkCons.GET_RECOURD_LIST);
        map.put("User", User);
        map.put("currencyCode", mCurrencyCode);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, NetWorkCons.BASE_URL, jsonMap);
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

        /**mCurrencyCode 货币编号
         * 矿工：C01  矿场主：C02 **/
        if (LoginType.equals("Miner")) {
            mCurrencyCode = "C01";
            map.put("sys", FrSysCode);
        } else if (LoginType.equals("MinerMaster")) {
            mCurrencyCode = "C02";
            map.put("sys", FMasterCode);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", NetWorkCons.GET_RECOURD_LIST);
        map.put("User", User);
        map.put("currencyCode", mCurrencyCode);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, NetWorkCons.BASE_URL, jsonMap);
    }

    @Override
    public void getRefreshData(int eventTag, String result) {
        List<SlyRecordListBean> resultList = parseResult(result);

        mResultList.clear();
        if (resultList != null && resultList.size() != 0) {
            mResultList.addAll(resultList);
            pageStatusTextTv.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            showPageStatusView("");
        } else {
            pageStatusTextTv.setText("暂时木有数据哦！");
            pageStatusTextTv.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
        refreshListView();

//        if (mResultList.size() == 0) {
//            showPageStatusView("暂时木有数据哦！");
//        } else {
//            hidePageStatusView();
//        }
    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {
        List<SlyRecordListBean> resultList = parseResult(result);

        if (resultList.size() == 0) {
            iLoadView.showFinishView(loadMoreView);
        }
        mResultList.addAll(resultList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
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

        SlyRecordAdapter mIntermediary = new SlyRecordAdapter (mContext, mResultList);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        recyclerView.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<SlyRecordListBean> parseResult(String result) {
//        数据解析
//        try {
//
//            SlyReturnListBean mReturnBean = JSON.parseObject(result, SlyReturnListBean.class);
//
//            if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
//                List<SlyRecordListBean> SlyRecordListBean = JSON.parseArray(mReturnBean.getData().getRows().toString(), SlyRecordListBean.class);
//
//                return SlyRecordListBean;
//            } else {
//                ToastUtils.showToast(mReturnBean.getMsg());
//            }
//        } catch (Exception e) {
//            ToastUtils.showToast(e.toString());
//        }
        JsonHelper<SlyRecordListBean> dataParser = new JsonHelper<SlyRecordListBean>(
                SlyRecordListBean.class);

        try {
            JSONObject jsonObject = new JSONObject(result);
            String searchResult = jsonObject.getString("data");
//            pageSize = jsonObject.optInt("page_size",0);
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void getReplareList(int pageSize, int pageNo) {
        if (pageNo == 1) mResultList.removeAll(mResultList);
        Map map = new HashMap();


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
