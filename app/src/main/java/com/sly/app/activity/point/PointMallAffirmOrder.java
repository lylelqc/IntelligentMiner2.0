package com.sly.app.activity.point;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.mine.UpDataPwActivity;
import com.sly.app.activity.mine.address.AddressActivity;
import com.sly.app.activity.order.OrderPayFinish;
import com.sly.app.adapter.order.AffirmRecyclerViewAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.AddressBean;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.GoodsInfo;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;
import com.sly.app.view.pwd.SercurityDialog;

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

import static com.sly.app.utils.AppLog.LogCatW;

/**
 */

public class PointMallAffirmOrder extends BaseActivity {

    private static final int REQUEST_CODE = 1;
    @BindView(R.id.no_address_icon)
    ImageView noAddressIcon;
    @BindView(R.id.no_address_rl)
    RelativeLayout noAddressRl;
    @BindView(R.id.choose_address_name)
    TextView chooseAddressName;
    @BindView(R.id.choose_address_phone)
    TextView chooseAddressPhone;
    @BindView(R.id.choose_address_tv)
    TextView chooseAddressTv;
    @BindView(R.id.has_address_ll)
    LinearLayout hasAddressLl;
    @BindView(R.id.address)
    LinearLayout address;
    @BindView(R.id.deviser)
    View deviser;
    @BindView(R.id.tv_express_way)
    TextView tvExpressWay;
    @BindView(R.id.tv_express_price)
    TextView tvExpressPrice;
    @BindView(R.id.tv_affirm_remark)
    EditText tvAffirmRemark;
    @BindView(R.id.postager)
    LinearLayout postager;
    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_pay_Allprice)
    TextView tvPayAllprice;
    @BindView(R.id.tv_affirm)
    TextView tvAffirm;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.rl_view)
    RecyclerView rlView;
    @BindView(R.id.sc_view)
    NestedScrollView scView;
    @BindView(R.id.btn_title_back)
    LinearLayout mBtnTitleBack;
    @BindView(R.id.btn_title_text)
    TextView mBtnTitleText;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    private AffirmRecyclerViewAdapter adapter;
    private String MemberCode, Token;
    private double totlaPrice;
    private String MallType,  PayPrice, Buyer = "JW888888", LeaveWord = "", Remark = "Android";
    private AddressBean mAddressBean;
    private ArrayList<GoodsInfo> InfoList;
    private List<AddressBean> mAddBeen;
    private GoodsBean mGoodsInfo;
    private String sukId;
    private boolean isSelectAddress;
    private boolean isPwd = false;
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_affirm;
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Bundle mBundle = getIntent().getExtras();
        mGoodsInfo = mBundle.getParcelable("GoodBean");
        InfoList = mBundle.getParcelableArrayList("pBuy");
        totlaPrice = mGoodsInfo.getMartPrice();
        sukId = mBundle.getString("sukId");
        adapter = new AffirmRecyclerViewAdapter(this, InfoList, "积分");
        if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
            ToastUtils.showToast("登录失效");
        } else {
            getDeliveryAddress(MemberCode, Token);
            RegPwd(MemberCode, Token);
        }
    }

    @Override
    protected void initView() {
        mBtnTitleText.setText("确认订单");
        tvTotalPrice.setText(totlaPrice + "");//总价
        tvPayAllprice.setText(totlaPrice + "");
        tvTotalCount.setText(1 + "");
        rlView.clearFocus();
        rlView.setFocusable(false);
        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
        rlView.setLayoutManager(mLayoutManager);
        rlView.setItemAnimator(new DefaultItemAnimator());
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        lineVertical.setSize(5);
        lineVertical.setColor(0xFFf2f2f2);
        rlView.addItemDecoration(lineVertical);
        rlView.setAdapter(adapter);
    }


    @Override
    protected void setListener() {


    }


    @OnClick({R.id.no_address_rl, R.id.address, R.id.tv_affirm, R.id.btn_title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.no_address_rl:
                isSelectAddress = true;
                Intent intent = new Intent(this, AddressActivity.class);
                intent.putExtra("sign", "WS");
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.address:
                isSelectAddress = true;
                Intent intent2 = new Intent(this, AddressActivity.class);
                intent2.putExtra("sign", "WS");
                startActivityForResult(intent2, REQUEST_CODE);
                break;
            case R.id.tv_affirm:
                LeaveWord = tvAffirmRemark.getText().toString();
                if (mAddressBean != null) {
                    final String addressId = mAddressBean.getM045_ID() + "";
                    if (isPwd == true) {
                        SercurityDialog mDialog = new SercurityDialog(this, totlaPrice+"");
                        mDialog.setOnInputCompleteListener(new SercurityDialog.InputCompleteListener() {
                            @Override
                            public void inputComplete(String passWord) {
                                CreateOrder(MemberCode, Token, mGoodsInfo.getComID()+"", sukId, Buyer, addressId, LeaveWord, Remark, passWord);
                            }
                        });
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                    } else {
                        ToastUtils.showToast("您还未设置支付密码");
                        intent = new Intent();
                        intent.putExtra("pay", "pay");
                        intent.setClass(PointMallAffirmOrder.this, UpDataPwActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.btn_title_back:
                finish();
                break;
        }
    }

    private void CreateOrder(String memberCode, String token, String comID, String OptionID, String buyer, String ConsigneeID, String leaveWord, String remark, String pwd) {
        initProgressDialog("生成订单中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Buyer", memberCode);
        map.put("Token", token);
        map.put("Saler", buyer);
        map.put("LeaveWord", leaveWord);
        map.put("Remark", remark);
        map.put("OptionID", OptionID);
        map.put("ConsigneeID", ConsigneeID);
        map.put("PayPassword", pwd);
        map.put("ComID", comID);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.POINT_CREAT_ORDER, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                LogCatW(NetWorkConstant.POINT_CREAT_ORDER, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("OrderNo", mReturnBean.getData());
                    startActivityWithExtras(OrderPayFinish.class, mBundle);
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }
        });
        dismissProgressDialog();
    }

    private void getDeliveryAddress(String memberCode, final String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.ADDRESS_GET_LIST, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                Logcat.e("del 返回码:" + statusCode);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                Logcat.e("返回码:" + statusCode + "返回参数:" + backlogJsonStr + "提交的内容：" + json + " headers :" + headers);
                ReturnBean mReturnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getMsg().equals("成功")) {
                    mAddBeen = JSON.parseArray(mReturnBean.getData(), AddressBean.class);
                    if (mAddBeen.size() > 0) {
                        mAddressBean = mAddBeen.get(0);
                        chooseAddressName.setText(mAddressBean.getM045_Consignee());
                        chooseAddressPhone.setText(mAddressBean.getM045_Mobile());
                        chooseAddressTv.setText(mAddressBean.getM045_Address());
                    } else {
                        noAddressRl.setVisibility(View.VISIBLE);
                        hasAddressLl.setVisibility(View.GONE);
                    }
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


    /**
     * @param memberCode
     * @param token
     */
    private void RegPwd(String memberCode, final String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("PruseCode", memberCode);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.PURSE_IS_NULL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                Logcat.e("-----------" + statusCode);
                NetLogCat.W(NetWorkConstant.PURSE_IS_NULL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().equals("已有支付密码")) {
                    isPwd = true;
                    return true;
                }
                return false;
            }
        });
    }
    //地址回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 选择的地址
         */
        switch (requestCode) {
            case REQUEST_CODE:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        noAddressRl.setVisibility(View.GONE);
                        hasAddressLl.setVisibility(View.VISIBLE);
                        mAddressBean = bundle.getParcelable("result");
                        Logcat.i("--------------------------" + mAddressBean.toString());
                        chooseAddressName.setText(mAddressBean.getM045_Consignee());
                        chooseAddressPhone.setText(mAddressBean.getM045_Mobile());
                        chooseAddressTv.setText(mAddressBean.getM045_Address());
                    }
                }
                break;
        }
    }
}
