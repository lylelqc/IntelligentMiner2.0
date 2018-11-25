package com.sly.app.activity.yunw.repair;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.Helper.ActivityHelper;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.DialogListAdapter;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.FormBean;
import com.sly.app.model.yunw.repair.RepairSparesBean;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;

public class RepairFormActivity extends BaseActivity implements ICommonViewUi, DialogListAdapter.OnItemClickListener {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;

    @BindView(R.id.rl_choose)
    RelativeLayout rlChoose;
    @BindView(R.id.tv_choose_good)
    TextView tvChooseGood;
    @BindView(R.id.et_good_num)
    EditText etGoodNum;
    @BindView(R.id.et_single_price)
    EditText etSinglePrice;
    @BindView(R.id.tv_single_price_delete)
    TextView tvSinglePriceDelete;
    @BindView(R.id.tv_add_goods)
    TextView tvAddGoods;

    // 添加备件的layout
    @BindView(R.id.ll_add_item)
    LinearLayout llAddItem;

    @BindView(R.id.tv_form_spares_num)
    TextView tvFormSparesNum;
    @BindView(R.id.tv_form_spares_money)
    TextView tvFormSparesMoney;

    @BindView(R.id.et_hour_free)
    EditText etHourFree;

    @BindView(R.id.et_repair_desciption)
    EditText etRepairDescip;

    @BindView(R.id.tv_form_all_money)
    TextView tvFormAllMoney;
    @BindView(R.id.tv_commit_bill)
    TextView tvCommitBill;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String detailID;

    private List<RepairSparesBean> sparesList = new ArrayList<>();
    private List<FormBean> formBeanList= new ArrayList<>();
    private Dialog mDialog;
    private CommonRequestPresenterImpl iCommonRequestPresenter;

