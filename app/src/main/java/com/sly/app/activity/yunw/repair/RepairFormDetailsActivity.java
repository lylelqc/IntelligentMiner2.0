package com.sly.app.activity.yunw.repair;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.Helper.ActivityHelper;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.sly.mine.notice.Sly2NoticeActivity;
import com.sly.app.comm.EventBusTags;
import com.sly.app.comm.NetConstant;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.FormBean;
import com.sly.app.model.PostResult;
import com.sly.app.model.ReturnBean;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.AppUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.iviews.ICommonViewUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import vip.devkit.library.Logcat;

public class RepairFormDetailsActivity extends BaseActivity implements ICommonViewUi {

    @BindView(R.id.btn_main_back)
    LinearLayout btnMainBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.tv_red_num)
    TextView tvRedNum;

    @BindView(R.id.ll_form_details_additem)
    LinearLayout llFormDetailsAddItem;
    @BindView(R.id.tv_form_details_hourfree)
    TextView tvFormDetailsHourfree;
    @BindView(R.id.tv_form_details_sparesfree)
    TextView tvFormDetailsSparesfree;
    @BindView(R.id.tv_form_details_desciption)
    TextView tvFormDetailsDescripition;
    @BindView(R.id.tv_form_details_allfree)
    TextView tvFormDetailsAllfree;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    private String User;
    private String Token;
    private String LoginType;
    private String Key;
    private String MineCode;
    private int allFree;
    private String parts = "";

    private List<FormBean> formList = new ArrayList<>();
    private String detailid, desciption, hourFree;
    private CommonRequestPresenterImpl iCommonRequestPresenter;

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
        // 把当前activty加入压入栈中
        ActivityHelper.getInstance().pushOneActivity(this);
        formList = (List<FormBean>) getIntent().getExtras().getSerializable("SparesList");
        detailid = getIntent().getExtras().getString("DetailID");
        hourFree = getIntent().getExtras().getString("HourFree");
        desciption = getIntent().getExtras().getString("Desciption");

        Logcat.e("detailid = "+ detailid +"desciption = "+ desciption
                 +"hourFree = "+ hourFree + "size = " + formList.size() );

        User = SharedPreferencesUtil.getString(this, "User");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        MineCode = SharedPreferencesUtil.getString(this, "MineCode","None");

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        tvMainTitle.setText(getString(R.string.repair_form));

        intitNewsCount();
        setDataForView();
    }

    private void intitNewsCount() {
        String count = AppUtils.getNewsCount(this);
        if("0".equals(count)){
            tvRedNum.setVisibility(View.GONE);
        }else{
            tvRedNum.setVisibility(View.VISIBLE);
            tvRedNum.setText(count);
        }
    }

    private void setDataForView() {
        int allMoney = 0;
        int i = 0;
        for(FormBean bean : formList){
            i++;
            addViewItem(bean.getName(), bean.getNum(), bean.getPrice(),i);

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
        tvFormDetailsHourfree.setText(hourFree);
        tvFormDetailsSparesfree.setText(allMoney+"");
        allFree = hourFree1 + allMoney;
        tvFormDetailsDescripition.setText(desciption);
        tvFormDetailsAllfree.setText(allFree+"");

    }

    private void addViewItem(String choose, String num, String price, int count) {
        View goodItemView = View.inflate(this, R.layout.item_goods, null);
        if((count % 2) == 1){
            goodItemView.setBackgroundColor(getResources().getColor(R.color.white));
        }else{
            goodItemView.setBackgroundColor(getResources().getColor(R.color.sly_bg_f8f8f8));
        }

        TextView tvGoodName = goodItemView.findViewById(R.id.tv_good_name);
        TextView tvGoodNum = goodItemView.findViewById(R.id.tv_good_num);
        TextView tvGoodMoney = goodItemView.findViewById(R.id.tv_good_money);

        tvGoodName.setText(choose);
        tvGoodNum.setText(num);
        tvGoodMoney.setText("¥ " + price);

        llFormDetailsAddItem.addView(goodItemView);
    }

    @OnClick({R.id.btn_main_back,R.id.tv_commit, R.id.rl_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_notice:
                AppUtils.goActivity(this, Sly2NoticeActivity.class);
                break;
            case R.id.tv_commit:
//                showCustomDialog(this, getString(R.string.comfirm_success));
                toRequest(NetConstant.EventTags.GET_YUNW_REPAIR_SUBMIT);
                break;
        }
    }

    @Override
    public void toRequest(int eventTag) {
        Map map = new HashMap();

        map.put("Rounter", NetConstant.GET_YUNW_REPAIR_SUBMIT);
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("User", User);
        map.put("minecode", MineCode);
        map.put("DetailID", detailid);
        map.put("repairsum", allFree+"");
        map.put("parts", parts);
        map.put("userinfo", desciption);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        Logcat.e("提交参数 - " + mapJson);
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        Logcat.e("返回参数"+result);
        ReturnBean returnBean = JSON.parseObject(result, ReturnBean.class);
        if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {
            showCustomDialog(this, getString(R.string.comfirm_success));
        }else{
            ToastUtils.showToast(returnBean.getMsg());
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

    private void showCustomDialog(Context context, String content){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_general_style);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvDetails = dialog.findViewById(R.id.tv_dialog_content);
        tvDetails.setText(content);
        dialog.findViewById(R.id.cancel_action).setVisibility(View.GONE);
        dialog.findViewById(R.id.view_line).setVisibility(View.GONE);
        TextView confirm = dialog.findViewById(R.id.confirm_action);

        dialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ActivityHelper.getInstance().popMoreActivity(3);
                EventBus.getDefault().post(new PostResult(EventBusTags.JUMP_REPAIR_BILL_TREATING));
            }
        });
    }
}
