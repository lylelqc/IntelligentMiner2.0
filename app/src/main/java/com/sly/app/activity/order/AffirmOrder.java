package com.sly.app.activity.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.sly.app.activity.mine.address.AddressActivity;
import com.sly.app.adapter.order.AffirmRecyclerViewAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.AddressBean;
import com.sly.app.model.GoodsInfo;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

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
 * Created by 01 on 2017/9/18.
 */

public class AffirmOrder extends BaseActivity {

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
    private String ComID, MallType, PostAge = "0", PayPrice, Buyer = "", Points = "0", LeaveWord = "", Remark = "Android";
    private AddressBean mAddressBean;
    private ArrayList<GoodsInfo> InfoList;
    private List<AddressBean> mAddBeen;
    private List<String> mComIdList = new ArrayList<>();
    private boolean isSelectAddress;

    private static final int ORDER_FLAG_A = 1;
    private static final int ORDER_FLAG_B = 2;//暂时不用
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ORDER_FLAG_A: {
                    Logcat.i("线程A--------------------------");
                    AddressBean mAddressBean = (AddressBean) msg.obj;
                    Logcat.i("线程A------" + mAddressBean.toString());
                    getComPostAge(mComIdList, mAddressBean.getM045_ID() + "");//获取运费   待完善因为现在是获取一个商品的运费。
                    break;
                }
                case ORDER_FLAG_B: {
                    break;
                }
                default:
                    break;
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
    protected int setLayoutId() {
        return R.layout.activity_affirm;
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Bundle mBundle = getIntent().getExtras();
        InfoList = mBundle.getParcelableArrayList("buy");
        totlaPrice = mBundle.getDouble("TotalPrice");
        for (int i = 0; i < InfoList.size(); i++) {
            GoodsInfo mGoodsInfo = InfoList.get(i);
            mComIdList.add(mGoodsInfo.getComID());
            if (mGoodsInfo.getComTitle().contains("专享") && mGoodsInfo.getComTitle().contains("礼包")) {
                Remark = Remark + ",礼包";
            }
        }
        MallType = InfoList.get(0).getMallType();
        ComID = InfoList.get(0).getComID();
        adapter = new AffirmRecyclerViewAdapter(this, InfoList);
        getDeliveryAddress(MemberCode, Token);

        Logcat.i("商品ID：" + mComIdList.size() + "/json:" + CommonUtils.GsonToJson(mComIdList));
    }


    @Override
    protected void initView() {
        mBtnTitleText.setText("确认订单");
        tvTotalPrice.setText(totlaPrice + "");//总价
        tvPayAllprice.setText(totlaPrice + "");
        tvTotalCount.setText(InfoList.size() + "");
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


    @OnClick({R.id.address, R.id.tv_affirm, R.id.btn_title_back})
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
                LeaveWord = tvAffirmRemark.getText().toString().trim();
                Double PaySum = Double.valueOf(totlaPrice) + Double.valueOf(PostAge);
                if (isSelectAddress != true) {
                    if (mAddBeen.size() > 0) {
                        CreateOrder(MemberCode, Token, MallType, Buyer, PaySum + "", PostAge, mAddressBean.getM045_ID() + "", LeaveWord, Remark, "sc");
                    } else {
                        ToastUtils.showToast("请选择收货地址");
                    }
                } else {
                    CreateOrder(MemberCode, Token, MallType, Buyer, PaySum + "", PostAge, mAddressBean.getM045_ID() + "", LeaveWord, Remark, "sc");
                }
                break;
            case R.id.btn_title_back:
                finish();
                break;
        }
    }

    /**
     * @param memberCode
     * @param token
     * @param mallType
     * @param buyer
     * @param payPrice
     * @param postAge
     * @param addressId
     * @param leaveWord
     * @param remark
     * @param WsType
     */
    private void CreateOrder(String memberCode, String token, String mallType, String buyer, final String payPrice, String postAge, String addressId, String leaveWord, String remark, String WsType) {
        initProgressDialog("生成订单中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Buyer", memberCode);
        map.put("Token", token);
        map.put("MallType", mallType);
        map.put("Postage", postAge);
        map.put("LeaveWord", leaveWord);
        map.put("Remark", remark);
        map.put("ConsigneeID", addressId);
        String URL = NetWorkConstant.WS_ORDER_ADD;
        final String json = CommonUtils.GsonToJson(map);
        final String finalURL = URL;
        HttpClient.postJson(URL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                LogCatW(finalURL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("OrderNo", mReturnBean.getData());
                    mBundle.putString("PayPrice", payPrice);
                    mBundle.putString("MallType", MallType);
                    startActivityWithExtras(OrderPayActivity.class, mBundle);
                    finish();
                }
                return false;
            }
        });
        dismissProgressDialog();
    }

    /**
     * @param comID
     * @param consigneeID
     */
    private void getComPostAge(List<String> comID, String consigneeID) {
        Map map = new HashMap();
        map.put("ComList", comID);
        map.put("ConsigneeID", consigneeID);
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        final String json = CommonUtils.GsonToJson(map);
        Logcat.i("----------------" + json);
        HttpClient.postJson(NetWorkConstant.COM_GET_POSTAGE, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog();
                NetLogCat.W(NetWorkConstant.COM_GET_POSTAGE, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1") && mReturnBean.getMsg().equals("成功")) {
                    PostAge = mReturnBean.getData();
                    tvExpressPrice.setText("￥" + Double.valueOf(PostAge)+"");
                    Double price = Double.valueOf(totlaPrice) + Double.valueOf(PostAge);
                    tvPayAllprice.setText(price + "");//实付款  计算折等之后的价格}
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
                        chooseAddressPhone.setText(mAddressBean.getM045_Tel());
                        chooseAddressTv.setText(mAddressBean.getM045_Address());
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Logcat.i(mAddressBean.toString());
                                Message msg = new Message();
                                msg.what = ORDER_FLAG_A;
                                msg.obj = mAddressBean;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
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
                        chooseAddressPhone.setText(mAddressBean.getM045_Tel());
                        chooseAddressTv.setText(mAddressBean.getM045_Address());
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Logcat.i(mAddressBean.toString());
                                Message msg = new Message();
                                msg.what = ORDER_FLAG_A;
                                msg.obj = mAddressBean;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }
                }
                break;
        }
    }
}
