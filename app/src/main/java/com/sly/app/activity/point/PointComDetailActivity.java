package com.sly.app.activity.point;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.mine.CustomerServiceActivity;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.http.type3.HtmlService;
import com.sly.app.model.AttrsBean;
import com.sly.app.model.GoodsBean;
import com.sly.app.model.GoodsInfo;
import com.sly.app.model.ReturnBean;
import com.sly.app.styles.bar.ImmersionBar;
import com.sly.app.utils.BannerGlideImageLoader;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ThreadUtils;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;
import vip.devkit.library.ViewUtils;
import vip.devkit.view.common.banner.BannerConfig;
import vip.devkit.view.common.banner.BannerV;
import vip.devkit.view.common.banner.listener.OnBannerListener;
import vip.devkit.view.common.suklib.SukDialog;
import vip.devkit.view.common.suklib.model.SuKResultBean;
import vip.devkit.view.common.suklib.model.SukPriceBean;

/**
 * 作者 by K
 * 时间：on 2017/9/12 18:25
 * 邮箱 by  vip@devkit.vip
 * 类用途：
 * 最后修改：
 */
public class PointComDetailActivity extends BaseActivity {
    @BindView(R.id.bv_m_banner)
    BannerV mBvMBanner;
    @BindView(R.id.tv_m_m_com_name)
    TextView mTvMMComName;
    @BindView(R.id.tv_m_m_c_price_a)
    TextView mTvMMCPriceA;
    @BindView(R.id.tv_m_m_c_price_b)
    TextView mTvMMCPriceB;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tv_m_m_com_code)
    TextView mTvMMComCode;
    @BindView(R.id.textView2)
    TextView mTextView2;
    @BindView(R.id.tv_m_m_com_Postage)
    TextView mTvMMComPostage;
    @BindView(R.id.tv_detail_flg)
    TextView mTvDetailFlg;
    @BindView(R.id.tv_detail_flg_msg)
    TextView mTvDetailFlgMsg;
    @BindView(R.id.detail_tv_guige)
    TextView mDetailTvGuige;
    @BindView(R.id.rl_norms)
    RelativeLayout mRlNorms;
    @BindView(R.id.ll_layout)
    LinearLayout mLlLayout;
    @BindView(R.id.dtv_group)
    TextView mDtvGroup;
    @BindView(R.id.wv_img)
    WebView mWvImg;
    @BindView(R.id.nsv_view)
    NestedScrollView mNsvView;
    @BindView(R.id.btn_title_back)
    LinearLayout mBtnTitleBack;
    @BindView(R.id.btn_title_text)
    TextView mBtnTitleText;
    @BindView(R.id.iv_m_mall_cart)
    ImageView mIvMMallCart;
    @BindView(R.id.ll_layout_bar)
    LinearLayout mLlLayoutBar;
    @BindView(R.id.tv_service)
    TextView mTvService;
    @BindView(R.id.tv_com_collection)
    TextView mTvComCollection;
    @BindView(R.id.tv_buy_submit)
    TextView mTvBuySubmit;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;
    @BindView(R.id.ll_layout_price)
    LinearLayout mLayoutPrice;
    private GoodsBean mGoodsBean;
    private String MemberCode, Token;
    private SukDialog mSukDialog;
    private int flag = 0;
    String htmls = null;

    private List<SukPriceBean> mPriceBean = new ArrayList<>();
    private List<String> mBannerUrl = new ArrayList<>();
    private List<String> mAttrA = new ArrayList<>();
    private List<String> mAttrB = new ArrayList<>();
    private List<String> mAttrC = new ArrayList<>();
    private static final int HTML_FLAG = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HTML_FLAG:
                    if (!CommonUtils.isBlank(htmls)) {
                        mWvImg.loadDataWithBaseURL(null, htmls.replaceAll("<img","<img style='max-width:100%;height:auto;'"), "text/html", "utf-8", null);
                    }
                    break;
            }
            return false;
        }
    });
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlLayoutBar)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initData() {
        Bundle mBundle = getIntent().getExtras();
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        mGoodsBean = mBundle.getParcelable("ComBean");
//        mBannerUrl = mGoodsBean.getImgList();
        GetComIDInfo(mGoodsBean.getComID() + "");//商品信息
        //   GetComAttrS(mGoodsBean.getComID() + "");//商品商品选项
        GetComAttr(mGoodsBean.getComID() + "");//商品选项属性
        initBannerView( mGoodsBean.getImgList());//Banner
    }
    private void initBannerView(List<String> bannerUrl) {
        for (int i = 0; i < bannerUrl.size(); i++) {
            bannerUrl.set(i,bannerUrl.get(i).replace("40-40", "800-800"));
        }
      //  Collections.reverse(bannerUrl);   //  实现list集合逆序排列
        setBannerView(bannerUrl);
    }


    @Override
    protected void initView() {
        mBtnTitleText.setText("");
        mSukDialog = new SukDialog();
        mTvMMCPriceA.setText(CommonUtils.getDoubleStr(Double.valueOf(mGoodsBean.getMartPrice())));
        mTvMMCPriceB.setText(CommonUtils.getDoubleStr(Double.valueOf(mGoodsBean.getMartPrice())));
        mTvMMComCode.setText(mGoodsBean.getComCode() + "");
        mTvMMComName.setText(mGoodsBean.getComTitle());
        mTvMMComPostage.setText(mGoodsBean.getPostage());
        mLayoutPrice.setVisibility(View.GONE);
        ThreadUtils.runOnChildThread(new Runnable() {
            @Override
            public void run() {
                try {
                    htmls = HtmlService.getHtml(NetWorkConstant.COM_DETAIL_VIEW + mGoodsBean.getComID());
                    if (!CommonUtils.isBlank(htmls)){
                        Message msg = new Message();
                        msg.what = HTML_FLAG;
                        mHandler.sendMessage(msg);
                    }
                    Logcat.i("获取的网络数据：" + htmls);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (!CommonUtils.isBlank(htmls)) {
            mWvImg.loadDataWithBaseURL(null, htmls.replaceAll("<img","<img style='max-width:100%;height:auto;'"), "text/html", "utf-8", null);
        } else {
            mWvImg.loadUrl(NetWorkConstant.COM_DETAIL_VIEW + mGoodsBean.getComID());
        }
        WebSettings webSettings = mWvImg.getSettings();
        webSettings.setDefaultFontSize(10);        // 默认文字尺寸，默认值16，取值范围1-72
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWvImg.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mNsvView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // TODO Auto-generated method stub
                if (scrollY <= 0) {
                    mLlLayoutBar.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
                    mBtnTitleText.setText("");
                } else if (scrollY > 0 && scrollY <= ViewUtils.getViewHeight(mBvMBanner)) {
                    float scale = (float) scrollY / ViewUtils.getViewHeight(mBvMBanner);
                    float alpha = (255 * scale);
                    mLlLayoutBar.setBackgroundColor(Color.argb((int) alpha, 23, 170, 221));
                    mBtnTitleText.setText("");
                } else {
                    mLlLayoutBar.setBackgroundColor(Color.argb((int) 255, 23, 170, 221));
                    mLlLayoutBar.setBackground(getResources().getDrawable(R.drawable.home_nav_img_bg));
                    ImmersionBar.with(PointComDetailActivity.this).fitsSystemWindows(false).transparentStatusBar().init();
                    mBtnTitleText.setText("商品详情");
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_m_p_detail;
    }

    @Override
    protected void setListener() {
        mBvMBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Logcat.i("第:" + position + " 个Banner ");
            }
        });
    }


    @OnClick({R.id.btn_title_back, R.id.iv_m_mall_cart, R.id.tv_service, R.id.tv_com_collection, R.id.tv_buy_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.iv_m_mall_cart:
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    startActivityWithoutExtras(MPSCActivity.class);
                }
                break;
            case R.id.tv_service:
                startActivityWithoutExtras(CustomerServiceActivity.class);
                break;
            case R.id.tv_com_collection:
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    if (flag == 0) {
                        ProCollection(mGoodsBean.getComID() + "", MemberCode, Token, "add",mGoodsBean.getMallType());
                        Drawable top = getResources().getDrawable(R.drawable.mall_goodsdetails_collection_s);
                        mTvComCollection.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);//left ,top,right ,bottom
                    } else if (flag == 1) {
                        ProCollection(mGoodsBean.getComID() + "", MemberCode, Token, "del",mGoodsBean.getMallType());
                        Drawable top = getResources().getDrawable(R.drawable.mall_goodsdetails_collection);
                        mTvComCollection.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    }
                    flag = (flag + 1) % 2;
                }
                break;
           /* case R.id.tv_cart_submit:
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    int DefaultStock;
                    DefaultStock = 50;
                    mSukDialog.initAmountViewHint("该商品最多可购买：");
                    mSukDialog.initBooData(true, true);
                    mSukDialog.initViewData(mGoodsBean.getComTitle(), mGoodsBean.getImgList().get(0), mGoodsBean.getPoint(), DefaultStock, mGoodsBean.getComCode(), "积分：");
                    mSukDialog.initDialog(this, mPriceBean, mAttrA, mAttrB, mAttrC, new SukDialog.onInputNameEvent() {
                        @Override
                        public void onClick(String data) {
                            //     Logcat.i("返回的 json 数据：" + data);
                        }

                        @Override
                        public void onClick(SuKResultBean mSuKResultBean) {
                            Logcat.i("SuKResultBean : " + mSuKResultBean.toString());
                            String suk = mSuKResultBean.getAttrs1();
                            if (!CommonUtils.isBlank(mSuKResultBean.getAttrs2())) {
                                suk = suk + "|" + mSuKResultBean.getAttrs2();
                            }
                            AddCart(mGoodsBean.getComID() + "", suk, mSuKResultBean.getPrice() + "", mSuKResultBean.getCount() + "", MemberCode, Token, "addCart");
                        }
                    });
                }
                break;*/
            case R.id.tv_buy_submit:
                if (CommonUtils.isBlank(MemberCode) || CommonUtils.isBlank(Token)) {
                    ToastUtils.showToast("请先登录");
                    startActivityWithoutExtras(LoginActivity.class);
                } else {
                    Logcat.i("----------------" + mPriceBean.toString() + mAttrA.size() + mAttrB.size());
                    int DefaultStock;
                    DefaultStock = 50;
                    mSukDialog.initAmountViewHint("该商品最多可购买：");
                    mSukDialog.initBooData(true, true);
                    mSukDialog.initBooData(true, true);
                    mSukDialog.initViewData(mGoodsBean.getComTitle(), mGoodsBean.getDescript().replace("40-40","400-400"), mGoodsBean.getMartPrice(), DefaultStock, mGoodsBean.getComCode(), "积分：");
                    mSukDialog.initDialog(this, mPriceBean, mAttrA, mAttrB, mAttrC, new SukDialog.onInputNameEvent() {
                        @Override
                        public void onClick(String data) {
                            //        Logcat.i("返回的 json 数据：" + data);
                        }

                        @Override
                        public void onClick(SuKResultBean mSuKResultBean) {
                            Logcat.i("SuKResultBean : " + mSuKResultBean.toString());
                            String suk = mSuKResultBean.getAttrs1();
                            if (!CommonUtils.isBlank(mSuKResultBean.getAttrs2())) {
                                suk = suk + "|" + mSuKResultBean.getAttrs2();
                            }
                            proAffirmOrderInfo(mSuKResultBean.getSukId());
                        }
                    });
                }
                break;
        }
    }

    private void proAffirmOrderInfo( String sukId) {
        Bundle mBundle = new Bundle();
        Logcat.i("----------------------------:" + mGoodsBean.toString());
        ArrayList<GoodsInfo> buyList = new ArrayList<>();
        String ImgUrl = null;
        if (!CommonUtils.isBlank(mGoodsBean.getDescript())){
            ImgUrl=mGoodsBean.getDescript();
        }else {
            if (!CommonUtils.isBlank(mGoodsBean.getImgList().get(0))){
                ImgUrl=mGoodsBean.getImgList().get(0);
            }
        }
        buyList.add(
                new GoodsInfo(
                        mGoodsBean.getComCode() + "",
                        mGoodsBean.getComID() + "",
                        mGoodsBean.getComTitle(),
                        mGoodsBean.getPoint(),
                        1,
                        ImgUrl,
                        sukId,
                        "",
                        "",
                        mGoodsBean.getMallType(), "", "", true));
        mBundle.putParcelableArrayList("pBuy", buyList);
        mBundle.putParcelable("GoodBean", mGoodsBean);
        mBundle.putString("sukId",sukId);
        startActivityWithExtras(PointMallAffirmOrder.class, mBundle);
    }
    /**
     * @param comId
     * @param memberCode
     * @param token
     */
    private void ProCollection(String comId, String memberCode, String token, final String flags,String mallType) {
        initProgressDialog("加载中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        map.put("ComID", comId);
        map.put("MallType", mallType);
        final String json = CommonUtils.GsonToJson(map);
        String URL = NetWorkConstant.COM_COLLECTION;//收藏;
        if (!flags.equals("add")) {
            URL = NetWorkConstant.COM_DEL_COLLECTION;//收藏
        }
        HttpClient.postJson(URL, json, new HttpResponseHandler() {
                    @Override
                    public boolean onSuccess(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        dismissProgressDialog();
                        NetLogCat.W(NetWorkConstant.COM_COLLECTION, json, statusCode, content);
                        ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                        if (mReturnBean.getStatus().equals("1")) {
                            if (flags.equals("add")) {
                                ToastUtils.showToast("收藏成功");
                            } else {
                                ToastUtils.showToast("取消收藏成功");
                            }
                        } else {
                            ToastUtils.showToast(mReturnBean.getMsg());
                        }
                        return false;
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                    }
                }

        );


    }

    /**
     * @param comID
     * @param attrId
     * @param memberCode
     * @param mToken
     * @param submitType
     */
    private void AddCart(String comID, String attrId, String price, String count, String memberCode, String mToken, final String submitType) {
        initProgressDialog("加载中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", mToken);
        map.put("ComID", comID);
        map.put("ComTitle", mGoodsBean.getComTitle());
        map.put("Price", price + "");
        map.put("quantity", count);
        map.put("ComPicUrl", mGoodsBean.getImgList().get(0));
        map.put("optionIDCombin", attrId);

        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.CART_ADD, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                dismissProgressDialog(true);
                NetLogCat.W(NetWorkConstant.CART_ADD, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    if (!CommonUtils.isBlank(submitType) && submitType.equals("buy")) {
                        startActivityWithoutExtras(MPSCActivity.class);
                    }
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }
        });
    }

    private void setBannerView(List<String> data) {
        //设置banner样式
        mBvMBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBvMBanner.setImageLoader(new BannerGlideImageLoader());
        //设置图片集合
        mBvMBanner.setImages(data);
        //设置自动轮播，默认为true
        mBvMBanner.isAutoPlay(true);
        //设置轮播时间
        mBvMBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBvMBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBvMBanner.start();
    }

    private void GetComIDInfo(String comID) {
        initProgressDialog("加载中...", true);
        Map<String, String> map = new HashMap<>();
        map.put("ComID", comID);
        map.put("MemberCode", "");
        map.put("Token", "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_DETAIL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.I(NetWorkConstant.COM_DETAIL, json, statusCode, content);
                dismissProgressDialog(true);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                //  mGoodsBean = JSON.parseObject(mReturnBean.getData(), GoodsBean.class);
                return false;
            }
        });
    }

    /**
     * @param ComID
     */
    private void GetComAttr(String ComID) {
        Map<String, String> map = new HashMap<>();
        map.put("ComID", ComID);
        map.put("OptionIDCombin", "");
        map.put("MemberCode", "");
        map.put("Token", "");
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_ATTR, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.COM_ATTR, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                List<AttrsBean> mAttrs = JSON.parseArray(mReturnBean.getData(), AttrsBean.class);
                for (int i = 0; i < mAttrs.size(); i++) {
                    String attrs1 = mAttrs.get(i).getOptionGroupName();
                    String attrs2 = mAttrs.get(i).getComOptionName();
                    String attrs3 = "";
                    Double price = Double.valueOf(mAttrs.get(i).getPrice());
               //   int Stock = mAttrs.get(i).getStock() - mAttrs.get(i).getSold();
                    if (!CommonUtils.isBlank(mAttrs.get(i).getOptionGroupName())){
                        mAttrA.add(mAttrs.get(i).getOptionGroupName());
                    }
                    if (!CommonUtils.isBlank(mAttrs.get(i).getComOptionName())){
                        mAttrB.add(mAttrs.get(i).getComOptionName());
                    }
                    mPriceBean.add(new SukPriceBean(mAttrs.get(i).getGroupID()+"",attrs1, attrs2, attrs3, price, 1));
                }
                return false;
            }
        });
        dismissProgressDialog();
    }

}
