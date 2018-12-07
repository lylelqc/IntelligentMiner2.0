package com.sly.app.activity.machineSeat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.activity.sly.mine.IdCardActivity;
import com.sly.app.adapter.AddImgAdapter;
import com.sly.app.base.BaseActivity;
import com.sly.app.comm.AppConfig;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.BitmapUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringUtils;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.CityDialog;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import vip.devkit.library.Logcat;
import vip.devkit.view.common.ImgPicker.ImagePicker;
import vip.devkit.view.common.ImgPicker.bean.ImageItem;
import vip.devkit.view.common.ImgPicker.ui.ImageGridActivity;
import vip.devkit.view.common.ImgPicker.ui.ImagePreviewDelActivity;

public class ReleaseOreActivity extends BaseActivity {

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
    @BindView(R.id.et_begin_price)
    EditText etBeginPrice;
    @BindView(R.id.et_end_price)
    EditText etEndPrice;
    @BindView(R.id.et_manager_fee)
    EditText etManagerFee;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.et_tell)
    EditText etTell;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private String User, FrSysCode, FMasterCode, Token, Key;
    private String LoginType = "None";
    private String mRounter = "";
    List<ImageItem> mItemList = new ArrayList<>();
    private String[] picArray = {"", "", "", ""};
    private String[] picNameArray = {"", "", "", ""};
    AddImgAdapter mAdapter;
    private int maxImgCount = 4;
    private String Province, City, County;
    private String ProvinceCode;
    private String CityCode;
    private String CountryCode;
    private String address = "";
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

    int maxInputNum = 300;
    private boolean isRz = false;

    @Override
    protected void initData() {

        User = SharedPreferencesUtil.getString(this, "User");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key");
        LoginType = SharedPreferencesUtil.getString(this, "LoginType", "None");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_release_ore;
    }

    @Override
    protected void initView() {
        tvMainTitle.setText("发布机位");
        AppConfig.getInstance().initImgPicker(4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        MyGridItemDecoration decoration = new MyGridItemDecoration(MyGridItemDecoration.HORIZONTAL);
        decoration.setColor(Color.parseColor("#f4f4f4"));
        decoration.setSize(20);
        rvList.addItemDecoration(decoration);
        rvList.setLayoutManager(layoutManager);
        mAdapter = new AddImgAdapter(this, new ArrayList<ImageItem>(), 4);
        rvList.setAdapter(mAdapter);
        initProgressDialog("获取信息，请稍候...", false);
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

//        goodsDescriptionEdt.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    //通知父控件不要干扰
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    //通知父控件不要干扰
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    v.getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                return false;
//            }
//        });

        /*etBeginPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                EditText et=(EditText)v;
                if (!hasFocus) {// 失去焦点
//                    et.setHint(et.getTag().toString());
                } else {
//                    String hint=et.getHint().toString();
//                    et.setTag(hint);//保存预设字
//                    et.setHint(null);
                    etEndPrice.setGravity(Gravity.CENTER);
                }
            }
        });

        etEndPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        EditText et=(EditText)v;
                        if (!hasFocus) {// 失去焦点
//                            et.setHint(et.getTag().toString());
                        } else {
//                            String hint=et.getHint().toString();
//                            et.setTag(hint);//保存预设字
//                            et.setHint(null);
                            etEndPrice.setGravity(Gravity.CENTER);
                        }
                    }
                });*/
    }

    @Override
    protected void setListener() {
        mAdapter.setOnItemClickListener(new AddImgAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logcat.e("点击了 1：" + 0 + "/" + position + "/" + "");
                if (position == 999) {
                    AppConfig.getInstance().initImgPicker(maxImgCount - mItemList.size());
                    Intent intent = new Intent(ReleaseOreActivity.this, ImageGridActivity.class);
                    startActivityForResult(intent, PHOTO_PICKER);
                } else {
                    Intent intentPreview = new Intent(ReleaseOreActivity.this, ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) mAdapter.getImages());
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                    startActivityForResult(intentPreview, PREVIEW_PICKER);
                }
            }
        });

        toRzIdStatus();
    }


    private static final int PHOTO_PICKER = 0x000101;
    private static final int PREVIEW_PICKER = PHOTO_PICKER + 1;

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
//                            picArray[i] = BitmapUtil.convertIconToString(bitmapFormUri);
//                            picNameArray[i] = images.get(i).name;
//                            picArray[i] = BitmapUtil.Bitmap2Bytes(bitmapFormUri);
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
    }


    @OnClick({R.id.btn_main_back, R.id.rl_adress, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_adress:
                showSelectAddDialog();
//                setRegoster();
                break;
            case R.id.btn_submit:
                //            picArray[0]==null?new byte[]{}:picArray[0]
//                            , picArray[1]==null?new byte[]{}:picArray[1]
//                            , picArray[2]==null?new byte[]{}:picArray[2]
//                            , picArray[3]==null?new byte[]{}:picArray[3]

                if (NotEmpty()) {
                    if(isRz){
                        dialog();
                    }else{
                        ToastUtils.showToast("请先实名认证，再发布机位!");
                    }
                }
                break;
        }
    }

    private void dialog() {
        final Dialog dialog = new Dialog(ReleaseOreActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.release_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_action);
        cancelButton.setText("取消");
        cancelButton.setTextColor(getResources().getColor(R.color.bot_text_color_norm));
        Button confirmButton = (Button) dialog.findViewById(R.id.confirm_action);
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
                doSubmit(goodsDescriptionEdt.getText().toString(), address, etNum.getText().toString(),
                        etBeginPrice.getText().toString(), etEndPrice.getText().toString(), etManagerFee.getText().toString(), etTell.getText().toString(),
                        picArray[0], picArray[1], picArray[2], picArray[3]
                        , ProvinceCode, CityCode, CountryCode, picNameArray[0], picNameArray[1], picNameArray[2], picNameArray[3]);
            }
        });
    }

    public void showSelectAddDialog() {

        mBottomSheetDialog.show();
    }

    private void setRegoster() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken()
                    , InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
        WindowManager wm = this.getWindowManager();
        int a = wm.getDefaultDisplay().getWidth();
        CityDialog cityDialog = CityDialog.getInstance();
        cityDialog.setNicknameDialog(this, new CityDialog.onInputNameEvent() {
            @Override
            public void onClick(final String data, String pid, String cid, String aid) {
                //   tv_address.setText(data);
                String[] allcity = data.split(" ");
                Province = allcity[0];
                City = allcity[1];
                County = allcity[2];
                address = Province + City + County;
                tv1.setText("位置： " + Province);
                ProvinceCode = pid;
                CityCode = cid;
                CountryCode = aid;
            }
        }, a);
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
        }
        else if (CommonUtils.isBlank(etEndPrice.getText().toString())
                && CommonUtils.isBlank(etBeginPrice.getText().toString())) {
            ToastUtils.showToast("服务单价不能为空");
            return false;
        }
        else if (CommonUtils.isBlank(etManagerFee.getText().toString())) {
            ToastUtils.showToast("管理费不能为空");
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

    private void doSubmit(String intro, String position, String capacity, String minPowerPrice, String maxPowerPrice, String manageFee, String linker,
                          String file1, String file2, String file3, String file4, String province, String city, String country
            , String fileName1, String fileName2, String fileName3, String fileName4
    ) {
        initProgressDialog("正在发布", false);
        Map map = new HashMap();
        if (LoginType.equals("MinerMaster")) {
            map.put("sysCode", FMasterCode);
        } else {
            map.put("sysCode", FrSysCode);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Sly.020");
        map.put("User", User);
        /**
         * intro	是	string	矿场介绍
         position	是	string	矿场位置
         capacity	是	int	容量
         minPowerPrice	是	decimal	电费单价（起）
         maxPowerPrice	是	decimal	电费单价（止）
         manageFee	是	decimal	管理费用
         linker	是	string	联系方式
         file1	是	string	资质文件1
         file2	是	string	资质文件2
         file3	是	string	资质文件3
         file4	是	string	资质文件4
         province	是	string	省份
         city	是	string	城市
         country	是	string	城区县
         */
        map.put("intro", intro);
        map.put("position", position);
        map.put("capacity", capacity);
        map.put("minPowerPrice", minPowerPrice);
        map.put("maxPowerPrice", maxPowerPrice);
        map.put("manageFee", manageFee);
        map.put("linker", linker);
        map.put("file1", file1);
        map.put("file2", file2);
        map.put("file3", file3);
        map.put("file4", file4);
        map.put("fileName1", fileName1);
        map.put("fileName2", fileName2);
        map.put("fileName3", fileName3);
        map.put("fileName4", fileName4);
        map.put("province", province);
        map.put("city", CommonUtils.isBlank(city) ? "None" : city);
        map.put("country", CommonUtils.isBlank(country) ? "None" : country);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                try {
                    dismissProgressDialog();
//                    LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                    ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                    ToastUtils.showToast("提交" + mReturnBean.getMsg());
                    if (mReturnBean.getMsg().equals("成功")) {
                        finish();
                    }
                } catch (Exception e) {
                    Logcat.e("Exception", e.toString());
                }
                dismissProgressDialog();
            }
        });

    }

    private void toRzIdStatus(){
        Map map = new HashMap();
        if (LoginType.equals("MinerMaster")) {
            map.put("sysCode", FMasterCode);
        } else {
            map.put("sysCode", FrSysCode);
        }

        map.put("Token", Token);
        map.put("LoginType", LoginType);
        map.put("Rounter", "Sly.031");
        map.put("User", User);

        Map<String, String> mapJson = new HashMap<>();
        mapJson.putAll(map);
        mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(this).getSign(map, Key)));
        final String json = CommonUtils.GsonToJson(mapJson);
        HttpClient.postJson(NetWorkCons.BASE_URL, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                try {
//                    LogCatW(NetWorkCons.BASE_URL, json, statusCode, content);
                    ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                    if (mReturnBean.getStatus().equals("1")) {
                        if(mReturnBean.getData().equals("false")){
                            isRz = false;
                        }else{
                            isRz = true;
                        }
                    }
                } catch (Exception e) {
                    Logcat.e("Exception", e.toString());
                }
                dismissProgressDialog();
            }
        });
    }

    private void initSelectDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_select_address, null);

        provinceEt = (WheelView) view.findViewById(R.id.select_province_et);
        cityEt = (WheelView) view.findViewById(R.id.select_city_et);
        areaEt = (WheelView) view.findViewById(R.id.select_area_et);

        view.findViewById(R.id.select_enter_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultProvinceValue = provinceEt.getSelectedText();
                mDefaultCityValue = cityEt.getSelectedText();
                mDefaultAreaValue = areaEt.getSelectedText();

                tv1.setText("位置：" + mDefaultProvinceValue);
                ProvinceCode = mDefaultProvinceIdValue;
                CityCode = mDefaultCityIdValue;
                CountryCode = mDefaultAreaIdValue;
                address = mDefaultProvinceValue;
//                getMachineSeatData(SysNumber, Key, Token, LoginType, User, mDefaultProvinceIdValue, mDefaultCityIdValue, mDefaultAreaIdValue, pageSize, pageNo);
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
        dismissProgressDialog();
    }

}

