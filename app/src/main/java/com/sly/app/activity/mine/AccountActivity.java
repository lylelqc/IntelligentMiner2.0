package com.sly.app.activity.mine;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.model.UserInfoBean;
import com.sly.app.model.sly.KgFullInfoBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.CityDialog;
import com.sly.app.view.CustomPopwindow;
import com.sly.app.view.dateview.DateDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;
import vip.devkit.library.RelyUtil.IOUtil;
import vip.devkit.view.common.ImgPicker.ImagePicker;
import vip.devkit.view.common.ImgPicker.bean.ImageItem;
import vip.devkit.view.common.ImgPicker.ui.ImageGridActivity;

public class AccountActivity extends BaseActivity {
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView mTvMainRight;
    @BindView(R.id.ll_right)
    LinearLayout mLlRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.touxiang)
    ImageView mTouxiang;
    @BindView(R.id.rl_icon)
    RelativeLayout mRlIcon;
    @BindView(R.id.tv_id)
    TextView mTvId;
    @BindView(R.id.rl_id)
    RelativeLayout mRlId;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.rl_nickname)
    RelativeLayout mRlNickname;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout mRlSex;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.textView7)
    TextView mTextView7;
    @BindView(R.id.rl_mobile)
    RelativeLayout mRlMobile;
    @BindView(R.id.tv_id_card)
    TextView mTvIdCard;
    @BindView(R.id.rl_id_card)
    RelativeLayout mRlIdCard;
    @BindView(R.id.tv_brithDay)
    TextView mTvBrithDay;
    @BindView(R.id.rl_bir)
    RelativeLayout mRlBir;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;
    @BindView(R.id.containe)
    LinearLayout mContaine;
    @BindView(R.id.tv_user_level_1)
    TextView mTvUserLevel1;
    @BindView(R.id.tv_user_level_2)
    TextView mTvUserLevel2;
    private CustomPopwindow customPopwindow;
    private KgFullInfoBean persons;
    private Realm realm;
    private Uri imageUri;
    private String FrSysCode, FMasterCode, Token, User, Key, CusCode;
    String ProvinceCode, CityCode, CountyCode;

    Dialog mDialog;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initData() {
        realm = Realm.getDefaultInstance();
        User = SharedPreferencesUtil.getString(this, "User");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        Key = SharedPreferencesUtil.getString(this, "Key");
        persons = realm.where(KgFullInfoBean.class).equalTo("F18_MinerSysCode", FrSysCode).findFirst();
        Logcat.i("--------------" + persons.toString());
    }

    @Override
    protected void initView() {
        mTvMainTitle.setText("会员账户管理");
//        mTvId.setText(persons.getMemberCode());
//        mTvName.setText(persons.getName());
//        mTvMobile.setText(CommonUtils.proMobile(persons.getPhone()));
//        mTvBrithDay.setText(persons.getBirthDay());
//        mTvAddress.setText(persons.getProvince());
//        if (!CommonUtils.isBlank(persons.getParperNo())) {
//            mTvIdCard.setText(CommonUtils.proIdCard(persons.getParperNo()));
//        }
//        if (!CommonUtils.isBlank(persons.getSex()) && persons.getSex().equals("Girl")) {
//            mTvSex.setText("女");
//        } else if (!CommonUtils.isBlank(persons.getSex()) && persons.getSex().equals("Boy")) {
//            mTvSex.setText("男");
//        } else {
//            mTvSex.setText(persons.getSex());
//        }
//        mTvUserLevel1.setText(persons.getMemberLeveName());
//        mTvUserLevel2.setText(persons.getEmployeeLevel());
//        if (persons.getImageURl() != null) {
//            GlideImgManager.glideLoader(this, persons.getImageURl().replace("9898", "9000"), R.drawable.h_portrait, R.drawable.h_portrait, mTouxiang);
//        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_account_manage;
    }

    @Override
    protected void setListener() {

    }


    private void showChangeNameDialog(final String type) {
        mDialog = new Dialog(mContext, R.style.AppDialog);
        mDialog.setContentView(R.layout.dialog_updat_name);
        mDialog.setCanceledOnTouchOutside(false);
        final EditText mEtName = mDialog.findViewById(R.id.et_dialog_txt);
        ((TextView) mDialog.findViewById(R.id.tv_dialog_title)).setText("修改" + type);
        mEtName.setHint("请输入" + type);
        mDialog.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getStr = mEtName.getText().toString();
                if (CommonUtils.isBlank(getStr)) {
                    ToastUtils.showToast("请输入" + type);
                } else {
                    if (type.equals("性别")) {
                        if (getStr.equals("男") || getStr.equals("女")) {
                            ChangeUserInfo(getStr, type);
                        } else {
                            ToastUtils.showToast("请输入正确的性别");
                        }
                    } else {
                        if (CommonUtils.matcherIdentityCard(getStr) == false) {
                            ToastUtils.showToast("请输入正确的身份证号");
                        } else {
                            ChangeUserInfo(getStr, type);
                        }
                    }
                }
            }
        });
        mDialog.show();

    }


    private void ChangeUserInfo(final String str, final String type) {
        final String MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        final String Token = SharedPreferencesUtil.getString(this, "Token");
        initProgressDialog("修改中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        String netUrl = null;
        if (type.equals("性别")) {
            if (str.equals("男")) {
                map.put("Sex", "Boy");
            } else {
                map.put("Sex", "Girl");
            }
            netUrl = NetWorkConstant.UdapaterSex;
        } else if (type.equals("身份证号")) {
            map.put("PageNo", str);
            netUrl = NetWorkConstant.UdapaterParperNo;
        }
        final String finalNetUrl = netUrl;
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(finalNetUrl, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(finalNetUrl, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                dismissProgressDialog();
                mDialog.dismiss();
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("修改成功");
                    if (type.equals("身份证号")) {
                        mTvIdCard.setText(CommonUtils.proIdCard(str));
                    } else {
                        mTvSex.setText(str);
                    }
                    getUserInfo(mContext, MemberCode, Token);
                } else {
                    ToastUtils.showToast("修改失败");
                }
                return false;
            }
        });


    }

    /**
     * 修改生日
     *
     * @param str
     */
    private void ChangeUserInfo(final String str) {
        final String MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        final String Token = SharedPreferencesUtil.getString(this, "Token");
        initProgressDialog("修改中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("BirthDay", str);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.UdapaterBrithDay, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.UdapaterBrithDay, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                dismissProgressDialog();
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("修改成功");
                    mTvBrithDay.setText(str);
                    getUserInfo(mContext, MemberCode, Token);
                } else {
                    ToastUtils.showToast("修改失败");
                }
                return false;
            }
        });


    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(AccountActivity.this, ImageGridActivity.class);
            customPopwindow.dismiss();
            customPopwindow.backgroundAlpha(AccountActivity.this, 1f);
            switch (v.getId()) {
                case R.id.ll_select01:
                    //相机
                    intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                    startActivityForResult(intent, CAMERA_PICKER);
                    break;
                case R.id.ll_select02:
                    //相册
                    startActivityForResult(intent, PHOTO_PICKER);
                    break;
                case R.id.ll_select03:
                    break;
            }
        }
    };
    private static final int PHOTO_PICKER = 0x000101;
    private static final int CAMERA_PICKER = PHOTO_PICKER + 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            switch (requestCode) {
                case PHOTO_PICKER:
                    if (data != null) {
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        Logcat.i("选择的图片数据：PHOTO_PICKER" + Arrays.toString(images.toArray()));
                        ContentResolver cr = getContentResolver();
                        Bitmap bitmap = null;
                        InputStream in = null;
                        try {
                            in = this.getContentResolver().openInputStream(Uri.fromFile(new File(images.get(0).path)));
                            bitmap = BitmapFactory.decodeStream(in);
                            IOUtil.close(in);
//                            uploadMultiFile(bitmap, ".jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                       /* Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(cr.openInputStream(Uri.fromFile(new File(images.get(0).path))));
                            uploadMultiFile(bitmap, ".jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }*/
                    } else {
                        Toast.makeText(this, "请重新选择", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_PICKER:
                    if (data != null) {
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        Logcat.i("选择的图片数据：CAMERA_PICKER" + Arrays.toString(images.toArray()));
                        ContentResolver cr = this.getContentResolver();
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(cr.openInputStream(Uri.fromFile(new File(images.get(0).path))));
//                            uploadMultiFile(bitmap, ".jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "请重新选择", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /*private void uploadMultiFile(final Bitmap bitmap, String imgType) {
        initProgressDialog("头像上传中...", true);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        final String name = "DQC_" + dateFormat.format(date);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("fileName", name + imgType);
        map.put("file", BitmapUtil.convertIconToString(bitmap));
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GET_IMG, json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                dismissProgressDialog();
                Logcat.e("-----------" + statusCode);
                LogCatW(NetWorkConstant.USER_GET_IMG, "", statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("修改成功");
                    getUserInfo(mContext, MemberCode, Token);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Logcat.e("网络错误：" + e);
                dismissProgressDialog();
            }
        });
        dismissProgressDialog();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getUserInfo(Context mContext, final String memberCode, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", memberCode);
        map.put("Token", token);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.USER_GET_INFO, json, new HttpResponseHandler() {
                    @Override
                    public boolean onSuccess(int statusCode, Headers headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        String backlogJsonStr = content;
                        backlogJsonStr = backlogJsonStr.replace("\\", "");
                        backlogJsonStr = backlogJsonStr.replace("null", "\"null\"");//替换空字符串
                        backlogJsonStr = backlogJsonStr.substring(1, backlogJsonStr.length() - 1);
                        Logcat.e("返回码:" + statusCode + "返回参数:" + backlogJsonStr + "提交的内容：" + json + " headers :" + headers);
                        final ReturnBean returnBean = JSON.parseObject(backlogJsonStr, ReturnBean.class);
                        if (returnBean.getStatus().equals("1")) {
                            UserInfoBean userInfo = realm.where(UserInfoBean.class).equalTo("MemberCode", memberCode).findFirst();
                            if (userInfo != null) {
                                realm.beginTransaction();
                                UserInfoBean persons = realm.where(UserInfoBean.class).equalTo("MemberCode", memberCode).findFirst();
                                UserInfoBean userInfoBean = CommonUtils.GsonToObject(returnBean.getData(), UserInfoBean.class);
                                persons.setDiscount(userInfoBean.getDiscount());
                                persons.setImageURl(userInfoBean.getImageURl());
                                persons.setMemberCode(userInfoBean.getMemberCode());
                                persons.setName(userInfoBean.getName());
                                persons.setPhone(userInfoBean.getPhone());
                                persons.setRegisterType(userInfoBean.getRegisterType());
                                persons.setPruseCode(userInfoBean.getPruseCode());
                                persons.setRegisterTime(userInfoBean.getRegisterTime());
                                persons.setBirthDay(userInfoBean.getBirthDay());
                                persons.setCity(userInfoBean.getCity());
                                persons.setCounty(userInfoBean.getCounty());
                                persons.setProvince(userInfoBean.getProvince());
                                persons.setSex(userInfoBean.getSex());
                                persons.setParperNo(userInfoBean.getParperNo());
                                persons.setEmployeeLevel(userInfoBean.getEmployeeLevel());
                                persons.setMemberLeveName(userInfoBean.getMemberLeveName());
                                realm.commitTransaction();
                            } else {
                                UserInfoBean userInfoBean = CommonUtils.GsonToObject(returnBean.getData(), UserInfoBean.class);
                                realm.copyToRealm(userInfoBean);//插入数据
                            }
                            UserInfoBean mUser = realm.where(UserInfoBean.class).equalTo("MemberCode", memberCode).findFirst();
                            Logcat.e("------------" + mUser.toString());
                            if (!mUser.getImageURl().isEmpty()) {
                                GlideImgManager.glideLoader(AccountActivity.this, mUser.getImageURl().replace("9898", "9000"), R.drawable.h_portrait, R.drawable.h_portrait, mTouxiang);
                            }
                        }
                        return false;
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Logcat.e("网络连接失败:" + e);
                    }
                }
        );
    }


    @OnClick({R.id.btn_main_back, R.id.touxiang, R.id.tv_sex, R.id.rl_id_card, R.id.tv_brithDay, R.id.tv_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.touxiang:
                customPopwindow = new CustomPopwindow(this, itemsOnClick, "1");
                customPopwindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_sex:
                showChangeNameDialog("性别");
                break;
            case R.id.rl_id_card:
                showChangeNameDialog("身份证号");
                break;
            case R.id.tv_brithDay:
                DateDialog dataView = new DateDialog();
                dataView.setNicknameDialog(this, new CityDialog.onInputNameEvent() {
                    @Override
                    public void onClick(String data,String pid,String cid,String aid) {
                        Logcat.i("-----------" + data);
                        ChangeUserInfo(data);
                    }
                }, 0);

                break;
            case R.id.tv_address:
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
                    public void onClick(final String data,String pid,String cid,String aid) {
                        //   tv_address.setText(data);
                        String[] allcity = data.split(" ");
                        ProvinceCode = allcity[0];
                        CityCode = allcity[1];
                        CountyCode = allcity[2];
                        mTvAddress.setText(ProvinceCode + CountyCode + CountyCode);
                    }
                }, a);
                if (!CommonUtils.isBlank(ProvinceCode)) {
                    //  ProUserAddress(ProvinceCode, CityCode, CountyCode);
                }
                break;
        }
    }

    /*private void ProUserAddress(String provinceCode, String cityCode, String countyCode) {
        initProgressDialog("修改中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("MemberCode", MemberCode);
        map.put("Token", Token);
        map.put("provinceCode", provinceCode);
        map.put("cityCode", cityCode);
        map.put("countyCode", countyCode);
        final String json = CommonUtils.GsonToJson(map);
        HttpClient.postJson(NetWorkConstant.COM_GET_LIST, json, new HttpResponseHandler() {
            @Override
            public boolean onSuccess(int statusCode, Headers headers, String content) {
                super.onSuccess(statusCode, headers, content);
                NetLogCat.W(NetWorkConstant.COM_GET_LIST, json, statusCode, content);
                ReturnBean mReturnBean = JSON.parseObject(CommonUtils.proStr(content), ReturnBean.class);
                dismissProgressDialog();
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("修改成功");
                    getUserInfo(mContext, MemberCode, Token);
                } else {
                    ToastUtils.showToast("修改失败");
                }
                return false;
            }
        });

    }*/
}
