package com.sly.app.activity.sly.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.model.sly.balance.MachineOnlineAreaBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.FlowLayout.FlowLayout;
import com.sly.app.view.FlowLayout.TagAdapter;
import com.sly.app.view.FlowLayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class SlyMachineOnlineActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.ll_allArea)
    LinearLayout llAllArea;
    @BindView(R.id.ll_chooseArea)
    LinearLayout tvChooseArea;
    @BindView(R.id.fl_area_Name)
    TagFlowLayout flAreaName;
    @BindView(R.id.tv_commit_plan)
    TextView tvCommitPlan;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String PersonSysCode;

    private List<MachineOnlineAreaBean> onlineAreaList;
    private Set<Integer> AreaSet;
    private String areaCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_machineonline;
    }

    @Override
    protected void initViewsAndEvents() {
        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode","None");
        tvMainTitle.setText("矿机上线");
        getAreaDatas();
    }

    private void getAreaDatas() {
        Map map = new HashMap();
        map.put("Rounter", "SmartOnline.001");
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("mineCode", MineCode);
        map.put("personSysCode", PersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("获取区域返回码:" + statusCode + "返回参数:" + content);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                Logcat.i("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);
                try {
                    onlineAreaList = OnlineParseResult(backlogJsonStr);
                    init();
                } catch (Exception e) {
                    ToastUtils.showToast(e.toString());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("错误信息：" + request.toString() + "/" + e);
            }
        });
    }

    private void init() {
        final LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
        flAreaName.setAdapter(new TagAdapter(onlineAreaList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView areaName = (TextView) mInflater.inflate(R.layout.flow_online_area_tv,
                        flAreaName, false);
                MachineOnlineAreaBean bean = (MachineOnlineAreaBean)o;
                areaName.setText(bean.getMine06_MineAreaName());
                return areaName;
            }
        });

        flAreaName.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
//                Log.d("tag","choose:" + selectPosSet.toString());
                AreaSet = selectPosSet;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAreaDatas();
    }

    @OnClick({R.id.btn_main_back, R.id.tv_commit_plan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_commit_plan:
                if(AreaSet != null && AreaSet.size() > 0){
                    int count = 0;
                    StringBuilder builder = new StringBuilder();
                    for(Integer chooseItem : AreaSet){
                        count ++;
                        MachineOnlineAreaBean bean = onlineAreaList.get(chooseItem);
                        if(count > 1){
                            builder.append(","+ bean.getMine06_MineAreaCode());
                        }else{
                            builder.append(bean.getMine06_MineAreaCode());
                        }
                    }
                    areaCodes = builder.toString();
                    toRequestCommitPlan(areaCodes);
                }else{
                    ToastUtils.showToast("请选择区域");
                }
                break;
        }
    }

    public List<MachineOnlineAreaBean> OnlineParseResult(String result) {
        JsonHelper<MachineOnlineAreaBean> dataParser = new JsonHelper<MachineOnlineAreaBean>(
                MachineOnlineAreaBean.class);

        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
            String searchResult = jsonObject.getString("Rows");
            return dataParser.getDatas(searchResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void toRequestCommitPlan(String result) {
        Map map = new HashMap();
        map.put("Rounter", "SmartOnline.002");
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Token", Token);
        map.put("mineCode", MineCode);
        map.put("personSysCode", PersonSysCode);
        map.put("areaCodes",areaCodes);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("提交返回码:" + statusCode + "返回参数:" + content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if(mReturnBean.getStatus().equals("1") && mReturnBean.getMsg().equals("成功")){
                    new AlertDialog.Builder(SlyMachineOnlineActivity.this)
                            .setMessage("提交成功!")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.setClass(SlyMachineOnlineActivity.this, SlyOnlineListActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("提交错误信息：" + request.toString() + "/" + e);
            }
        });
    }
}
