package com.sly.app.fragment.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.RuningtimeRecyclerViewAdapter;
import com.sly.app.base.HeaderViewPagerFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.sly.AllDMinerMasterTableBean;
import com.sly.app.model.sly.AllDMinerTableBean;
import com.sly.app.model.sly.MinerMasterChartBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.styles.bar.ImmersionBar;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 作者 by K
 * 时间：on 2017/8/29 14:36
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class DeviceAFragment extends HeaderViewPagerFragment {
    ListView mLvList;

    @BindView(R.id.ll_top_chart)
    LinearLayout llTopChart;
    @BindView(R.id.tv_miner)
    TextView tvMiner;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_run_count)
    TextView tvRunCount;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.tv_run_present)
    TextView tvRunPresent;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.ll_zong)
    LinearLayout LlZong;
//    @BindView(R.id.tv_run_week_present)
//    TextView tvRunWeekPresent;
//    @BindView(R.id.tv_week_1)
//    TextView tvWeek1;
//    @BindView(R.id.tv_run_month_present)
//    TextView tvRunMonthPresent;
//    @BindView(R.id.tv_month_2)
//    TextView tvMonth2;
//    @BindView(R.id.bar_chart)
////    BarChart03View barChart;
    @BindView(R.id.ll_miner)
    LinearLayout llMiner;
    @BindView(R.id.chart_rv)
    RecyclerView chartRv;
    @BindView(R.id.sc_view)
    NestedScrollView scView;
    @BindView(R.id.scroll_rv)
    ScrollView scroll_rv;
    private String User;
    private String Token;
    private String Key;
    private String LoginType;
    private String SysNumber;
    private String pageSize = "10";
    private int pageNo = 1;
    private RuningtimeRecyclerViewAdapter mRunRvAdapter;
    private List<MinerMasterChartBean> MinerMasterChartList;
    private Unbinder unbinder;
    private boolean isViewCreate = false;
    private boolean isUIVisable = false;





    public static DeviceAFragment newInstance() {
        return new DeviceAFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void initScroll() {
        scView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    Logcat.i("------------------" + "Scroll DOWN");
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Logcat.i("------------------" + "BOTTOM SCROLL");
//                    pageNo += 1;
//                        getMinerMasterChartData();
                }
                if (scrollY <= 0) {
                    Logcat.i("------------------" + "TOP SCROLL");
                } else {
                    ImmersionBar.with(getActivity()).fitsSystemWindows(false).transparentStatusBar().init();
                }
            }
        });
    }


    protected void init(View view) {
        MinerMasterChartList = new ArrayList<>();
        User = SharedPreferencesUtil.getString(getActivity(), "User");
        Token = SharedPreferencesUtil.getString(getActivity(), "Token");
        Key = SharedPreferencesUtil.getString(getActivity(), "Key", "None");
        LoginType = SharedPreferencesUtil.getString(getActivity(), "LoginType", "None");
        SysNumber = SharedPreferencesUtil.getString(getActivity(), LoginType.equals("Miner") ? "FrSysCode" : "FMasterCode");
        initRv();
        initScroll();
        isMiner();
        getAllData();
        getMinerMasterChartData();
    }

    private void initRv() {
        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        lineVertical.setSize(5);
        lineVertical.setColor(0xFFf4f4f4);
        chartRv.setLayoutManager(mLayoutManager);
        chartRv.addItemDecoration(lineVertical);
        mRunRvAdapter = new RuningtimeRecyclerViewAdapter(getActivity(), MinerMasterChartList);
        chartRv.setAdapter(mRunRvAdapter);
    }
    private void getAllData() {
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        if (LoginType.equals("Miner")) {
            map.put("minerSysCode", SysNumber);
            map.put("Rounter", "Miner.011");
        } else {
            map.put("mineMasterCode", SysNumber);
            map.put("Rounter", "MineMaster.015");
        }
//        map.put("pageSize", pageSize);
//        map.put("pageNo", pageNo + "");
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(getActivity()).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                try {
                    LogCatW(NetWorkCons.BASE_URL + this, json, statusCode, content);
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);

                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
                        if (LoginType.equals("Miner")) {
                            List<AllDMinerTableBean> chartList = JSON.parseArray(mReturnBean.getData().getRows().toString(), AllDMinerTableBean.class);
                            Logcat.e(chartList.toString());
                            AllDMinerTableBean allDataTableBean = chartList.get(0);

                        } else {
                            List<AllDMinerMasterTableBean> chartList = JSON.parseArray(mReturnBean.getData().getRows().toString(), AllDMinerMasterTableBean.class);
                            Logcat.e(chartList.toString());
                            AllDMinerMasterTableBean allDataTableBean = chartList.get(0);
                            tvRunCount.setText(allDataTableBean.getRunMachineCount()+"");
                            double runRate = allDataTableBean.getRunRate()*100;
                            tvRunPresent.setText(String.format("%.2f", runRate) + "%");

                        }

                    } else {
                        ToastUtils.showToast(mReturnBean.getMsg());
                    }
                } catch (Exception e) {
//                    ToastUtils.showToast(e.toString());
                }
            }


        });
    }
    private void getMinerMasterChartData() {
        if (pageNo == 1) {
            MinerMasterChartList.removeAll(MinerMasterChartList);
        }
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        if(LoginType.equals("Miner")){
            map.put("minerSysCode", SysNumber);
            map.put("Rounter","Miner.012"  );
        }else {
            map.put("minerMasterCode", SysNumber);
            map.put("Rounter","MineMaster.018" );
        }
//        map.put("pageSize", pageSize);
//        map.put("pageNo", pageNo + "");
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(getActivity()).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                    LogCatW(this+"---"+NetWorkCons.BASE_URL, json, statusCode, content);
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
                        List<MinerMasterChartBean> chartList = JSON.parseArray(mReturnBean.getData().getRows().toString(), MinerMasterChartBean.class);
                        MinerMasterChartList.addAll(chartList);

                        mRunRvAdapter.notifyDataSetChanged();

                    } else {
                        ToastUtils.showToast(mReturnBean.getMsg());
                }
            }
        });
    }

    private void setAllMasterChartView(AllDMinerMasterTableBean minerMasterChartList) {

//        List<Double> data  = new ArrayList<>();
//        List<String> Xdata  = new ArrayList<>();
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        barChart.hideYLable();
//        barChart.setYData(100,0,1,20);
//        barChart.setData(data);
//        barChart.setXData(Xdata);
//        barChart.refreshChart();
    }
    private void setAllChartView(AllDMinerMasterTableBean minerMasterChartList) {
//        List<Double> data  = new ArrayList<>();
//        List<String> Xdata  = new ArrayList<>();
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        data.add(Double.parseDouble("0"));
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        Xdata.add("运行率");
//        barChart.hideYLable();
//        barChart.setYData(100,0,1,20);
//        barChart.setData(data);
//        barChart.setXData(Xdata);
//        barChart.refreshChart();
    }

