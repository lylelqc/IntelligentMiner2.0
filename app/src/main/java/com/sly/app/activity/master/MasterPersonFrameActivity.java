package com.sly.app.activity.master;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.adapter.master.PersonFrameExpandListAdapter;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.master.MasterPersonListBean;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.ExpandableListViewDividerAdapter;
import com.sly.app.view.SwipeExpandableListView;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MasterPersonFrameActivity extends BaseActivity implements ICommonViewUi,
        PersonFrameExpandListAdapter.OnClickDeleteListenter {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.tv_master_frame_mine_name)
    TextView tvFrameMineName;

    @BindView(R.id.expandable_list_view)
    SwipeExpandableListView ExpandableListView;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String MineName;
    private String PersonSysCode;

    private List<MasterPersonListBean> mResultList = new ArrayList<>();
    private CommonRequestPresenterImpl iCommonRequestPresenter;
    private PersonFrameExpandListAdapter adapter;

    private List<MasterPersonListBean> firstList;
    private List<List<MasterPersonListBean>> secondList;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_master_person_frame;
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
        tvFrameMineName.setText(MineName);

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_MASTER_PERSON_MANAGE_LIST);

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
    protected void onResume() {
        super.onResume();
        toRequest(NetConstant.EventTags.GET_MASTER_PERSON_MANAGE_LIST);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        if (eventTag == NetConstant.EventTags.GET_MASTER_PERSON_MANAGE_LIST) {
            map.put("Rounter", NetConstant.GET_MASTER_PERSON_MANAGE_LIST);
        } else {
            map.put("Rounter", NetConstant.DELETE_EVERY_PERSON);
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
        Logcat.e("返回参数" + result);
        if (eventTag == NetConstant.EventTags.GET_MASTER_PERSON_MANAGE_LIST) {
//            mResultList.clear();
            List<MasterPersonListBean> resultList =
                    (List<MasterPersonListBean>) AppUtils.parseRowsResult(result, MasterPersonListBean.class);
//            mResultList.addAll(resultList);

            //一级数据
            firstList = new ArrayList<>();
            for (MasterPersonListBean bean : resultList) {
                if (bean.getGrade() == 2) {
                    firstList.add(bean);
                }
            }

            //二级数据
            secondList = new ArrayList<>();
            //剩余数据
            List<MasterPersonListBean> thirdList = new ArrayList<>();

            List<MasterPersonListBean> childList = new ArrayList<>();
            if(!AppUtils.isListBlank(firstList)){
                for (MasterPersonListBean bean1 : firstList) {
                    for(MasterPersonListBean bean2 : resultList){
                        if(bean1.getParent().equals(bean2.getPersonSysCode())){
                            childList.add(bean1);
                        }

                        if(bean2.getParent() == null && bean2.getGrade() == 3){
                            thirdList.add(bean2);
                        }
                    }
                    secondList.add(childList);
                }
            }
            refreshListView();
        } else {
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
        adapter = new PersonFrameExpandListAdapter(this, firstList, secondList);
        ExpandableListView.setGroupIndicator(null);//把指示器设为null
        ExpandableListView.setAdapter(new ExpandableListViewDividerAdapter(adapter));
        adapter.setOnClickDeleteListenter(this);

        ExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                boolean groupExpanded = expandableListView.isGroupExpanded(i);
                if (groupExpanded) {
                    expandableListView.collapseGroup(i);
                } else {
                    expandableListView.expandGroup(i, true);
                }

                adapter.setIndicatorState(i, !groupExpanded);
                return false;

            }
        });

        ExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
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
    public void onItemClick(View view, String position) {
        String[] part = position.split("-");
        if(part[1].equals("null")){
            int pos = Integer.parseInt(part[0]);
            adapter.delete(pos);
            PersonSysCode = firstList.get(pos).getPersonSysCode();
        }
        else{
            int pos1 = Integer.parseInt(part[0]);
            int pos2 = Integer.parseInt(part[1]);
            adapter.delete(pos1, pos2);
            PersonSysCode = secondList.get(pos1).get(pos2).getPersonSysCode();
        }
        toRequest(NetConstant.EventTags.DELETE_EVERY_PERSON);
    }

    @OnClick({R.id.btn_main_back, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
        }
    }

    private void showCustomDialog(Context context, String content) {
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
