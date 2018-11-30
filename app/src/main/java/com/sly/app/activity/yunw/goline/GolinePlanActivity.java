package com.sly.app.activity.yunw.goline;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.yunw.goline.GolinePlanRecyclerViewAdapter;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.yunw.goline.GolinePlanListBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class GolinePlanActivity extends BaseActivity implements ICommonViewUi, GolinePlanRecyclerViewAdapter.OnItemClickListener{

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
//    @BindView(R.id.tv_main_right)
//    TextView tvMainRight;
    @BindView(R.id.tv_main_right_left)
    TextView tvMainRightLeft;

    @BindView(R.id.recycler_plan_view)
    RecyclerView rvPlanListView;

    @BindView(R.id.ll_goline_plan_btn)
    LinearLayout llPlanBtn;
    @BindView(R.id.tv_goline_plan_all_chose)
    TextView tvPlanAllChose;
    @BindView(R.id.tv_goline_plan_delete)
    TextView tvPlanDelete;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String PersonSysCode;
    private String planID;

    private List<GolinePlanListBean> mGolinePlanList = new ArrayList<>();
    ICommonRequestPresenter iCommonRequestPresenter;
    private GolinePlanRecyclerViewAdapter adapter;
    private boolean isEditStatus;
    private int clickCount = 0;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goline_plan;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText(getString(R.string.machine_goline));
//        tvMainRightLeft.setBackground(getResources().getDrawable(R.drawable.go_online_list));
        tvMainRightLeft.setText(getString(R.string.machine_edit));
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode","None");

        toRequest(NetConstant.EventTags.GET_GOLINE_PLAN_LIST);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        if(eventTag == NetConstant.EventTags.GET_GOLINE_PLAN_LIST){
            map.put("Rounter", NetConstant.GET_GOLINE_PLAN_LIST);
        }else{
            map.put("Rounter", NetConstant.DELETE_GOLINE_PLAN);
            map.put("planID", planID);
        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("mineCode", MineCode);
        map.put("personSysCode", PersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数 = "+result);
        if(eventTag == NetConstant.EventTags.GET_GOLINE_PLAN_LIST){
            mGolinePlanList =
                    (List<GolinePlanListBean>) AppUtils.parseRowsResult(result, GolinePlanListBean.class);
            refreshListView();
        }else{
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                mGolinePlanList.remove(adapter.getIndex());
                adapter.notifyDataSetChanged();
                ToastUtils.showToast(getString(R.string.delete_success));
            }else{
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
    }

    private void refreshListView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvPlanListView.setLayoutManager(layoutManager);
        adapter = new GolinePlanRecyclerViewAdapter(mContext, mGolinePlanList);
        rvPlanListView.setAdapter(adapter);
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

    @OnClick({R.id.btn_main_back, R.id.tv_main_right_left, R.id.tv_goline_plan_delete })
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_main_right_left:
                clickCount++;
                setEditBtnVisibility();
                break;
            case R.id.tv_goline_plan_delete:
                if( adapter.getIndex() != -1){
                    showCustomDialog(this, R.layout.dialog_general_style, 2, getString(R.string.request_delete_plan));
                }
                break;
//            case R.id.tv_goline_plan_all_chose:
//
//                break;

        }
    }

    private void setEditBtnVisibility(){
        // 0不可编辑状态  1可编辑状态
        if(clickCount % 2 == 0){
            tvMainRightLeft.setText(getString(R.string.machine_edit));
            llPlanBtn.setVisibility(View.GONE);
            isEditStatus = false;
            adapter.setIndex(-1);
        }else{
            tvMainRightLeft.setText(getString(R.string.dialog_cancel));
            llPlanBtn.setVisibility(View.VISIBLE);
            isEditStatus = true;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(isEditStatus){
            adapter.setIndex(position);
        }else{
            String planID = mGolinePlanList.get(position).getMine68_PlanID();
            Bundle bundle = new Bundle();
            bundle.putString("planID", planID);
            AppUtils.goActivity(this, PlanMachineListActivity.class, bundle);
        }
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
                planID = mGolinePlanList.get(adapter.getIndex()).getMine68_PlanID();
                toRequest(NetConstant.EventTags.DELETE_GOLINE_PLAN);
            }
        });
    }
}
