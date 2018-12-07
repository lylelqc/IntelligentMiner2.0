package com.sly.app.activity.mine.address;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.order.AffirmOrder;
import com.sly.app.adapter.AddressAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.AddressBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ThreadUtils;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;


/**
 * 地址管理  By k
 */
public class AddressActivity extends BaseActivity implements AddressAdapter.ProInterface {

    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView mTvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout mLlRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.lv_address)
    ListView mLvAddress;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.btn_addnew)
    Button mBtnAdd;
    private List<AddressBean> mBeanList = new ArrayList<>();
    private AddressAdapter mAdapter;
    private int NOTIF = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NOTIF) {
                mAdapter.notifyDataSetChanged();
                if (mBeanList.size() > 0) {
                    mLvAddress.setVisibility(View.VISIBLE);
                    mTvAddress.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initData() {
        initProgressDialog("加载中...", false);
        new Thread(new GetAddressThread()).start();
        dismissProgressDialog();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.address_layout;
    }

    @Override
    protected void setListener() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getStringExtra("sign") != null&&mBeanList.size()>0) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("result", mBeanList.get(0));
                resultIntent.putExtras(bundle);
                setResult(AffirmOrder.RESULT_OK, resultIntent);
                finish();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("我的地址");
        mLvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Logcat.i("点击了：" + i);
                if (getIntent().getStringExtra("sign") != null) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("result", mBeanList.get(i));
                    resultIntent.putExtras(bundle);
                    setResult(AffirmOrder.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }


    @OnClick({R.id.btn_main_back, R.id.btn_addnew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                if (getIntent().getStringExtra("sign") != null&&mBeanList.size()>0) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("result", mBeanList.get(0));
                    resultIntent.putExtras(bundle);
                    setResult(AffirmOrder.RESULT_OK, resultIntent);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.btn_addnew:
                Bundle mBundle = new Bundle();
                mBundle.putString("sign",getIntent().getStringExtra("sign"));
                startActivityWithExtras(SettingAddress.class,mBundle);
                finish();
                break;
        }
    }

    public class GetAddressThread implements Runnable {
        @Override
        public void run() {
            Map<String, String> map = new HashMap<>();
            String  MemberCode =  SharedPreferencesUtil.getString(AddressActivity.this, "MemberCode");
            String  Token = SharedPreferencesUtil.getString(AddressActivity.this, "Token");
            map.put("MemberCode", MemberCode);
            map.put("Token", Token);
            final String json = CommonUtils.GsonToJson(map);
            Logcat.i("-------"+json);
            HttpClient.postJson(NetWorkConstant.ADDRESS_GET_LIST, json, new HttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode,  String content) {
                    super.onSuccess(statusCode,  content);
                    Logcat.e("del 返回码:" + statusCode);
                    String backlogJsonStr = content;
                    backlogJsonStr = backlogJsonStr.replace("\\", "");
                    backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                    Logcat.e("返回码:" + statusCode + "返回参数:" + backlogJsonStr + "提交的内容：" + json + " headers :" );
                    ReturnBean mReturnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                    if (mReturnBean.getStatus().equals("1")) {
                        List<AddressBean> list = JSON.parseArray(mReturnBean.getData(), AddressBean.class);
                        mBeanList.addAll(list);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new AddressAdapter(AddressActivity.this, mBeanList, R.layout.item_address);
                                mAdapter.setProInterface(AddressActivity.this);
                                mLvAddress.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                                if (mBeanList.size() > 0) {
                                    mLvAddress.setVisibility(View.VISIBLE);
                                    mTvAddress.setVisibility(View.GONE);
                                }
                            }
                        });
                        Message msg = new Message();
                        msg.what = NOTIF;
                        msg.obj = mAdapter;
                        handler.sendMessage(msg);
                    }else {
                     ToastUtils.showToast(mReturnBean.getMsg());
                    }
                }
                @Override
                public void onFailure(Request request, IOException e) {
                    super.onFailure(request, e);
                    Logcat.e("网络连接失败：" + e);
                }
            });
        }
    }

    /**
     * @param i  对应位置
     * @param id 地址id
     */
    public void AddressDel(final int i, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("M045_MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        map.put("M045_ID", id);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ADDRESS_DEL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                Logcat.e(" default 返回码:" + statusCode);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                Logcat.e("返回码:" + statusCode + "返回参数:" + backlogJsonStr + "提交的内容：" + json + " headers :" + headers);
                ReturnBean mReturnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                if (mReturnBean.getMsg().equals("成功")) {
                    mBeanList.remove(i);
                    ToastUtils.showToast("删除成功");
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    /**
     * @param id 地址ID
     */
    public void AddressDefault(final int position, final String id, final boolean IsDefault) {
        initProgressDialog("加载中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("M045_MemberCode", SharedPreferencesUtil.getString(mContext, "MemberCode"));
        map.put("Token", SharedPreferencesUtil.getString(mContext, "Token"));
        map.put("M045_ID", id);
        map.put("M045_Default", IsDefault + "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ADDRESS_DEFAULT, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                Logcat.e("返回码:" + statusCode);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                Logcat.e("返回码:" + statusCode + "返回参数:" + backlogJsonStr + "提交的内容：" + json + " headers :" + headers);
                ReturnBean mReturnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                if (mReturnBean.getMsg().equals("成功")) {
                    ToastUtils.showToast("设置默认地址成功");
                    if (IsDefault == true) {
                        for (int i = 0; i < mBeanList.size(); i++) {
                            if (i != position) {
                                mBeanList.get(i).setM045_Default(false);
                            }
                        }
                        if (position != 0) {
                            AddressBean temp = mBeanList.get(position);
                            mBeanList.remove(position);
                            mBeanList.add(0, temp);
                            mAdapter.notifyDataSetChanged();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    @Override
    public void delAddress(int position, String id) {
        AddressDel(position, id);
    }

    @Override
    public void defaultAddress(int position, String id, boolean isDefault) {
        AddressDefault(position, id, isDefault);
    }

}
