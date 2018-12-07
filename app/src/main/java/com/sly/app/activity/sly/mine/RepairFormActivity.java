package com.sly.app.activity.sly.mine;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.adapter.DialogListAdapter;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.FormBean;
import com.sly.app.model.sly.UserNameBean;
import com.sly.app.model.sly.balance.SlyReturnListBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import vip.devkit.library.Logcat;

public class RepairFormActivity {//extends BaseActivity implements DialogListAdapter.OnItemClickListener

    /*@BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView tvMainRight;
    @BindView(R.id.rl_choose)
    RelativeLayout rlChoose;
    @BindView(R.id.tv_choose_good)
    TextView tvChooseGood;
    @BindView(R.id.et_good_num)
    EditText etGoodNum;
    @BindView(R.id.et_single_price)
    EditText etSinglePrice;
    @BindView(R.id.tv_add_goods)
    TextView tvAddGoods;
    @BindView(R.id.ll_add_item)
    LinearLayout llAddItem;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.et_hour_free)
    EditText etHourFree;
    @BindView(R.id.et_repair_desciption)
    EditText etRepairDescip;
    @BindView(R.id.et_repair_note)
    EditText etRepairNote;
    @BindView(R.id.tv_commit_bill)
    TextView tvCommitBill;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String PersonSysCode;
    private String detailid;

    private List<UserNameBean> goodsList = new ArrayList<>();
    private List<FormBean> formBeanList= new ArrayList<>();
    private Dialog mDialog;

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
        detailid = getIntent().getStringExtra("detailid");
        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode","None");
        tvMainTitle.setText("维修报单");
        getAreaDatas(NetWorkCons.EventTags.GET_REPAIR_GOODS);
    }

    private void getAreaDatas(int eventType) {
        Map map = new HashMap();

        map.put("Rounter", NetWorkCons.GET_REPAIR_GOODS);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("minecode", MineCode);
//        map.put("personSysCode", PcersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("获取备件返回码:" + statusCode + "提交参数" + json +"返回参数:" + content);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                Logcat.i("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);
                final SlyReturnListBean returnBean = JSON.parseObject(backlogJsonStr, SlyReturnListBean.class);
                if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                    if (returnBean.getData().getRows().size() > 0) {
                        goodsList = JSON.parseArray(returnBean.getData().getRows().toString(), UserNameBean.class);
                    }
                } else {
                    ToastUtils.showToast(returnBean.getMsg());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("错误信息：" + request.toString() + "/" + e);
            }
        });
    }

    @OnClick({R.id.btn_main_back, R.id.rl_choose, R.id.tv_add_goods, R.id.tv_commit_bill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_choose:
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
                    String note = etRepairNote.getText().toString().trim();

                    if(CommonUtils.isBlank(desciption)){
                        ToastUtils.showToast("请输入维修描述");
                        return;
                    }

                    Logcat.e("size = "+ formBeanList.size());

                    Intent intent = new Intent(this, FormDetailsActivity.class);
                    intent.putExtra("detailid", detailid);
                    intent.putExtra("desciption", desciption);
                    intent.putExtra("note", note);
                    intent.putExtra("hourFree", CommonUtils.isBlank(hourFree)? "0": hourFree);
                    intent.putExtra("goodList", (Serializable)formBeanList);
                    startActivity(intent);
                    formBeanList.clear();
                }
                break;
        }
    }

    private void addDataToView() {
        String goodNum = etGoodNum.getText().toString().trim();
        String singlePrice = etSinglePrice.getText().toString().trim();
        if((goodNum == null || TextUtils.isEmpty(goodNum)) ||
                (singlePrice == null || TextUtils.isEmpty(singlePrice))){
            ToastUtils.showToast("请输入数量或单价");
            return;
        }

        String goodChoose = tvChooseGood.getText().toString().trim();
        if(goodChoose == null || TextUtils.isEmpty(goodChoose)){
            ToastUtils.showToast("请选择备件");
            return;
        }
        addViewItem(goodChoose, goodNum, singlePrice);

        printData(false);
    }

    //添加ViewItem
    private void addViewItem(String choose, String num, String price) {
        View goodItemView = View.inflate(this, R.layout.item_add_goods, null);
        TextView tvGoodName = (TextView) goodItemView.findViewById(R.id.tv_good_name);
        TextView tvGoodNum = (TextView) goodItemView.findViewById(R.id.tv_good_num);
        TextView tvGoodMoney = (TextView) goodItemView.findViewById(R.id.tv_good_money);
        ImageView ivGoodDelete = (ImageView) goodItemView.findViewById(R.id.tv_good_delete);

        tvGoodName.setText(choose);
        tvGoodNum.setText(num);
        tvGoodMoney.setText(price);

        llAddItem.addView(goodItemView);
        if(llAddItem.getChildCount() > 0){
            for (int i = 0; i < llAddItem.getChildCount(); i++) {
                final View childAt = llAddItem.getChildAt(i);
                ImageView ivDelete = (ImageView) childAt.findViewById(R.id.tv_good_delete);
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从LinearLayout容器中删除当前点击到的ViewItem
                        llAddItem.removeView(childAt);
                        printData(false);
                    }
                });
            }
        }
    }

    //获取所有动态添加的Item，找到控件的id，获取数据
    private void printData(boolean tag) {
        int allMoney = 0;
        if(llAddItem.getChildCount() > 0){
            for (int i = 0; i < llAddItem.getChildCount(); i++) {
                View childAt = llAddItem.getChildAt(i);
                TextView tvGoodName = (TextView) childAt.findViewById(R.id.tv_good_name);
                TextView tvGoodNum = (TextView) childAt.findViewById(R.id.tv_good_num);
                TextView tvGoodMoney = (TextView) childAt.findViewById(R.id.tv_good_money);

                String name = tvGoodName.getText().toString().trim();
                String num = tvGoodNum.getText().toString().trim();
                String price = tvGoodMoney.getText().toString().trim();

                Logcat.e("name：" + name + "-----num："
                        + num + "-----money：" + price);

                int num1 = Integer.parseInt(num);
                int price1 = Integer.parseInt(price);
                allMoney += num1 * price1;

                if(tag){
                    for(UserNameBean bean : goodsList){
                        if(name.equals(bean.getName())){
                            String code = bean.getCode();
                            addDataToList(code, name, num, price);
                        }
                    }
                }
            }
            tvAllMoney.setText( "¥ " + allMoney);
        }else{
            tvAllMoney.setText("¥ 0");
        }
    }

    private void addDataToList(String code, String name, String num, String price){
        FormBean bean = new FormBean();
        bean.setCode(code);
        bean.setName(name);
        bean.setNum(num);
        bean.setPrice(price);
        formBeanList.add(bean);
    }

    private void showDialogForChoose() {
        mDialog = new Dialog(mContext, R.style.AppDialog);
        mDialog.setContentView(R.layout.dialog_goods_list);
//        mDialog.setCanceledOnTouchOutside(false);
        ListView lvGoods = mDialog.findViewById(R.id.lv_dialog_goods);
        DialogListAdapter adapter = new DialogListAdapter(this, goodsList, lvGoods);
        adapter.setOnItemClickListener(this);
        lvGoods.setAdapter(adapter);
        mDialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Logcat.e(position + " - "+ goodsList.get(position));
        mDialog.dismiss();
        tvChooseGood.setText(goodsList.get(position).getName());
    }*/
}
