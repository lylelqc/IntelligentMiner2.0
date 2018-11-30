package com.sly.app.activity.yunw.goline;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.yunw.goline.PlanMachineListRecyclerViewAdapter;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.yunw.goline.PlanAreaBean;
import com.sly.app.model.yunw.goline.PlanMachineListBean;
import com.sly.app.model.yunw.goline.UserNameBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PopupView.ManageAreaCheckPopView;
import com.sly.app.view.PopupView.PlanAreaCheckPopView;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class PlanMachineListActivity extends BaseActivity implements ICommonViewUi,
        PlanMachineListRecyclerViewAdapter.OnItemClickListener, PlanAreaCheckPopView.OnSearchClickListener {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    //    @BindView(R.id.tv_main_right)
//    TextView tvMainRight;

    @BindView(R.id.rl_plan_machine_header)
    RelativeLayout rlPlanMachineHeader;
    @BindView(R.id.rl_machine_list_area)
    RelativeLayout rlPlanMachineChoseArea;
    @BindView(R.id.tv_plan_machine_manage)
    TextView tvPlanMachineManage;

    @BindView(R.id.view_plan_machine_chose_header)
    View viewChoseHeader;
    @BindView(R.id.ll_plan_machine_miner_code)
    LinearLayout llPlanMachineMinerCode;
    @BindView(R.id.tv_plan_machine_miner_icon)
    TextView tvPlanMachineMinerIcon;
    @BindView(R.id.ll_plan_machine_vip_code)
    LinearLayout llPlanMachineVipCode;
    @BindView(R.id.tv_plan_machine_vip_icon)
    TextView tvPlanMachineVipIcon;

    @BindView(R.id.recycler_plan_machine_view)
    RecyclerView rvPlanMachineListView;

    @BindView(R.id.rl_plan_machine_commit_plan)
    RelativeLayout rlPlanMachineCommit;
    @BindView(R.id.tv_plan_machine_commit)
    TextView tvPlanMachineCommit;
    @BindView(R.id.rl_plan_machine_bind_vip)
    RelativeLayout rlPlanMachineBindVip;
    @BindView(R.id.tv_plan_machine_bind_vip)
    TextView tvPlanMahcineBindVip;

    @BindView(R.id.ll_shadow)
    LinearLayout llShadow;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String PersonSysCode;
    private String planID;
    private String areaCode = "";
    private String orderField = "Worker";
    private String orderBy = "ASC";
    private String FrSysCode;
    private String Worker;
    private String vipCode;

    private List<PlanMachineListBean> mPlanMachineList = new ArrayList<>();
    private List<PlanAreaBean> mPlanAreaList = new ArrayList<>();
    ICommonRequestPresenter iCommonRequestPresenter;
    private PlanMachineListRecyclerViewAdapter adapter;
    private boolean isEditStatus;
    private int clickCount = 0;
    private int mMinerCount = 0;
    private int mVipCount = 0;
    private int currentPosition = -1;

    private PlanAreaCheckPopView mPlanAreaCheckPopView;
    private boolean OwnerIsNull;
    private TextView vipName;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_plan_machine_list;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText(getString(R.string.machine_scan_details));

        planID = getIntent().getExtras().getString("planID");
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode","None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode","None");

        toRequest(NetConstant.EventTags.GET_PLAN_MACHINE_AREA);
        toRequest(NetConstant.EventTags.GET_PLAN_MACHINE_LIST);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        if(eventTag == NetConstant.EventTags.GET_PLAN_MACHINE_LIST){
            map.put("Rounter", NetConstant.GET_PLAN_MACHINE_LIST);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);
            map.put("planID", planID);
            map.put("areaCode", areaCode);
            map.put("orderField", orderField);
            map.put("orderBy", orderBy);
        }
        else if(eventTag == NetConstant.EventTags.GET_PLAN_MACHINE_AREA){
            map.put("Rounter", NetConstant.GET_PLAN_MACHINE_AREA);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);
            map.put("planID", planID);
        }
        else if(eventTag == NetConstant.EventTags.COMMIT_PLAN_MACHINE_LIST){
            map.put("Rounter", NetConstant.COMMIT_PLAN_MACHINE_LIST);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);
            map.put("planID", planID);
        }
        else if(eventTag == NetConstant.EventTags.GET_NAME_BY_VIP_CODE){
            map.put("Rounter", NetConstant.GET_NAME_BY_VIP_CODE);
            map.put("minerSysCode", vipCode);
        }else if(eventTag == NetConstant.EventTags.BIND_VIP_CODE){
            map.put("Rounter", NetConstant.BIND_VIP_CODE);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);
            map.put("planID", planID);
            if(currentPosition != -1){
                Worker = mPlanMachineList.get(adapter.getIndex()).getWorker();
            }
            map.put("Worker", Worker);
            map.put("minerSysCode", vipCode);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 = "+result);
        if(eventTag == NetConstant.EventTags.GET_PLAN_MACHINE_LIST){
            mPlanMachineList =
                    (List<PlanMachineListBean>) AppUtils.parseRowsResult(result, PlanMachineListBean.class);
            refreshListView();
        }
        else if(eventTag == NetConstant.EventTags.GET_PLAN_MACHINE_AREA){
            mPlanAreaList =
                    (List<PlanAreaBean>) AppUtils.parseRowsResult(result, PlanAreaBean.class);
        }
        else if(eventTag == NetConstant.EventTags.COMMIT_PLAN_MACHINE_LIST){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") ) {
                ToastUtils.showToast(getString(R.string.comfirm_success));
                finish();
            }else{
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
        else if(eventTag == NetConstant.EventTags.GET_NAME_BY_VIP_CODE){
            List<UserNameBean> list =
                    (List<UserNameBean>) AppUtils.parseRowsResult(result, UserNameBean.class);
            if(vipName != null && list != null && list.size() > 0){
                vipName.setText(list.get(0).getName());
            }
        }else if(eventTag == NetConstant.EventTags.BIND_VIP_CODE){
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") ) {
                ToastUtils.showToast(getString(R.string.bind_vip_success));
            }else{
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
//        else{
//            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
//            if (returnBean.getStatus().equals("1") ) {
//                showCustomDialog(this, R.layout.dialog_general_style, 1, getString(R.string.delete_success),2);
//            }else{
//                ToastUtils.showToast(returnBean.getMsg());
//            }
//        }
    }

    private void refreshListView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvPlanMachineListView.setLayoutManager(layoutManager);
        adapter = new PlanMachineListRecyclerViewAdapter(mContext, mPlanMachineList);
        rvPlanMachineListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
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

    @OnClick({R.id.btn_main_back, R.id.rl_machine_list_area, R.id.tv_plan_machine_manage,
            R.id.ll_plan_machine_miner_code, R.id.ll_plan_machine_vip_code, R.id.tv_plan_machine_commit,
            R.id.tv_plan_machine_bind_vip })
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_machine_list_area:
                mPlanAreaCheckPopView = new PlanAreaCheckPopView(this, mPlanAreaList);
                mPlanAreaCheckPopView.setPlanAreaSearchClickListener(this);
                mPlanAreaCheckPopView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        llShadow.setVisibility(View.GONE);
                    }
                });
                mPlanAreaCheckPopView.showFilterPopup(rlPlanMachineHeader);

                AlphaAnimation appearAnimation1 = new AlphaAnimation(0, 1);
                appearAnimation1.setDuration(170);
                llShadow.setAnimation(appearAnimation1);
                llShadow.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_plan_machine_manage:
                clickCount ++;
                setEditBtnVisibility();
                break;
            case R.id.ll_plan_machine_miner_code:
                mMinerCount ++;
                setHeaderSort("Worker", mMinerCount % 2);
                break;
            case R.id.ll_plan_machine_vip_code:
                mVipCount ++;
                setHeaderSort("Member", mVipCount % 2);
                break;
            case R.id.tv_plan_machine_commit:
                for(PlanMachineListBean bean : mPlanMachineList){
                    if(AppUtils.isBlank(bean.getOwner())){
                        OwnerIsNull = true;
                        break;
                    }else {
                        OwnerIsNull = false;
                    }
                }
                if(OwnerIsNull){
                    ToastUtils.showToast(getString(R.string.vip_no_bind_no_commit));
                }else{
                    showCustomDialog(this, R.layout.dialog_general_style2, 2, getString(R.string.request_commit_plan), 1);
                }
                break;
            case R.id.tv_plan_machine_bind_vip:
                String title = tvPlanMahcineBindVip.getText().toString().trim();
                if(adapter.getIndex() != -1){
                    showCustomDialog(this, R.layout.dialog_bind_vip, 2, title, 2);
                }
                break;
