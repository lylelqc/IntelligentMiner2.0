package com.sly.app.activity.sly.mine.notice;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.notice.YunwNoticeOldListBean;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import butterknife.BindView;

public class NoticeOldDetailsActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;

    @BindView(R.id.iv_notice_old_pic)
    ImageView ivDetailsOldPic;
    @BindView(R.id.tv_notice_old_title)
    TextView tvDetailsOldTitle;
    @BindView(R.id.tv_details_old_date)
    TextView tvDetailsOldDate;
    @BindView(R.id.tv_details_old_description)
    TextView tvDetailsOldDescription;

    private String User, Token, FrSysCode, FMasterCode, PersonSysCode, Key, LoginType, mineType;
    private YunwNoticeOldListBean mCommonNoticeBean;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_notice_old_details;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText(getString(R.string.sly_notice));

        mCommonNoticeBean = (YunwNoticeOldListBean) getIntent().getExtras().getSerializable("CommonNoticeInfo");
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(mContext,"PersonSysCode","None");

        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        mineType = SharedPreferencesUtil.getString(mContext, "mineType", "None");

        initViewInfo();
    }

    private void initViewInfo() {
        GlideImgManager.glideLoader(mContext, NetConstant.IMG_URL2 + mCommonNoticeBean.getMine85_Pic(),
                0, ivDetailsOldPic);
        tvDetailsOldTitle.setText(mCommonNoticeBean.getMine85_Title());
        String date = mCommonNoticeBean.getMine85_Time().split(" ")[0];
        tvDetailsOldDate.setText(date.substring(date.length()-5, date.length()));
        tvDetailsOldDescription.setText(mCommonNoticeBean.getMine85_Content());
    }
}
