package com.sly.app.activity.master;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.liucanwen.app.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.liucanwen.app.headerfooterrecyclerview.RecyclerViewUtils;
import com.sly.app.Helper.ActivityHelper;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.master.MasterPersonManageRecyclerViewAdapter;
import com.sly.app.adapter.yunw.goline.MachineGolineRecyclerViewAdapter;
import com.sly.app.adapter.yunw.offline.MachineOfflineRecyclerViewAdapter;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.FormBean;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.master.MasterPersonListBean;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;
import com.sly.app.view.SwipeItemLayout;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import vip.devkit.library.Logcat;

public class MasterPersonManageActivity extends BaseActivity implements ICommonViewUi,
        MasterPersonManageRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;
    @BindView(R.id.tv_main_right_left)
    TextView tvMainRightLeft;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String MineName;
    private String PersonSysCode;

    private List<MasterPersonListBean> mResultList = new ArrayList<>();
    private CommonRequestPresenterImpl iCommonRequestPresenter;
    private MasterPersonManageRecyclerViewAdapter adapter;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_master_person_manage;
    }

    @Override
    protected void initViewsAndEvents() {

        MineCode = getIntent().getExtras().getString("MineCode");
        MineName = getIntent().getExtras().getString("MineName");

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        tvMainTitle.setText(getString(R.string.master_person_manage));
        tvMainRightLeft.setBackground(getResources().getDrawable(R.drawable.go_online_list));

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_MASTER_PERSON_MANAGE_LIST);

    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if("0".equals(count)){
            tvRedNum.setVisibility(View.GONE);
        }else{
            tvRedNum.setVisibility(View.VISIBLE);
            tvRedNum.setText(count);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toRequest(NetConstant.EventTags.GET_MASTER_PERSON_MANAGE_LIST);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        if(eventTag == NetConstant.EventTags.GET_MASTER_PERSON_MANAGE_LIST){
            map.put("Rounter", NetConstant.GET_MASTER_PERSON_MANAGE_LIST);
        }
        else{
            map.put("Rounter", NetConstant.DELETE_MASTER_PERSON);
            map.put("personSysCode", PersonSysCode);
        }
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("mineCode", MineCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数 - " + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数"+result);
        if(eventTag == NetConstant.EventTags.GET_MASTER_PERSON_MANAGE_LIST){
            mResultList.clear();
            List<MasterPersonListBean> resultList =
                    (List<MasterPersonListBean>) AppUtils.parseRowsResult(result, MasterPersonListBean.class);
            mResultList.addAll(resultList);
            refreshListView();
        }
        else{
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                ToastUtils.showToast(getString(R.string.delete_success));
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
    }

    private void refreshListView() {
        if(adapter == null ){
            adapter = new MasterPersonManageRecyclerViewAdapter(mContext, mResultList);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
            recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
            recyclerView.setLayoutManager(layoutManager);
        }
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

    @Override
    public void onItemClick(View view, int position) {
        MasterPersonListBean bean = mResultList.get(position);
        String personSysCode = mResultList.get(position).getPersonSysCode();
        Bundle bundle = new Bundle();
        bundle.putString("MineCode", MineCode);
        bundle.putString("PersonSysCode", personSysCode);
        switch (view.getId()){
            case R.id.tv_master_person_add_modify:
                if(bean.getGrade() == 3){
                    AppUtils.goActivity(this, MasterSetManagerActivity.class, bundle);
                }
                else{
                    bundle.putString("Manager", bean.getMineName());
                    AppUtils.goActivity(this, MasterAddPersonActivity.class, bundle);
                }
                break;
            case R.id.delete:
                PersonSysCode = personSysCode;
                showCustomDialog(this, getString(R.string.request_delete_plan));
                break;
        }
    }

    @OnClick({R.id.btn_main_back,R.id.tv_main_right_left, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_main_right_left:
                Bundle bundle = new Bundle();
                bundle.putString("MineCode", MineCode);
                bundle.putString("MineName", MineName);
                AppUtils.goActivity(this, MasterPersonFrameActivity.class, bundle);
                break;

        }
    }

    private void showCustomDialog(Context context, String content){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_general_style);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
        tvDetails.setText(content);
        TextView cancel = dialog.findViewById(R.id.cancel_action);
        TextView confirm = dialog.findViewById(R.id.confirm_action);

        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonSysCode = "";
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                toRequest(NetConstant.EventTags.DELETE_MASTER_PERSON);
            }
        });
    }

}
