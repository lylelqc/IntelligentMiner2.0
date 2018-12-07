package com.sly.app.fragment.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.adapter.InComeRecyclerViewAdapter;
import com.sly.app.adapter.SuanLiRecyclerViewAdapter;
import com.sly.app.base.HeaderViewPagerFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.sly.InComeChartBean;
import com.sly.app.model.sly.SuanLiChartBean;
import com.sly.app.model.sly.balance.SlyReturnListBean;
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
import vip.devkit.library.ListUtil;
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
public class DeviceCFragment extends HeaderViewPagerFragment {
    ListView mLvList;
    Unbinder unbinder;
    @BindView(R.id.chart_rv)
    RecyclerView chartRv;
    @BindView(R.id.sc_view)
    NestedScrollView scrollView;
    @BindView(R.id.scroll_view)
    ScrollView scroll_view;
    private List<SuanLiChartBean> suanLiChartBeans;
    private List<InComeChartBean> inComeChartBeans;
    private String User;
    private String Token;
    private String Key;
    private String LoginType;
    private String SysNumber;
    private String pageSize = "10";
    private int pageNo = 1;
    private SuanLiRecyclerViewAdapter mSuanLiAdapter;
    private InComeRecyclerViewAdapter mInComeAdapter;
    private String Rounter = "";
    private boolean isViewCreate = false;
    private boolean isUIVisable = false;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
////        isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
//        if (isVisibleToUser) {
//            isUIVisable = true;
//            lazyLoad();
//        } else {
//            isUIVisable = false;
//        }
//    }

    private void lazyLoad() {
        if (isUIVisable && isViewCreate) {
            if (LoginType.equals("Miner")) {
                getSuanLiChartData();//这个卡
            } else {
                getInComeChartData();
            }
            //数据加载完毕,恢复标记,防止重复加载
            isUIVisable = false;
            isViewCreate = false;
        }

    }

    public static DeviceCFragment newInstance() {
        return new DeviceCFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_c, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (LoginType.equals("Miner")) {
            getSuanLiChartData();
        } else {
            getInComeChartData();
        }
        isViewCreate = true;
    }

    private void initScroll() {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    Logcat.i("------------------" + "Scroll DOWN");
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Logcat.i("------------------" + "BOTTOM SCROLL");
//                    pageNo += 1;
//                    if(LoginType.equals("Miner")){
//                    getSuanLiChartData();
//                    }else {
//                        getInComeChartData();
//                    }
                }
                if (scrollY <= 0) {
                    Logcat.i("------------------" + "TOP SCROLL");
                } else {
                    ImmersionBar.with(getActivity()).fitsSystemWindows(false).transparentStatusBar().init();
                }
            }
        });
    }

    private void getSuanLiChartData() {
        if (pageNo == 1&& !ListUtil.isEmpty( suanLiChartBeans)) {
            suanLiChartBeans.removeAll(suanLiChartBeans);
        }
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", Rounter);
        map.put("User", User);
        map.put("minerSysCode", SysNumber);
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
                    LogCatW(this+"---"+ NetWorkCons.BASE_URL, json, statusCode, content);
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
                        List<SuanLiChartBean> chartList = JSON.parseArray(mReturnBean.getData().getRows().toString(), SuanLiChartBean.class);
                        suanLiChartBeans.addAll(chartList);
                        mSuanLiAdapter = new SuanLiRecyclerViewAdapter(getActivity(),suanLiChartBeans);
                        chartRv.setAdapter(mSuanLiAdapter);
                        mSuanLiAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showToast(mReturnBean.getMsg());
                    }

                } catch (Exception e) {
                    ToastUtils.showToast(e.toString());
                }
            }
        });
    }
    private void getInComeChartData() {
        if (pageNo == 1) {
            inComeChartBeans.removeAll(inComeChartBeans);
        }
        Map map = new HashMap();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", Rounter);
        map.put("User", User);
        map.put("minerMasterCode", SysNumber);
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
                    LogCatW(this+"---"+ NetWorkCons.BASE_URL, json, statusCode, content);
                    SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                    if (mReturnBean.getStatus().equals("1") && mReturnBean.getData().getRows().size() > 0) {
                        List<InComeChartBean> chartList = JSON.parseArray(mReturnBean.getData().getRows().toString(), InComeChartBean.class);
                        inComeChartBeans.addAll(chartList);
                        Logcat.i("inComeChartBeans："+inComeChartBeans.size()+"/"+chartList.size());
                        mInComeAdapter = new InComeRecyclerViewAdapter(getActivity(),inComeChartBeans);
                        chartRv.setAdapter(mInComeAdapter);
                        if ( mInComeAdapter!=null){
                            mInComeAdapter.notifyDataSetChanged();
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

    private void initRv() {
        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(1, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        lineVertical.setSize(5);
        lineVertical.setColor(0xFFf4f4f4);
        chartRv.setLayoutManager(mLayoutManager);
        chartRv.addItemDecoration(lineVertical);
    }

    protected void initView(View view) {
        suanLiChartBeans = new ArrayList<>();
        inComeChartBeans = new ArrayList<>();
        User = SharedPreferencesUtil.getString(getActivity(), "User");
        Token = SharedPreferencesUtil.getString(getActivity(), "Token");
        Key = SharedPreferencesUtil.getString(getActivity(), "Key", "None");
        LoginType = SharedPreferencesUtil.getString(getActivity(), "LoginType", "None");
        SysNumber = SharedPreferencesUtil.getString(getActivity(), LoginType.equals("Miner") ? "FrSysCode" : "FMasterCode");

        initRv();
        initScroll();
        isMiner(LoginType);
    }

    private void isMiner(String LoginType) {
        if (LoginType.equals("Miner")) {
            Rounter = "Miner.013";
        } else {
            Rounter = "MineMaster.016";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.sc_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public View getScrollableView() {
        return scroll_view;
    }
}
