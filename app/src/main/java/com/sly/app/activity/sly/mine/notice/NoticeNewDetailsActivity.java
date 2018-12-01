package com.sly.app.activity.sly.mine.notice;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.yunw.machine.MachineDetailsActivity;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.notice.YunwNoticeNewDetailsBean;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

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
    private String MessageID;
    private CommonRequestPresenterImpl iCommonRequestPresenter;
    private List<YunwNoticeNewDetailsBean> mResultList = new ArrayList<>();

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

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        MessageID = getIntent().getExtras().getString("MessageID");
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(mContext, "PersonSysCode", "None");

        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        mineType = SharedPreferencesUtil.getString(mContext, "mineType", "None");

        toRequest(NetConstant.EventTags.GET_NEW_NOTICE_DETAILS);
        toRequest(NetConstant.EventTags.CAHNGE_NEWS_STATUS);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
//        if ("Miner".equals(mineType)) {
//            map.put("Rounter", NetWorkCons.GET_MINER_NOTICE_LIST);
//            map.put("minerSysCode", FrSysCode);
//        } else if ("MinerMaster".equals(mineType)) {
//            map.put("Rounter", NetWorkCons.GET_MINEMASTER_NOTICE_LIST);
//            map.put("minerMasterCode", FMasterCode);
//        } else {
        if (eventTag == NetConstant.EventTags.GET_NEW_NOTICE_DETAILS){
            map.put("Rounter", NetConstant.GET_NEW_NOTICE_DETAILS);
        }else{
            map.put("Rounter", NetConstant.CAHNGE_NEWS_STATUS);
        }

        map.put("MessageID", MessageID);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        Map<String, String> jsonMap = new ArrayMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, jsonMap);
        Logcat.e("提交参数 - " + jsonMap);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if (eventTag == NetConstant.EventTags.GET_NEW_NOTICE_DETAILS) {
            mResultList =
                    (List<YunwNoticeNewDetailsBean>) AppUtils.parseRowsResult(result, YunwNoticeNewDetailsBean.class);
            if (!AppUtils.isListBlank(mResultList)) {
                YunwNoticeNewDetailsBean bean = mResultList.get(0);
                tvDetailsNewName.setText(bean.getMine48_Title());
                tvDetailsNewTime.setText(bean.getMine48_Time());
                tvDetailsNewIP.setText(bean.getMine08_IP());
                tvDetailsNewType.setText(bean.getMine08_MAC());
                tvDetailsNewReason.setText(bean.getMine48_Message());
            }
        }

    }

    @OnClick({R.id.btn_main_back, R.id.tv_details_new_check})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_details_new_check:
//                Bundle bundle = new Bundle();
//                bundle.putString("machineSysCode","");
//                AppUtils.goActivity(this, MachineDetailsActivity.class, bundle);
                break;
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
}
