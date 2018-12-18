package com.sly.app.activity.miner;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.miner.MinerRepairDetailsBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MinerFreeDetailsActivty extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.miner_cost_details_money)
    TextView tvDetailsMoney;
    @BindView(R.id.miner_cost_details_model)
    TextView tvDetailsModel;
    @BindView(R.id.miner_cost_details_repair)
    TextView tvDetailsRepair;

    @BindView(R.id.miner_cost_details_maintenance_numbers)
    TextView tvDetailsBillCode;
    @BindView(R.id.miner_cost_details_ptime)
    TextView tvDetailsPtime;
    @BindView(R.id.miner_cost_details_etime)
    TextView tvDetailsEtime;
    @BindView(R.id.miner_cost_details_machine_num)
    TextView tvDetailsMachineNum;
    @BindView(R.id.miner_cost_details_failure_reason)
    TextView tvDetailsMachineReason;
    @BindView(R.id.miner_cost_details_maintenance_descriped)
    TextView tvDetailsRepairDescriped;

    private String User, LoginType, FrSysCode, FMasterCode, MineCode, Token, Key, machineSysCode;
    private String BillNo;
    ICommonRequestPresenter iCommonRequestPresenter;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_miner_cost_details;
    }

    @Override
    protected void initViewsAndEvents() {

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        BillNo = getIntent().getExtras().getString("BillNo");

        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");
        tvMainTitle.setText(getResources().getString(R.string.miner_repair_free_details));

        intitNewsCount();

        toRequest(NetConstant.EventTags.GET_MINER_REPAIR_DETAILS);
    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if ("0".equals(count)) {
            tvRedNum.setVisibility(View.GONE);
        } else {
            tvRedNum.setVisibility(View.VISIBLE);
            tvRedNum.setText(count);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        if (eventTag == NetConstant.EventTags.GET_MINER_REPAIR_DETAILS) {
            map.put("Rounter", NetConstant.GET_MINER_REPAIR_DETAILS);
            map.put("BillNo", BillNo);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 = " + result);
        if (eventTag == NetConstant.EventTags.GET_MINER_REPAIR_DETAILS) {
            List<MinerRepairDetailsBean> beanList =
                    (List<MinerRepairDetailsBean>) AppUtils.parseRowsResult(result, MinerRepairDetailsBean.class);
//            tvRepairFreeMoney.setText(bean.get(0).getRepairSum()+"");
            MinerRepairDetailsBean bean = beanList.get(0);
            tvDetailsMoney.setText(bean.getRepairSum()+"");
            tvDetailsModel.setText(bean.getModel());
            tvDetailsRepair.setText(bean.getResultName());
            tvDetailsBillCode.setText(bean.getBillNo());
            tvDetailsPtime.setText(bean.getPtime());
            tvDetailsEtime.setText(bean.getEndTime());
            tvDetailsMachineNum.setText(bean.getMachineCode());
            tvDetailsMachineReason.setText(bean.getInfo());
            tvDetailsRepairDescriped.setText(bean.getRepairInfo());
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
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
        }
    }
}
