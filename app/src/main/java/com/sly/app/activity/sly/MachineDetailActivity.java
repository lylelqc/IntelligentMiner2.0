package com.sly.app.activity.sly;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.sly.MyMachineDetail;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MachineDetailActivity extends BaseActivity implements ICommonViewUi {

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
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_machine_code)
    TextView tvMachineCode;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.rl_item_rapare)
    RelativeLayout rlItemRapare;
    @BindView(R.id.tv_machine_name)
    TextView tvMachineName;
    @BindView(R.id.ll_L1)
    LinearLayout llL1;
    @BindView(R.id.tv_run_hours)
    TextView tvRunHours;
    @BindView(R.id.ll_L2)
    LinearLayout llL2;
    @BindView(R.id.tv_run_rate)
    TextView tvRunRate;
    @BindView(R.id.tv_run_status)
    TextView tvRunStatus;
//    @BindView(R.id.tv_power_sum)
//    TextView tvPowerSum;
//    @BindView(R.id.tv_pools)
//    TextView tvPools;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_machine_type)
    TextView tvMachineType;
    @BindView(R.id.ll_machine_type)
    LinearLayout llMachineType;
//    @BindView(R.id.tv_machine_energy)
//    TextView tvMachineEnergy;
    @BindView(R.id.tv_power_sum_total)
    TextView tvPowerSumTotal;
    private String User, SysCode, CusCode, Token, Key;
    private String LoginType = "None";
    ICommonRequestPresenter iCommonRequestPresenter;
    private String mMachineSysCode = "None";

    @OnClick(R.id.btn_main_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_machine_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        mMachineSysCode = getIntent().getExtras().getString("machineSysCod");
        tvMainTitle.setText("设备详情");
        User = SharedPreferencesUtil.getString(this, "User");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        SysCode = SharedPreferencesUtil.getString(this, LoginType.equals("Miner") ? "FrSysCode": "FMasterCode");
        CusCode = SharedPreferencesUtil.getString(this, "CusCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key");
        toRequest(NetWorkCons.EventTags.GET_MY_MACHINE_DETAIL);
    }

    @Override
    public void toRequest(int eventTag) {
        if (NetWorkCons.EventTags.GET_MY_MACHINE_DETAIL == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_MY_MACHINE_DETAIL);
            map.put("User", User);
            map.put("machineSysCode", mMachineSysCode);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_MY_MACHINE_DETAIL, mContext, NetWorkCons.BASE_URL, mapJson);
        }

    }

    @Override
    public void getRequestData(int eventTag, String result) {

        if (NetWorkCons.EventTags.GET_MY_MACHINE_DETAIL == eventTag) {
            JsonHelper<MyMachineDetail> dataParser = new JsonHelper<MyMachineDetail>(
                    MyMachineDetail.class);

            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                String searchResult = jsonObject.getString("Rows");
//            pageSize = jsonObject.optInt("page_size",0);
                List<MyMachineDetail> datas = dataParser.getDatas(searchResult);
                setDetailView(datas.get(0));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setDetailView(MyMachineDetail myMachineDetail) {
        tvMachineCode.setText(myMachineDetail.getMachineCode() + "");
        tvMachineName.setText(myMachineDetail.getMineName() + "");
        tvMachineType.setText(myMachineDetail.getModel()+"");
//        tvMachineEnergy.setText(myMachineDetail.getNHRate()+"T/W");

        String totalPrice = "0";
        String price = "0";
        if (!StringUtils.isBlank(myMachineDetail.getPrice())){
            price = String.format("%.2f", Double.valueOf(myMachineDetail.getPrice()));
        }
        if (!StringUtils.isBlank(myMachineDetail.getPowerSum())){
            totalPrice = String.format("%.2f", Double.valueOf(myMachineDetail.getPowerSum()));
        }
//        tvPowerSum.setText(price + "元");
        tvPowerSumTotal.setText(totalPrice+"元");

        int runHours = ((Number) (Float.parseFloat(myMachineDetail.getRunHours()))).intValue();
        String formatRunHours = String.format("%.2f", Double.valueOf(myMachineDetail.getRunHours()));

        tvRunHours.setText(myMachineDetail.getTimeShow() + "");

        tvRunRate.setText(String.format("%.2f", Double.valueOf(myMachineDetail.getRunRate()) * 100) + "%");
        tvRunStatus.setText(myMachineDetail.getStatusName());
        String pools = myMachineDetail.getPools();
        String[] poolName = pools.split("\\|");
        StringBuffer poolNames = new StringBuffer();
        for (String name : poolName) {
            String[] namelist = name.split(",");
            if(namelist.length > 2){
                poolNames.append("URL：" + namelist[0] + " \n " + "Worker：" + namelist[1] + "\n" + "Password：" + namelist[2] + "\n\n");
            }else{
                poolNames.append("URL：" + namelist[0] + " \n " + "Worker：" + namelist[1] + "\n" + "Password：x \n\n");
            }
        }
//        tvPools.setText(poolNames + "");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