//            case R.id.tv_goline_plan_all_chose:
//
//                break;

        }
    }

    // 点击头部排序
    private void setHeaderSort(String tag, int count) {
        Drawable drawableUp = getResources().getDrawable(R.drawable.havepaixu_tingji_icon);
        Drawable drawableLow = getResources().getDrawable(R.drawable.paixu_tingji_icon);

        if(tag.equals("Worker")){
            // 1为升序   0为降序
            tvPlanMachineMinerIcon.setBackground(count == 1 ? drawableUp : drawableLow);
        }else{
            tvPlanMachineVipIcon.setBackground(count == 1 ? drawableUp : drawableLow);
        }
        orderField = tag;
        orderBy = (count == 1) ? "ASC" : "DESC";
        toRequest(NetConstant.EventTags.GET_PLAN_MACHINE_LIST);
    }

    // 管理按钮状态
    private void setEditBtnVisibility() {
        // 0不可编辑状态  1可编辑状态
        if (clickCount % 2 == 0) {
            tvPlanMachineManage.setText(getString(R.string.machine_manage));
            viewChoseHeader.setVisibility(View.GONE);
            rlPlanMachineCommit.setVisibility(View.VISIBLE);
            rlPlanMachineBindVip.setVisibility(View.GONE);
            adapter.setIndex(-1);
            isEditStatus = false;
            adapter.setCheckBox(false);
        } else {
            tvPlanMachineManage.setText(getString(R.string.machine_finish));
            viewChoseHeader.setVisibility(View.VISIBLE);
            rlPlanMachineCommit.setVisibility(View.GONE);
            rlPlanMachineBindVip.setVisibility(View.VISIBLE);
            isEditStatus = true;
            adapter.setCheckBox(true);
        }
    }

    @Override
    public void onPlanAreaSearchClick(View view, int position) {
        if(mPlanAreaCheckPopView != null ){
            areaCode = mPlanAreaCheckPopView.getPlanAreaCode();
            toRequest(NetConstant.EventTags.GET_PLAN_MACHINE_LIST);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch(view.getId()){
            case R.id.cb_plan_machine_chose_item:
                int index = adapter.getIndex();
                if(index != -1){
                    String owner = mPlanMachineList.get(index).getOwner();
                    if(!AppUtils.isBlank(owner)){
                        tvPlanMahcineBindVip.setText(getString(R.string.machine_change_vip));
                    }else{
                        tvPlanMahcineBindVip.setText(getString(R.string.machine_bind_vip));
                    }
                }else{
                    tvPlanMahcineBindVip.setText(getString(R.string.machine_bind_vip));
                }
                break;
            case R.id.ll_plan_machine_details:
                if(!isEditStatus){
                    Bundle bundle = new Bundle();
                    bundle.putString("planID", planID);
                    bundle.putString("areaCode", areaCode);
                    String Worker = mPlanMachineList.get(position).getWorker();
                    bundle.putString("Worker", Worker);
                    AppUtils.goActivity(this, AllMachineListActivity.class, bundle);
                }
                break;
        }
    }

    private void showCustomDialog(Context context, int layout, final int btnType, String content, final int tag){
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
        }else{
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(content);
            final EditText etContent = dialog.findViewById(R.id.et_dialog_content);
            etContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.toString().length() == 8){
                        vipCode = editable.toString().trim();
                        toRequest(NetConstant.EventTags.GET_NAME_BY_VIP_CODE);
                    }
                }
            });
            vipName = dialog.findViewById(R.id.tv_vip_name);
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
                if(tag == 2){
                    vipCode = "";
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(tag == 1){
                    toRequest(NetConstant.EventTags.COMMIT_PLAN_MACHINE_LIST);
                }else{
                    toRequest(NetConstant.EventTags.BIND_VIP_CODE);
                }
            }
        });
    }
}
