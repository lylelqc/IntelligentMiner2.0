package com.sly.app.activity.mine;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.ReplareTaketRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.model.ReplaceListBean;
import com.sly.app.model.sly.balance.SlyReturnListBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.OnRecyclerViewListener;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ReplareTaketActivity extends BaseActivity implements IRecyclerViewUi, SwipeRefreshLayout.OnRefreshListener,
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
    @BindView(R.id.repaire_rv)
    RecyclerView repaireRv;
    @BindView(R.id.top)
    LinearLayout top;
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
    private List<ReplaceListBean> mResultList = new ArrayList<>();
//    private String User, SysCode, CusCode, Token, Key;
    private String User, FrSysCode, FMasterCode, Token, Key;
    private String LoginType = "None";
    private String mRounter = "";


    private ReplareTaketRecyclerViewAdapter mRecyclerViewAdapter;
    HeaderAndFooterRecyclerViewAdapter adapter;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;

    ICommonRequestPresenter iCommonRequestPresenter;

    IRecyclerViewPresenter iRecyclerViewPresenter;

    int pageSize = 0;
    private int mCurrentPage;
    private boolean isRequesting = false;//标记，是否正在刷新

    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstRefresh();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_repaire_taket;
    }

    @Override
    protected void initViewsAndEvents() {
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        swipeRefreshLayout.setOnRefreshListener(this);
        repaireRv.addOnScrollListener(new MyScrollListener());
        tvMainTitle.setText("维修单");
        User = SharedPreferencesUtil.getString(this, "User");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key");
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

        Map<String, String> map = new HashMap<String, String>();
        if (LoginType.equals("Miner")) {
            mRounter = "Miner.009";
            map.put("minerSysCode", FrSysCode);
        } else {
            mRounter = "MineMaster.011";
            map.put("mineMasterCode", FMasterCode);

        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", mRounter);
        map.put("User", User);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, NetWorkCons.BASE_URL, mapJson);
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

        Map<String, String> map = new HashMap<String, String>();
        if (LoginType.equals("Miner")) {
            mRounter = "Miner.009";
            map.put("minerSysCode", FrSysCode);
        } else {
            mRounter = "MineMaster.011";
            map.put("mineMasterCode", FMasterCode);

        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", mRounter);
        map.put("User", User);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, NetWorkCons.BASE_URL, mapJson);
    }

    @Override
    public void getRefreshData(int eventTag, String result) {
        List<ReplaceListBean> resultList = parseResult(result);

        mResultList.clear();
        if (resultList != null && resultList.size() != 0) {
            mResultList.addAll(resultList);
            pageStatusTextTv.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            pageStatusTextTv.setText("暂时木有数据哦！");
            pageStatusTextTv.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }

        refreshListView();
    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {
        List<ReplaceListBean> resultList = parseResult(result);

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

        ReplareTaketRecyclerViewAdapter mIntermediary = new ReplareTaketRecyclerViewAdapter(mContext, mResultList);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        repaireRv.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(repaireRv, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        repaireRv.setLayoutManager(layoutManager);
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<ReplaceListBean> parseResult(String result) {
//        数据解析
        try {

            SlyReturnListBean mReturnBean = JSON.parseObject(result, SlyReturnListBean.class);

            if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
                List<ReplaceListBean> replaceListBean = JSON.parseArray(mReturnBean.getData().getRows().toString(), ReplaceListBean.class);

                return replaceListBean;
            } else {
                ToastUtils.showToast(mReturnBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showToast(e.toString());
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
