package com.sly.app.activity.master;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.qrc.ShareActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.master.MasterAccountExecRecyclerViewAdapter;
import com.sly.app.adapter.yunw.machine.MachineManageRecyclerViewAdapter;
import com.sly.app.base.Contants;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.listener.LoadMoreClickListener;
import com.sly.app.listener.OnRecyclerViewListener;
import com.sly.app.model.PostResult;
import com.sly.app.model.master.MasterAccountExecBean;
import com.sly.app.model.master.MasterAccountPermissionBean;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.model.yunw.machine.MachineListBean;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;
import com.sly.app.model.yunw.machine.MachineTypeBean;
import com.sly.app.presenter.IRecyclerViewPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.presenter.impl.RecyclerViewPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.NetUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.Yunw.ManageAllChoicePopView;
import com.sly.app.view.PopupView.Yunw.ManageAreaCheckPopView;
import com.sly.app.view.PopupView.Yunw.ManageStatusCheckPopView;
import com.sly.app.view.iviews.ICommonViewUi;
import com.sly.app.view.iviews.ILoadView;
import com.sly.app.view.iviews.ILoadViewImpl;
import com.sly.app.view.iviews.IRecyclerViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MasterAccountExecActivity extends BaseActivity implements ICommonViewUi, MasterAccountExecRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.ll_comm_layout)
    LinearLayout llComLayout;
    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.tv_master_exec_account)
    TextView tvMasterExecAccout;
    @BindView(R.id.tv_master_add_account)
    TextView tvMasterAddAccount;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String User, Token, FrSysCode, FMasterCode, MineCode, PersonSysCode, Key, LoginType;
    private String Mobile;
    private String ChildMobile;
    private String Permission;
    private String accredit;

    private int mCancelCount = 1;
    private int mFreeCount = 1;
    private int mPowerCount = 1;
    private int mRateCount = 1;
    private int mPartsCount = 1;


    private List<MasterAccountExecBean> mResultList = new ArrayList<>();

    private MachineManageRecyclerViewAdapter mIntermediary;
    private CommonRequestPresenterImpl iCommonRequestPresenter;
    private MasterAccountExecRecyclerViewAdapter adapter;
    private List<MasterAccountPermissionBean> mPermissionList;

    private Dialog dialog;
    private TextView tvMyAccount;
    private TextView tvAuthAccount;
    private TextView tvAccountAuth;
    private TextView freeAuth;
    private TextView eletricAuth;
    private TextView RunAuth;
    private TextView spareAuth;
    private Drawable open;
    private Drawable close;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_master_accountexec;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    public void onEvent(PostResult postResult) {
        super.onEvent(postResult);
        if (EventBusTags.LOGIN_SUCCESS.equals(postResult.getTag())) {

        }
    }

    @Override
    protected void initViewsAndEvents() {
//        areaCode = getIntent().getExtras().getString("areaCode");
//        statusCode = getIntent().getExtras().getString("statusCode");
//        if(AppUtils.isBlank(areaCode)){
//            areaCode = "";
//        }
//        if(AppUtils.isBlank(statusCode)){
//            statusCode = "";
//        }

        MineCode = getIntent().getExtras().getString("MineCode");

        tvMainTitle.setText(getString(R.string.master_account_exec));
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        FrSysCode = SharedPreferencesUtil.getString(mContext, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(mContext, "FMasterCode", "None");

        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");

        tvMasterExecAccout.setText(User);
        open = getResources().getDrawable(R.drawable.miners_open_icon);
        close = getResources().getDrawable(R.drawable.miners_shut_icon);

        intitNewsCount();

        toRequest(NetConstant.EventTags.GET_CHILD_ACCOUNTEXEC);
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

    // 非列表请求数据
    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);


        if (eventTag == NetConstant.EventTags.ADD_ACCOUNT_EXEC) {
            map.put("Rounter", NetConstant.ADD_ACCOUNT_EXEC);
            map.put("Mobile", Mobile);
            map.put("mineCode", MineCode);
            map.put("masterSysCode", FMasterCode);
        }else if (eventTag == NetConstant.EventTags.GET_AUTH_ACCOUNT_PERMISSION){
            map.put("Rounter", NetConstant.GET_AUTH_ACCOUNT_PERMISSION);
            map.put("Mobile", ChildMobile);
        }else if(eventTag == NetConstant.EventTags.SET_PERMISSION_FOR_ACCOUNT){
            map.put("Rounter", NetConstant.SET_PERMISSION_FOR_ACCOUNT);
            map.put("mineCode", MineCode);
            map.put("masterSysCode", FMasterCode);
            map.put("Mobile", ChildMobile);
            map.put("Permission", Permission);
            map.put("accredit", accredit);
        }
        else{
            map.put("Rounter", NetConstant.GET_CHILD_ACCOUNTEXEC);
            map.put("mineCode", MineCode);
            map.put("masterSysCode", FMasterCode);
        }

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数 - " + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 - " + result);
        if (eventTag == NetConstant.EventTags.ADD_ACCOUNT_EXEC) {
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                ToastUtils.showToast(getString(R.string.add_success));
                toRequest(NetConstant.EventTags.GET_CHILD_ACCOUNTEXEC);
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }
        }else if(eventTag == NetConstant.EventTags.GET_AUTH_ACCOUNT_PERMISSION){
            mPermissionList =
                    (List<MasterAccountPermissionBean>) AppUtils.parseRowsResult(result, MasterAccountPermissionBean.class);
            if (!AppUtils.isListBlank(mPermissionList)){
                MasterAccountPermissionBean bean = mPermissionList.get(0);
                AuthorizeDialog(bean.isCancel(), bean.isUsingFee(),
                        bean.isUsingPower(), bean.isUsingRate(), bean.isUsingPart());
            }
        }else if(eventTag == NetConstant.EventTags.SET_PERMISSION_FOR_ACCOUNT){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {

            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
        else {
            mResultList =
                    (List<MasterAccountExecBean>) AppUtils.parseRowsResult(result, MasterAccountExecBean.class);
            refreshListView();
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

    public void refreshListView() {
        adapter = new MasterAccountExecRecyclerViewAdapter(mContext, mResultList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_main_back, R.id.rl_notice, R.id.tv_master_add_account,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_master_add_account:
                showCustomDialog(this, R.layout.dialog_general_style2, 2, getString(R.string.create_accountexec), 1);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ChildMobile = mResultList.get(position).getToMobile();
        toRequest(NetConstant.EventTags.GET_AUTH_ACCOUNT_PERMISSION);
    }

    // 操作提示框
    private void showCustomDialog(Context context, int layout, final int btnType, String content, final int tag) {
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
        // 获取控件
        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        if (layout == R.layout.dialog_general_style) {
            TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
            tvDetails.setText(content);
        }
        title.setText(content);
        final EditText etDescriptions = dialog.findViewById(R.id.et_dialog_content);
        etDescriptions.setHint(getString(R.string.input_accountexec));
        etDescriptions.setHintTextColor(getResources().getColor(R.color.sly_text_999999));

        TextView cancel = dialog.findViewById(R.id.cancel_action);
        TextView confirm = dialog.findViewById(R.id.confirm_action);
        View line = dialog.findViewById(R.id.view_line);
        if (btnType == 1) {
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
                if (btnType != 1) {
                    if (tag == 1) {
                        Mobile = etDescriptions.getText().toString().trim();
                        toRequest(NetConstant.EventTags.ADD_ACCOUNT_EXEC);
                    }
                }
            }
        });
    }

    private void AuthorizeDialog(boolean isCancel, boolean isFree, boolean isPower, boolean isRate, boolean isParts) {
        if(dialog == null){
            dialog = new Dialog(this, R.style.quick_option_dialog);
            dialog.setContentView(R.layout.dialog_authorize);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setWindowAnimations((R.style.popWindow_anim_style));

            tvMyAccount = (TextView) dialog.findViewById(R.id.tv_master_my_account);
            tvAuthAccount = (TextView) dialog.findViewById(R.id.tv_master_auth_account);
            tvAccountAuth = (TextView) dialog.findViewById(R.id.tv_master_account_auth);

            freeAuth = (TextView) dialog.findViewById(R.id.tv_master_auth_free);
            eletricAuth = (TextView) dialog.findViewById(R.id.tv_master_auth_electric);
            RunAuth = (TextView) dialog.findViewById(R.id.tv_master_auth_run);
            spareAuth = (TextView) dialog.findViewById(R.id.tv_master_auth_spare);
        }

        tvMyAccount.setText(User);
        tvAuthAccount.setText(ChildMobile);

        // 总按钮设置
        if(isCancel){
            tvAccountAuth.setBackground(open);
            freeAuth.setClickable(true);
            eletricAuth.setClickable(true);
            RunAuth.setClickable(true);
            spareAuth.setClickable(true);
            mCancelCount ++;
        }else{
            tvAccountAuth.setBackground(close);
            freeAuth.setClickable(false);
            eletricAuth.setClickable(false);
            RunAuth.setClickable(false);
            spareAuth.setClickable(false);
        }
        // 费用按钮设置
        if(isFree){
            freeAuth.setBackground(open);
            mCancelCount ++;
        }else{
            freeAuth.setBackground(close);
        }
        // 耗电按钮设置
        if(isPower){
            eletricAuth.setBackground(open);
            mCancelCount ++;
        }else{
            eletricAuth.setBackground(close);
        }
        // 运行按钮设置
        if(isRate){
            RunAuth.setBackground(open);
            mCancelCount ++;
        }else{
            RunAuth.setBackground(close);
        }
        // 配件按钮设置
        if(isParts){
            spareAuth.setBackground(open);
            mCancelCount ++;
        }else{
            spareAuth.setBackground(close);
        }

        //点击按钮监听 0为授权 1为未授权
        tvAccountAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancelCount ++;
                setBgForBtn("Mine89_IsCancel",mCancelCount % 2);
            }
        });

        freeAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFreeCount ++;
                setBgForBtn("Mine89_UsingFee",mFreeCount % 2);
            }
        });

        eletricAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPowerCount ++;
                setBgForBtn("Mine89_UsingPower",mPowerCount % 2);
            }
        });

        RunAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRateCount ++;
                setBgForBtn("Mine89_UsingRate",mRateCount % 2);
            }
        });

        spareAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPartsCount ++;
                setBgForBtn("Mine89_UsingPart",mPartsCount % 2);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mCancelCount = 1;
                mFreeCount = 1;
                mPowerCount = 1;
                mRateCount = 1;
                mPartsCount = 1;
            }
        });

        dialog.show();
    }

    private void setBgForBtn(String name, int tag){
        Permission = name;
        accredit = tag+"";
        toRequest(NetConstant.EventTags.SET_PERMISSION_FOR_ACCOUNT);

        if(name.equals("Mine89_IsCancel")){
            if(tag == 0){
                tvAccountAuth.setBackground(open);
                freeAuth.setClickable(true);
                eletricAuth.setClickable(true);
                RunAuth.setClickable(true);
                spareAuth.setClickable(true);
            }else{
                tvAccountAuth.setBackground(close);
                freeAuth.setClickable(false);
                eletricAuth.setClickable(false);
                RunAuth.setClickable(false);
                spareAuth.setClickable(false);
            }
        }
        else if(name.equals("Mine89_UsingFee")){
            if(tag == 0){
                freeAuth.setBackground(open);
            }else{
                freeAuth.setBackground(close);
            }
        }
        else if(name.equals("Mine89_UsingPower")){
            if(tag == 0){
                eletricAuth.setBackground(open);
            }else{
                eletricAuth.setBackground(close);
            }
        }
        else if(name.equals("Mine89_UsingRate")){
            if(tag == 0){
                RunAuth.setBackground(open);
            }else{
                RunAuth.setBackground(close);
            }
        }
        else if(name.equals("Mine89_UsingPart")){
            if(tag == 0){
                spareAuth.setBackground(open);
            }else{
                spareAuth.setBackground(close);
            }
        }
    }
}