    private double hourFree;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_repair_form;
    }

    @Override
    protected void initViewsAndEvents() {
        // 把当前activty加入压入栈中
        ActivityHelper.getInstance().pushOneActivity(this);

        detailID = getIntent().getExtras().getString("DetailID");

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        tvMainTitle.setText(getString(R.string.repair_form));

        tvSinglePriceDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSinglePrice.setText("");
            }
        });

        etHourFree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    String hourfree = etHourFree.getText().toString().trim();
                    if(!AppUtils.isBlank(hourfree)){
                        String sparesMoney = tvFormSparesMoney.getText().toString().trim();
                        tvFormAllMoney.setText((Integer.parseInt(hourfree)+Integer.parseInt(sparesMoney))+"");
                    }
                }
            }
        });

        toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_SPARES);
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        map.put("Rounter", NetConstant.GET_YUNW_REPAIR_SPARES);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("minecode", MineCode);
//        map.put("personSysCode", PcersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("获取备件:" + result);
        List<RepairSparesBean> list = (List<RepairSparesBean>) AppUtils.parseRowsResult(result, RepairSparesBean.class);
        sparesList.clear();
        if(list != null && list.size() > 0){
            sparesList.addAll(list);
        }
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


    @OnClick({R.id.btn_main_back, R.id.rl_choose, R.id.tv_add_goods, R.id.tv_commit_bill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_choose:
                if(sparesList.size() == 0){
                    ToastUtils.showToast(getString(R.string.repair_loading_spares));
                    return ;
                }
                showDialogForChoose();
                break;
            case R.id.tv_add_goods:
                addDataToView();
                break;
            case R.id.tv_commit_bill:
                if(llAddItem.getChildCount() > 0){
                    printData(true);
                    String hourFree = etHourFree.getText().toString().trim();
                    String desciption = etRepairDescip.getText().toString().trim();

                    if(CommonUtils.isBlank(desciption)){
                        ToastUtils.showToast(getString(R.string.repair_input_repair_description));
                        return;
                    }

                    Logcat.e("size = "+ formBeanList.size());

                    Bundle bundle = new Bundle();
                    bundle.putString("DetailID", detailID);
                    bundle.putString("Desciption", desciption);
                    bundle.putString("HourFree", AppUtils.isBlank(hourFree)? "0": hourFree);
                    bundle.putSerializable("SparesList", (Serializable)formBeanList);
                    AppUtils.goActivity(this, RepairFormDetailsActivity.class, bundle);
                    formBeanList.clear();
                }else{
                    ToastUtils.showToast(getString(R.string.repair_add_spares));
                }
                break;
        }
    }

    /**
     * 添加备件数据到linearLayout中
     */
    private void addDataToView() {
        String goodNum = etGoodNum.getText().toString().trim();
        String singlePrice = etSinglePrice.getText().toString().trim();
        if((goodNum == null || TextUtils.isEmpty(goodNum)) ||
                (singlePrice == null || TextUtils.isEmpty(singlePrice))){
            ToastUtils.showToast(getString(R.string.repair_input_repair_num_price));
            return;
        }

        String goodChoose = tvChooseGood.getText().toString().trim();
        if(goodChoose == null || TextUtils.isEmpty(goodChoose)){
            ToastUtils.showToast(getString(R.string.repair_choose_spares));
            return;
        }
        addViewItem(goodChoose, goodNum, singlePrice);

        printData(false);
    }

    //添加ViewItem
    private void addViewItem(String choose, String num, String price) {
        View goodItemView = View.inflate(this, R.layout.item_add_goods, null);
        TextView tvGoodName = goodItemView.findViewById(R.id.tv_good_name);
        TextView tvGoodNum = goodItemView.findViewById(R.id.tv_good_num);
        TextView tvGoodMoney = goodItemView.findViewById(R.id.tv_good_money);

        tvGoodName.setText(choose);
        tvGoodNum.setText(num);
        tvGoodMoney.setText(price);

        llAddItem.addView(goodItemView);
        if(llAddItem.getChildCount() > 0){
            for (int i = 0; i < llAddItem.getChildCount(); i++) {
                final View childView = llAddItem.getChildAt(i);
                ImageView ivDelete = childView.findViewById(R.id.tv_good_delete);
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从LinearLayout容器中删除当前点击到的ViewItem
                        llAddItem.removeView(childView);
                        printData(false);
                    }
                });
            }
        }
    }

    /**
     * 获取所有动态添加的Item，找到控件的id，获取数据
     * @param tag 通过标志判断是否点击了生成报单
     */
    private void printData(boolean tag) {

        int sparesMoney = 0;
        String hoursMoney = etHourFree.getText().toString().trim();
        if(llAddItem.getChildCount() > 0){
            for (int i = 0; i < llAddItem.getChildCount(); i++) {
                View childAt = llAddItem.getChildAt(i);
                TextView tvGoodName = childAt.findViewById(R.id.tv_good_name);
                TextView tvGoodNum = childAt.findViewById(R.id.tv_good_num);
                TextView tvGoodMoney = childAt.findViewById(R.id.tv_good_money);

                String name = tvGoodName.getText().toString().trim();
                String num = tvGoodNum.getText().toString().trim();
                String price = tvGoodMoney.getText().toString().trim();

                Logcat.e("name：" + name + "-----num："
                        + num + "-----money：" + price);

                int num1 = Integer.parseInt(num);
                int price1 = Integer.parseInt(price);
                sparesMoney += num1 * price1;

                if(tag){
                    for(RepairSparesBean bean : sparesList){
                        if(name.equals(bean.getPartName())){
                            String code = bean.getPartID();
                            addDataToList(code, name, num, price);
                        }
                    }
                }
            }
            tvFormSparesNum.setText(llAddItem.getChildCount()+"");
            tvFormSparesMoney.setText(sparesMoney+"");

            if(AppUtils.isBlank(hoursMoney)){
                tvFormAllMoney.setText(sparesMoney+"");
            }else{
                tvFormAllMoney.setText((sparesMoney + Integer.parseInt(hoursMoney))+"");
            }

        }else{
            tvFormSparesNum.setText("0");
            tvFormSparesMoney.setText("0");
            if(AppUtils.isBlank(hoursMoney)) {
                tvFormAllMoney.setText("0");
            }else{
                tvFormAllMoney.setText(Integer.parseInt(hoursMoney)+"");
            }
        }
    }

    // 添加备件信息到list中
    private void addDataToList(String code, String name, String num, String price){
        FormBean bean = new FormBean();
        bean.setCode(code);
        bean.setName(name);
        bean.setNum(num);
        bean.setPrice(price);
        formBeanList.add(bean);
    }

    // 显示备件选择
    private void showDialogForChoose() {
        mDialog = new Dialog(mContext, R.style.AppDialog);
        mDialog.setContentView(R.layout.dialog_goods_list);
//        mDialog.setCanceledOnTouchOutside(false);
        ListView lvGoods = mDialog.findViewById(R.id.lv_dialog_goods);
        DialogListAdapter adapter = new DialogListAdapter(this, sparesList, lvGoods);
        adapter.setOnItemClickListener(this);
        lvGoods.setAdapter(adapter);
        mDialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Logcat.e(position + " - "+ sparesList.get(position));
        mDialog.dismiss();
        tvChooseGood.setText(sparesList.get(position).getPartName());
    }

}
