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
import com.sly.app.adapter.MachineManagerRecycAdapter;
import com.sly.app.base.Contants;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.model.sly.MachineManageBean;
import com.sly.app.model.sly.UserNameBean;
import com.sly.app.model.sly.balance.SlyReturnListBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.OnRecyclerViewListener;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.ViewPopup;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class SlyMachineManagerActivity extends BaseActivity implements IRecyclerViewUi, MachineManagerRecycAdapter.OnItemClickListener,
        ViewPopup.OnSearchClickListener, LoadMoreClickListener {

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
    private String PersonSysCode;
    private String minercode = "";
    private String areacode = "";
    private String modelcode = "";
    private String statuscode = "";
    private String target = "";

    HeaderAndFooterRecyclerViewAdapter adapter;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    IRecyclerViewPresenter iRecyclerViewPresenter;

    private List<MachineManageBean> mResultList = new ArrayList<>();
    private List<UserNameBean> mManageAreaList = new ArrayList<>();
    private List<UserNameBean> mManageAreaTypeList = new ArrayList<>();
    private List<UserNameBean> mManageMachineStatusList = new ArrayList<>();

    int pageSize = 0;
    private int mCurrentPage;
    private boolean isRequesting = false;//标记，是否正在刷新
    private String mRounter;
    private ViewPopup mPopupWindow;
    private boolean isMinerMaster;

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
        areacode = getIntent().getStringExtra("areaCode");
        if(areacode == null){
            areacode = "";
            isMinerMaster = false;
        }else{
            isMinerMaster = true;
        }

        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");

        tvMainTitle.setText("矿机管理");
        tvMainRight.setText("查询");
        tvMainRight.setTextSize(10);
        tvMainRight.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.operation_search), null, null);

        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        MachineManagerRv.addOnScrollListener(new MyScrollListener());
        firstRefresh();
        toRequestManageInfo(NetWorkCons.EventTags.GET_MANAGE_AREA);
        toRequestManageInfo(NetWorkCons.EventTags.GET_MANAGE_AREA_TYPE);
        toRequestManageInfo(NetWorkCons.EventTags.GET_MANAGE_MACHINE_STATUS);
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
        map.put("Rounter", NetWorkCons.GET_MANAGE_MACHINE_LIST);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Token", Token);
        map.put("personSysCode", PersonSysCode);
        map.put("areacode", areacode);
        map.put("modelcode", modelcode);
        map.put("statuscode", statuscode);
        map.put("minerCode", minercode);
        map.put("target", target);
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
        map.put("Rounter", NetWorkCons.GET_MANAGE_MACHINE_LIST);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Token", Token);
        map.put("personSysCode", PersonSysCode);
        map.put("areacode", areacode);
        map.put("modelcode", modelcode);
        map.put("statuscode", statuscode);
        map.put("minerCode", minercode);
        map.put("target", target);
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
        List<MachineManageBean> resultList = parseResult(result);

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
        MachineManagerRecycAdapter mIntermediary = new MachineManagerRecycAdapter(mContext, mResultList);
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
        List<MachineManageBean> resultList = parseResult(result);

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
            case R.id.tv_main_right:
                mPopupWindow = new ViewPopup(this, mManageAreaList, mManageAreaTypeList, mManageMachineStatusList, isMinerMaster);
                mPopupWindow.setSearchClickListener(this);
                mPopupWindow.showFilterPopup(tvMainRight);
                break;
        }
    }

    @Override
    public void clickLoadMoreData() {
        toLoadMoreRequest();
    }

    @Override
    public void onItemClick(View view, int position) {
        String machinesyscode = mResultList.get(position).getMachinesyscode();
        Intent intent = new Intent(SlyMachineManagerActivity.this, MineMachineDetailActivity.class);
        intent.putExtra("machineSysCode", machinesyscode);
        startActivity(intent);
    }

    @Override
    public void onSearchClick(View view, int position) {
        List<Set<Integer>> selectsList = mPopupWindow.getChooseInfo();
        Set<Integer> areaSet = selectsList.get(0);
        Set<Integer> typeSet = selectsList.get(1);
        Set<Integer> statusSet = selectsList.get(2);

        if(areaSet != null && areaSet.size() > 0){
            for(Integer position1 : areaSet){
                areacode = mManageAreaList.get(position1).getCode();
            }
        }else{
            areacode = "";
        }
        if(typeSet != null && typeSet.size() > 0){
            for(Integer position2 : typeSet){
                modelcode = mManageAreaTypeList.get(position2).getCode();
            }
        }else{
            modelcode = "";
        }

        if(statusSet != null && statusSet.size() > 0){
            for(Integer position3 : statusSet){
                statuscode = mManageMachineStatusList.get(position3).getCode();
            }
        }else{
            statuscode = "";
        }

        String[] editsList = mPopupWindow.getEditTextInfo();
        minercode = minercode != null ? editsList[0]: "";
        target = target != null ? editsList[1]: "";
        toRefreshRequest();
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

    private void toRequestManageInfo(final int eventType){
        Map map = new HashMap();
        if (eventType == NetWorkCons.EventTags.GET_MANAGE_AREA) {
            mRounter = NetWorkCons.GET_MANAGE_AREA;
        } else if(eventType == NetWorkCons.EventTags.GET_MANAGE_AREA_TYPE){
            mRounter = NetWorkCons.GET_MANAGE_AREA_TYPE;
        }else if(eventType == NetWorkCons.EventTags.GET_MANAGE_MACHINE_STATUS){
            mRounter = NetWorkCons.GET_MANAGE_MACHINE_STATUS;
        }

        map.put("Rounter", mRounter);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("personSysCode", PersonSysCode);

        Logcat.i("获取信息");
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public boolean onSuccess(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        String backlogJsonStr = content;
                        backlogJsonStr = backlogJsonStr.replace("\\", "");
                        backlogJsonStr = backlogJsonStr.replace("null", "\"null\"");//替换空字符串
                        backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                        Logcat.e("返回码:" + statusCode + "返回参数:" + backlogJsonStr + "提交的内容：" + json + " headers :" + headers);
                        if(eventType == NetWorkCons.EventTags.GET_MANAGE_AREA){
                            final SlyReturnListBean returnBean = JSON.parseObject(backlogJsonStr, SlyReturnListBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                if(returnBean.getData().getRows().size() > 0){
                                    mManageAreaList = JSON.parseArray(returnBean.getData().getRows().toString(), UserNameBean.class);
                                }
                            } else {
                                ToastUtils.showToast(returnBean.getMsg());
                            }
                        } else if(eventType == NetWorkCons.EventTags.GET_MANAGE_AREA_TYPE){
                            final SlyReturnListBean returnBean = JSON.parseObject(backlogJsonStr, SlyReturnListBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                if(returnBean.getData().getRows().size() > 0){
                                    mManageAreaTypeList = JSON.parseArray(returnBean.getData().getRows().toString(), UserNameBean.class);
                                }
                            } else {
                                ToastUtils.showToast(returnBean.getMsg());
                            }
                        } else if(eventType == NetWorkCons.EventTags.GET_MANAGE_MACHINE_STATUS){
                            final SlyReturnListBean returnBean = JSON.parseObject(backlogJsonStr, SlyReturnListBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                if(returnBean.getData().getRows().size() > 0){
                                    mManageMachineStatusList = JSON.parseArray(returnBean.getData().getRows().toString(), UserNameBean.class);
                                }
                            } else {
                                ToastUtils.showToast(returnBean.getMsg());
                            }
                        }
                        return false;
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<MachineManageBean> parseResult(String result) {
//        数据解析
        try {
            SlyReturnListBean mReturnBean = JSON.parseObject(result, SlyReturnListBean.class);
            if (mReturnBean.getStatus().equals("1")) {
                if(mReturnBean.getData().getRows().size() > 0) {
                    List<MachineManageBean> machineManageBean = JSON.parseArray(mReturnBean.getData().getRows().toString(), MachineManageBean.class);
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
