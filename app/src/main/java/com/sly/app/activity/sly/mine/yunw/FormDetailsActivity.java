package com.sly.app.activity.sly.mine.yunw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.FormBean;
import com.sly.app.model.ReturnBean;
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
import vip.devkit.library.Logcat;

public class FormDetailsActivity extends BaseActivity {


    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.ll_item)
    LinearLayout llItem;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_money2)
    TextView tvMoney2;
    @BindView(R.id.tv_money3)
    TextView tvMoney3;
    @BindView(R.id.tv_desciption)
    TextView tvDesciption;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private String PersonSysCode;
    private int allFree;
    private String parts;

    private List<FormBean> formList = new ArrayList<>();
    private String detailid, desciption, note, hourFree;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_form_details;
    }

    @Override
    protected void initViewsAndEvents() {
        formList = (List<FormBean>) getIntent().getSerializableExtra("goodList");
        detailid = getIntent().getStringExtra("detailid");
        desciption = getIntent().getStringExtra("desciption");
        note = getIntent().getStringExtra("note");
        hourFree = getIntent().getStringExtra("hourFree");

        Logcat.e("detailid = "+ detailid +"desciption = "+ desciption
                +"note = "+ note +"hourFree = "+ hourFree + "size = " + formList.size() );

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");
        PersonSysCode = SharedPreferencesUtil.getString(this, "PersonSysCode","None");
        tvMainTitle.setText("报单详情");

        setDataForView();
    }

    private void setDataForView() {
        int allMoney = 0;
        int i = 0;
        for(FormBean bean : formList){
            i++;
            Logcat.e("code = "+ bean.getCode() +"name = "+ bean.getName()
                    +"num = "+ bean.getNum() +"price = "+ bean.getPrice());
            addViewItem(bean.getName(), bean.getNum(), bean.getPrice());

            int num1 = Integer.parseInt(bean.getNum());
            int price1 = Integer.parseInt(bean.getPrice());
            allMoney += num1 * price1;

            if(i > 1){
                parts += "|"+bean.getCode() + "," + bean.getName()+ "," +bean.getNum();
            }else{
                parts += bean.getCode() + "," + bean.getName()+ "," +bean.getNum();
            }
        }

        int hourFree1 = Integer.parseInt(hourFree);
        tvMoney.setText(hourFree);
        tvMoney2.setText(allMoney+"");
        allFree = hourFree1+ allMoney;
        tvMoney3.setText(allFree+"");
        tvDesciption.setText(desciption);
        tvNote.setText(note);

    }

    private void addViewItem(String choose, String num, String price) {
        View goodItemView = View.inflate(this, R.layout.item_goods, null);
        TextView tvGoodName = goodItemView.findViewById(R.id.tv_good_name);
        TextView tvGoodNum = goodItemView.findViewById(R.id.tv_good_num);
        TextView tvGoodMoney = goodItemView.findViewById(R.id.tv_good_money);

        tvGoodName.setText(choose);
        tvGoodNum.setText(num);
        tvGoodMoney.setText("¥ " + price);

        llItem.addView(goodItemView);
    }

    private void commitDatas() {
        Map map = new HashMap();

        map.put("Rounter", NetWorkCons.GET_REPAIR_GOODS_DEAL);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("minecode", MineCode);
        map.put("repairdetailcode", detailid);
        map.put("parts", parts);
        map.put("repairsum", allFree+"");
        map.put("userinfo", desciption);
        map.put("remark", note);
//        map.put("personSysCode", PcersonSysCode);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                Logcat.e("获取报单返回码:" + statusCode + "提交参数" + json +"返回参数:" + content);
                String backlogJsonStr = content;
                backlogJsonStr = backlogJsonStr.replace("\\", "");
                backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                Logcat.i("地址4:" + NetWorkCons.BASE_URL + "参数:" + json + "返回码:" + statusCode + "返回参数:" + backlogJsonStr);
                final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
                    new AlertDialog.Builder(FormDetailsActivity.this)
                            .setMessage("提交成功!")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    FormDetailsActivity.this.finish();
                                }
                            }).show();
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

    @OnClick({R.id.btn_main_back,R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.tv_commit:
                commitDatas();
                break;
        }
    }
}
