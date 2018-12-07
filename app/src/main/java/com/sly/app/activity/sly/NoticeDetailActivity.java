package com.sly.app.activity.sly;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.MinerMasterNoticeDetail;
import com.sly.app.model.sly.MinerNoticeDetail;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeDetailActivity extends BaseActivity implements ICommonViewUi {
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
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.tv_msg_title)
    TextView tvMsgTitle;
    @BindView(R.id.tv_msg_code)
    TextView tvMsgCode;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.tv_msg_type)
    TextView tvMsgType;
    @BindView(R.id.tv_msg_time)
    TextView tvMsgTime;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_msg_data)
    TextView tvMsgData;
    private String User, FrSysCode, FMasterCode, Token, Key;
    private String LoginType = "None";
    private String mMessageID = "";
    private MinerNoticeDetail mResult ;
    private List<MinerMasterNoticeDetail> minerMasterNoticeDetailBean;



    private CommonRequestPresenterImpl iCommonRequestPresenter;


    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sly_notice;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        initData();
    }

    private void initData() {
        mMessageID = getIntent().getExtras().getString("MessageID");
        tvMainTitle.setText("消息详情");
        User = SharedPreferencesUtil.getString(this, "User");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key");

        if ("Miner".equals(LoginType)) {
            toRequest(NetWorkCons.EventTags.GET_NOTICE_DETAIL_MINER);
        } else {
            toRequest(NetWorkCons.EventTags.GET_NOTICE_DETAIL_MINERMASTER);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        if (NetWorkCons.EventTags.GET_NOTICE_DETAIL_MINER == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_NOTICE_DETAIL_MINER);
            map.put("User", User);
            map.put("messageID", mMessageID);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_NOTICE_DETAIL_MINER, mContext, NetWorkCons.BASE_URL, mapJson);
        } else if (NetWorkCons.EventTags.GET_NOTICE_DETAIL_MINERMASTER == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_NOTICE_DETAIL_MINERMASTER);
            map.put("User", User);
            map.put("messageID", mMessageID);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_NOTICE_DETAIL_MINERMASTER, mContext, NetWorkCons.BASE_URL, mapJson);
        }else if (NetWorkCons.EventTags.SET_MASTER_NOTICE_STATUS == eventTag){
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.SET_MASTER_NOTICE_STATUS);
            map.put("User", User);
            map.put("messageID", mMessageID);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.SET_MASTER_NOTICE_STATUS, mContext, NetWorkCons.BASE_URL, mapJson);
        }else if (NetWorkCons.EventTags.SET_MINER_NOTICE_STATUS == eventTag){
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.SET_MINER_NOTICE_STATUS);
            map.put("User", User);
            map.put("messageID", mMessageID);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.SET_MINER_NOTICE_STATUS, mContext, NetWorkCons.BASE_URL, mapJson);
        }

    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if (NetWorkCons.EventTags.GET_NOTICE_DETAIL_MINER == eventTag) {
//            Logcat.e("消息参数 - " + result);
            JsonHelper<MinerNoticeDetail> jsonHelper = new JsonHelper<>(MinerNoticeDetail.class);
//            mResult = jsonHelper.getDatas(result, "data").get(0);
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                String searchResult = jsonObject.getString("Rows");
                mResult = jsonHelper.getDatas(searchResult).get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvMsgTitle.setText(mResult.getMine34_Title()+"");
            tvMsgType.setText(mResult.getMine34_MessageClass()+"");
            tvMsgCode.setText(mResult.getMine34_MessageID()+"");
            tvMsgTime.setText(mResult.getMine34_Time()+"");
            tvMsgData.setText(mResult.getMine34_Message()+"");
            if (!mResult.isMine34_IsRead()){
                toRequest(NetWorkCons.EventTags.SET_MINER_NOTICE_STATUS);
            }

        } else if (NetWorkCons.EventTags.GET_NOTICE_DETAIL_MINERMASTER == eventTag) {
            JsonHelper<MinerMasterNoticeDetail> dataParser = new JsonHelper<>(MinerMasterNoticeDetail.class);
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                String searchResult = jsonObject.getString("Rows");
                minerMasterNoticeDetailBean = dataParser.getDatas(searchResult);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!minerMasterNoticeDetailBean.get(0).isMine48_IsRead()){
                toRequest(NetWorkCons.EventTags.SET_MASTER_NOTICE_STATUS);
            }
            tvMsgTitle.setText(minerMasterNoticeDetailBean.get(0).getMine48_Title());
            tvMsgType.setText(minerMasterNoticeDetailBean.get(0).getMine48_MessageClass());
            tvMsgCode.setText(minerMasterNoticeDetailBean.get(0).getMine48_MessageID());
            tvMsgTime.setText(minerMasterNoticeDetailBean.get(0).getMine48_Time());
            tvMsgData.setText(minerMasterNoticeDetailBean.get(0).getMine48_Message());
            if (!minerMasterNoticeDetailBean.get(0).isMine48_IsRead()){
                toRequest(NetWorkCons.EventTags.SET_MASTER_NOTICE_STATUS);
            }
        }else if (NetWorkCons.EventTags.SET_MINER_NOTICE_STATUS == eventTag){

        }else if (NetWorkCons.EventTags.SET_MASTER_NOTICE_STATUS == eventTag){

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
}
