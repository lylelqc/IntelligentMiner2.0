package com.sly.app.activity.sly.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.RepairDocRecyclerAdapter;
import com.sly.app.base.Contants;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.model.sly.RepairDetailsBean;
import com.sly.app.model.sly.RepairDocBean;
import com.sly.app.model.sly.ReturnBean;
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
import com.sly.app.view.PopupView.CheckPopupView;
import com.sly.app.view.PopupView.RepairDetailsPopupView;
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

public class SlyRepairDocActivity extends BaseActivity implements IRecyclerViewUi, RepairDocRecyclerAdapter.OnItemClickListener,
        CheckPopupView.OnSearchClickListener, RepairDetailsPopupView.OnBtnClickListener,LoadMoreClickListener {

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
    RecyclerView RepairDocManagerRv;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String PersonSysCode;
    private String billo = "";
    private String beginptime = "";
    private String endptime = "";
    private String begintime = "";
    private String endtime = "";
    private String status = "";
    private String detailid = "";
    private String ip = "";

    private String clickBillno = "";

    HeaderAndFooterRecyclerViewAdapter adapter;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    IRecyclerViewPresenter iRecyclerViewPresenter;

    private List<RepairDocBean> mResultList = new ArrayList<>();
    private List<UserNameBean> mRepairStatusList = new ArrayList<>();

    int pageSize = 0;
    private int mCurrentPage;
    private boolean isRequesting = false;//标记，是否正在刷新
    private String mRounter;

    private CheckPopupView mCheckPopupWindow;
    private RepairDetailsPopupView mDetailsPopupWindow;
    private Set<Integer> statusSet;


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

        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");

        tvMainTitle.setText("维修单据管理");
        tvMainRight.setText("查询");
        tvMainRight.setTextSize(10);
        tvMainRight.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.operation_search), null, null);

        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext, this);
        loadMoreView = iLoadView.inflate();
        RepairDocManagerRv.addOnScrollListener(new MyScrollListener());
        toRequestInfo(NetWorkCons.EventTags.GET_REPAIR_RESULT_STATUS, null);
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
        map.put("Rounter", NetWorkCons.GET_MANAGE_MACHINE_DOC);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Token", Token);
        map.put("minecode", MineCode);
        map.put("personSysCode", PersonSysCode);
        map.put("billo", billo);
        map.put("ip",ip);
        map.put("beginptime", beginptime);
        map.put("endptime", endptime);
        map.put("begintime", begintime);
        map.put("endtime", endtime);
        map.put("status", status);
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
        map.put("Rounter", NetWorkCons.GET_MANAGE_MACHINE_DOC);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Token", Token);
        map.put("minecode", MineCode);
        map.put("personSysCode", PersonSysCode);
        map.put("billo", billo);
        map.put("ip",ip);
        map.put("beginptime", beginptime);
        map.put("endptime", endptime);
        map.put("begintime", begintime);
        map.put("endtime", endtime);
        map.put("status", status);
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
        Logcat.e("维修单据返回参数:" + result);
        List<RepairDocBean> resultList = parseResult(result);

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
        RepairDocRecyclerAdapter mIntermediary = new RepairDocRecyclerAdapter(mContext, mResultList);
        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);
        RepairDocManagerRv.setAdapter(adapter);
        if (mResultList.size() >= NetWorkCons.Request.PAGE_NUMBER) {
            RecyclerViewUtils.setFooterView(RepairDocManagerRv, loadMoreView);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        RepairDocManagerRv.setLayoutManager(layoutManager);
        mIntermediary.setOnItemClickListener(this);
    }

    //加载更多
    @Override
    public void getLoadMoreData(int eventTag, String result) {
        List<RepairDocBean> resultList = parseResult(result);

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

    @OnClick({R.id.btn_main_back, R.id.tv_main_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_main_right:
                mCheckPopupWindow = new CheckPopupView(this, mRepairStatusList);
                mCheckPopupWindow.setSearchClickListener(this);
                mCheckPopupWindow.showFilterPopup(tvMainRight);
                break;
        }
    }

    @Override
    public void clickLoadMoreData() {
        toLoadMoreRequest();
    }

    @Override
    public void onItemClick(View view, int position) {
        clickBillno = mResultList.get(position).getBillno();
        String resultCode = mResultList.get(position).getResultCode();
//        Intent intent = new Intent(SlyRepairDocActivity.this, MineMachineDetailActivity.class);
//        intent.putExtra("machineSysCode", billNo);
//        startActivity(intent);
        Logcat.e("维修单状态 - " + resultCode);
        toRequestInfo(NetWorkCons.EventTags.GET_REPAIR_RESULT_DETAILS, resultCode);
    }

    @Override
    public void onSearchClick(View view, int position) {
        statusSet = mCheckPopupWindow.getChooseInfo();
        if(statusSet != null && statusSet.size() > 0){
            for(Integer in: statusSet){
                status = mRepairStatusList.get(in).getCode();
            }
        }else{
            status = "";
        }
        //派单时间为空
        String[] etInfo = mCheckPopupWindow.getEditTextInfo();
        if((etInfo[2] != null && !TextUtils.isEmpty(etInfo[2]))
                && (etInfo[2] == null || TextUtils.isEmpty(etInfo[2]))){
            ToastUtils.showToast("请输入起始时间");
            return;
        }
        //结束时间为空
        if((etInfo[4] != null && !TextUtils.isEmpty(etInfo[4]))
                && (etInfo[3] == null || TextUtils.isEmpty(etInfo[3]))){
            ToastUtils.showToast("请输入起始时间");
            return;
        }
        billo = billo != null ? etInfo[0] : "";
        beginptime = beginptime != null ? etInfo[1] : "";
        endptime = endptime != null ? etInfo[2] : "";
        begintime = begintime != null ? etInfo[3] : "";
        endtime = endtime != null ? etInfo[4] : "";
        ip = ip != null ? etInfo[5] : "";
        toRefreshRequest();
        mCheckPopupWindow.dismiss();
    }

    @Override
    public void onBtnClick(View view, int position) {
        if(position == 1){
            Intent intent = new Intent(SlyRepairDocActivity.this, RepairFormActivity.class);
            intent.putExtra("detailid",detailid);
            startActivity(intent);
        }else{
            toRequestInfo(NetWorkCons.EventTags.GET_REPAIR_DEALED, null);
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

    private void toRequestInfo(final int eventType, final String resultCode) {
        Map map = new HashMap();
        if (eventType == NetWorkCons.EventTags.GET_REPAIR_RESULT_STATUS) {
            mRounter = NetWorkCons.GET_REPAIR_RESULT_STATUS;
        }else if(eventType == NetWorkCons.EventTags.GET_REPAIR_RESULT_DETAILS){
            mRounter = NetWorkCons.GET_REPAIR_RESULT_DETAILS;
            map.put("billno", clickBillno);
        }else if(eventType == NetWorkCons.EventTags.GET_REPAIR_DEALED){
            mRounter = NetWorkCons.GET_REPAIR_DEALED;
            map.put("detailid", detailid);
        }

        map.put("Rounter", mRounter);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
//        map.put("personSysCode", PersonSysCode);

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
                        if (eventType == NetWorkCons.EventTags.GET_REPAIR_RESULT_STATUS) {
                            final SlyReturnListBean returnBean = JSON.parseObject(backlogJsonStr, SlyReturnListBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                if (returnBean.getData().getRows().size() > 0) {
                                    mRepairStatusList = JSON.parseArray(returnBean.getData().getRows().toString(), UserNameBean.class);
                                }
                            } else {
                                ToastUtils.showToast(returnBean.getMsg());
                            }
                        } else if (eventType == NetWorkCons.EventTags.GET_REPAIR_RESULT_DETAILS) {
                            final SlyReturnListBean returnBean = JSON.parseObject(backlogJsonStr, SlyReturnListBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                                if (returnBean.getData().getRows().size() > 0) {
                                    List<RepairDetailsBean> list = JSON.parseArray(returnBean.getData().getRows().toString(), RepairDetailsBean.class);
                                    detailid = list.get(0).getDetailid();
                                    Logcat.e("维修单详情 - " + list.get(0).toString());
                                    mDetailsPopupWindow = new RepairDetailsPopupView(SlyRepairDocActivity.this, list.get(0), resultCode);
                                    mDetailsPopupWindow.setOnBtnClickListener(SlyRepairDocActivity.this);
                                    mDetailsPopupWindow.showFilterPopup(tvMainRight);
                                }
                            } else {
                                ToastUtils.showToast(returnBean.getMsg());
                            }
                        } else if (eventType == NetWorkCons.EventTags.GET_REPAIR_DEALED) {
                            final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功") ) {
                                mDetailsPopupWindow.dismiss();
                                new AlertDialog.Builder(SlyRepairDocActivity.this)
                                        .setMessage("提交成功!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            } else {
                                mDetailsPopupWindow.dismiss();
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
    public List<RepairDocBean> parseResult(String result) {
//        数据解析
        try {
            SlyReturnListBean mReturnBean = JSON.parseObject(result, SlyReturnListBean.class);
            if (mReturnBean.getStatus().equals("1")) {
                if (mReturnBean.getData().getRows().size() > 0) {
                    List<RepairDocBean> machineManageBean = JSON.parseArray(mReturnBean.getData().getRows().toString(), RepairDocBean.class);
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
