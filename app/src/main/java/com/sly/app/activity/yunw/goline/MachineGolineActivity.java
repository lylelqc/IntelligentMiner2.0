package com.sly.app.activity.yunw.goline;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.activity.yunw.machine.MachineChangePoolActivity;
import com.sly.app.adapter.yunw.goline.GolinePlanRecyclerViewAdapter;
import com.sly.app.adapter.yunw.goline.MachineGolineRecyclerViewAdapter;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.yunw.goline.GolineAreaBean;
import com.sly.app.model.yunw.goline.GolinePlanListBean;
import com.sly.app.model.yunw.machine.MachineManageAreaBean;
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
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MachineGolineActivity extends BaseActivity implements ICommonViewUi{

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

    @BindView(R.id.ll_goline_chooseArea)
    LinearLayout llGolineChoseArea;

    @BindView(R.id.recycler_area_view)
    RecyclerView rvAreaListView;

    @BindView(R.id.tv_goline_commit_plan)
    TextView tvGolineCommit;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String PersonSysCode;
    private String areaCodes;

    private List<MachineManageAreaBean> mGolineaAreaList = new ArrayList<>();
    ICommonRequestPresenter iCommonRequestPresenter;
    private MachineGolineRecyclerViewAdapter adapter;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_machine_goline;
    }

    @Override
    protected void initViewsAndEvents() {
        tvMainTitle.setText(getString(R.string.machine_goline));
        tvMainRightLeft.setBackground(getResources().getDrawable(R.drawable.go_online_list));
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode","None");

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_YUNW_MANAGE_AREA);
    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if("0".equals(count)){
            tvRedNum.setVisibility(View.GONE);
        }else{
            tvRedNum.setText(count);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();
        if(eventTag == NetConstant.EventTags.GET_YUNW_MANAGE_AREA){
            map.put("Rounter", NetConstant.GET_YUNW_MANAGE_AREA);
        }else{
            map.put("Rounter", NetConstant.GOLINE_COMMIT_PLAN);
            map.put("areaCodes", areaCodes);
            map.put("mineCode", MineCode);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("personSysCode", PersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数" + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(eventTag == NetConstant.EventTags.GET_YUNW_MANAGE_AREA){
            mGolineaAreaList =
                    (List<MachineManageAreaBean>) AppUtils.parseRowsResult(result, MachineManageAreaBean.class);
            refreshListView();
        }else{
            ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
            if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                AppUtils.goActivity(MachineGolineActivity.this, GolinePlanActivity.class);
                adapter.clearAreaSet();
                adapter.notifyDataSetChanged();
            }else{
                ToastUtils.showToast(returnBean.getMsg());
            }
        }
    }

    private void refreshListView() {
        MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(4, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        rvAreaListView.setLayoutManager(mLayoutManager);
        rvAreaListView.addItemDecoration(lineVertical);
        rvAreaListView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MachineGolineRecyclerViewAdapter(mContext, mGolineaAreaList);
        rvAreaListView.setAdapter(adapter);
//        adapter.setOnItemClickListener(this);
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
    protected void onPause() {
        super.onPause();
    }

    @OnClick({R.id.btn_main_back, R.id.tv_main_right_left, R.id.tv_goline_commit_plan, R.id.rl_notice })
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_main_right_left:
                AppUtils.goActivity(this, GolinePlanActivity.class);
                break;
            case R.id.tv_goline_commit_plan:
                if(adapter != null && adapter.getAreaSet().size() > 0){
                    getAreaCode(adapter.getAreaSet());
                    toRequest(NetConstant.EventTags.GOLINE_COMMIT_PLAN);
                }else{
                    ToastUtils.showToast(getString(R.string.area_no_chose));
                }
                break;
        }
    }

    // 整合区域编号
    private void getAreaCode(Set<Integer> set) {
        int count = 0;
        StringBuilder builder = new StringBuilder();
        for(Integer in : set){
            count ++;
            if(count > 1){
                builder.append(","+ mGolineaAreaList.get(in).getAreaSysCode());
            }else{
                builder.append(mGolineaAreaList.get(in).getAreaSysCode());
            }
        }
        areaCodes = builder.toString();
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
            }
        });
    }
}
