package com.sly.app.activity.sly.mine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.BitmapUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;

import static com.sly.app.utils.AppLog.LogCatW;

public class SlyRechargeActivity extends BaseActivity {
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
    @BindView(R.id.tv_card_num)
    TextView tvCardNum;
    @BindView(R.id.et_recharge_sum)
    EditText etRechargeSum;
    @BindView(R.id.iv_recharge_pz)
    ImageView ivRechargePz;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.tv_card_bank_name)
    TextView tvCardBankName;


    private String img_data1;// ImageView的图片，压缩成PNG，并得到二进制流数据
    private boolean isSelectPhoto = false;
    private String fileName1 = "";

    /**
     * 动态添加权限
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1001;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PHOTO_PICKED = 0x000101;
    private static int PHOTO_NOW = 0;
    private Uri imageUri;
//    private String User, SysCode, Token, Key;
    private String User, FrSysCode, FMasterCode, Token, Key;
    private String LoginType = "None";

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.acctivity_sly_recharge;
    }

    @Override
    protected void initView() {
        tvMainTitle.setText("充值");
        User = SharedPreferencesUtil.getString(this, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode","None");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode","None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
//        initRechargeBankCode();
        TextChange textChange = new TextChange();
        etRechargeSum.addTextChangedListener(textChange);
    }

    private void initRechargeBankCode() {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Member.007");
        map.put("User", User);
        map.put("sys", "sly");

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("status").equals("1")) {
                        SlyReturnListBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), SlyReturnListBean.class);
                        if (mReturnBean.getData().getRows().size() != 0) {
                            String data = mReturnBean.getData().getRows().get(0).toString();
//                            WithdrawalsInfoBean withdrawDataBean = JSON.parseObject(data, WithdrawalsInfoBean.class);
//                            tvCardNum.setText(withdrawDataBean.getPay26_AccountNo());
//                            tvCardName.setText(withdrawDataBean.getPay26_AccountName());
//                            tvCardBankName.setText(withdrawDataBean.getPay26_Branch());
                        }
                    } else {
                        ToastUtils.showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dismissProgressDialog();
            }
        });
    }

    @OnClick({R.id.btn_main_back, R.id.iv_recharge_pz, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.btn_submit:
                if (isSelectPhoto == true) {
                    ivRechargePz.setDrawingCacheEnabled(true);
                    String rechargeSum = etRechargeSum.getText().toString();
                    if (Integer.valueOf(rechargeSum) <= 0) {
                        ToastUtils.showToast("请检查您的充值金额");
                    } else {
                        toRecharge(rechargeSum);
                    }
                } else {
                    ToastUtils.showToast("需要上传付款凭证");
                }
                break;
            case R.id.iv_recharge_pz:
                verifyStoragePermissions(SlyRechargeActivity.this, PHOTO_PICKED);
                PHOTO_NOW = PHOTO_PICKED;
                break;
//            case R.id.ll_right:
//                CommonUtil2.goActivity(mContext,SlyRechargeRecordActivity.class);
//                break;
        }
    }

    private void toRecharge(String rechargeSum) {
        final Map<String, String> map = new HashMap<>();
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        if (LoginType.equals("Miner")) {
            map.put("Rounter", "Member.009");
            map.put("sys", FrSysCode);
        } else {
            map.put("Rounter", "Member.009");
            map.put("sys", FMasterCode);
        }
        map.put("User", User);
        map.put("sum", rechargeSum);
        map.put("fileName", fileName1);
        map.put("file", img_data1);
        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));

        Gson gson = new Gson();
        final String json = gson.toJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
//                NetLogCat.I("充值", json, statusCode, content);
                try {
                    JSONObject jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("status").equals("1")) {
                        finish();
                    } else {
                        ToastUtils.showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        });

    }

    @Override
    protected void setListener() {

    }

    //EditText的监听器
    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etRechargeSum.getText().toString().isEmpty()) {
                btnSubmit.setBackgroundResource(R.drawable.bg_login_btn_b);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setClickable(false);
                return;
            }
            if (etRechargeSum.length() > 0) {
                btnSubmit.setBackgroundResource(R.drawable.bg_login_btn_a);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setClickable(true);
            }
        }

    }

    public void verifyStoragePermissions(Activity activity, int photoPicked) {
        int permission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        } else {
            selectPhoto(photoPicked);
        }

    }

    /**
     * 选择图片
     *
     * @param
     * @param photoPicked
     */
    public void selectPhoto(int photoPicked) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, photoPicked);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1001) {
            if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                //用户同意使用write
                selectPhoto(PHOTO_NOW);
            } else {
                //用户不同意，自行处理即可
                ToastUtils.showToast("请开启相应权限");
                //用户不同意，向用户展示该权限作用
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("相册需要赋予访问存储的权限，不开启将无法正常工作！")
                            .setPositiveButton("马上授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("狠心拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                    return;
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_PICKED:
                    imageUri = data.getData();
                    if (imageUri != null) {
                        Glide.with(SlyRechargeActivity.this).load(imageUri)
                                .placeholder(R.drawable.my_authentication3)
                                .error(R.drawable.my_authentication3)
                                .into(ivRechargePz);
//                        Logcat.i("img_Bytes: " + imageUri);
                        try {
                            ivRechargePz.setDrawingCacheEnabled(true);
                            ContentResolver cr = this.getContentResolver();
                            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageUri));
                            img_data1 = BitmapUtil.convertIconToString(bitmap);
                            ivRechargePz.setDrawingCacheEnabled(false);
                            isSelectPhoto = true;
                            // put(img_data, "pcc");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Date date1 = new Date();
                    fileName1 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date1) + ".jpg";
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
