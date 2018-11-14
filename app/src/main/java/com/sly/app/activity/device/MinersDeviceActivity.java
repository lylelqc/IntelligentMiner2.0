package com.sly.app.activity.device;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.base.HeaderViewPagerFragment;
import com.sly.app.fragment.device.DeviceAFragment;
import com.sly.app.fragment.device.DeviceBFragment;
import com.sly.app.fragment.device.DeviceCFragment;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.sly.AllDMinerMasterTableBean;
import com.sly.app.model.sly.AllDMinerTableBean;
import com.sly.app.model.sly.SlyReturnListBean;
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
import vip.devkit.library.ListUtil;
import vip.devkit.library.Logcat;

import static com.sly.app.utils.AppLog.LogCatW;

/**
 * 文 件 名: MinersDeviceActivity
 * 功能描述:
 * 创 建 人: By k
 * 邮    箱:vip@devkit.vip
 * 网    站:www.devkit.vip
 * 创建日期: 2018/8/27
 * 版   本: V 1.0
 * 代码修改:（修改人 - 修改时间）
 * 修改备注：
 */
public class MinersDeviceActivity extends BaseActivity {


    List<String> mTabList;
    List<HeaderViewPagerFragment> mFragments;
    DeviceAFragment mAFragment;
    DeviceBFragment mBFragment;
    DeviceCFragment mCFragment;
    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout llCommLayout;
    @BindView(R.id.tv_dev_count)
    TextView tvDevCount;
    @BindView(R.id.tv_devnow_count)
    TextView tvDevnowCount;
    @BindView(R.id.tv_incom_or_suanli)
    TextView tvIncomOrSuanli;
    @BindView(R.id.tv_dev_money)
    TextView tvDevMoney;
    @BindView(R.id.tab_view)
    TabLayout tabView;
    @BindView(R.id.vp_view)
    ViewPager vpView;
    @BindView(R.id.scrollableLayout)
    LinearLayout scrollableLayout;

