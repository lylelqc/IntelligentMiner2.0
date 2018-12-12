package com.sly.app.activity.master;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.yunw.goline.GolinePlanActivity;
import com.sly.app.adapter.master.AuthAccountMineRecyclerViewAdapter;
import com.sly.app.adapter.yunw.goline.MachineGolineRecyclerViewAdapter;
import com.sly.app.comm.BusEvent;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.master.MasterMineBean;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import vip.devkit.library.Logcat;

public class AuthAccountSetMineActivity extends BaseActivity implements ICommonViewUi, AuthAccountMineRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.recycler_area_view)
    RecyclerView rvAreaListView;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String ChildAccount;

    private List<MasterMineBean> mMineList = new ArrayList<>();
    ICommonRequestPresenter iCommonRequestPresenter;
    private AuthAccountMineRecyclerViewAdapter adapter;
    private String ChildMobile;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_account_chose_mine;
    }

    @Override
    protected void initViewsAndEvents() {

        tvMainTitle.setText(getString(R.string.master_auth_account));
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        ChildAccount = SharedPreferencesUtil.getString(this, "ChildAccount", "None");

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_AUTH_ACCOUNT_MINE);
    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if("0".equals(count)){
            tvRedNum.setVisibility(View.GONE);
        }else{
            tvRedNum.setText(count);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        if(eventTag == NetConstant.EventTags.GET_AUTH_ACCOUNT_MINE){
            map.put("Rounter", NetConstant.GET_AUTH_ACCOUNT_MINE);
            map.put("Mobile", User);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(eventTag == NetConstant.EventTags.GET_AUTH_ACCOUNT_MINE){
            mMineList =
                    (List<MasterMineBean>) AppUtils.parseRowsResult(result, MasterMineBean.class);
            refreshListView();
        }
    }

    private void refreshListView() {
        MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(3, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        rvAreaListView.setLayoutManager(mLayoutManager);
        rvAreaListView.addItemDecoration(lineVertical);
        rvAreaListView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AuthAccountMineRecyclerViewAdapter(mContext, mMineList);
        rvAreaListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {

    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {

    }

    @Override
    public void isRequesting(int eventTag, boolean status) {

    }

    @OnClick({R.id.btn_main_back, R.id.rl_notice })
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String mineCode = mMineList.get(position).getMineCode();
        SharedPreferencesUtil.putString(mContext, "authMineCode",mineCode);
        SharedPreferencesUtil.putBoolean(mContext, "authIsMaster",false);
        EventBus.getDefault().post(new PostResult(EventBusTags.CHOOSE_AUTH_MINE_AREA));
        finish();
    }
}
