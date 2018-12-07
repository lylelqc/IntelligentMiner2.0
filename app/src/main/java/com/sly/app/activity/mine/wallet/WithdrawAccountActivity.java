package com.sly.app.activity.mine.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.comm.BusEvent;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.UserInfoBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringFormatUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.realm.Realm;


/**
 * Created by 01 on 2017/6/16.
 */
public class WithdrawAccountActivity extends BaseActivity implements ICommonViewUi {
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
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.sp_view)
    Spinner spView;
    @BindView(R.id.bt_binding)
    Button btBinding;
    @BindView(R.id.et_bank)
    EditText etBank;
    private String setBank, name, bankCode, bankName;
    private String MemberCode, Token;
    private String Tag;
    private Realm realm;
    UserInfoBean persons;
    private String IdCardName;
    private String mRounter;
    private String mCardId;

    ICommonRequestPresenter iCommonRequestPresenter;


    @OnClick({R.id.btn_main_back, R.id.bt_binding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                CommonUtil2.goActivity(mContext, WithdrawActivity.class);
                finish();
                break;
            case R.id.bt_binding:
                name = etName.getText().toString().trim();
                bankCode = etCode.getText().toString().trim();
                bankName = etBank.getText().toString().trim();
                if (CommonUtil2.isEmpty(name) || CommonUtil2.isEmpty(bankCode)) {
                    ToastUtils.showToast("请输入完整信息");
                    return;
                }
                if (bankCode.length() == 16 || bankCode.length() == 19) {
//                    if (setBank.equals("请选择发卡银行"))  {
//                        ToastUtils.showToast("请选择发卡银行");
//                    } else {
//                        bindPurseBankCard();
//                    }
                    if (CommonUtil2.isEmpty(bankName)) {
                        ToastUtils.showToast("请输入正确的开户行信息");
                    } else {
                        toRequest(NetWorkCons.EventTags.BIND_BANK_CARD);
                    }
                } else {
                    ToastUtils.showToast("请输入正确的银行账户");
                    return;
                }
                break;
        }
    }


    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_account;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        initData();
        initView();
    }

    private void initView() {
        IdCardName = SharedPreferencesUtil.getString(WithdrawAccountActivity.this, "IdCardName", "None");
        etName.setText(IdCardName);
        if (Tag != null && Tag.equals("updata")) {
            mRounter = "Member.011";
            btBinding.setText("更新提现账户");
            tvMainTitle.setText("更新提现账户");
        } else {
            btBinding.setText("绑定提现账户");
            tvMainTitle.setText("绑定提现账户");
            mRounter = "Member.005";
        }
        String str1 = "目前仅支持绑定一个银行借记卡账户，提现成功后，系统将默认绑定此银行借记卡账号，为了账户安全绑定后暂时无法修改，所以请使用你的个人银行卡并仔细核对银行信息确保银行卡信息正确。";
        StringFormatUtil spanStr = new StringFormatUtil(this, str1,
                "银行借记卡", R.color.text_red).fillColor();
        tvContent.setText(spanStr.getResult());
    }

    private void initData() {
        realm = Realm.getDefaultInstance();
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        //  getUserName(MemberCode, Token);
        persons = realm.where(UserInfoBean.class)
                .equalTo("MemberCode", MemberCode)
                .findFirst();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Tag = bundle.getString("Tag");
                mCardId = bundle.getString("CardId");
            }
        }
    }

    @Override
    public void toRequest(int eventTag) {
        if (NetWorkCons.EventTags.BIND_BANK_CARD == eventTag) {
            String token = SharedPreferencesUtil.getString(this, "Token", "None");
            String user = SharedPreferencesUtil.getString(this, "User", "None");
            String key = SharedPreferencesUtil.getString(this, "Key", "None");
            String loginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
            String sysCode = SharedPreferencesUtil.getString(this, loginType.equals("Miner") ? "FrSysCode" : "FMasterCode", "None");

            Map map = new HashMap();

            map.put("Token", token);
            map.put("LoginType", loginType);
            if (Tag != null && Tag.equals("updata")) {
                mRounter = "Member.011";
                btBinding.setText("更新提现账户");
                tvMainTitle.setText("更新提现账户");
                map.put("ID", mCardId + "");
            } else {
                btBinding.setText("绑定提现账户");
                tvMainTitle.setText("绑定提现账户");
                mRounter = "Member.005";
                map.put("sys", sysCode);
            }
            map.put("Rounter", mRounter);
            map.put("User", user);
            map.put("bankNo", bankCode);
            map.put("openingBank", bankName);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.BIND_BANK_CARD, mContext, NetWorkCons.BASE_URL, mapJson);
//            Gson gson = new Gson();
//            final String json = gson.toJson(mapJson);
//            Logcat.i("提交的参数：" + json + " 地址 :" + NetWorkCons.BASE_URL);
//            HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, String content) {
//                    super.onSuccess(statusCode, content);
//                    LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
//                    ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
//                    if (mReturnBean.getStatus().equals("1")&& mReturnBean.getMsg().equals("成功")) {
//                        ToastUtils.showToast(mReturnBean.getData());
//                        startActivityWithoutExtras(WithdrawActivity.class);
//
//                        finish();
//                    } else {
//                        ToastUtils.showToast(mReturnBean.getMsg());
//                    }
//                }
//            });
        }

    }

    @Override
    public void getRequestData(int eventTag, String result) {
        finish();
        EventBus.getDefault().post(new PostResult(BusEvent.UPDATE_BANK_LIST));
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
