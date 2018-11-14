package com.sly.app.activity.sly.mine;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.sly.MDetailsFooterBean;
import com.sly.app.model.sly.MDetailsHeaderBean;
import com.sly.app.model.sly.MDetailsPicDataBean;
import com.sly.app.model.sly.ReturnBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.ChartUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.chartView.LineChartMarkView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class MineMachineDetailActivity extends BaseActivity {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.ll_pools1)
    LinearLayout llPools1;
    @BindView(R.id.ll_pools2)
    LinearLayout llPools2;
    @BindView(R.id.ll_pools3)
    LinearLayout llPools3;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_machine_type)
    TextView tvMachineType;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_run_ip)
    TextView tvIP;
    @BindView(R.id.tv_belong_area)
    TextView tvBelongArea;
    @BindView(R.id.tv_run_rate)
    TextView tvRunRate;
    @BindView(R.id.tv_money_type)
    TextView tvMoneyType;
    @BindView(R.id.lc_sly_bodong)
    LineChart LcSlyBoDong;
    @BindView(R.id.tv_miner_pool1)
    TextView tvMinerPool1;
    @BindView(R.id.tv_miner1)
    TextView tvMiner1;
    @BindView(R.id.tv_password1)
    TextView tvPassword1;
    @BindView(R.id.tv_miner_pool2)
    TextView tvMinerPool2;
    @BindView(R.id.tv_miner2)
    TextView tvMiner2;
    @BindView(R.id.tv_password2)
    TextView tvPassword2;
    @BindView(R.id.tv_miner_pool3)
    TextView tvMinerPool3;
    @BindView(R.id.tv_miner3)
    TextView tvMiner3;
    @BindView(R.id.tv_password3)
    TextView tvPassword3;
    @BindView(R.id.tv_begintime)
    TextView tvBeginTime;
    @BindView(R.id.tv_endtime)
    TextView tvEndTime;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_time3)
    TextView tvTime3;
    @BindView(R.id.tv_time4)
    TextView tvTime4;

    private String User, Token, Key, LoginType;
    private String machineSysCode;
    private String mineSysCode;
    private String[] xValues;

    private List<MDetailsHeaderBean> mHeaderList = new ArrayList<>();
    private List<MDetailsFooterBean> mFooterList = new ArrayList<>();
    private List<MDetailsPicDataBean> mPicDataList = new ArrayList<>();

    private String isMaster = "";

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_mine_machine_detail;
    }

    @Override
    protected void initViewsAndEvents() {

        User = SharedPreferencesUtil.getString(this, "User", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        mineSysCode = SharedPreferencesUtil.getString(this, "MineCode", "None");

        machineSysCode = getIntent().getStringExtra("machineSysCode");
        String mineCode1 = getIntent().getStringExtra("mineCode");
        isMaster = getIntent().getStringExtra("isMaster");
        if(mineCode1 != null){
            mineSysCode = mineCode1;
        }

        tvMainTitle.setText("矿机详情");
        toRequest(NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_HEAR);
        toRequest(NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_FOOT);
        toRequest(NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_PIC);
    }

    private void initBodongPic(LineChart lineChart) {
        //显示边界
        /***图表设置***/
        //右下角标题设无
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //设置背景色
        lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        //设置不能缩放
        lineChart.setScaleEnabled(false);
//        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
//        lineChart.setPinchZoom(true);

//        float ratio = (float) mPicDataList.size()/(float) 10;
//        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
//        lineChart.zoom(ratio,1f,0,0);

        /***XY轴的设置***/
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYaxis = lineChart.getAxisRight();

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);

        //禁止网格线
        xAxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        //设置网格虚线
//        leftYAxis.enableGridDashedLine(10f, 0f, 0f);
        rightYaxis.setDrawGridLines(false);
        rightYaxis.setEnabled(false);

        //自定义X轴的值
        // 设置X轴分割数量
        xAxis.setLabelCount(4,false);
        formatXData();
        setDataToView();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });

        //自定义y轴的值
        leftYAxis.setLabelCount(6, true);
        leftYAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Math.round((value / 1000)) + "T±5% ";
            }
        });

        /***折线图例 标签 设置***/
        Legend legend = lineChart.getLegend();
        //设置图例
