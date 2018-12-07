package com.sly.app.activity.mine.wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.BusinessAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.balance.BusinessInfo;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 交易记录
 */
public class WRecordActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.no_business)
    LinearLayout mNoBusiness;
    @BindView(R.id.lv_business)
    ListView mLvBusiness;
    private BusinessAdapter adapter;
    private String MemberCode;
    private String Token;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("交易记录");
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        if (bundle!=null){
            if (bundle.getString("marks")!=null){
                getBusiness(MemberCode,Token,bundle.getString("marks"),1);
            }
        }else {
            getBusiness(MemberCode,Token,"YB",1);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_business;
    }

    @Override
    protected void setListener() {
    }

    private void setListView(List<BusinessInfo> list) {
        if (list.size() != 0) {
            mNoBusiness.setVisibility(View.GONE);
            adapter = new BusinessAdapter(list, this);
            mLvBusiness.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            mNoBusiness.setVisibility(View.VISIBLE);
        }
    }

    private void getBusiness(String memberCode, String token,String CurrencyCode,int pageNo) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PruseCode", memberCode);
        map.put("CurrencyCode", CurrencyCode);
        map.put("PageSize", "30");
        map.put("PageNo", pageNo+"");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.getBusiness, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                LogCatW(NetWorkConstant.getBusiness, json, statusCode,content);
                final ReturnBean returnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (returnBean.getStatus().equals("1")) {
                    List<BusinessInfo> infoList = JSON.parseArray(returnBean.getData(), BusinessInfo.class);
                    setListView(infoList);
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });
    }
    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }
}