//    private void getMineChartData() {
//        if (pageNo == 1) mMall.removeAll(mMall);
//        Map map = new HashMap();
//        map.put("Token", token);
//        map.put("LoginType", loginType);
//        map.put("Rounter", "MineMaster.006");
//        map.put("User", user);
//        map.put("provinceCode", provinceCode);
//        map.put("cityCode", cityCode);
//        map.put("countryCode", countryCode);
//        map.put("pageSize", pageSize);
//        map.put("pageNo", pageNo + "");
//        Map<String, String> mapJson = new HashMap<>();
//        mapJson.putAll(map);
//        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mActivity).getSign(map, key)));
//        final String json = CommonUtils.GsonToJson(mapJson);
//        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, String content) {
//                super.onSuccess(statusCode, content);
//                dismissProgressDialog();
//                try {
//                    LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
//                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
//
//                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
//                        List<MachineSeatDataBean> machineSeatDataBeans = JSON.parseArray(mReturnBean.getData().getRows().toString(), MachineSeatDataBean.class);
//
//                        mMall.addAll(machineSeatDataBeans);
//                        mRecyclerViewAdapter.notifyDataSetChanged();
//                    } else {
//                        ToastUtils.showToast(mReturnBean.getMsg());
//                    }
//                    dismissProgressDialog();
//                } catch (Exception e) {
//                    ToastUtils.showToast(e.toString());
//                }
//            }
//        });
//    }

    private void isMiner() {
        if (LoginType.equals("Miner")) {
            tvMiner.setVisibility(View.GONE);
//            chartRv.setVisibility(View.INVISIBLE);
            llMiner.setVisibility(View.INVISIBLE);
            LlZong.setVisibility(View.GONE);
        } else {
            tvMiner.setVisibility(View.VISIBLE);
//            chartRv.setVisibility(View.VISIBLE);
            llMiner.setVisibility(View.VISIBLE);
            LlZong.setVisibility(View.VISIBLE);
        }

    }



    @OnClick({R.id.iv_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_2:
                break;
        }
    }


    @Override
    public View getScrollableView() {
        return scroll_rv;
    }
}
