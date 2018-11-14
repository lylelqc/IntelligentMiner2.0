package com.sly.app.activity.sly.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.sly.HomeMsgDetailBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlyHomeMsgDetailActivity extends BaseActivity implements ICommonViewUi {

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
    @BindView(R.id.tv_msg_title)
    TextView tvMsgTitle;
    @BindView(R.id.tv_msg_time)
    TextView tvMsgTime;
    @BindView(R.id.tv_msg_data)
    TextView tvMsgData;
    @BindView(R.id.iv_msg_pic)
    ImageView ivMsgPic;
    private String User, FrSysCode, FMasterCode, Token, Key;
    private String LoginType = "None";
    ICommonRequestPresenter iCommonRequestPresenter;
    private String MsgID;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home_msg_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        MsgID = getIntent().getExtras().getString("MsgID");
        tvMainTitle.setText("播报详情");
        User = SharedPreferencesUtil.getString(this, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        toRequest(NetWorkCons.EventTags.GET_HOME_MSG_DETAIL);
    }

    @Override
    public void toRequest(int eventTag) {
        if (NetWorkCons.EventTags.GET_HOME_MSG_DETAIL == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_HOME_MSG_DETAIL);
            map.put("User", User);
            map.put("ID", MsgID);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_HOME_MSG_DETAIL, mContext, NetWorkCons.BASE_URL, mapJson);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if (NetWorkCons.EventTags.GET_HOME_MSG_DETAIL == eventTag) {
            JsonHelper<HomeMsgDetailBean> dataParser = new JsonHelper<HomeMsgDetailBean>(
                    HomeMsgDetailBean.class);

            HomeMsgDetailBean data = dataParser.getData(result, "data");
//            tvMsgTitle.setText(data.getF66_Title());
//            tvMsgTime.setText(data.getF66_Time());
            if (!CommonUtil2.isEmpty(data.getF66_Pic())) {
                GlideImgManager.glideLoader(mContext, NetWorkCons.IMG_URL2 + data.getF66_Pic(), 0, ivMsgPic);
            }
            tvMsgData.setText(data.getF66_Content());
        }
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

    @OnClick({R.id.btn_main_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
