package com.sly.app.activity.machineSeat;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.MachineSeatOreAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.sly.MachineSeatDataBean;
import com.sly.app.model.sly.balance.SlyReturnListBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sly.app.utils.AppLog.LogCatW;

public class MachineSeatDetailActivity extends BaseActivity {
    @BindView(R.id.ore_introduce)
    TextView oreIntroduce;
    @BindView(R.id.ore_1)
    TextView ore1;
    @BindView(R.id.ore_number)
    TextView oreNumber;
    @BindView(R.id.ore_2)
    TextView ore2;
    @BindView(R.id.ore_price)
    TextView orePrice;
    @BindView(R.id.ore_3)
    TextView ore3;
    @BindView(R.id.ore_manager)
    TextView oreManager;
    @BindView(R.id.ore_zizhi)
    TextView oreZizhi;
    @BindView(R.id.rv_ore)
    RecyclerView rvOre;
    @BindView(R.id.ore_4)
    TextView ore4;
    @BindView(R.id.ore_phone)
    TextView orePhone;
    @BindView(R.id.sc_view)
    NestedScrollView scView;
    @BindView(R.id.ll_back_btn)
    LinearLayout llBackBtn;
    @BindView(R.id.ore_adress)
    TextView oreAdress;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_manager_name)
    TextView tvManagerName;
    @BindView(R.id.iv_rz_status)
    ImageView tvRzStatus;

    private MachineSeatDataBean machineSeat;
    private String machineSeatID = "";
    private List<MachineSeatDataBean> mMall = new ArrayList<>();
    private String User, Token, Key, LoginType, SysNumber;
    private String ProvinceCode = "None";
    private String CityCode = "None";
    private String CountryCode = "None";
    private String pageSize = "10";
    private int pageNo = 1;
    private List<String> mData = new ArrayList<>();
    private MachineSeatOreAdapter rvOreAdapter;

    @Override
    protected void initData() {
        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        SysNumber = SharedPreferencesUtil.getString(this, LoginType.equals("Miner") ? "FrSysCode" : "FMasterCode", "None");
        machineSeat = getIntent().getExtras().getParcelable("MachineSeatDataBean");
//        getMachineSeatData(SysNumber, Token, LoginType, User, machineSeat.getMine57_ID());

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_machine_detail;
    }

    @Override
    protected void initView() {
        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(2, MyStaggeredGridLayoutManager.VERTICAL);
        rvOre.setLayoutManager(mLayoutManager);
        rvOre.setItemAnimator(new DefaultItemAnimator());
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        lineVertical.setSize(15);
        lineVertical.setColor(0xFFf4f4f4);
        rvOre.addItemDecoration(lineVertical);
        MyGridItemDecoration lineVertica2 = new MyGridItemDecoration(MyGridItemDecoration.HORIZONTAL);
        lineVertica2.setSize(15);
        lineVertica2.setColor(0xFFf4f4f4);
        rvOre.addItemDecoration(lineVertica2);

        setView(machineSeat);

//        mData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534347208679&di=f767d393d81f2999772dfcf9eff9c203&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201509%2F03%2F20150903095425_fxa52.jpeg");
//        mData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534347208679&di=f767d393d81f2999772dfcf9eff9c203&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201509%2F03%2F20150903095425_fxa52.jpeg");
//        mData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534347208679&di=f767d393d81f2999772dfcf9eff9c203&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201509%2F03%2F20150903095425_fxa52.jpeg");
//        mData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534347208679&di=f767d393d81f2999772dfcf9eff9c203&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201509%2F03%2F20150903095425_fxa52.jpeg");

    }

    private void getMachineSeatData(String SysNumber, String token, String loginType, String user, String machineSeatID) {
        if (pageNo == 1) mMall.removeAll(mMall);
        Map map = new HashMap();

//        map.put("billNo", machineSeatID);
        map.put("Token", token);
        map.put("LoginType", loginType);
        map.put("Rounter", "MineMaster.009");
        map.put("User", user);
        map.put("Sys", SysNumber);
        map.put("ID", machineSeatID);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("status").equals("1") && jsonObject.optString("msg").equals("成功")) {
                        SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                        List<MachineSeatDataBean> machineSeatDataBeans = JSON.parseArray(mReturnBean.getData().getRows().toString(), MachineSeatDataBean.class);
                        setView(machineSeatDataBeans.get(0));
                        ToastUtils.showToast(jsonObject.optString("msg"));
                    } else {
                        ToastUtils.showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
            }
        });

    }

    private void setView(MachineSeatDataBean machineSeatDataBean) {
        String introduce = machineSeatDataBean.getMine79_Intro().replace("u000a", "\n");
        oreIntroduce.setText(introduce);
        oreAdress.setText(machineSeatDataBean.getProvince());
        oreNumber.setText(machineSeatDataBean.getMine79_Count()+"");
        orePhone.setText(machineSeatDataBean.getMine79_Link());
        orePrice.setText(Double.valueOf(machineSeatDataBean.getMine79_MinPrice()) + "-" + Double.valueOf(machineSeatDataBean.getMine79_MaxPrice()) + "元");
        oreManager.setText(Double.valueOf(machineSeatDataBean.getMine79_ManageFee()) + " 元/月");
        GlideImgManager.glideLoader(this, NetWorkCons.IMG_URL + machineSeatDataBean.getMine18_ImageURl(), R.drawable.seat_photo, R.drawable.seat_photo, ivIcon);
        tvManagerName.setText(machineSeatDataBean.getMine20_Name());
        if(machineSeatDataBean.getMine79_AuditStatusCode().equals("1")){
            tvRzStatus.setImageResource(R.drawable.authentication_icon_1);
        }else{
            tvRzStatus.setImageResource(R.drawable.authentication_icon_2);
        }

        mData.add(NetWorkCons.IMG_URL + machineSeatDataBean.getMine79_Pic1());
        if (!StringUtils.isBlank(machineSeatDataBean.getMine79_Pic2())){
            mData.add(NetWorkCons.IMG_URL + machineSeatDataBean.getMine79_Pic2());
        }
        if (!StringUtils.isBlank(machineSeatDataBean.getMine79_Pic3())){
            mData.add(NetWorkCons.IMG_URL + machineSeatDataBean.getMine79_Pic3());
        }
        if (!StringUtils.isBlank(machineSeatDataBean.getMine79_Pic4())){
            mData.add(NetWorkCons.IMG_URL + machineSeatDataBean.getMine79_Pic4());
        }
        rvOreAdapter = new MachineSeatOreAdapter(this, mData);
        rvOre.setAdapter(rvOreAdapter);
        rvOreAdapter.notifyDataSetChanged();

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.ll_back_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back_btn:
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
