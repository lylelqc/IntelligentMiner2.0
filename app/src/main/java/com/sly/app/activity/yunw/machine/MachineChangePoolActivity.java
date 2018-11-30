package com.sly.app.activity.yunw.machine;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.Helper.ActivityHelper;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReturnBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.ChangeIpDuanPopView;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import vip.devkit.library.Logcat;

public class MachineChangePoolActivity extends BaseActivity implements ICommonViewUi, ChangeIpDuanPopView.OnSearchClickListener {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;

    @BindView(R.id.et_change_mine1_pool)
    EditText etChangeMine1Pool;
    @BindView(R.id.et_change_miner1_code)
    EditText etChangeMiner1Code;
    @BindView(R.id.et_change_pwd1)
    EditText etChangePwd1;

    @BindView(R.id.et_change_mine2_pool)
    EditText etChangeMine2Pool;
    @BindView(R.id.et_change_miner2_code)
    EditText etChangeMiner2Code;
    @BindView(R.id.et_change_pwd2)
    EditText etChangePwd2;

    @BindView(R.id.et_change_mine3_pool)
    EditText etChangeMine3Pool;
    @BindView(R.id.et_change_miner3_code)
    EditText etChangeMiner3Code;
    @BindView(R.id.et_change_pwd3)
    EditText etChangePwd3;

    @BindView(R.id.tv_ip_duan)
    TextView tvIpDuan;
    @BindView(R.id.rl_spinner)
    RelativeLayout rlSpinner;

    @BindView(R.id.tv_change_comfirm)
    TextView tvChangeComfirm;

    ICommonRequestPresenter iCommonRequestPresenter;

    private String User,LoginType, MineCode, PersonSysCode, Token, Key, workers;
    private ChangeIpDuanPopView mChangeIpDuanView;
    private String ipSectionCount = "";
    private String url1 = "";
    private String worker1 = "";
    private String password1 = "";
    private String url2 = "";
    private String worker2 = "";
    private String password2 = "";
    private String url3 = "";
    private String worker3 = "";
    private String password3 = "";
    private String machineSysCodes= "";

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_change_machine_pool;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        tvMainTitle.setText(getString(R.string.machine_change_pool));

        machineSysCodes = getIntent().getExtras().getString("machineSysCodes");
        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");
        toRequest(NetConstant.EventTags.SET_DETAILS_AND_MANAGE_POOL);

    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Rounter", NetConstant.SET_DETAILS_AND_MANAGE_POOL);

        map.put("mineCode", MineCode);
        map.put("personSysCode", PersonSysCode);
        map.put("machineSysCodes", machineSysCodes);
        map.put("ipSectionCount", ipSectionCount);
        map.put("url1", url1);
        map.put("worker1", worker1);
        map.put("password1", password1);
        map.put("url2", url2);
        map.put("worker2", worker2);
        map.put("password2", password2);
        map.put("url3", url3);
        map.put("worker3", worker3);
        map.put("password3", password3);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
        if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
            showCustomDialog(this, R.layout.dialog_general_style, 1, getString(R.string.comfirm_success));
        }else{
            ToastUtils.showToast(returnBean.getMsg());
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

    private void getEtData() {
        url1 = etChangeMine1Pool.getText().toString().trim();
        worker1 = etChangeMiner1Code.getText().toString().trim();
        password1 = etChangePwd1.getText().toString().trim();
        url2 = etChangeMine2Pool.getText().toString().trim();
        worker2 = etChangeMiner2Code.getText().toString().trim();
        password2 = etChangePwd2.getText().toString().trim();
        url3 = etChangeMine3Pool.getText().toString().trim();
        worker3 = etChangeMiner3Code.getText().toString().trim();
        password3 = etChangePwd3.getText().toString().trim();
        ipSectionCount = tvIpDuan.getText().toString().trim();
//        toRequest();
    }

    @OnClick({R.id.btn_main_back, R.id.tv_change_comfirm, R.id.rl_spinner})
    public void onViewClicked(View view){
        switch(view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_change_comfirm:
                getEtData();
                break;
            case R.id.rl_spinner:
                if(mChangeIpDuanView == null){
                    mChangeIpDuanView = new ChangeIpDuanPopView(this);
                    mChangeIpDuanView.showFilterPopup(rlSpinner);
                    mChangeIpDuanView.setSearchClickListener(this);
                }else{
                    mChangeIpDuanView.showFilterPopup(rlSpinner);
                }
                break;
        }
    }

    @Override
    public void onSearchClick(View view, int position) {
        tvIpDuan.setText(position+"");
        mChangeIpDuanView.dismiss();
    }

    private void showCustomDialog(Context context, int layout, final int btnType, String content){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(layout == R.layout.dialog_general_style){
            TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
            tvDetails.setText(content);
        }

        TextView cancel = dialog.findViewById(R.id.cancel_action);
        TextView confirm = dialog.findViewById(R.id.confirm_action);
        View line = dialog.findViewById(R.id.view_line);
        if(btnType == 1){
            cancel.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                MachineChangePoolActivity.this.finish();
            }
        });
    }
}
