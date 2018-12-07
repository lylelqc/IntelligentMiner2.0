package com.sly.app.activity.sly.mine;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.sly.balance.MachineOnlineAreaBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.FlowLayout.FlowLayout;
import com.sly.app.view.FlowLayout.TagAdapter;
import com.sly.app.view.FlowLayout.TagFlowLayout;
import com.sly.app.view.iviews.ICommonViewUi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class CheckAreaActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.fl_check_area_Name)
    TagFlowLayout mCheckAreaName;

    private String User,LoginType, MineCode, PersonSysCode, Token, Key, PlanID;
    ICommonRequestPresenter iCommonRequestPresenter;
    private List<MachineOnlineAreaBean> areaBeanList = new ArrayList<>();

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_check_area;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        PlanID = getIntent().getStringExtra("PlanID");
        Logcat.e(PlanID);
        tvMainTitle.setText("查看区域");
        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode", "None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode", "None");
        toRequest(NetWorkCons.EventTags.GET_PLAN_AREA_LIST);
    }

    @Override
    public void toRequest(int eventTag) {
        if (NetWorkCons.EventTags.GET_PLAN_AREA_LIST == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_PLAN_AREA_LIST);
            map.put("User", User);
            map.put("mineCode", MineCode);
            map.put("personSysCode", PersonSysCode);
            map.put("planID", PlanID);

            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_PLAN_AREA_LIST, mContext, NetWorkCons.BASE_URL, mapJson);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(NetWorkCons.EventTags.GET_PLAN_AREA_LIST == eventTag){
            String backlogJsonStr = result;
            backlogJsonStr = backlogJsonStr.replace("\\", "");
            backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
            Logcat.i("地址4:" + NetWorkCons.BASE_URL +  "返回参数:" + backlogJsonStr);
            try {
                JsonHelper<MachineOnlineAreaBean> dataParser = new JsonHelper<MachineOnlineAreaBean>(
                        MachineOnlineAreaBean.class);

                try {
                    JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                    String searchResult = jsonObject.getString("Rows");
                    areaBeanList = dataParser.getDatas(searchResult);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                init();
            } catch (Exception e) {
                ToastUtils.showToast(e.toString());
            }
        }
    }

    private void init() {

        final LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
        mCheckAreaName.setAdapter(new TagAdapter(areaBeanList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView areaName = (TextView) mInflater.inflate(R.layout.flow_check_area_tv,
                        mCheckAreaName, false);
                MachineOnlineAreaBean bean = (MachineOnlineAreaBean)o;
                areaName.setText(bean.getMine06_MineAreaName());
                return areaName;
            }
        });

        mCheckAreaName.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String SysAreaCode = areaBeanList.get(position).getMine06_MineSysAreaCode();
                Intent intent = new Intent(CheckAreaActivity.this, MachineListActivity.class);
                intent.putExtra("eventType", NetWorkCons.EventTags.GET_CHECK_MACHINE_LIST+"");
                intent.putExtra("PlanID", PlanID);
                intent.putExtra("areaSysCode", SysAreaCode);
                startActivity(intent);
                return true;
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

    @OnClick({R.id.btn_main_back})
    public void onViewClicked(View view){
        switch(view.getId()){
            case R.id.btn_main_back:
                finish();
                break;
        }
    }
}
