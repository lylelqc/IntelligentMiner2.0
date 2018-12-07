package com.sly.app.activity.sly.mine;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.MachineRunRateBean;
import com.sly.app.model.MineAreaInfoBean;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class SlyMachineMonitorActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_online_machine1)
    TextView tvOnlineMachine1;
    @BindView(R.id.tv_online_machine2)
    TextView tvOnlineMachine2;
    @BindView(R.id.tv_online_machine3)
    TextView tvOnlineMachine3;
    @BindView(R.id.tv_monitor_rate1)
    TextView tvMonitorRate1;
    @BindView(R.id.tv_monitor_rate2)
    TextView tvMonitorRate2;
    @BindView(R.id.tv_monitor_rate3)
    TextView tvMonitorRate3;
    @BindView(R.id.ll_mine_area_info)
    LinearLayout llMineAreaInfo;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String PersonSysCode;

    private List<MachineRunRateBean> mMachineRateList = new ArrayList<>();
    private List<MineAreaInfoBean> mAreaInfoList = new ArrayList<>();

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_monitor;
    }

    @Override
    protected void initViewsAndEvents() {
        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode","None");
        tvMainTitle.setText("矿机监控");

        getMonitorDatas(NetWorkCons.EventTags.GET_MONITOR_RATE);
        getMonitorDatas(NetWorkCons.EventTags.GET_MONITOR_AREA_INFO);
    }

    private void getMonitorDatas(final int eventType) {
        Map map = new HashMap();

        if(eventType == NetWorkCons.EventTags.GET_MONITOR_RATE){
            map.put("Rounter", NetWorkCons.GET_MONITOR_RATE);
            map.put("mineCode", MineCode);
        }else{
            map.put("Rounter", NetWorkCons.GET_MONITOR_AREA_INFO);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("personSysCode", PersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("获取报单返回码:" + statusCode + "提交参数" + json +"返回参数:" + content);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                Logcat.i("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);
                if(eventType == NetWorkCons.EventTags.GET_MONITOR_RATE){
                    final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                    if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                        mMachineRateList = JSON.parseArray(returnBean.getData().toString(), MachineRunRateBean.class);
                        for(MachineRunRateBean bean : mMachineRateList){
//                            Logcat.e(bean.getStatusCode()+" code");
                            if(bean.getStatusCode().equals("00")){
                                tvOnlineMachine1.setText(bean.getMachineCount()+"");
                                double rate = bean.getRate() * 100;
                                if(rate == 100){
                                    tvMonitorRate1.setText("100");
                                }else{
                                    tvMonitorRate1.setText(String.format("%.2f", rate));
                                }
                            }else if(bean.getStatusCode().equals("01")){
                                tvOnlineMachine2.setText(bean.getMachineCount()+"");
                                double rate = bean.getRate() * 100;
                                if(rate == 100){
                                    tvMonitorRate2.setText("100");
                                }else{
                                    tvMonitorRate2.setText(String.format("%.2f", rate));
                                }
                            } else if(bean.getStatusCode().equals("02")){ //bean.getStatusCode().equals("02") ||
                                tvOnlineMachine3.setText(bean.getMachineCount()+"");
                                double rate = bean.getRate() * 100;
                                if(rate == 100){
                                    tvMonitorRate3.setText("100");
                                }else{
                                    tvMonitorRate3.setText(String.format("%.2f", rate));
                                }
                            }
                        }
                    } else {
                        ToastUtils.showToast(returnBean.getMsg());
                    }
                }else if(eventType == NetWorkCons.EventTags.GET_MONITOR_AREA_INFO){
                    final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                    if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                        mAreaInfoList = JSON.parseArray(returnBean.getData().toString(), MineAreaInfoBean.class);
                        for(MineAreaInfoBean bean : mAreaInfoList){
                            addViewItem(bean);
                        }
                    } else {
                        ToastUtils.showToast(returnBean.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("错误信息：" + request.toString() + "/" + e);
            }
        });
    }

    private void addViewItem(final MineAreaInfoBean bean) {
        View itemView = View.inflate(this, R.layout.item_mine_info, null);
        TextView areaName = (TextView) itemView.findViewById(R.id.tv_area_name);
        TextView allNum = (TextView) itemView.findViewById(R.id.tv_all_num);
        TextView normalNum = (TextView) itemView.findViewById(R.id.tv_normal_num);
        TextView runRate = (TextView) itemView.findViewById(R.id.tv_run_rate1);
        TextView status = (TextView) itemView.findViewById(R.id.tv_status);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SlyMachineMonitorActivity.this, SlyMachineManagerActivity.class);
                intent.putExtra("areaCode", bean.getCode());
                startActivity(intent);
            }
        });

        areaName.setText(bean.getName());
        allNum.setText(bean.getTotalCount()+"");
        normalNum.setText(bean.getNormalCount()+"");

        double rate = bean.getRunRate();
        if(rate == 100){
            runRate.setText("100%");
            status.setBackgroundColor(Color.parseColor("#90d262"));
        }else if(rate == 0){
            runRate.setText("0%");
            status.setBackgroundColor(Color.parseColor("#ff6e6e"));
        }else{
            runRate.setText(String.format("%.2f", rate)+"%");
        }
        llMineAreaInfo.addView(itemView);
    }

    @OnClick({R.id.btn_main_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
        }
    }
}
