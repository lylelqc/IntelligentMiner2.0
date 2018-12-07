package com.sly.app.activity.mine.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.CityDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.CommonUtil;
import vip.devkit.library.Logcat;

/**
 * Created by 01 on 2016/10/20.
 * <p/>
 * 设置收货地址
 */
public class SettingAddress extends BaseActivity {
    @BindView(R.id.tv_main_right)
    TextView mTvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout mLlRight;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_tell)
    EditText mEtTell;
    @BindView(R.id.tv_region)
    TextView mTvRegion;
    @BindView(R.id.imageView3)
    ImageView mImageView3;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.cb_default)
    CheckBox mCbDefule;
    @BindView(R.id.btn_ok)
    Button mBtnOk;
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    private String Name, Tell, Address, isDefault, Province, City, County, Region;
    Boolean reg = false;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("新增收货地址");
    }

    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_address_setting;
    }

    @Override
    protected void setListener() {
    }


    private void setRegoster() {
        InputMethodManager inputMethodManager = (InputMethodManager) SettingAddress.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(SettingAddress.this.getCurrentFocus().getWindowToken()
                , InputMethodManager.HIDE_NOT_ALWAYS);
        WindowManager wm = SettingAddress.this.getWindowManager();
        int a = wm.getDefaultDisplay().getWidth();
        CityDialog cityDialog = CityDialog.getInstance();
        cityDialog.setNicknameDialog(SettingAddress.this, new CityDialog.onInputNameEvent() {
            @Override
            public void onClick(final String data,String pid,String cid,String aid) {
                mTvRegion.setText(data);
                Region = mTvRegion.getText().toString();
                String[] allcity = data.split(" ");
                Province = allcity[0];
                City = allcity[1];
                County = allcity[2];
                mTvRegion.setClickable(true);
            }
        }, a);
    }

    private static void setShowWith(int mWidth, Window mWindow, double d) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = (int) (mWidth * d);
        mWindow.setAttributes(lp);
    }

    private void RegData() {
        Name = mEtName.getText().toString();
        Tell = mEtTell.getText().toString();
        Address = mEtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Tell) || TextUtils.isEmpty(Address) || TextUtils.isEmpty(Region)) {
            Toast.makeText(SettingAddress.this, "嗨，等一下,请输入完整信息", Toast.LENGTH_SHORT).show();
            mBtnOk.setClickable(true);
        } else {
            if (!(CommonUtil.isMobile(Tell))) {
                ToastUtils.showToast("嗨，等一下,电话号码不正确哦");
                mBtnOk.setClickable(true);
                return;
            } else {
                AddressAdd(Name, Tell, Address, Province, City, County, mCbDefule.isChecked());
            }
        }
    }

    private void AddressAdd(String name, String tell, String address, String province, String city, String region, Boolean checked) {
        Map<String, String> map = new HashMap<>();
        map.put("M045_MemberCode", SharedPreferencesUtil.getString(this, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(this, "Token"));
        map.put("M045_Consignee", name);
        map.put("M045_Mobile", tell);
        map.put("M045_Tel", tell);
        map.put("M045_Address", address);
        map.put("M045_ProvinceName", province);
        map.put("M045_CityName", city);
        map.put("M045_CountyName", region);
        map.put("M045_Default", checked + "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ADDRESS_ADD, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                Logcat.e("返回码:" + statusCode);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                Logcat.e("返回码:" + statusCode + "返回参数:" + backlogJsonStr + "提交的内容：" + json + " headers :" + headers);
                ReturnBean mReturnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("添加成功");
                    startActivityWithoutExtras(AddressActivity.class);
                }
                return false;
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络连接失败：" + e);
            }
        });
    }

    @OnClick({R.id.btn_main_back, R.id.tv_region, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                Intent intent = getIntent();
                if (null != intent) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        Intent intent2 = new Intent(this, AddressActivity.class);
                        intent2.putExtra("sign", "WS");
                        startActivity(intent2);
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.tv_region:
                setRegoster();
                break;
            case R.id.btn_ok:
                RegData();
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            if (null != intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Intent intent2 = new Intent(this, AddressActivity.class);
                    intent2.putExtra("sign", "WS");
                    startActivity(intent2);
                    finish();
                } else {
                    finish();
                }
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
