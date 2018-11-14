package com.sly.app.activity.sly.mine;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.sly.app.R;
import com.sly.app.activity.BaseActivity;
import com.sly.app.comm.EventBusTags;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.PostResult;
import com.sly.app.presenter.ICommonRequestPresenter;
import com.sly.app.presenter.impl.CommonRequestPresenterImpl;
import com.sly.app.utils.ApiSIgnUtil;
import com.sly.app.utils.BitmapUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.EncryptUtil;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.LoginMsgHelper;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringHelper;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.PhotoDialog;
import com.sly.app.view.iviews.ICommonViewUi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/6/21.
 */

public class UserInfoEditActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener, ICommonViewUi {

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
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private String SysCode, Token, User, Key, CusCode, UserHeadImgurl, NickName, Name, Phone, Email, LoginType;
    private String img_data;// ImageView的图片，压缩成PNG，并得到二进制流数据
    private String fileName = "";
    int choosePhoto = 0; //0 个人头像 1背景
    InvokeParam invokeParam;
    TakePhoto takePhoto;
    PhotoDialog photoDialog;


    ICommonRequestPresenter iCommonRequestPresenter;
    List<String> sexAarray = new ArrayList<>();

    String userImgPath = "";
    String ALYImgPayh = "";

    boolean isChange = false;
    String bgPath = "";

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sly_userinfo;
    }

    @Override
    protected void initViewsAndEvents() {
        User = SharedPreferencesUtil.getString(this, "User", "None");
        LoginType = SharedPreferencesUtil.getString(mContext, "LoginType", "None");
        SysCode = SharedPreferencesUtil.getString(this, LoginType.equals("Miner") ? "FrSysCode": "FMasterCode", "None");
        CusCode = SharedPreferencesUtil.getString(this, "CusCode", "None");
        Token = SharedPreferencesUtil.getString(this, "Token", "None");
        Key = SharedPreferencesUtil.getString(this, "Key", "None");
        UserHeadImgurl = SharedPreferencesUtil.getString(this, "UserHeadImg");
        NickName = SharedPreferencesUtil.getString(this, "NickName");
        Name = SharedPreferencesUtil.getString(this, "Name");
        Phone = SharedPreferencesUtil.getString(this, "Phone", "None");
        Email = SharedPreferencesUtil.getString(this, "Email", "None");
        iCommonRequestPresenter = new CommonRequestPresenterImpl(this, this);
        initUI();
    }

    @OnClick(R.id.touxiang)
    public void img() {
        choosePhoto = 0;
        photoDialog = new PhotoDialog(mContext, takePhoto, false);
        photoDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void takeSuccess(TResult result) {
        if (choosePhoto == 0) {
            userImgPath = result.getImage().getOriginalPath();
            GlideImgManager.glideLoader(this, result.getImage().getOriginalPath(), R.drawable.my_photo_no, R.drawable.my_photo_no, touxiang);


            showImg(userImgPath);

            Date date1 = new Date();
            fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date1) + ".jpg";
        }


    }

    private void showImg(String imgUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(mContext).load(userImgPath).asBitmap()
                            .centerCrop()
                            .into(500, 500)
                            .get();
                    img_data = BitmapUtil.convertIconToString(bitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        showToastShort("图片失败-->" + msg);
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void toRequest(int eventTag) {
        if (eventTag == NetWorkCons.EventTags.EDITUSER) {
            Map map = new HashMap();
            map.put("Token", Token);
            map.put("LoginType", LoginType);
            map.put("Rounter", "Member.010");
            map.put("User", User);
            map.put("sys", SysCode);
            map.put("nickName", tvNickname.getText().toString() + "");
            map.put("fileName", fileName + "");
            map.put("file", img_data + "");
            Map<String, String> mapJson = new HashMap<>();
            mapJson.putAll(map);
            mapJson.put("Sign", EncryptUtil.MD5(ApiSIgnUtil.init(mContext).getSign(map, Key)));
            iCommonRequestPresenter.request(NetWorkCons.EventTags.EDITUSER, mContext, NetWorkCons.BASE_URL, mapJson);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if (NetWorkCons.EventTags.EDITUSER == eventTag) {
//
//            LoginMsg loginMsg = LoginMsgHelper.getResult(mContext);
//
//            if(!StringHelper.isEmpty(userNameEd.getText().toString())){
//                loginMsg.setNick(userNameEd.getText().toString());
//            }
//            if(!StringHelper.isEmpty(userSexEd.getText().toString())){
//                loginMsg.setSex(userSexEd.getText().toString());
//            }
//            if(!StringHelper.isEmpty(userDataEd.getText().toString())){
//                loginMsg.setBirth(userDataEd.getText().toString());
//            }
//            if(!StringHelper.isEmpty(userDetailEd.getText().toString())){
//                loginMsg.setSign(userDetailEd.getText().toString());
//            }
//            if(!StringHelper.isEmpty(ALYImgPayh)){
//                loginMsg.setAvatar(ALYImgPayh);
//                ALYImgPayh = null;
//            }
//
//            PreferenceUtils.setPrefString(mContext, Contants.Preference.loginMsg, JsonHelper.getJson(loginMsg));
            EventBus.getDefault().post(new PostResult(EventBusTags.LOGIN));
            dimissProgress();
//            String s = JsonHelper.getJson(loginMsg);
//            System.out.println("转换json "+s);
//            PreferenceUtils.setPrefString(mContext, Contants.Preference.USER_BG,bgPath);
            new MaterialDialog.Builder(this)
                    .content("个人资料修改成功")
                    .positiveText("确定")
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        dimissProgress();
        showToastShort(msg);
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
        dimissProgress();
        showToastShort(msg);
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        if (status)
            showProgress("数据提交...");
        else if (NetWorkCons.EventTags.EDITUSER != eventTag) {
            dimissProgress();
        }
    }

    public void initUI() {
        tvMainTitle.setText("个人信息");
        tvNickname.setText(NickName);
        tvId.setText(SysCode);
        tvPhone.setText(Phone);
        tvEmail.setText(Email);
        GlideImgManager.glideLoader(this, NetWorkCons.IMG_URL+UserHeadImgurl, R.drawable.my_photo_no, R.drawable.my_photo_no, touxiang);
        if (LoginMsgHelper.isLogin(mContext)) {
//            LoginMsg loginMsg = LoginMsgHelper.getResult(mContext);
//            if(!StringHelper.isEmpty(loginMsg.getAvatar())){
//                Glide.with(mContext).load(loginMsg.getAvatar()+"?x-oss-process=image/resize,m_mfit,h_100,w_100").
//                        placeholder(R.drawable.user_moren).
//                        error(R.drawable.user_moren).
//                        bitmapTransform(new CropCircleTransformation(mContext)).
//                        crossFade(1000).into(userImg);
        }

    }

    @OnClick({R.id.btn_main_back, R.id.rl_nickname, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.rl_nickname:
                showChangeNameDialog("昵称");
                break;
            case R.id.tv_submit:
                if (!StringHelper.isEmpty(userImgPath) || !StringHelper.isNoAllEmpty(tvNickname)) {
                    toRequest(NetWorkCons.EventTags.EDITUSER);
                } else {
                    showToastShort("请修改个人资料");
                }
                break;
        }
    }

    Dialog mDialog;

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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(new PostResult(EventBusTags.LOGIN));
        super.onDestroy();
    }
}
