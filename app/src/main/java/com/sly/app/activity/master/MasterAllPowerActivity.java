package com.sly.app.activity.master;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.master.MasterAllPowerBean;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.BarUtil;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.view.iviews.ICommonViewUi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class MasterAllPowerActivity extends BaseActivity implements ICommonViewUi{

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.rl_master_chose_date)
    RelativeLayout rlMasterChoseDate;
    @BindView(R.id.tv_master_chose_date)
    TextView tvMasterChoseDate;
    @BindView(R.id.tv_master_power_month)
    TextView tvMasterPowerMonth;

    @BindView(R.id.tv_master_date)
    TextView tvMasterDate;
    @BindView(R.id.tv_master_power_day)
    TextView tvMasterPowerDay;;
    @BindView(R.id.bar_chart)
    BarChart bcFreePic;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;

    private List<MasterAllPowerBean> mResultList = new ArrayList<>();
    private CommonRequestPresenterImpl iCommonRequestPresenter;

    private String Year = "";
    private String Month = "";
    private int day = 0;
    private BarUtil barUtil;
    private Dialog dialog;
    private DatePicker picker;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_master_all_power;
    }

    @Override
    protected void initViewsAndEvents() {

        MineCode = getIntent().getExtras().getString("MineCode");

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        tvMainTitle.setText(getString(R.string.master_power));

        Calendar calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) + "";
        Month = (calendar.get(Calendar.MONTH) + 1) + "";
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tvMasterChoseDate.setText(Year + "年" + Month +"月");

        intitNewsCount();
        toRequest(NetConstant.EventTags.GET_MASTER_MONTH_POWER);

    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if ("0".equals(count)) {
            tvRedNum.setVisibility(View.GONE);
        } else {
            tvRedNum.setVisibility(View.VISIBLE);
            tvRedNum.setText(count);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toRequest(NetConstant.EventTags.GET_MASTER_MONTH_POWER);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        map.put("Rounter", NetConstant.GET_MASTER_MONTH_POWER);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("mineCode", MineCode);
        map.put("Year", Year);
        map.put("Month", Month);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数 - " + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数" + result);
        if (eventTag == NetConstant.EventTags.GET_MASTER_MONTH_POWER) {
            mResultList.clear();
            List<MasterAllPowerBean> resultList =
                    (List<MasterAllPowerBean>) AppUtils.parseRowsResult(result, MasterAllPowerBean.class);
            mResultList.addAll(resultList);
            if(!AppUtils.isListBlank(mResultList)){
                initBarChart();
                refreshListView(day, true);
            }else {
                bcFreePic.clear();
            }
        }
    }

    private void refreshListView(int position, boolean isDecase) {
        MasterAllPowerBean bean = mResultList.get(position-1);
        tvMasterPowerMonth.setText(bean.getPowerSum()+"");
        tvMasterDate.setText(bean.getDate().split(" ")[0]);
        tvMasterPowerDay.setText(bean.getDatePowerSum() + "");
    }

    private void initBarChart() {
        LinkedHashMap<String, List<Float>> chartDataMap = new LinkedHashMap<>();
        List<Float> xValues = new ArrayList<>();
        List<Float> yValue1 = new ArrayList<>();
        int colors = getResources().getColor(R.color.sly_navigation_bar);

        for (MasterAllPowerBean bean : mResultList) {
            xValues.add((float)bean.getDay());
            yValue1.add((float) bean.getDatePowerSum());
        }

        chartDataMap.put(getString(R.string.master_free_manage), yValue1);

        barUtil = new BarUtil(bcFreePic);
        barUtil.initCalPowerPic(this, bcFreePic, mResultList, false);
        barUtil.showBarChart(xValues, yValue1,"", colors);

        bcFreePic.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                BarDataSet dataSets = (BarDataSet) bcFreePic.getBarData().getDataSetForEntry(e);
                Logcat.e(e.getX()+" - X轴");
                refreshListView((int)e.getX(), false);
            }

            @Override
            public void onNothingSelected() {

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

    @OnClick({R.id.btn_main_back, R.id.rl_notice,R.id.rl_master_chose_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.rl_master_chose_date:
                dialog();
                break;
        }
    }

    private void dialog() {
        dialog = new Dialog(mContext, R.style.quick_option_dialog);
        dialog.setContentView(R.layout.dialog_date_picker);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setWindowAnimations((R.style.popWindow_anim_style));
        dialog.show();
        Button btnSelCancel = dialog.findViewById(R.id.repair_date_sel_cancel);
        Button btnSelOk = dialog.findViewById(R.id.repair_date_sel_ok);
        picker = dialog.findViewById(R.id.date_picker);
        //设置分割线颜色
        setDatePickerDividerColor();

        btnSelCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSelOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                Year = picker.getYear()+"";
                String month = (picker.getMonth()+1 < 10) ? "0" + (picker.getMonth()+1) : "" + (picker.getMonth()+1);
                Month = month;
                String date = picker.getYear() + "年" + month +"月";
                tvMasterChoseDate.setText(date);
            }
        });
    }

    /**
     * 设置时间选择器的分割线颜色
     */
    private void setDatePickerDividerColor() {
        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) picker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(Color.parseColor("#7cbcf2")));//设置分割线颜色
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
