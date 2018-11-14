package com.sly.app.activity.sly.home;

import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.sly.CalReturnBean;
import com.sly.app.model.sly.CalerDefult;
import com.sly.app.model.sly.CalerFirstBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.JsonHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class SlyCalerActivity extends BaseActivity implements ICommonViewUi {
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
    @BindView(R.id.iv_chooise)
    ImageView ivChooise;
    @BindView(R.id.tv_bto)
    TextView tvBto;
    @BindView(R.id.rl_chooise)
    RelativeLayout rlChooise;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.et_3)
    EditText et3;
    @BindView(R.id.iv_1_x)
    ImageView iv1X;
    @BindView(R.id.iv2_1)
    ImageView iv21;
    @BindView(R.id.tv2_2)
    TextView tv22;
    @BindView(R.id.et2_3)
    EditText et23;
    @BindView(R.id.iv2_1_x)
    ImageView iv21X;
    @BindView(R.id.iv3_1)
    ImageView iv31;
    @BindView(R.id.tv3_2)
    TextView tv32;
    @BindView(R.id.et3_3)
    EditText et33;
    @BindView(R.id.iv3_1_x)
    ImageView iv31X;
    @BindView(R.id.iv4_1)
    ImageView iv41;
    @BindView(R.id.tv4_2)
    TextView tv42;
    @BindView(R.id.et4_3)
    EditText et43;
    @BindView(R.id.iv4_1_x)
    ImageView iv41X;
    @BindView(R.id.iv5_1)
    ImageView iv51;
    @BindView(R.id.tv5_2)
    TextView tv52;
    @BindView(R.id.et5_3)
    EditText et53;
    @BindView(R.id.iv5_1_x)
    ImageView iv51X;
    @BindView(R.id.iv6_1)
    ImageView iv61;
    @BindView(R.id.tv6_2)
    TextView tv62;
    @BindView(R.id.et6_3)
    TextView et63;
    @BindView(R.id.iv7_1)
    ImageView iv71;
    @BindView(R.id.tv7_2)
    TextView tv72;
    @BindView(R.id.et7_3)
    TextView et73;
    @BindView(R.id.iv8_1)
    ImageView iv81;
    @BindView(R.id.tv8_2)
    TextView tv82;
    @BindView(R.id.et8_3)
    TextView et83;
    @BindView(R.id.tv_price_b)
    EditText tvPriceB;
    @BindView(R.id.tv_price_1)
    TextView tvPrice1;
    @BindView(R.id.tv_price_2)
    TextView tvPrice2;
    @BindView(R.id.tv_start_b)
    EditText tvStartB;
    @BindView(R.id.tv_price2_1)
    TextView tvPrice21;
    @BindView(R.id.tv_price3_b)
    EditText tvPrice3B;
    @BindView(R.id.tv_hard_b)
    TextView tvHardB;
    @BindView(R.id.tv_price3_2)
    TextView tvPrice32;
    @BindView(R.id.tv_money_b)
    EditText tvMoneyB;
    @BindView(R.id.tv_price4_1)
    TextView tvPrice41;
    @BindView(R.id.tv_price4_2)
    TextView tvPrice42;
    @BindView(R.id.tv_cal)
    TextView tvCal;
    @BindView(R.id.tv_day_get_money)
    TextView tvDayGetMoney;
    @BindView(R.id.tv_back_day)
    TextView tvBackDay;
    @BindView(R.id.tv_day_1)
    TextView tvDay1;
    @BindView(R.id.tv_yu_money)
    TextView tvYuMoney;
    @BindView(R.id.tv_day_2)
    TextView tvDay2;
    @BindView(R.id.tv_yu_dian)
    TextView tvYuDian;
    @BindView(R.id.tv_day_3)
    TextView tvDay3;
    @BindView(R.id.tv_yu_lirun)
    TextView tvYuLirun;
    @BindView(R.id.tv_day2_1)
    TextView tvDay21;
    @BindView(R.id.tv_yu_chenben)
    TextView tvYuChenben;
    @BindView(R.id.tv_day2_2)
    TextView tvDay22;
    @BindView(R.id.tv_yu_t_price)
    TextView tvYuTPrice;
    @BindView(R.id.tv_day2_3)
    TextView tvDay23;
    @BindView(R.id.tv_yu_huibao)
    TextView tvYuHuibao;
    @BindView(R.id.tv_day3_1)
    TextView tvDay31;
    @BindView(R.id.tv_yu_zong_shouru)
    TextView tvYuZongShouru;
    @BindView(R.id.tv_day3_2)
    TextView tvDay32;
    @BindView(R.id.tv_yu_zong_lirun)
    TextView tvYuZongLirun;
    @BindView(R.id.tv_day3_3)
    TextView tvDay33;
    @BindView(R.id.tv_yu_zong_dianfei)
    TextView tvYuZongDianfei;
    @BindView(R.id.sc_view)
    NestedScrollView scView;
    private String User;
    private String FrSysCode;
    private String FMasterCode;
    private String Token;
    private String Key;
    private String LoginType;
    private List<CalReturnBean.DataBean> calDataList;
    private String KuPrice = "";
    private double TPrice = 00.00;
    private double TSuanli = 00.00;
    private double TCost = 00.00;
    private double currency = 00.00;//比价
    private long startingDifficulty = 0;//起始难度
    ICommonRequestPresenter iCommonRequestPresenter;
    private double amplification = 00.00;//难度增幅
    private double proportion = 00.00;//收益比
    private double TCount = 00.00;//矿机数量
    private double TDianfei = 00.00;//电费

    private CalerFirstBean data1;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sly_caler;
    }

    @Override
    protected void initViewsAndEvents() {
        calDataList = new ArrayList<>();
        tvMainTitle.setText("挖矿计算器");
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        User = SharedPreferencesUtil.getString(this, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        et23.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    TCount = Double.parseDouble(editable.toString());
                }
            }
        });

        et23.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 失去焦点
                    if(data1 != null && et23.getText().toString().equals("")){
                        et23.setText(data1.getMCount());
                    }
                }
            }
        });

        et53.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    TDianfei = Double.parseDouble(editable.toString());
                }
            }
        });

        et53.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 失去焦点
                    if(data1 != null && et53.getText().toString().equals("")){
                        et53.setText(data1.getMEnergy());
                    }
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("") && !editable.toString().equals("0")
                        && !editable.toString().equals("0.00")
                        && !editable.toString().equals("0.0")) {
                    TPrice = Double.parseDouble(editable.toString());
                    setTPrice();
                    setTCost();
                }
            }
        });

        et3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 失去焦点
                    if(data1 != null && et3.getText().toString().equals("")){
                        et3.setText(data1.getPrice());
                    }
                }
            }
        });

        et33.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("") && !editable.toString().equals("0")
                        && !editable.toString().equals("0.00")
                        && !editable.toString().equals("0.0")) {
                    TSuanli = Double.parseDouble(editable.toString());
                    setTPrice();
                    setTCost();
                }
            }
        });

        et33.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 失去焦点
                    if(data1 != null && et33.getText().toString().equals("")){
                        et33.setText(data1.getMPower());
                    }
                }
            }
        });

        et43.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("") && !editable.toString().equals("0")
                        && !editable.toString().equals("0.00")
                        && !editable.toString().equals("0.0")) {
                    TCost = Double.parseDouble(editable.toString());
                    setTCost();
                    setTPrice();
                }
            }
        });

        et43.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 失去焦点
                    if(data1 != null && et43.getText().toString().equals("")){
                        et43.setText(data1.getDissipation());
                    }
                }
            }
        });

        tvPriceB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("") && !editable.toString().equals("0")
                        && !editable.toString().equals("0.00")
                        && !editable.toString().equals("0.0")) {
                    currency = Double.parseDouble(editable.toString());
                }
            }
        });
        tvStartB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("") && !editable.toString().equals("0")
                        && !editable.toString().equals("0.00")
                        && !editable.toString().equals("0.0")) {
                    if (editable.toString() != null) {

                        startingDifficulty = Long.parseLong(editable.toString());
                    }
                }
            }
        });
        tvPrice3B.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("") && !editable.toString().equals("0")
                        && !editable.toString().equals("0.00")
                        && !editable.toString().equals("0.0")) {
                    amplification = Double.parseDouble(editable.toString());
                }
            }
        });
        tvMoneyB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("") && !editable.toString().equals("0")
                        && !editable.toString().equals("0.00")
                        && !editable.toString().equals("0.0")) {
                    proportion = Double.parseDouble(editable.toString());
                }
            }
        });

        toRequest(NetWorkCons.EventTags.CALER_FIRST);
        toRequest(NetWorkCons.EventTags.GET_CALCULATER_DEFULT_NUM);
    }

    private void setTCost() {
        if (TSuanli == 0.00 || TCost == 0.00) {
            return;
        } else {
            et63.setText(String.format("%.2f", TCost / TSuanli));
        }
    }

    private void setTPrice() {
        if (TPrice == 0.00 || TSuanli == 0.00) {
            return;
        } else {
            et73.setText(String.format("%.2f", TPrice / TSuanli));
        }
    }

    @Override
    public void toRequest(int eventTag) {
        if (NetWorkCons.EventTags.CALER_FIRST == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.CALER_FIRST);
            map.put("User", User);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.CALER_FIRST, mContext, NetWorkCons.BASE_URL, mapJson);
        } else if (NetWorkCons.EventTags.GET_CALCULATER_DEFULT_NUM == eventTag) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_CALCULATER_DEFULT_NUM);
            map.put("User", User);
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_CALCULATER_DEFULT_NUM, mContext, NetWorkCons.BASE_URL, mapJson);
        } else if (NetWorkCons.EventTags.GET_CALCULATER == eventTag) {
            Map<String, String> map = new HashMap<>();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", NetWorkCons.GET_CALCULATER);
            map.put("User", User);
            map.put("sys", LoginType.equals("Miner") ? FrSysCode : FMasterCode);
            /**
             price	是	string	矿机价格
             count	是	s
             tring	矿机数量
             force	是	string	单台矿机算力
             dissipation	是	string	单台矿机功耗
             energy	是	string	电费
             currency	是	string	币价
             startingDifficulty	是	string	起始难度
             amplification	是	string	难度增幅（百分比）
             proportion	是	string	收益比（百分比）
             beginDate	是	string	开始时间
             endDate	是	string	结束时间(不能小于当前时间)
             */
            map.put("price", TPrice + "");

            int count2 = ((Number) (Float.parseFloat(TCount + ""))).intValue();
            map.put("count", count2 + "");

            int force2 = ((Number) (Float.parseFloat(TSuanli + ""))).intValue();
            map.put("force", TSuanli + "");

            int dissipation2 = ((Number) (Float.parseFloat(TCost + ""))).intValue();
            map.put("dissipation", dissipation2 + "");

            map.put("energy", TDianfei + "");
            map.put("currency", currency + "");

            Long startingDifficulty2 = Long.parseLong(String.valueOf(startingDifficulty));
            map.put("startingDifficulty", startingDifficulty2 + "");

            map.put("amplification", amplification + "");
            map.put("proportion", proportion + "");
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final String beginTime = dateFormat.format(date);

            Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
            t.setToNow(); // 取得系统时间。
            int year = t.year;
            int month = t.month + 1;
            int day = t.monthDay;
            String beginDate = year + "-" + month + "-" + day;
            int endYear = year + 1;
            String endDate = endYear + "-" + month + "-" + day;
            map.put("beginDate", beginDate + "");
            map.put("endDate", endDate + "");
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.GET_CALCULATER, mContext, NetWorkCons.BASE_URL, mapJson);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {

        if (NetWorkCons.EventTags.CALER_FIRST == eventTag) {
            dimissProgress();
            JsonHelper<CalerFirstBean> dataParser = new JsonHelper<CalerFirstBean>(CalerFirstBean.class);
            CalerFirstBean data = dataParser.getData(result, "data");
            data1 = data;
            setFirstCaler(data);
        } else if (NetWorkCons.EventTags.GET_CALCULATER_DEFULT_NUM == eventTag) {
            dimissProgress();
            JsonHelper<CalerDefult> dataParser = new JsonHelper<CalerDefult>(CalerDefult.class);
            CalerDefult data = dataParser.getData(result, "data");

            if (data!=null){
                String CurrencyBPrice = String.format("%.2f", Double.valueOf(data.getCurrency()));
                tvPriceB.setText(CurrencyBPrice + "");
            }
//            tvStartB.setText(data.getDifficulty()+"" );

        } else if (NetWorkCons.EventTags.GET_CALCULATER == eventTag) {
            dimissProgress();
            JsonHelper<CalReturnBean> dataParser = new JsonHelper<CalReturnBean>(CalReturnBean.class);
            CalReturnBean calReturnBean = dataParser.getData(result, "data");
            calDataList = calReturnBean.getList();
            setCalReturn(calReturnBean);
        }
    }

    private void setFirstCaler(CalerFirstBean data) {
        /**
         price	是	string	矿机价格
         count	是	string	矿机数量
         force	是	string	单台矿机算力
         dissipation	是	string	单台矿机功耗
         energy	是	string	电费
         currency	是	string	币价
         startingDifficulty	是	string	起始难度
         amplification	是	string	难度增幅（百分比）
         proportion	是	string	收益比（百分比）
         beginDate	是	string	开始时间
         endDate	是	string	结束时间(不能小于当前时间)
         */


        et3.setText(data.getPrice());
        et23.setText(data.getMCount());
        et33.setText(data.getMPower());
        et43.setText(data.getDissipation());
        et53.setText(data.getMEnergy());
        et63.setText(data.getDissipationOfEveryT());
        et73.setText(data.getPriceOfEveryT());
        et83.setText(data.getAllPower());
        tvYuTPrice.setText("¥ " + data.getPriceOfEveryT());
        tvBackDay.setText(data.getBackToTheDays() + " 天");
        tvDayGetMoney.setText("¥ " + data.getTheDailyIncomeT() + " BTC/TH");
        tvPriceB.setText(data.getCurrency());
        tvStartB.setText(data.getStartingDifficulty());
        tvYuMoney.setText("¥ "+ data.getTheDailyIncome());
        tvYuDian.setText("¥ "+ data.getTheDailyEnergy());
        tvYuLirun.setText("¥ "+ data.getTheDailyRofit());
        tvYuChenben.setText("¥ "+ data.getGrosspMillCost());
        tvYuHuibao.setText(data.getROI() + " %");
        tvPrice3B.setText(data.getAmplification());
        tvMoneyB.setText(data.getEarningsRatio());
        et83.setText(data.getAllPower());
        tvYuZongShouru.setText("¥ "+ data.getGrosspIncome());
        tvYuZongLirun.setText("¥ "+ data.getGrosspRofit());
        tvYuZongDianfei.setText("¥ "+ data.getGrosspEnergy());

        TPrice = Double.parseDouble(data.getPrice());
        TCount = Double.parseDouble(data.getMCount());
        TSuanli = Double.parseDouble(data.getMPower());
        TCost = Double.parseDouble(data.getDissipation());
        TDianfei = Double.parseDouble(data.getMEnergy());

        currency = Double.parseDouble(data.getCurrency());
        startingDifficulty = Long.parseLong(data.getStartingDifficulty());
        amplification = Double.parseDouble(data.getAmplification());
        proportion = Double.parseDouble(data.getEarningsRatio());
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        dimissProgress();
        showToastShort(msg);
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
        dimissProgress();
        showToastShort(msg);




    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        if (status)
            showProgress("数据提交...");
    }

    private void DoCal(String price, String count, String force, String dissipation,
                       String energy,
                       String beginDate, String endDate
    ) {
        final Map<String, String> map = new HashMap<>();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Calculater.001");
        map.put("User", User);
        map.put("sys", LoginType.equals("Miner") ? FrSysCode : FMasterCode);
        /**
         price	是	string	矿机价格
         count	是	s
         tring	矿机数量
         force	是	string	单台矿机算力
         dissipation	是	string	单台矿机功耗
         energy	是	string	电费
         currency	是	string	币价
         startingDifficulty	是	string	起始难度
         amplification	是	string	难度增幅（百分比）
         proportion	是	string	收益比（百分比）
         beginDate	是	string	开始时间
         endDate	是	string	结束时间(不能小于当前时间)
         */
        map.put("price", price);

        int count2 = ((Number) (Float.parseFloat(count))).intValue();
        map.put("count", count2 + "");

        int force2 = ((Number) (Float.parseFloat(force))).intValue();
        map.put("force", force2 + "");

        int dissipation2 = ((Number) (Float.parseFloat(dissipation))).intValue();
        map.put("dissipation", dissipation2 + "");
        map.put("energy", energy);
        map.put("currency", currency + "");

        Long startingDifficulty2 = Long.parseLong(String.valueOf(startingDifficulty));
        map.put("startingDifficulty", startingDifficulty2 + "");

        map.put("amplification", amplification + "");
        map.put("proportion", proportion + "");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String beginTime = dateFormat.format(date);

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        beginDate = year + "-" + month + "-" + day;
        int endYear = year + 1;
        endDate = endYear + "-" + month + "-" + day;
        map.put("beginDate", beginDate + "");
        map.put("endDate", endDate + "");
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        Gson gson = new Gson();
        final String json = gson.toJson(mapJson);
        Logcat.i("提交的参数：" + json + " 地址 :" + NetWorkCons.BASE_URL);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.I(NetWorkCons.BASE_URL, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    CalReturnBean calReturnBean = JSON.parseObject(mReturnBean.getData().toString(), CalReturnBean.class);
                    calDataList = calReturnBean.getList();
                    setCalReturn(calReturnBean);
//                    ToastUtils.showToast(mReturnBean.getMsg());
                } else {
                    ToastUtils.showToast(mReturnBean.getMsg());
                }
                return false;
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });

    }

    private void setCalReturn(CalReturnBean calReturnBean) {
        tvDayGetMoney.setText("¥ " + calReturnBean.getTheDailyIncomeT() + " BTC/TH");
        tvBackDay.setText(calReturnBean.getBackToTheDays() + " 天");

        tvYuMoney.setText("¥ " + calReturnBean.getTheDailyIncome());
        tvYuDian.setText("¥ " + calReturnBean.getTheDailyEnergy());
        tvYuLirun.setText("¥ " + calReturnBean.getTheDailyRofit());

        tvYuChenben.setText("¥ " + calReturnBean.getGrosspMillCost());
        tvYuTPrice.setText("¥ " + calReturnBean.getPriceOfEveryT());
        tvYuHuibao.setText(calReturnBean.getROI() + "%");

        tvYuZongShouru.setText("¥ " + calReturnBean.getGrosspIncome());
        tvYuZongLirun.setText("¥ " + calReturnBean.getGrosspRofit());
        tvYuZongDianfei.setText("¥ " + calReturnBean.getGrosspEnergy());


    }


    @OnClick({R.id.btn_main_back, R.id.rl_chooise, R.id.iv_1, R.id.tv_2, R.id.et_3, R.id.iv_1_x, R.id.iv2_1, R.id.tv2_2, R.id.et2_3, R.id.iv2_1_x, R.id.iv3_1, R.id.tv3_2, R.id.et3_3, R.id.iv3_1_x, R.id.iv4_1, R.id.tv4_2, R.id.et4_3, R.id.iv4_1_x, R.id.iv5_1, R.id.tv5_2, R.id.et5_3, R.id.iv5_1_x, R.id.iv6_1, R.id.tv6_2, R.id.et6_3, R.id.iv7_1, R.id.tv7_2, R.id.et7_3, R.id.iv8_1, R.id.tv8_2, R.id.et8_3, R.id.tv_cal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_chooise:
                break;
            case R.id.iv_1:
                break;
            case R.id.tv_2:
                break;
            case R.id.et_3:
                break;
            case R.id.iv_1_x://单台矿机价格,撤销
                et3.setText("");
                break;
            case R.id.iv2_1:
                break;
            case R.id.tv2_2:
                break;
            case R.id.et2_3:
                break;
            case R.id.iv2_1_x://矿机数量
                et23.setText("");
                break;
            case R.id.iv3_1:
                break;
            case R.id.tv3_2:
                break;
            case R.id.et3_3:
                break;
            case R.id.iv3_1_x://矿机算力
                et33.setText("");
                break;
            case R.id.iv4_1:
                break;
            case R.id.tv4_2:
                break;
            case R.id.et4_3:
                break;
            case R.id.iv4_1_x://矿机功耗
                et43.setText("");
                break;
            case R.id.iv5_1:
                break;
            case R.id.tv5_2:
                break;
            case R.id.et5_3:
                break;
            case R.id.iv5_1_x://电费
                et53.setText("");
                break;
            case R.id.iv6_1:
                break;
            case R.id.tv6_2:
                break;
            case R.id.et6_3:
                break;
            case R.id.iv7_1:
                break;
            case R.id.tv7_2:
                break;
            case R.id.et7_3:
                break;
            case R.id.iv8_1:
                break;
            case R.id.tv8_2:
                break;
            case R.id.et8_3:
                break;
            case R.id.tv_cal:
                hasCalData();
                break;
        }
    }

    private void hasCalData() {
        if (et3.getText().toString().equals("00.00") || et3.getText().toString().isEmpty()) {
            ToastUtils.showToast("请输入单台矿机价格");
            return;
        }
        if (et23.getText().toString().equals("00.00") || et23.getText().toString().isEmpty()) {
            ToastUtils.showToast("请输入矿机数量");
            return;
        }
        if (et33.getText().toString().equals("00.00") || et33.getText().toString().isEmpty()) {
            ToastUtils.showToast("请输入算力价格");
            return;
        }
        if (et43.getText().toString().equals("00.00") || et43.getText().toString().isEmpty()) {
            ToastUtils.showToast("请输入矿机功耗");
            return;
        }
        if (et53.getText().toString().equals("00.00") || et53.getText().toString().isEmpty()) {
            ToastUtils.showToast("请输入电费");
            return;
        }
        if (currency == 00.00 || tvPriceB.getText().toString().isEmpty()) {
            ToastUtils.showToast("币价不能为空");
            return;
        }
        if (startingDifficulty == 00.00 || tvStartB.getText().toString().isEmpty()) {
            ToastUtils.showToast("起始难度不能为空");
            return;
        }
        if (amplification == 00.00 || tvPrice3B.getText().toString().isEmpty()) {
            ToastUtils.showToast("难度增幅不能为空");
            return;
        }
        if (proportion == 00.00 || tvMoneyB.getText().toString().isEmpty()) {
            ToastUtils.showToast("收益比不能为空");
            return;
        }

        showProgress("数据提交...");
        toRequest(NetWorkCons.EventTags.GET_CALCULATER);
//        DoCal(TPrice + "", TCount + "", TSuanli + "", TCost + "", TDianfei + ""
//                , "", ""
//        );
    }
}
