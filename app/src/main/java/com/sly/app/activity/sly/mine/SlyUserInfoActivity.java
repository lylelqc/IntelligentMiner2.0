package com.sly.app.activity.sly.mine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.http.NetLogCat;
import com.sly.app.http.NetWorkCons;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.model.ReturnBean;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.BitmapUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.CustomPopwindow;
import com.sly.app.view.iviews.ICommonViewUi;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;
import vip.devkit.library.Logcat;
import vip.devkit.library.RelyUtil.IOUtil;
import vip.devkit.view.common.ImgPicker.ImagePicker;
import vip.devkit.view.common.ImgPicker.bean.ImageItem;
import vip.devkit.view.common.ImgPicker.ui.ImageGridActivity;

public class SlyUserInfoActivity extends BaseActivity implements ICommonViewUi {
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
    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.rl_icon)
    RelativeLayout rlIcon;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.rl_id)
    RelativeLayout rlId;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.rl_email)
    RelativeLayout rlEmail;
    private CustomPopwindow customPopwindow;
    private String FrSysCode, FMasterCode, Token, User, Key, UserHeadImgurl, NickName, Name, Phone, Email, LoginType;
    private String img_data;// ImageView的图片，压缩成PNG，并得到二进制流数据
    private String fileName = "";
    private Uri imageUri;
    Dialog mDialog;
    /**
     * 动态添加权限
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1001;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int PHOTO_PICKER = 0x000101;
    private static final int CAMERA_PICKER = PHOTO_PICKER + 1;
    private static int PHOTO_NOW = 0;

    ICommonRequestPresenter iCommonRequestPresenter;


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(SlyUserInfoActivity.this, ImageGridActivity.class);
            customPopwindow.dismiss();
            customPopwindow.backgroundAlpha(SlyUserInfoActivity.this, 1f);
            switch (v.getId()) {
                case R.id.ll_select01:
                    //相机
//                    intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
//                    startActivityForResult(intent, CAMERA_PICKER);
                    verifyStoragePermissions(SlyUserInfoActivity.this, CAMERA_PICKER);
                    PHOTO_NOW = CAMERA_PICKER;
                    break;
                case R.id.ll_select02:
                    //相册
//                    startActivityForResult(intent, PHOTO_PICKER);
                    verifyStoragePermissions(SlyUserInfoActivity.this, PHOTO_PICKER);
                    PHOTO_NOW = PHOTO_PICKER;
                    break;
                case R.id.ll_select03:
                    break;
            }
        }
    };

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
        Intent intent = new Intent();
        if (photoPicked == PHOTO_PICKER) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        } else {
            //相机
            intent = new Intent(SlyUserInfoActivity.this, ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        }
        startActivityForResult(intent, photoPicked);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PHOTO_PICKER) {
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
        }else{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // 权限未被授予
               ToastUtils.showToast( "相机权限未被授予，需要申请！");
                // 相机权限未被授予，需要申请！
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    // 如果访问了，但是没有被授予权限，则需要告诉用户，使用此权限的好处
                    ToastUtils.showToast(  "申请权限说明！");
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("相机需要赋予访问权限，不开启将无法正常工作！")
                            .setPositiveButton("马上授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 这里重新申请权限
                                    ActivityCompat.requestPermissions(SlyUserInfoActivity.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            CAMERA_PICKER);
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
                } else {
                    // 第一次申请，就直接申请
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            CAMERA_PICKER);
                }
            } else {
                ToastUtils.showToast( "相机权限已经被受理，开始预览相机！");
                selectPhoto(PHOTO_NOW);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_PICKER:
                    if (data!=null){
                        imageUri = data.getData();
                    }
                    if (imageUri != null) {
                        Glide.with(SlyUserInfoActivity.this).load(imageUri)
                                    .placeholder(R.drawable.my_photo)
                                    .error(R.drawable.my_photo)
                                    .into(touxiang);
                        Logcat.i("img_Bytes: " + imageUri);
                        InputStream in = null;
                        try {
                            in = this.getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            img_data = BitmapUtil.convertIconToString(bitmap);
                            IOUtil.close(in);
                            // put(img_data, "pcc");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Date date1 = new Date();
                    fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date1) + ".jpg";
                    break;
                case CAMERA_PICKER:
                    if (data!=null){
                        imageUri = data.getData();
                    }
                    if (imageUri != null) {
                        Glide.with(SlyUserInfoActivity.this).load(imageUri)
                                    .placeholder(R.drawable.my_photo)
                                    .error(R.drawable.my_photo)
                                    .into(touxiang);
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        Logcat.i("选择的图片数据：PHOTO_PICKER" + Arrays.toString(images.toArray()));
                        InputStream in = null;
                        try {
                            in = this.getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            img_data = BitmapUtil.convertIconToString(bitmap);
                            IOUtil.close(in);
                            // put(img_data, "pcc");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Date date2 = new Date();
                    fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date2) + ".jpg";
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

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sly_userinfo;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(this, this);
        User = SharedPreferencesUtil.getString(this, "User", "None");
        FrSysCode = SharedPreferencesUtil.getString(this, "FrSysCode", "None");
        FMasterCode = SharedPreferencesUtil.getString(this, "FMasterCode", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        UserHeadImgurl = SharedPreferencesUtil.getString(this, "UserHeadImg");
        NickName = SharedPreferencesUtil.getString(this, "NickName");
        Name = SharedPreferencesUtil.getString(this, "Name");
        Phone = SharedPreferencesUtil.getString(this, "Phone", "None");
        Email = SharedPreferencesUtil.getString(this, "Email", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        initUI();
    }

    public void initUI() {
        tvMainTitle.setText("个人信息");
        tvNickname.setText(NickName);
        tvId.setText(LoginType.equals("Miner") ? FrSysCode : FMasterCode);
        tvPhone.setText(Phone);
        tvEmail.setText(Email);
        GlideImgManager.glideLoader(this, UserHeadImgurl, R.drawable.h_portrait, R.drawable.h_portrait, touxiang);
    }

    @Override
    public void toRequest(int eventTag) {
        if (eventTag == NetWorkCons.EventTags.EDITUSER) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", "Member.010");
            map.put("User", User);
            map.put("sys", LoginType.equals("Miner") ? FrSysCode : FMasterCode);
            map.put("nickName", tvNickname.getText().toString() + "");
            map.put("fileName", fileName+"");
            map.put("file", img_data+"");
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
            iCommonRequestPresenter.request(eventTag, mContext, NetWorkCons.BASE_URL, mapJson);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                        if (returnBean.getStatus().equals("1") && returnBean.getMsg().equals("成功")) {

                        } else {

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


    @OnClick({R.id.btn_main_back, R.id.touxiang, R.id.rl_nickname, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.touxiang:
                customPopwindow = new CustomPopwindow(this, itemsOnClick, "1");
                customPopwindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_nickname:
                showChangeNameDialog("昵称");
                break;
            case R.id.tv_submit:
                toRequest(NetWorkCons.EventTags.EDITUSER);
                break;
        }
    }


    private void showChangeNameDialog(final String type) {
        mDialog = new Dialog(mContext, R.style.AppDialog);
        mDialog.setContentView(R.layout.dialog_updat_name);
        mDialog.setCanceledOnTouchOutside(false);
        final EditText mEtName = (EditText) mDialog.findViewById(R.id.et_dialog_txt);
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
                    tvNickname.setText(getStr);
                    mDialog.dismiss();
//                    if (type.equals("性别")) {
//                        if (getStr.equals("男") || getStr.equals("女")) {
//                            ChangeUserInfo(getStr, type);
//                        } else {
//                            ToastUtils.showToast("请输入正确的性别");
//                        }
//                    } else {
//                        if (CommonUtils.matcherIdentityCard(getStr) == false) {
//                            ToastUtils.showToast("请输入正确的身份证号");
//                        } else {
//                            ChangeUserInfo(getStr, type);
//                        }
//                    }
                }
            }
        });
        mDialog.show();

    }

    private void ChangeUserInfo(final String str, final String type) {
        final String MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        final String Token = SharedPreferencesUtil.getString(this, "Token");
//        initProgressDialog("修改中...", false);
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
//                dismissProgressDialog();
                mDialog.dismiss();
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("修改成功");
                    if (type.equals("身份证号")) {
                    } else {
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
//        initProgressDialog("修改中...", false);
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
//                dismissProgressDialog();
                if (mReturnBean.getStatus().equals("1")) {
                    ToastUtils.showToast("修改成功");
                    getUserInfo(mContext, MemberCode, Token);
                } else {
                    ToastUtils.showToast("修改失败");
                }
                return false;
            }
        });


    }
}
