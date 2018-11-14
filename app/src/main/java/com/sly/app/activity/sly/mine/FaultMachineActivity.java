package com.sly.app.activity.sly.mine;

import android.content.Intent;
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
import com.sly.app.adapter.FaultMachineRecycAdapter;
import com.sly.app.base.Contants;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.model.sly.FaultMachineBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.OnRecyclerViewListener;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.ViewPopup;
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

public class FaultMachineActivity extends BaseActivity implements IRecyclerViewUi, FaultMachineRecycAdapter.OnItemClickListener,
        LoadMoreClickListener {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.page_status_icon_iv)
    ImageView pageStatusIconIv;
    @BindView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;
    @BindView(R.id.rv_machine_manage)
    RecyclerView MachineManagerRv;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String mineCode = "";
    private String model = "";

    HeaderAndFooterRecyclerViewAdapter adapter;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    IRecyclerViewPresenter iRecyclerViewPresenter;

    private List<FaultMachineBean> mResultList = new ArrayList<>();

    int pageSize = 0;
    private int mCurrentPage;
    private boolean isRequesting = false;//标记，是否正在刷新
    private String mRounter;
    private ViewPopup mPopupWindow;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_machine_manager;
    }

    @Override
    protected void initViewsAndEvents() {
        mineCode = getIntent().getExtras().getString("mineCode");
        model = getIntent().getExtras().getString("model");

        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");

        tvMainTitle.setText("故障设备列表");
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        MachineManagerRv.addOnScrollListener(new MyScrollListener());
        firstRefresh();
    }

    private void firstRefresh() {
        if (NetUtils.isNetworkConnected(mContext)) {
            toRefreshRequest();
        } else {
            if (mResultList != null && mResultList.size() > 0) {
                return;
            }
            showRefreshRetry(Contants.NetStatus.NETWORK_MAYBE_DISABLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstRefresh();
    }

    //发送第一次请求
    @Override
    public void toRefreshRequest() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            return;
        }
        mCurrentPage = 1;

        Map<String, String> map = new HashMap<String, String>();
        map.put("Rounter", "Sly.029");
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Token", Token);
        map.put("mineCode", mineCode);
        map.put("model", model);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    //发送更多请求
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
        map.put("Rounter", "Sly.029");
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Token", Token);
        map.put("mineCode", mineCode);
        map.put("model", model);
        map.put("pageSize", NetWorkCons.Request.PAGE_NUMBER + "");
        map.put("pageNo", "" + mCurrentPage);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    //请求返回的数据
    @Override
    public void getRefreshData(int eventTag, String result) {
        Logcat.e("矿机管理返回参数:" + result);
        List<FaultMachineBean> resultList = parseResult(result);

        mResultList.clear();
        if (resultList != null && resultList.size() != 0) {
            mResultList.addAll(resultList);
            pageStatusTextTv.setVisibility(View.GONE);
        } else {
            pageStatusTextTv.setText("暂时木有数据哦！");
            pageStatusTextTv.setVisibility(View.VISIBLE);
        }
        refreshListView();
    }

    //链接适配器
    public void refreshListView() {
        FaultMachineRecycAdapter mIntermediary = new FaultMachineRecycAdapter(mContext, mResultList);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        MachineManagerRv.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(MachineManagerRv, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        MachineManagerRv.setLayoutManager(layoutManager);
        mIntermediary.setOnItemClickListener(this);
    }

    //加载更多
    @Override
    public void getLoadMoreData(int eventTag, String result) {
        Logcat.e("矿机管理返回参数:" + result);
        List<FaultMachineBean> resultList = parseResult(result);

        if (resultList.size() == 0) {
            return;
        }
        mResultList.addAll(resultList);
        adapter.notifyDataSetChanged();
    }

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
        }
    }

    @OnClick({R.id.btn_main_back, R.id.tv_main_title, R.id.tv_main_right})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
        }
    }

    @Override
    public void clickLoadMoreData() {
        toLoadMoreRequest();
    }

    @Override
    public void onItemClick(View view, int position) {
        String machinesyscode = mResultList.get(position).getSyscode();
        Intent intent = new Intent(this, MineMachineDetailActivity.class);
        intent.putExtra("machineSysCode", machinesyscode);
        intent.putExtra("mineCode", mineCode);
        intent.putExtra("isMaster", "true");
        startActivity(intent);
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

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<FaultMachineBean> parseResult(String result) {
//        数据解析
        try {
            SlyReturnListBean mReturnBean = JSON.parseObject(result, SlyReturnListBean.class);
            if (mReturnBean.getStatus().equals("1")) {
                if(mReturnBean.getData().getRows().size() > 0) {
                    List<FaultMachineBean> machineManageBean = JSON.parseArray(mReturnBean.getData().getRows().toString(), FaultMachineBean.class);
                    return machineManageBean;
                }
            } else {
                ToastUtils.showToast(mReturnBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showToast(e.toString());
        }
        return new ArrayList<>();
    }
}
