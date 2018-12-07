package com.sly.app.activity.sly.mine;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.MachineOnlineListRecyclerAdapter;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.sly.MachineOnlineListBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class SlyOnlineListActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.machine_online_list_rv)
    RecyclerView mOnlineListRv;

    private List<MachineOnlineListBean> mResultList = new ArrayList<MachineOnlineListBean>();
    ICommonRequestPresenter iCommonRequestPresenter;

    private MachineOnlineListRecyclerAdapter adapter;

    private String User,LoginType, MineCode, PersonSysCode, Token, Key;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_online_list;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        tvMainTitle.setText("矿机上线");
        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");
        toRequest(NetWorkCons.EventTags.GET_MACHINE_ONLINE_LIST);
    }

    @Override
    public void toRequest(int eventTag) {
        if (NetWorkCons.EventTags.GET_MACHINE_ONLINE_LIST == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_MACHINE_ONLINE_LIST);
            map.put("User", User);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);


            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_MACHINE_ONLINE_LIST, mContext, NetWorkCons.BASE_URL, mapJson);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("上机计划返回参数:" + result);
        if(NetWorkCons.EventTags.GET_MACHINE_ONLINE_LIST == eventTag){
            List<MachineOnlineListBean> resultList = parseResult(result);

            mResultList.clear();
            if (resultList != null && resultList.size() != 0) {
                mResultList.addAll(resultList);
            }
            refreshListView();
        }
    }

    private void refreshListView() {
        adapter = new MachineOnlineListRecyclerAdapter(mContext, mResultList);
        mOnlineListRv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mOnlineListRv.setItemAnimator(new DefaultItemAnimator());
        mOnlineListRv.setLayoutManager(layoutManager);
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

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<MachineOnlineListBean> parseResult(String result) {
//        数据解析
        JsonHelper<MachineOnlineListBean> dataParser = new JsonHelper<MachineOnlineListBean>(
                MachineOnlineListBean.class);
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
            String searchResult = jsonObject.getString("Rows");
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_main_back})
    public void onViewClicked(View view){
        switch(view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
        }
    }
}
