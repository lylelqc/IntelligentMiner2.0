package com.sly.app.fragment.sly;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.activity.sly.home.SlyBannerDetailsActivity;
import com.sly.app.activity.sly.home.SlyCalerActivity;
import com.sly.app.adapter.HomeHotAdapter;
import com.sly.app.adapter.HomeRecyclerViewAdapter;
import com.sly.app.adapter.ScrollAdAdapter;
import com.sly.app.fragment.BaseFragment;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.sly.SlyMsgBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.BannerGlideImageLoader;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.DrawableTextView;
import com.sly.app.view.MyGridView;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;
import vip.devkit.view.common.banner.BannerConfig;
import vip.devkit.view.common.banner.BannerV;
import vip.devkit.view.common.banner.listener.OnBannerListener;
import vip.devkit.view.common.rollinglayout.RollingLayout;

import static vip.devkit.view.common.rollinglayout.RollingLayoutAction.DOWN_UP;

/**
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class SlyHomeFragment extends BaseFragment {
    private static final String STATUS_KEY = "status";
    @BindView(R.id.banner_view)
    BannerV mBannerView;
    @BindView(R.id.tv_menu_1)
    DrawableTextView mTvMenu1;
    @BindView(R.id.tv_menu_2)
    DrawableTextView mTvMenu2;
    @BindView(R.id.tv_menu_3)
    DrawableTextView mTvMenu3;
    @BindView(R.id.tv_menu_4)
    DrawableTextView mTvMenu4;
    @BindView(R.id.tv_menu_5)
    DrawableTextView mTvMenu5;
    @BindView(R.id.tv_menu_6)
    DrawableTextView mTvMenu6;
    @BindView(R.id.tv_menu_7)
    DrawableTextView mTvMenu7;
    @BindView(R.id.tv_menu_8)
    DrawableTextView mTvMenu8;
    @BindView(R.id.scroll_layout_ad_1)
    RollingLayout mScrollLayoutAd1;
    @BindView(R.id.scroll_layout_ad_2)
    RollingLayout mScrollLayoutAd2;
    @BindView(R.id.gv_home_hot)
    MyGridView mGvHomeHot;
    @BindView(R.id.img_hot_1)
    ImageView mImgHot1;
    @BindView(R.id.textView6)
    TextView mTextView6;
    @BindView(R.id.tv_type_go)
    TextView mTvTypeGo;
    @BindView(R.id.rl_more)
    RelativeLayout mRlMore;
    @BindView(R.id.rl_view)
    RecyclerView mRlView;
    @BindView(R.id.sc_view)
    NestedScrollView mScView;
    @BindView(R.id.iv_scan)
    ImageView mIvScan;
    @BindView(R.id.iv_language)
    ImageView mIvLanguage;
    @BindView(R.id.rl_layout)
    RelativeLayout mRlLayout;
    @BindView(R.id.iv_toTop)
    ImageView mIvToTop;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.iv_caler)
    ImageView mIvCaler;
    @BindView(R.id.rl_user_name)
    RelativeLayout rlUserName;
    @BindView(R.id.tv_login_name)
    TextView tvLoginName;
    @BindView(R.id.tv_new_login)
    TextView tvNewLogin;
    @BindView(R.id.rl_just_login)
    RelativeLayout rlJustLogin;
    @BindView(R.id.rl_banner)
    RelativeLayout rlBanner;

    private int pageIndex = 1;
    private List<GoodsBean> mMall = new ArrayList<>();
    private List<GoodsBean> mHotData = new ArrayList<>();
    private List<SlyMsgBean> mAdList = new ArrayList<>();
    private HomeRecyclerViewAdapter mRecyclerViewAdapter;
    private HomeHotAdapter mHotAdapter;
    private ScrollAdAdapter mAdAdapter1, mAdAdapter2;
    private String MemberCode, Token,User,Key,LoginType;
    private View FooterView;
    private int flag = 0;
    private static final int VIEW_FLAG = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case VIEW_FLAG:
                    initAd();
                    break;
            }
            return false;
        }
    });

    public static String mContent = "???";

    public static SlyHomeFragment newInstance(String content) {
        SlyHomeFragment fragment = new SlyHomeFragment();
        mContent = content;
        return fragment;
    }
    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {
        getMessage();
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViewsAndEvents() {
        tvNewLogin.setText("立即\n登录");
        FooterView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_list_footer, null, false);
        final MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(2, MyStaggeredGridLayoutManager.VERTICAL);
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        MemberCode = SharedPreferencesUtil.getString(mContext, LoginType.equals("Miner") ? "FrSysCode": "FMasterCode");
        User = SharedPreferencesUtil.getString(mContext, "User", "None");
        Token = SharedPreferencesUtil.getString(mContext, "Token", "None");
        Key = SharedPreferencesUtil.getString(mContext, "Key", "None");
//        Logcat.e("姓名1 - " + User + " - " + Token);

        if("None".equals(User) || "None".equals(Token)){
            rlUserName.setVisibility(View.GONE);
            rlJustLogin.setVisibility(View.VISIBLE);
            rlBanner.setVisibility(View.GONE);
        }else{
            tvLoginName.setText(User);
            rlUserName.setVisibility(View.VISIBLE);
            rlJustLogin.setVisibility(View.GONE);
            rlBanner.setVisibility(View.VISIBLE);
        }

        initBanner();
        getMessage();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_home1;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    private void initAd() {
        mAdAdapter1 = new ScrollAdAdapter(mContext, mAdList, R.layout.item_home_scroll_ad, 1);
        mScrollLayoutAd1.setAdapter(mAdAdapter1);
        mScrollLayoutAd1.setRollingOrientation(DOWN_UP);
        mScrollLayoutAd1.setRollingPauseTime(4000);
        mScrollLayoutAd1.startRolling();
    }

    // 初始化 banner
    private void initBanner() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.banner_shouye_icon);
        list.add(R.drawable.banner02_shouye_image);
        list.add(R.drawable.banner03_shouye_image);
        setBannerView(list);
    }

    private void setBannerView(List<Integer> data) {
        //设置banner样式
        mBannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBannerView.setImageLoader(new BannerGlideImageLoader());
        //设置图片集合
        mBannerView.setImages(data);
        //设置banner动画效果
        //设置自动轮播，默认为true
        mBannerView.isAutoPlay(true);
        //设置轮播时间
        mBannerView.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBannerView.setIndicatorGravity(BannerConfig.CENTER);
        //设置点击事件
        mBannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent();
                if(position == 0){
                    intent.setClass(mContext, SlyBannerDetailsActivity.class);
                    intent.putExtra("bannerType", "MineMaster");
                }else if(position == 1){
                    intent.setClass(mContext, SlyBannerDetailsActivity.class);
                    intent.putExtra("bannerType", "Equity");
                }else{
                    intent.setClass(mContext, SlyBannerDetailsActivity.class);
                    intent.putExtra("bannerType", "MachineSeat");
                }
                startActivity(intent);
            }
        });
        //banner设置方法全部调用完毕时最后调用
        mBannerView.start();
    }

    //获取播报信息
    private void getMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("Rounter", NetWorkCons.MSG_GET_ALL);

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        String beginDate = year + "-" + month + "-" + day;
        int endDay = day -7;
        String endDate = year + "-" + month + "-" + endDay;

        map.put("begin", "None");
        map.put("end", "None");
        map.put("pageNo", 1 + "");
        map.put("pageSize", "3");//获取最近三条播报信息
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);

        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                NetLogCat.I(NetWorkCons.MSG_GET_ALL, json, statusCode, content);
                JsonHelper<SlyMsgBean> dataParser = new JsonHelper<>(SlyMsgBean.class);
                try {
                    JSONObject jsonObject = new JSONObject(CommonUtils.proStr(content)).getJSONObject("data");
                    String searchResult = jsonObject.getString("Rows");
//            pageSize = jsonObject.optInt("page_size",0);
                    List<SlyMsgBean> datas = dataParser.getDatas(searchResult);
                    mAdList.addAll(datas);
                    Message msg = new Message();
                    msg.what = VIEW_FLAG;
                    mHandler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
//                if (mReturnBean.getStatus().equals("1")) {
//                    List<MsgBean> msgBeans = JSON.parseArray(mReturnBean.getData(), MsgBean.class);
//
//                } else {
//                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络错误：" + e);
            }
        });
    }


    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }


    @OnClick({R.id.iv_toTop,R.id.iv_caler,R.id.iv_hosting,R.id.tv_new_login})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_toTop:
                mScView.smoothScrollTo(0, 0);
                break;
            case R.id.iv_caler:
                intent.setClass(getActivity(), SlyCalerActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_hosting:

                Bundle bundle = new Bundle();
                bundle.putString(SlyHomeFragment.STATUS_KEY, MainActivity.HOSTING);
                CommonUtil2.goActivity(mContext, MainActivity.class, bundle);
                break;
            case R.id.tv_new_login:
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

}