    private String User;
    private String Token;
    private String Key;
    private String LoginType;
    private String SysNumber;
    private String Rounter;
    private AllDMinerTableBean allDMinerTableBean;
    private AllDMinerMasterTableBean allDMinerMasterTableBean;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_minders_device;
    }

    @Override
    protected void initViewsAndEvents() {
        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        SysNumber = SharedPreferencesUtil.getString(this, LoginType.equals("Miner") ? "FrSysCode" : "FMasterCode");
        mFragments = new ArrayList<>();
        mTabList = new ArrayList<>();
        tvMainTitle.setText("统计");
        mTabList.add("运行率");
        mTabList.add("耗电量");
        mTabList.add(LoginType.equals("Miner") ? "24小时平均算力" : "收益额");
        tvIncomOrSuanli.setText("运行率");
        mAFragment = DeviceAFragment.newInstance();
        mBFragment = DeviceBFragment.newInstance();
        mCFragment = DeviceCFragment.newInstance();
        mFragments.add(mAFragment);
        mFragments.add(mBFragment);
        mFragments.add(mCFragment);
//        mTabView.addTab(mTabView.newTab().setText("运行率"));
//        mTabView.addTab(mTabView.newTab().setText("耗电量"));
//        mTabView.addTab(mTabView.newTab().setText("收益额"));
//        vpView.setOffscreenPageLimit(3);
        vpView.setCurrentItem(0);
        HAdapter mAdapter = new HAdapter(getSupportFragmentManager(), mTabList, mFragments);
        vpView.setAdapter(mAdapter);
        tabView.setSelectedTabIndicatorColor(getResources().getColor(R.color.sly_text_blue2));
        tabView.setupWithViewPager(vpView);
        getAllData();
        setListener();
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
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
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
                            allDMinerTableBean = chartList.get(0);
                            setMinerTableData(allDMinerTableBean);
                        } else {
                            List<AllDMinerMasterTableBean> chartList = JSON.parseArray(mReturnBean.getData().getRows().toString(), AllDMinerMasterTableBean.class);
                            Logcat.e(chartList.toString());
                            allDMinerMasterTableBean = chartList.get(0);
                            setMinerMasterTableData(allDMinerMasterTableBean);
                        }
                    } else {
                        ToastUtils.showToast(mReturnBean.getMsg());
                    }
                } catch (Exception e) {
                    ToastUtils.showToast(e.toString());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                ToastUtils.showToast("网络错误 -" + e.getMessage());
            }
        });
    }

    private void setMinerTableData(AllDMinerTableBean allDataTableBean) {
        tvDevCount.setText(allDataTableBean.getMachineCount() + "");
        tvDevnowCount.setText(allDataTableBean.getRunMachineCount() + "");
//        tvDevMoney.setText(allDataTableBean.getRunRate() * 100  + "");
        double runRate = allDataTableBean.getRunRate() * 100;
        tvDevMoney.setText(String.format("%.2f", runRate) + "%");
    }

    private void setMinerMasterTableData(AllDMinerMasterTableBean allDataTableBean) {
        tvDevCount.setText(allDataTableBean.getMachineCount() + "");
        tvDevnowCount.setText(allDataTableBean.getRunMachineCount() + "");
//        tvDevMoney.setText(allDataTableBean.getRunRate() * 100 + "");
        double runRate = allDataTableBean.getRunRate() * 100;
        tvDevMoney.setText(String.format("%.2f", runRate) + "%");
    }

    private void setListener() {
        tabView.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                selectedFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vpView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                selectedFragment(position);
                switch (position) {
                    case 0:
                        tvIncomOrSuanli.setText("运行率");
                        if(LoginType.equals("Miner")){
                            if(allDMinerTableBean != null){
                                tvDevMoney.setText(allDMinerTableBean.getUsepower()+"");
                                double runRate = allDMinerTableBean.getRunRate() * 100;
                                tvDevMoney.setText(String.format("%.2f", runRate) + "%");
                            }else {
                                ToastUtils.showToast("正在加载，请稍后再试!");
                            }
                        }else{
                            if(allDMinerMasterTableBean != null){
                                double runRate = allDMinerMasterTableBean.getRunRate() * 100;
                                tvDevMoney.setText(String.format("%.2f", runRate) + "%");
                            }else {
                                ToastUtils.showToast("正在加载，请稍后再试!");
                            }
                        }
                        break;
                    case 1:
                        tvIncomOrSuanli.setText(LoginType.equals("Miner") ? "当日耗电量（KW/H）" : "当日耗电量（KW/H）");
                        if(LoginType.equals("Miner")){
                            if(allDMinerTableBean != null){
                                tvDevMoney.setText(allDMinerTableBean.getUsepower()+"");
                            }else {
                                ToastUtils.showToast("正在加载，请稍后再试!");
                            }
                        }else{
                            if(allDMinerMasterTableBean != null){
                                tvDevMoney.setText(allDMinerMasterTableBean.getUsepower()+"");
                            }else {
                                ToastUtils.showToast("正在加载，请稍后再试!");
                            }
                        }
                        break;
                    case 2:
                        tvIncomOrSuanli.setText(LoginType.equals("Miner") ? "10分钟实时算力（TH/S）" : "累计收益（¥）");
                        if(LoginType.equals("Miner")){
                            if(allDMinerTableBean != null){
                                tvDevMoney.setText(String.format("%.2f", allDMinerTableBean.getUsecalc()));
                            }else {
                                ToastUtils.showToast("正在加载，请稍后再试!");
                            }
                        }else{
                            if(allDMinerMasterTableBean != null){
                                tvDevMoney.setText(String.format("%.2f", allDMinerMasterTableBean.getIncomeSum()));
                            }else {
                                ToastUtils.showToast("正在加载，请稍后再试!");
                            }
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (mAFragment == null) {
                    mAFragment = new DeviceAFragment();
                    mFragments.add(mAFragment);

                } else {
                    transaction.show(mAFragment);
                }
                break;
            case 1:
                if (mBFragment == null) {
                    mBFragment = new DeviceBFragment();
                    mFragments.add(mBFragment);
//                    transaction.add(R.id.content, mBFragment);
                } else {
                    transaction.show(mBFragment);
                }
                break;
            case 2:
                if (mCFragment == null) {
                    mCFragment = new DeviceCFragment();
                    mFragments.add(mCFragment);
//                    transaction.add(R.id.content, mCFragment);
                } else {
                    transaction.show(mCFragment);
                }
                break;
        }
        transaction.commit();
    }


    private void hideFragment(FragmentTransaction transaction) {
        if (mAFragment != null)
            transaction.hide(mAFragment);
        if (mCFragment != null)
            transaction.hide(mCFragment);
        if (mBFragment != null)
            transaction.hide(mBFragment);
    }

    @OnClick({R.id.btn_main_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
        }
    }


    public class HAdapter extends FragmentPagerAdapter {
        List<String> list;
        List<HeaderViewPagerFragment> fragments;
        FragmentManager fm;

        public HAdapter(FragmentManager fm, List<String> list, List<HeaderViewPagerFragment> fragments) {
            super(fm);
            this.fm = fm;
            this.fragments = fragments;
            this.list = list;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return ListUtil.isEmpty(list) ? 0 : list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            fm.beginTransaction().show(fragment).commitAllowingStateLoss();
            //###return super.instantiateItem(container, position);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //###super.destroyItem(container, position, object);
            Fragment fragment = fragments.get(position);
            fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }


}
