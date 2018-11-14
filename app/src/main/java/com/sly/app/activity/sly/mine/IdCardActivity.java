package com.sly.app.activity.sly.mine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.adapter.AddImgAdapter;
import com.sly.app.comm.AppConfig;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.sly.ManagerRzStatusBean;
import com.sly.app.model.sly.SlyReturnListBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.BitmapUtil;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import vip.devkit.library.RelyUtil.IOUtil;
import vip.devkit.view.common.ImgPicker.ImagePicker;
import vip.devkit.view.common.ImgPicker.bean.ImageItem;
import vip.devkit.view.common.ImgPicker.ui.ImageGridActivity;
import vip.devkit.view.common.ImgPicker.ui.ImagePreviewDelActivity;

public class IdCardActivity extends BaseActivity {
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
    @BindView(R.id.iv_img_1)
    ImageView ivImg1;
    @BindView(R.id.iv_img_2)
    ImageView ivImg2;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.ll_idcar)
    LinearLayout llIdcar;
    @BindView(R.id.ll_tv1)
    TextView llTv1;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.rl_renzheng)
    LinearLayout rlRenzheng;
    @BindView(R.id.tv_renzheng_status)
    TextView tvRenzhengStatus;
    @BindView(R.id.ll_null_view)
    LinearLayout llNullView;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.goods_description_tv)
    TextView goodsDescriptionTv;
    @BindView(R.id.goods_description_edt)
    EditText goodsDescriptionEdt;
    @BindView(R.id.goods_description_num_tv)
    TextView goodsDescriptionNumTv;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.rl_adress)
    RelativeLayout rlAdress;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_tell)
    EditText etTell;
    @BindView(R.id.ll_machine_msg)
    LinearLayout llMachineMsg;
    @BindView(R.id.tv_friendly_tips)
    TextView tvFriendlyTips;
    private String img_data1, img_data2;// ImageView的图片，压缩成PNG，并得到二进制流数据
    private boolean isSelectPhoto = false;
    private String fileName1 = "";
    private String fileName2 = "";

    /**
     * 动态添加权限
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1001;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PHOTO_PICKED = 0x000101;
    private static final int PHOTO_PICKED2 = 0x000102;
    private static final int PHOTO_PICKED3 = 0x000103;
    private static final int PHOTO_PICKED4 = 0x000104;
    private static final int PHOTO_PICKED5 = 0x000105;
    private static final int PHOTO_PICKED6 = 0x000106;

    private static int PHOTO_NOW = 0;
    private Uri imageUri;
    private String User, FrSysCode, FMasterCode, Token, Key;
    private String LoginType = "None";
    private String mRounter = "";
    private String cardName = "";
    private String cardid = "";
    private int Tag = 02;
    AddImgAdapter mAdapter;
    private int maxImgCount = 4;
    List<ImageItem> mItemList = new ArrayList<>();
    private static final int PHOTO_PICKER = 0x000109;
    private static final int PREVIEW_PICKER = PHOTO_PICKER + 1;
    private String[] picArray = {"", "", "", ""};
    private String[] picNameArray = {"", "", "", ""};
    private String address = "";
    int maxInputNum = 300;
    private String provinceCode;

    WheelView provinceEt;

    WheelView cityEt;

    WheelView areaEt;
    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONArray mJsonObj;

    /**
     * 所有省
     */
    private ArrayList<Map<String, String>> mProvinceDatas;
    private ArrayList<String> mProvinceValues = new ArrayList<>();
    /**
     * key - 省 value - 市s
     */
    private Map<String, ArrayList<Map<String, String>>> mCitisDatasMap = new HashMap<>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, ArrayList<Map<String, String>>> mAreaDatasMap = new HashMap<>();

    private boolean isFirst = true;

    Dialog mBottomSheetDialog;

    private String mDefaultProvinceValue = "", mDefaultCityValue = "", mDefaultAreaValue = "";
    private String mDefaultProvinceIdValue = "", mDefaultCityIdValue = "", mDefaultAreaIdValue = "";

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_id_card;
    }

    @Override
    protected void initViewsAndEvents() {
        User = SharedPreferencesUtil.getString(this, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
        initUI();

        /**01成为矿场主
         * 02实名认证**/
        Tag = getIntent().getIntExtra("tag", 02);
        if (Tag == 01) {
            tvMainTitle.setText("认证成为矿场主");
            llMachineMsg.setVisibility(View.VISIBLE);
        } else {
            tvMainTitle.setText("实名认证");
            llMachineMsg.setVisibility(View.GONE);
        }
//        checkStatus(LoginType, Tag);
        llNullView.setVisibility(View.GONE);
        rlRenzheng.setVisibility(View.VISIBLE);

        //设置温馨提示
        tvFriendlyTips.setText(Html.fromHtml("温馨提示: <br>"
                +"1、身份证照片必须<font color='#FF0000'>无水印和无反光</font>，如有水印和反光将不能通过审核；" + "<br>"
                + "2、请确保身份证尽量充满图片，文字清晰可见，图片需包含<font color='#FF0000'>完整身份证范围</font>。"));

        initJsonData();
        initSelectDialog();
        goodsDescriptionEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = maxInputNum - s.length();
                goodsDescriptionNumTv.setText("还能输入" + number + "字");
//                goodsDescriptionEdt.setSelection(goodsDescriptionEdt.getText().toString().length());//设置光标在最后
            }
        });

        /*goodsDescriptionEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //通知父控件不要干扰
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //通知父控件不要干扰
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });*/
    }

    private void initUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        MyGridItemDecoration decoration = new MyGridItemDecoration(MyGridItemDecoration.HORIZONTAL);
        decoration.setColor(Color.parseColor("#f4f4f4"));
        decoration.setSize(20);
        rvList.addItemDecoration(decoration);
        rvList.setLayoutManager(layoutManager);
        mAdapter = new AddImgAdapter(this, new ArrayList<ImageItem>(), 4);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AddImgAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logcat.e("点击了 1：" + 0 + "/" + position + "/" + "");
                if (position == 999) {
                    AppConfig.getInstance().initImgPicker(maxImgCount - mItemList.size());
                    Intent intent = new Intent(IdCardActivity.this, ImageGridActivity.class);
                    startActivityForResult(intent, PHOTO_PICKER);
                } else {
                    Intent intentPreview = new Intent(IdCardActivity.this, ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) mAdapter.getImages());
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                    startActivityForResult(intentPreview, PREVIEW_PICKER);
                }
            }
        });
    }

    private void checkStatus(final String loginType, final int tag) {
        Map map = new HashMap();

        map.put("Token", Token);
        map.put("LoginType", loginType);
        if (loginType.equals("Miner") && tag == 01) {
            map.put("Rounter", "Miner.025");
            map.put("sys", FrSysCode);
        } else if (loginType.equals("Miner")) {
            map.put("Rounter", "Miner.026");
            map.put("sys", FrSysCode);
        } else {
            map.put("Rounter", "MineMaster.014");
            map.put("mineMasterCode", FMasterCode);
        }
        map.put("User", User);

        Logcat.i("获取信息");
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.putAll(map);
        jsonMap.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(jsonMap);
//        Logcat.i("提交的参数：" + json);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        Logcat.i("API 地址：" + NetWorkCons.BASE_URL + "\n返回码:" + statusCode + "\n返回参数:" + CommonUtils.proStr(content));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(CommonUtils.proStr(content));
                            if (jsonObject.optString("status").equals("1")) {
//                                ToastUtils.showToast(jsonObject.optString("msg"));
                                setRenzhengView(jsonObject.optString("data"), CommonUtils.proStr(content));
                            } else {
                                ToastUtils.showToast(jsonObject.optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );

    }

    private void setRenzhengView(String data, String jsonStr) {
        if (LoginType.equals("Miner") && Tag == 01) {
            switch (Integer.valueOf(data)) {
                case 0:
                    if ("00".equals(data)) {
                        tvRenzhengStatus.setText("认证通过");
                        llNullView.setVisibility(View.VISIBLE);
                        rlRenzheng.setVisibility(View.GONE);
                        ToastUtils.showToast("您已认证成为矿场主，请重新登录");
                        SharedPreferencesUtil.clearString(this, "Discount");
                        SharedPreferencesUtil.putString(this, "Token", null);
                        SharedPreferencesUtil.putString(this, "MemberCode", null);
                        CommonUtil2.goActivity(mContext, LoginActivity.class);
                        finish();
                    } else {
                        llNullView.setVisibility(View.GONE);
                        rlRenzheng.setVisibility(View.VISIBLE);
                    }
                    break;
                case 01:
                    tvRenzhengStatus.setText("认证审核中");
                    llNullView.setVisibility(View.VISIBLE);
                    rlRenzheng.setVisibility(View.GONE);
                    break;
                case 02:
                    tvRenzhengStatus.setText("冻结");
                    llNullView.setVisibility(View.VISIBLE);
                    rlRenzheng.setVisibility(View.GONE);
                    break;
                case 03:
                    tvRenzhengStatus.setText("注销");
                    llNullView.setVisibility(View.VISIBLE);
                    rlRenzheng.setVisibility(View.GONE);
                    break;
            }
        } else if (LoginType.equals("Miner")) {
            SlyReturnListBean slyReturnListBean = JSON.parseObject(jsonStr, SlyReturnListBean.class);
            List<ManagerRzStatusBean> managerRzStatusBeans = JSON.parseArray(slyReturnListBean.getData().getRows().toString(), ManagerRzStatusBean.class);
            String statusCode = "";
            if (managerRzStatusBeans.size() == 0) {
                llNullView.setVisibility(View.GONE);
                rlRenzheng.setVisibility(View.VISIBLE);
            } else {
                statusCode = managerRzStatusBeans.get(0).getMine72_AuditStatusCode();
            }
            if ("Waitting".equals(statusCode.trim())) {
                tvRenzhengStatus.setText("等待审核");
                llNullView.setVisibility(View.VISIBLE);
                rlRenzheng.setVisibility(View.GONE);
            } else if ("Pass".equals(statusCode.trim())) {
                tvRenzhengStatus.setText("审核通过");
                llNullView.setVisibility(View.VISIBLE);
                rlRenzheng.setVisibility(View.GONE);
            } else if ("Back".equals(statusCode.trim()) || "Refuse".equals(statusCode.trim())) {
                llNullView.setVisibility(View.GONE);
                rlRenzheng.setVisibility(View.VISIBLE);
            }
        } else {
            SlyReturnListBean slyReturnListBean = JSON.parseObject(jsonStr, SlyReturnListBean.class);
            List<ManagerRzStatusBean> managerRzStatusBeans = JSON.parseArray(slyReturnListBean.getData().getRows().toString(), ManagerRzStatusBean.class);
            String statusName = "";
            if (managerRzStatusBeans.size() == 0) {
                llNullView.setVisibility(View.GONE);
                rlRenzheng.setVisibility(View.VISIBLE);
            } else {
                statusName = managerRzStatusBeans.get(0).getMine60_AuditStatusName();
            }
            if ("等待审核".equals(statusName)) {
                tvRenzhengStatus.setText("等待审核");
                llNullView.setVisibility(View.VISIBLE);
                rlRenzheng.setVisibility(View.GONE);
            } else if ("审核通过".equals(statusName)) {
                tvRenzhengStatus.setText("审核通过");
                llNullView.setVisibility(View.VISIBLE);
                rlRenzheng.setVisibility(View.GONE);
            } else if ("审核拒绝".equals(statusName) || "发回修改".equals(statusName)) {
                llNullView.setVisibility(View.GONE);
                rlRenzheng.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.iv_img_1, R.id.iv_img_2, R.id.rl_adress, R.id.tv_submit, R.id.btn_main_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.iv_img_1:
                verifyStoragePermissions(IdCardActivity.this, PHOTO_PICKED);
                PHOTO_NOW = PHOTO_PICKED;
                break;
            case R.id.iv_img_2:
                verifyStoragePermissions(IdCardActivity.this, PHOTO_PICKED2);
                PHOTO_NOW = PHOTO_PICKED;
                break;
            case R.id.rl_adress:
                showSelectAddDialog();
                break;
            case R.id.tv_submit:
                if (isSelectPhoto == true) {
                    ivImg1.setDrawingCacheEnabled(true);
                    ivImg2.setDrawingCacheEnabled(true);
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    final String name = dateFormat.format(date);

                    cardName = etName.getText().toString().trim();
                    cardid = etId.getText().toString().trim();

                    if (CommonUtil2.isEmpty(cardName) || CommonUtil2.isEmpty(cardid)) {
                        ToastUtils.showToast("需要姓名和证件号");
                    } else {
                        if (cardid.length() == 15 || cardid.length() == 18) {
//                            if (!CommonUtils.isIdentityCode(cardid)) {
//                                ToastUtils.showToast("15位或18位身份证号码不正确");
//                            } else {
//                                if (cardid.length() == 18 && !CommonUtils.isIdentityCode(cardid)) {
//                                    ToastUtils.showToast("18位身份证号码不符合国家规范");
//                                }
//                            }
                        } else {
                            ToastUtils.showToast("身份证号码长度必须等于15或18位");
                        }
                        if (Tag == 01) {
                            if (NotEmpty()) {
//                            dialog(goodsDescriptionEdt.getText().toString(), address, etNum.getText().toString(), etTell.getText().toString(), ProvinceCode);
                                dialog();
                            }
                        } else {
                            dialog();
                        }

                    }
                } else if (picNameArray.length == 0 || picArray.length == 0) {
                    ToastUtils.showToast("矿场资质/营业执照等相关图片至少要上传一张");
                } else {
                    ToastUtils.showToast("需要上传证书");
                }
                break;
        }
    }

    public boolean NotEmpty() {
        if(mItemList.size() == 0 ){
            ToastUtils.showToast("资质图片不能为空");
            return false;
        }
        if (CommonUtils.isBlank(address)) {
            ToastUtils.showToast("矿场位置不能为空");
            return false;
        } else if (CommonUtils.isBlank(goodsDescriptionEdt.getText().toString())) {
            ToastUtils.showToast("矿场介绍不能为空");
            return false;
        } else if (CommonUtils.isBlank(etNum.getText().toString())) {
            ToastUtils.showToast("机位数量不能为空");
            return false;
        } else if (CommonUtils.isBlank(etTell.getText().toString())) {
            ToastUtils.showToast("联系电话不能为空");
            return false;
        } else if (CommonUtils.isBlank(mItemList.get(0).toString())) {
            ToastUtils.showToast("资质图片不能为空");
            return false;
        }
        return true;
    }

    private void dialog() {
        final Dialog dialog = new Dialog(IdCardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.idcard_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(LoginType.equals("Miner") && Tag == 01){
            TextView tvDetails = dialog.findViewById(R.id.tv_dialog_detail);
            tvDetails.setText("提交审核资料后，1个工作日完成审核，审核通过后，即可发布成为矿场主");
        }
        dialog.show();
        Button cancelButton = dialog.findViewById(R.id.cancel_action);
        cancelButton.setText("取消");
        cancelButton.setTextColor(getResources().getColor(R.color.bot_text_color_norm));
        Button confirmButton = dialog.findViewById(R.id.confirm_action);
        confirmButton.setText("确定");
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                doRenzheng(cardName, cardid, dialog);
            }
        });
    }


    private void doRenzheng(String cardName, String cardid, final Dialog dialog) {
        final Map<String, String> map = new HashMap<>();
        showProgress("数据提交...");
        map.put("Token", Token);
        map.put("LoginType", LoginType);
        if (LoginType.equals("Miner") && Tag == 01) {
//            map.put("Rounter", "Miner.031");
            // dialog(goodsDescriptionEdt.getText().toString(), address, etNum.getText().toString(), etTell.getText().toString(), ProvinceCode);
            //intro	矿场介绍
            //position	矿场位置
            //inker	联系人电话
            //orecount	矿位数量
            //申请成为矿场主
            map.put("Rounter", "Sly.004");
            map.put("mineinfo", goodsDescriptionEdt.getText().toString());
            map.put("provinceCode", provinceCode);
            map.put("mobile", etTell.getText().toString());
            map.put("count", etNum.getText().toString());
            //身份证信息
            map.put("identity", cardid);
            map.put("zmFileName", fileName1);
            map.put("zmFile", img_data1);
            map.put("fmFileName", fileName2);
            map.put("fmFile", img_data2);
            //备注
            map.put("remark", "");

            map.put("fileName1", picNameArray[0]);
            map.put("file1", picArray[0]);

            /*Logcat.e("图片名称" + picNameArray[0] + " - " +  picNameArray[1]
                    + " - " +  picNameArray[2] + " - " +  picNameArray[3]);*/

            if (picArray.length >= 2 && picNameArray.length >= 2) {
                map.put("fileName2", picNameArray[1]);
                map.put("file2", picArray[1]);
            } else {
                map.put("fileName2", "None");
                map.put("file2", "None");
            }

            if (picArray.length >= 3 && picNameArray.length >= 3) {
                map.put("fileName3", picNameArray[2]);
                map.put("file3", picArray[2]);
            } else {
                map.put("fileName3", "None");
                map.put("file3", "None");
            }
            if (picArray.length == 4 && picNameArray.length == 4) {
                map.put("fileName4", picNameArray[3]);
                map.put("file4", picArray[3]);
            } else {
                map.put("fileName4", "None");
                map.put("file4", "None");
            }
        } else if (LoginType.equals("Miner")) {
            map.put("Rounter", "Miner.023");
            map.put("id", cardid);
            map.put("fileName1", fileName1);
            map.put("filePath1", img_data1);
            map.put("fileName2", fileName2);
            map.put("filePath2", img_data2);
        } else {
            map.put("Rounter", "MineMaster.005");
            map.put("id", cardid);
            map.put("fileName1", fileName1);
            map.put("filePath1", img_data1);
            map.put("fileName2", fileName2);
            map.put("filePath2", img_data2);
        }
        map.put("User", User);
        map.put("sys", LoginType.equals("Miner") ? FrSysCode : FMasterCode);
        map.put("name", cardName);

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
                NetLogCat.I("矿场主认证", "renzheng", statusCode, content);
                dimissProgress();
                try {
                    JSONObject jsonObject = new JSONObject(CommonUtils.proStr(content));
                    if (jsonObject.optString("status").equals("1")) {
                        ToastUtils.showToast("提交"+jsonObject.optString("msg"));
                        dialog.dismiss();
                        finish();
                    } else {
                        dialog.dismiss();
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
                dimissProgress();
            }
        });

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
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            switch (requestCode) {
                case PHOTO_PICKER:
                    if (data != null) {
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        mItemList.addAll(images);
                        mAdapter.getImages().clear();
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setImages(mItemList);
                        mAdapter.notifyDataSetChanged();
                        for (int i = 0; i < images.size(); i++) {
                            Bitmap bitmapFormUri = BitmapUtil.Path2Bitmap(images.get(i).path);
                            if (picArray[i] == "") {
                                picArray[i] = BitmapUtil.convertIconToString(bitmapFormUri);
                                picNameArray[i] = images.get(i).name;
//                            picArray[i] = BitmapUtil.Bitmap2Bytes(bitmapFormUri);
                            }else {
                                for(int j = 0; j < picArray.length; j++){
                                    if(picArray[j] == ""){
                                        picArray[j] = BitmapUtil.convertIconToString(bitmapFormUri);
                                        picNameArray[j] = images.get(i).name;
                                        break;
                                    }
                                }
                            }
                        }
//                        Logcat.i("选择的图片数据：PHOTO_PICKER" + mItemList.size() + "/" + Arrays.toString(images.toArray()));
                    } else {
                        ToastUtils.showToast("请重新选择");
                    }
                    break;
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == PREVIEW_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    mItemList.clear();
                    mAdapter.getImages().clear();
                    mAdapter.notifyDataSetChanged();
                    mItemList.addAll(images);
                    mAdapter.setImages(mItemList);
                    mAdapter.notifyDataSetChanged();
                    int i = picArray.length - images.size();
                    if(i > 0 && i < 5){
                        for(int j = 0; j < i; j++){
                            picArray[picArray.length-1-j] = "";
                            picNameArray[picArray.length-1-j] = "";
                        }
                    }
                }
            }
        }


        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_PICKED:
                    if (data != null) {
                        imageUri = data.getData();
                    }
                    if (imageUri != null) {
                        Glide.with(IdCardActivity.this).load(imageUri)
                                .placeholder(R.drawable.my_authentication3)
                                .error(R.drawable.my_authentication3)
                                .into(ivImg1);
//                        Logcat.i("img_Bytes: " + imageUri);
                        InputStream in = null;
                        try {
                            ivImg1.setDrawingCacheEnabled(true);
                            in = this.getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            img_data1 = BitmapUtil.convertIconToString(bitmap);
                            ivImg1.setDrawingCacheEnabled(false);
                            isSelectPhoto = true;
                            IOUtil.close(in);
//                            Logcat.i("img_data : " + img_data1);
                            // put(img_data, "pcc");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Date date1 = new Date();
                    fileName1 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date1) + ".jpg";
                    break;
                case PHOTO_PICKED2:
                    if (data != null) {
                        imageUri = data.getData();
                    }
                    if (imageUri != null) {
                        Glide.with(IdCardActivity.this).load(imageUri)
                                .placeholder(R.drawable.my_authentication2)
                                .error(R.drawable.my_authentication2)
                                .into(ivImg2);
//                        Logcat.i("img_Bytes: " + imageUri);
                        InputStream in = null;
                        try {
                            ivImg2.setDrawingCacheEnabled(true);
                            in = this.getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            img_data2 = BitmapUtil.convertIconToString(bitmap);
                            ivImg2.setDrawingCacheEnabled(false);
                            isSelectPhoto = true;
                            IOUtil.close(in);
//                            Logcat.i("img_data : " + img_data2);
                            // put(img_data, "pcc");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Date date2 = new Date();
                    fileName2 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date2) + ".jpg";
                    break;
            }
        }
    }

    public void showSelectAddDialog() {

        mBottomSheetDialog.show();
    }

    private void initSelectDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_select_address, null);

        provinceEt = view.findViewById(R.id.select_province_et);
        cityEt = view.findViewById(R.id.select_city_et);
        areaEt = view.findViewById(R.id.select_area_et);

        view.findViewById(R.id.select_enter_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultProvinceValue = provinceEt.getSelectedText();
                mDefaultCityValue = cityEt.getSelectedText();
                mDefaultAreaValue = areaEt.getSelectedText();

                tv1.setText("位置：" + mDefaultProvinceValue);
//                ProvinceCode = mDefaultProvinceIdValue;
//                CityCode = mDefaultCityIdValue;
//                CountryCode = mDefaultAreaIdValue;
                address = mDefaultProvinceValue;
                mBottomSheetDialog.dismiss();
            }
        });

        view.findViewById(R.id.select_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        provinceEt.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mHandler.removeMessages(0);
                Message message = new Message();
                message.what = 0;
                message.arg1 = id;
                mHandler.sendMessageDelayed(message, 100);

            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        cityEt.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mHandler.removeMessages(1);
                Message message = new Message();
                message.what = 1;
                message.arg1 = id;
                mHandler.sendMessageDelayed(message, 100);

            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        areaEt.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mHandler.removeMessages(2);
                Message message = new Message();
                message.what = 2;
                message.arg1 = id;
                mHandler.sendMessageDelayed(message, 100);

            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        provinceEt.setData(mProvinceValues);

        provinceEt.setDefault(0);
        Message message = new Message();
        message.arg1 = 0;
        message.what = 0;
        mHandler.sendMessageDelayed(message, 100);


        mBottomSheetDialog = new Dialog(mContext, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                if (!StringUtils.isEmpty(provinceEt.getSelectedText())) {

                    String areaId = mProvinceDatas.get(msg.arg1).get("area_id");
                    provinceCode = areaId;
                    Logcat.e("省份: " + provinceCode);
                    mDefaultProvinceIdValue = areaId;
                    ArrayList<Map<String, String>> list = mCitisDatasMap.get(areaId);
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (list == null || list.size() == 0) {
                        cityEt.resetData(arrayList);
                    } else {
                        for (Map<String, String> map : list) {
                            arrayList.add(map.get("area_name"));
                        }
                        cityEt.resetData(arrayList);
                    }

//                    if (isFirst) {
//                        if (arrayList.contains(mDefaultCityValue)) {
//                            cityEt.setDefault(list.indexOf(mDefaultCityValue));
//                        } else {
//                            cityEt.setDefault(0);
//                        }
//                    }
                    cityEt.setDefault(0);
                }

                if (!CommonUtils.isBlank(cityEt.getSelectedText())) {
                    String areaId = mProvinceDatas.get(msg.arg1).get("area_id");
                    String areaId2 = mCitisDatasMap.get(areaId).get(cityEt.getSelected()).get("area_id");
                    mDefaultCityIdValue = areaId2;

                    ArrayList<Map<String, String>> list = mAreaDatasMap.get(areaId2);
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (list == null || list.size() == 0) {
                        areaEt.resetData(arrayList);
                    } else {
                        for (Map<String, String> map : list) {
                            arrayList.add(map.get("area_name"));
                        }
                        areaEt.resetData(arrayList);
                    }

                    if (isFirst) {

                        if (list.contains(mDefaultAreaValue)) {

                            areaEt.setDefault(list.indexOf(mDefaultAreaValue));
                        } else {
                            areaEt.setDefault(2);
                        }
                    }
                }

                isFirst = false;
            } else if (msg.what == 1) {
                if (!CommonUtils.isBlank(cityEt.getSelectedText())) {

                    String areaId2 = mCitisDatasMap.get(mDefaultProvinceIdValue).get(cityEt.getSelected()).get("area_id");
                    mDefaultCityIdValue = areaId2;
                    ArrayList<Map<String, String>> list = mAreaDatasMap.get(areaId2);
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (list == null || list.size() == 0) {
                        areaEt.resetData(arrayList);
                    } else {
                        for (Map<String, String> map : list) {
                            arrayList.add(map.get("area_name"));
                        }
                        areaEt.resetData(arrayList);
                    }
                    areaEt.setDefault(0);

                    if (isFirst) {

                        if (list.contains(mDefaultAreaValue)) {

                            areaEt.setDefault(list.indexOf(mDefaultAreaValue));
                        } else {
                            areaEt.setDefault(2);
                        }
                    }
                }
            } else if (msg.what == 2) {

                String areaId2 = mCitisDatasMap.get(mDefaultProvinceIdValue).get(cityEt.getSelected()).get("area_id");
                mDefaultCityIdValue = areaId2;
                ArrayList<Map<String, String>> list = mAreaDatasMap.get(areaId2);
                mDefaultAreaIdValue = list.get(msg.arg1).get("area_id");
            }
        }
    };

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();

            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("pcc.json"));

            //文件读入流
            BufferedReader br = new BufferedReader(inputReader);
            //逐行读取test.txt内容，保存在line里
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
            inputReader.close();
            mJsonObj = new JSONArray(sb.toString());
            initDatas();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas() {
        try {

            mProvinceDatas = new ArrayList<>();

            for (int i = 0; i < mJsonObj.length(); i++) {
                Map<String, String> map = new HashMap<>();
                JSONObject jsonP = mJsonObj.getJSONObject(i);// 每个省的json对象
                map.put("area_id", jsonP.getString("ProvinceCode"));
                map.put("area_name", jsonP.getString("ProvinceName"));
                mProvinceValues.add(jsonP.getString("ProvinceName"));
                mProvinceDatas.add(map);
                System.out.println(mJsonObj.length() + "____" + jsonP.getString("ProvinceName"));
                JSONArray jsonCs = null;
                try {
                    jsonCs = jsonP.getJSONArray("City");
                } catch (Exception e1) {
                    continue;
                }
                ArrayList<Map<String, String>> mCitiesDatas = new ArrayList<>();
                for (int j = 0; j < jsonCs.length(); j++) {
                    Map<String, String> cmap = new HashMap<>();
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    cmap.put("area_id", jsonCity.getString("CityCode"));
                    cmap.put("area_name", jsonCity.getString("CityName"));
                    mCitiesDatas.add(cmap);

                    JSONArray jsonAreas = null;
                    try {
                        jsonAreas = jsonCity.getJSONArray("Area");
                    } catch (Exception e) {
                        continue;
                    }

                    ArrayList<Map<String, String>> mAreasDatas = new ArrayList<>();// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        Map<String, String> amap = new HashMap<>();
                        JSONObject jsonArea = jsonAreas.getJSONObject(k);
                        amap.put("area_id", jsonArea.getString("AreaCode"));
                        amap.put("area_name", jsonArea.getString("AreaName"));
                        mAreasDatas.add(amap);
                    }
                    mAreaDatasMap.put(jsonCity.getString("CityCode"), mAreasDatas);
                }

                mCitisDatasMap.put(jsonP.getString("ProvinceCode"), mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }


}
