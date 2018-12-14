package com.sly.app.activity.miner;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.miner.MinerBillDateBean;
import com.sly.app.model.miner.MinerFreeBean;
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
import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import vip.devkit.library.Logcat;

public class MinerFreeActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.rl_record_free_date)
    RelativeLayout rlRecordFreeDate;
    @BindView(R.id.tv_record_free_date)
    TextView tvRecordFreeDate;
    @BindView(R.id.tv_free_money)
    TextView tvFreeMoney;

    @BindView(R.id.tv_free_manage_money)
    TextView tvFreeManageMoney;
    @BindView(R.id.pb_free_manage_progress)
    ProgressBar pbFreeManage;
    @BindView(R.id.tv_free_manage_rate)
    TextView tvFreeManageRate;
    @BindView(R.id.tv_free_repair_money)
    TextView tvFreeRepairMoney;
    @BindView(R.id.pb_free_repair_progress)
    ProgressBar pbFreeRepair;
    @BindView(R.id.tv_free_repair_rate)
    TextView tvFreeRepairRate;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String FrSysCode;
    private String DataID;

    private List<MinerBillDateBean> mResultList = new ArrayList<>();
    private List<MinerFreeBean> mFreeList = new ArrayList<>();
    private CommonRequestPresenterImpl iCommonRequestPresenter;

    private Dialog dialog;
    private NumberPickerView picker;
    private String[] peroidArray;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_miner_free;
    }

    @Override
    protected void initViewsAndEvents() {
        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        tvMainTitle.setText(getString(R.string.master_free));

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_MINER_BILL_DATE);

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
        if (eventTag == NetConstant.EventTags.GET_MINER_BILL_DATE) {
            map.put("Rounter", NetConstant.GET_MINER_BILL_DATE);
            map.put("minerSysCode", FrSysCode);
        }
        else if(eventTag == NetConstant.EventTags.GET_MINER_PEROID_FREE){
            map.put("Rounter", NetConstant.GET_MINER_PEROID_FREE);
            map.put("DataID", DataID);
        }


        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数 - " + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数" + result);
        if (eventTag == NetConstant.EventTags.GET_MINER_BILL_DATE) {
            mResultList.clear();
            List<MinerBillDateBean> resultList =
                    (List<MinerBillDateBean>) AppUtils.parseRowsResult(result, MinerBillDateBean.class);
            mResultList.addAll(resultList);
            dealData();
            if(!AppUtils.isListBlank(mResultList)){
                DataID = mResultList.get(0).getDataID();
                toRequest(NetConstant.EventTags.GET_MINER_PEROID_FREE);
            }
        }else if(eventTag == NetConstant.EventTags.GET_MINER_PEROID_FREE){
            mFreeList = (List<MinerFreeBean>) AppUtils.parseRowsResult(result, MinerFreeBean.class);
            if(!AppUtils.isListBlank(mFreeList)){
                MinerFreeBean bean = mFreeList.get(0);
                double repiarFree = bean.getRepairSum();
                double powerFree = bean.getPowerFeeSum();
                double AllFree = repiarFree + powerFree;

                int repairRate = (int)((repiarFree / AllFree)*100);
                int powerRate = 100 - repairRate;

                tvFreeMoney.setText(AllFree+"");
                tvFreeManageMoney.setText(powerFree + "");
                tvFreeRepairMoney.setText(repiarFree + "");

                tvFreeManageRate.setText(powerRate + "%");
                tvFreeRepairRate.setText(repairRate + "%");
                pbFreeManage.setProgress(powerRate);
                pbFreeRepair.setProgress(repairRate);
            }
        }
    }

    private void dealData() {
        peroidArray = new String[mResultList.size()];
        for(int i = 0; i < mResultList.size(); i++){
            String begin = mResultList.get(i).getBeginDate().split(" ")[0].replace("-","/");
            String end = mResultList.get(i).getEndDate().split(" ")[0].replace("-","/");
            peroidArray[i] = begin + " - " + end;
        }
        tvRecordFreeDate.setText(peroidArray[0]);
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

    @OnClick({R.id.btn_main_back, R.id.rl_notice,R.id.rl_record_free_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.rl_record_free_date:
                dialog();
                break;
        }
    }

    private void dialog() {
        dialog = new Dialog(mContext, R.style.quick_option_dialog);
        dialog.setContentView(R.layout.dialog_period_picker);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setWindowAnimations((R.style.popWindow_anim_style));
        dialog.show();
        Button btnSelCancel = dialog.findViewById(R.id.repair_date_sel_cancel);
        Button btnSelOk = dialog.findViewById(R.id.repair_date_sel_ok);
        picker = dialog.findViewById(R.id.period_picker);
        picker.refreshByNewDisplayedValues(peroidArray);

        btnSelCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSelOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String date = picker.getContentByCurrValue();
                tvRecordFreeDate.setText(date);
                getPeroidData(date);
            }
        });
    }

    private void getPeroidData(String date) {
        for(int i = 0; i < peroidArray.length; i++){
            if(peroidArray[i].equals(date)){
                DataID = mResultList.get(i).getDataID();
                break;
            }
        }
        toRequest(NetConstant.EventTags.GET_MINER_PEROID_FREE);
    }

}