//        legend.setEnabled(false);
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

        //点击显示详情框
        LineChartMarkView mv = new LineChartMarkView(this, xAxis.getValueFormatter(), mPicDataList);
        mv.setChartView(LcSlyBoDong);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }

    public void toRequest(final int eventType) {
        final Map map = new HashMap();

        if(eventType == NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_HEAR){
            map.put("Rounter", NetWorkCons.GET_MANAGE_MACHINE_DETAILS_HEAR);
            map.put("sys", machineSysCode);
        }else if(eventType == NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_FOOT){
            map.put("Rounter", NetWorkCons.GET_MANAGE_MACHINE_DETAILS_FOOT);
            map.put("sys", machineSysCode);
        }else if(eventType == NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_PIC){
            map.put("Rounter", NetWorkCons.GET_MANAGE_MACHINE_DETAILS_PIC);
//            machineSysCode = "ea9c8ef4-e21b-48f4-8ea1-77c8cb6c63e7";
            map.put("mineMachineSysCode", machineSysCode);
            map.put("mineSysCode",mineSysCode);
        }

        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Token", Token);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("返回码 "+ statusCode + " 提交参数:" + json + " 返回参数: " + content);
                if(eventType == NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_HEAR){
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if(mReturnBean.getStatus().equals("1") ){
                        if(mReturnBean.getData().getRows().size() > 0){
                            mHeaderList = JSON.parseArray(mReturnBean.getData().getRows().toString(), MDetailsHeaderBean.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvMachineType.setText(mHeaderList.get(0).getMine08_Model());
                                    tvStatus.setText(mHeaderList.get(0).getMine07_MineMachineStatusName());
                                    tvIP.setText(mHeaderList.get(0).getMine08_IP());
                                    tvBelongArea.setText(mHeaderList.get(0).getMine06_MineAreaName());
                                    double rate = mHeaderList.get(0).getRunRate() * 100;
                                    tvRunRate.setText(String.format("%.2f", rate) + "%");
                                    tvMoneyType.setText(mHeaderList.get(0).getMine08_CoinType());
                                }
                            });
                        }
                    }
                }else if(eventType == NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_FOOT){
                    if(isMaster == null || !isMaster.equals("true")){
                        ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                        if(mReturnBean.getStatus().equals("1")){
                            mFooterList = JSON.parseArray(mReturnBean.getData().toString(), MDetailsFooterBean.class);
                                tvMinerPool1.setText(mFooterList.get(0).getF42_URL());
                                tvMiner1.setText(mFooterList.get(0).getF42_Worker());
                                tvPassword1.setText(mFooterList.get(0).getF42_Password());
                                tvMinerPool2.setText(mFooterList.get(1).getF42_URL());
                                tvMiner2.setText(mFooterList.get(1).getF42_Worker());
                                tvPassword2.setText(mFooterList.get(1).getF42_Password());
                                tvMinerPool3.setText(mFooterList.get(2).getF42_URL());
                                tvMiner3.setText(mFooterList.get(2).getF42_Worker());
                                tvPassword3.setText(mFooterList.get(2).getF42_Password());
                        }
                    }else{
                        llPools1.setVisibility(View.GONE);
                        llPools2.setVisibility(View.GONE);
                        llPools3.setVisibility(View.GONE);
                    }
                }else if(eventType == NetWorkCons.EventTags.GET_MANAGE_MACHINE_DETAILS_PIC){
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if(mReturnBean.getStatus().equals("1") ){
                        if(mReturnBean.getData().getRows().size() > 0){
                            mPicDataList = JSON.parseArray(mReturnBean.getData().getRows().toString(), MDetailsPicDataBean.class);
                            Collections.reverse(mPicDataList);
                            initBodongPic(LcSlyBoDong);
                            ChartUtil.showLineChart(mPicDataList, LcSlyBoDong, Color.parseColor("#D3F2F2"));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("提交错误信息：" + request.toString() + "/" + e);
            }
        });
    }


    @OnClick({R.id.btn_main_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
        }
    }

    //格式化日期
    private void formatXData(){
        if(mPicDataList != null && mPicDataList.size()>0){

            int chu = mPicDataList.size() / 4;
            int yu = mPicDataList.size() % 4;
            if(chu == 0){
                //初始化String[]
                xValues = new String[mPicDataList.size()];
                for(int i = 0; i <= mPicDataList.size()-1; i++){
                    String beginTime = mPicDataList.get(i).getMine63_DataTime();
                    String bthour = beginTime.split(" ")[1];
                    String btmin = bthour.substring(bthour.length() > 7 ? 3 : 2, bthour.length()-3);
                    if(Integer.parseInt(btmin) >= 30){
                        btmin = "30";
                    }else{
                        btmin = "00";
                    }
                    xValues[i] = /*btyear.substring(5,btyear.length())+ "\n"+*/
                            bthour.substring(0, bthour.length()-5) + btmin;
                }
            }else if(chu > 0){
                //取5次，隔chu的值取一次
                xValues = new String [5];
                for(int j = 0; j < 5; j++){
                    String beginTime;
                    if(j == 0){
                        beginTime = mPicDataList.get(0).getMine63_DataTime();
                    }else if(j == 4){
                        beginTime = mPicDataList.get(mPicDataList.size()-1).getMine63_DataTime();
                    }else{
                        beginTime = mPicDataList.get((j*chu)+ yu-1).getMine63_DataTime();
                    }
                    String bthour = beginTime.split(" ")[1];
                    String btmin = bthour.substring(bthour.length() > 7 ? 3 : 2, bthour.length()-3);
                    if(Integer.parseInt(btmin) >= 30){
                        btmin = "30";
                    }else{
                        btmin = "00";
                    }
                    xValues[j] = /*btyear.substring(5,btyear.length())+ "\n"+*/
                            bthour.substring(0, bthour.length()-5) + btmin;
                }
            }
            /*for(int i = 0; i <= mPicDataList.size()-1; i++){
                if(i == 0 ){
                    String beginTime = mPicDataList.get(i).getMine63_DataTime();
                    String btyear = beginTime.split(" ")[0];
                    String bthour = beginTime.split(" ")[1];
                    xValues[0] = btyear.substring(5,btyear.length())+ "\n"
                            + bthour.substring(0,bthour.length()-3);
                }
                if(i == (mPicDataList.size()-1)){
                    String endTime = mPicDataList.get(i).getMine63_DataTime();
                    String etyear = endTime.split(" ")[0];
                    String ethour = endTime.split(" ")[1];
                    xValues[1] = etyear.substring(5,etyear.length())+ "\n"
                            + ethour.substring(0,ethour.length()-3);
                    Logcat.e("波动图日期: = "+ endTime);
                }
            }*/
        }
    }

    //设置X轴数据
    private void setDataToView() {
        if(xValues.length == 5){
            tvBeginTime.setText(xValues[0]);
            tvTime2.setText(xValues[1]);
            tvTime3.setText(xValues[2]);
            tvTime4.setText(xValues[3]);
            tvEndTime.setText(xValues[4]);
        }else if(xValues.length == 4){
            tvBeginTime.setText(xValues[0]);
            tvTime2.setText(xValues[1]);
            tvTime3.setText(xValues[2]);
            tvTime4.setText(xValues[3]);
            tvEndTime.setText("");
        }else if(xValues.length == 3){
            tvBeginTime.setText(xValues[0]);
            tvTime2.setText(xValues[1]);
            tvTime3.setText(xValues[2]);
            tvTime4.setText("");
            tvEndTime.setText("");
        } else if(xValues.length == 2){
            tvBeginTime.setText(xValues[0]);
            tvTime2.setText(xValues[1]);
            tvTime3.setText("");
            tvTime4.setText("");
            tvEndTime.setText("");
        }else {
            tvBeginTime.setText(xValues[0]);
            tvTime2.setText("");
            tvTime3.setText("");
            tvTime4.setText("");
            tvEndTime.setText("");
        }
    }
}
