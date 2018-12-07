/**
 * ****************************** Copyright (c)*********************************\
 * *
 * *                 (c) Copyright 2017, DevKit.vip, china, qd. sd
 * *                          All Rights Reserved
 * *
 * *                           By(K)
 * *
 * *-----------------------------------版本信息------------------------------------
 * * 版    本: V0.1
 * *
 * *------------------------------------------------------------------------------
 * *******************************End of Head************************************\
 */
package com.sly.app.activity.notice;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.comm.Constants;
import com.sly.app.http.NetWorkConstant;
import com.sly.app.model.MsgBean;
import com.sly.app.utils.BitmapUtil;
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

/**
 * 文 件 名: MsgInfoActivity
 * 创 建 人: By k
 * 创建日期: 2017/12/27 10:45
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class MsgInfoActivity extends BaseActivity {
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
    @BindView(R.id.wv_msg)
    WebView mWvMsg;
    private MsgBean mMsgBean;
    private IWXAPI api;
    private Tencent mTencent;
    private String htmls = null;
    private static final int HTML_FLAG = 1;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initData() {
        mMsgBean = getIntent().getExtras().getParcelable("MsgInfo");
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_msg_info;
    }

    @Override
    protected void initView() {
        mBtnTitleText.setText("新闻公告");
        mTvTitleRight.setText("分享");
        mWvMsg.loadUrl(NetWorkConstant.MSG_INFO + mMsgBean.getID());
        WebSettings webSettings = mWvMsg.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWvMsg.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void setListener() {

    }


    private void ShareDialog() {
        final Dialog dialog = new Dialog(MsgInfoActivity.this, R.style.quick_option_dialog);
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
                dialog.dismiss();
            }
        });
    }

    private void shareToWx(String type) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = NetWorkConstant.MSG_INFO + mMsgBean.getID();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mMsgBean.getTitle();
        msg.description = mMsgBean.getIntro();
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon_app);
        Bitmap shareWx = CodeUtils.createImage(mMsgBean.getLogo().replace("UploadThumbnail40-40","/Upload/Thumbnail/400-400/"), 143, 143, null);
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
        params.putString(QQShare.SHARE_TO_QQ_TITLE, mMsgBean.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mMsgBean.getIntro());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetWorkConstant.MSG_INFO + mMsgBean.getID());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mMsgBean.getLogo().replace("UploadThumbnail40-40","/Upload/Thumbnail/400-400/"));
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mMsgBean.getTitle());
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 123);
        mTencent.shareToQQ(MsgInfoActivity.this, params, null);
    }

    private void shareToQzone() {
        ArrayList<String> path_arr = new ArrayList<>();
        path_arr.add(mMsgBean.getLogo().replace("UploadThumbnail40-40","/Upload/Thumbnail/400-400/"));
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mMsgBean.getTitle());
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mMsgBean.getIntro());
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, NetWorkConstant.MSG_INFO + mMsgBean.getID());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, path_arr);
        mTencent.shareToQzone(MsgInfoActivity.this, params, null);
    }


    @OnClick({R.id.btn_title_back, R.id.ll_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.ll_title_right:
                ShareDialog();
                break;
        }
    }
}
