package com.sly.app.activity.qrc;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.comm.Constants;
import com.sly.app.model.UserInfoBean;
import com.sly.app.utils.BitmapUtil;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * 作者 by K
 * 时间：on 2017/9/11 14:06
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ShareActivity extends BaseActivity {
    public Bitmap mBitmap = null;
    @BindView(R.id.btn_title_back)
    LinearLayout mBtnTitleBack;
    @BindView(R.id.btn_title_text)
    TextView mBtnTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout mLlTitleRight;
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    @BindView(R.id.iv_share_hp)
    ImageView mIvShareHp;
    @BindView(R.id.tv_share_record)
    TextView mTvRecord;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.iv_share_code)
    ImageView mIvShareCode;
    private String MemberCode, Token;
    private String ShareCode, ImageURl, NickName, MemberLeveName;
    private UserInfoBean persons;
    private Bitmap bitmap = null;
    private String bitmapURL = null;
    private Realm realm = Realm.getDefaultInstance();
    private IWXAPI api;
    private Tencent mTencent;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initData() {
        MemberCode = SharedPreferencesUtil.getString(this, "MemberCode");
        Token = SharedPreferencesUtil.getString(this, "Token");
        persons = realm.where(UserInfoBean.class).equalTo("MemberCode", MemberCode).findFirst();
        if (CommonUtils.isBlank(persons.getImageURl())) {
            ImageURl = "null";
        } else {
            ImageURl = persons.getImageURl().replace("9898", "9000");
        }
        NickName = persons.getName();
        if (CommonUtils.isBlank(persons.getMemberLeveName())) {
            MemberLeveName = "null";
        } else {
            MemberLeveName = persons.getMemberLeveName();
        }
    }

    @Override
    protected void initView() {
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this);
        mBtnTitleText.setText("邀请好友");
        mTvTitleRight.setText("分享");
        mTvTitleRight.setTextColor(Color.parseColor("#ffffff"));
        ShareCode = Constants.SHARE_REGISTER_URL + MemberCode + "&" + MemberCode + "&" + ImageURl + "&" + NickName + "&" + MemberLeveName + "&DQC";
        if (CommonUtils.isBlank(persons.getName())) {
            mTvName.setText(CommonUtils.replaceStr(3, 7, "****", MemberCode));
        } else {
            mTvName.setText(persons.getName());
        }
        mBitmap = CodeUtils.createImage(ShareCode, 300, 300, null);
        mIvShareCode.setImageBitmap(mBitmap);
        GlideImgManager.glideLoader(ShareActivity.this, ImageURl, R.drawable.h_portrait, R.drawable.h_portrait, mIvShareHp);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void setListener() {
    }


    @OnClick({R.id.btn_title_back, R.id.ll_title_right, R.id.tv_share_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.ll_title_right:
                ShareDialog();
                break;
            case R.id.tv_share_record:
                startActivityWithoutExtras(ShareRecordActivity.class);
                break;
        }
    }

    private void ShareDialog() {
        final Dialog dialog = new Dialog(ShareActivity.this, R.style.quick_option_dialog);
        dialog.setContentView(R.layout.dialog_share);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setWindowAnimations((R.style.popWindow_anim_style));
        dialog.show();
        LinearLayout shareToWx = (LinearLayout) dialog.findViewById(R.id.iv_wx);
        LinearLayout shareToWx2 = (LinearLayout) dialog.findViewById(R.id.iv_wx2);
        LinearLayout shareToQQ = (LinearLayout) dialog.findViewById(R.id.iv_qq1);
        LinearLayout shareToQzone = (LinearLayout) dialog.findViewById(R.id.iv_qq2);
        TextView tv_cance_share = (TextView) dialog.findViewById(R.id.tv_cance_share);
        shareToWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWx("WxA");
            }
        });
        shareToWx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWx("WxB");
            }
        });
        shareToQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToQQ();
            }
        });
        shareToQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToQzone();

            }
        });
        tv_cance_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new CountDownTimer() 倒计时
                dialog.dismiss();
            }
        });
    }

    private void shareToWx(String type) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constants.SHARE_REGISTER_URL + SharedPreferencesUtil.getString(this, "MemberCode");
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "美好地球村请好友 赢好礼";
        msg.description = "美好地球村邀请好友注册,享受邀请优厚待遇";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon_app);
        Bitmap shareWx = CodeUtils.createImage("http://h.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=e6a949502a3fb80e0c8469d004fd181c/342ac65c103853431cb2c2409813b07eca808856.jpg", 143, 143, null);
//    Bitmap  mBitmap = BitmapUtil.createWaterMaskBitmap(shareWx,thumb,0,100);//*[@id="doc"]/div[2]/div/div[1]/div/div[1]/div/img
        msg.thumbData = BitmapUtil.Bitmap2Bytes(shareWx);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        //WXSceneTimeline 微信朋友圈   WXSceneSession 微信聊天界面/给朋友  WXSceneFavorite 微信收藏
        if (type.equals("WxA")) {
            req.scene = req.WXSceneSession;
        } else if (type.equals("WxB")) {
            req.scene = req.WXSceneTimeline;
        }
        api.sendReq(req);
    }

    private void shareToQQ() {
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon_app);
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "美好地球村邀请好友注册 享好礼");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "美好地球村邀请好友注册,享受邀请优厚待遇");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constants.SHARE_REGISTER_URL + SharedPreferencesUtil.getString(this, "MemberCode"));
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://h.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=e6a949502a3fb80e0c8469d004fd181c/342ac65c103853431cb2c2409813b07eca808856.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "美好地球村邀请好友注册");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 123);
        mTencent.shareToQQ(ShareActivity.this, params, null);
    }

    private void shareToQzone() {
        ArrayList<String> path_arr = new ArrayList<>();
        path_arr.add("http://h.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=e6a949502a3fb80e0c8469d004fd181c/342ac65c103853431cb2c2409813b07eca808856.jpg");
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "美好地球村邀请好友注册 享好礼");
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "美好地球村邀请好友注册,享受邀请优厚待遇");
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, Constants.SHARE_REGISTER_URL + SharedPreferencesUtil.getString(this, "MemberCode"));
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, path_arr);
        mTencent.shareToQzone(ShareActivity.this, params, null);
    }


}
