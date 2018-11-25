package com.sly.app.activity.sly.mine.notice;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import butterknife.BindView;

public class NoticeNewDetailsActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_details_new_name)
    TextView tvDetailsNewName;
    @BindView(R.id.tv_details_new_time)
    TextView tvDetailsNewTime;
    @BindView(R.id.tv_details_new_ip)
    TextView tvDetailsNewIP;
    @BindView(R.id.tv_details_new_type)
    TextView tvDetailsNewType;
    @BindView(R.id.tv_details_new_reason)
    TextView tvDetailsNewReason;
    @BindView(R.id.tv_details_new_check)
    TextView tvDetailsNewCheck;

    private String User, Token, FrSysCode, FMasterCode, PersonSysCode, Key, LoginType, mineType;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_notice_new_details;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText(getString(R.string.sly_notice));
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(mContext,"PersonSysCode","None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        mineType = SharedPreferencesUtil.getString(mContext, "mineType", "None");
    }

    @Override
    public void toRequest(int eventTag) {

    }

    @Override
    public void getRequestData(int eventTag, String result) {

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
}
