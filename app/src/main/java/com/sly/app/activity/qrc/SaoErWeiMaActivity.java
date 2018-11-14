package com.sly.app.activity.qrc;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.activity.mine.RegisterActivity;
import com.sly.app.base.BaseActivity;
import com.sly.app.utils.CommonUtils;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import vip.devkit.library.Logcat;

/**
 * 作者 by K
 * 时间：on 2017/9/4 14:12
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class SaoErWeiMaActivity extends BaseActivity {
    int REQUEST_IMAGE, REQUEST_CAMERA;
    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Logcat.i("二维码内容：" + result);
            if (CommonUtils.isBlank(SharedPreferencesUtil.getString(SaoErWeiMaActivity.this, "Token"))) {
                Bundle bundle = new Bundle();
                bundle.putString("CodeInfo",result);
                startActivityWithExtras(RegisterActivity.class,bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("CodeInfo",result);
                startActivityWithExtras(ScanResultActivity.class,bundle);
            }
        }

        @Override
        public void onAnalyzeFailed() {
            ToastUtils.showToast("扫描失败,请重试");
        }
    };
    @BindView(R.id.fl_my_container)
    FrameLayout mFlMyContainer;
    @BindView(R.id.btn_main_back)
    LinearLayout mBtnMainBack;
    @BindView(R.id.ll_title_right)
    LinearLayout mLinearLayout;
    @BindView(R.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R.id.tv_main_right)
    TextView mTvMainRight;
    @BindView(R.id.second_button1)
    CheckBox mSecondButton1;
    @BindView(R.id.ll_layout)
    LinearLayout mLlLayout;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mTvMainRight.setText("相册");
        mTvMainRight.setTextColor(getResources().getColor(R.color.white));
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.activity_scan_view);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        mSecondButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSecondButton1.isChecked()) {
                    CodeUtils.isLightEnable(true);
                } else {
                    CodeUtils.isLightEnable(false);
                }
            }
        });
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
        mBtnMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SaoErWeiMaActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scan_qr_code;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlLayout)
                .keyboardEnable(true)
                .init();
    }


    /**
     * 打开相册扫描
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片
                    CodeUtils.analyzeBitmap(getRealFilePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Logcat.i("二维码内容：" + result);
                            if (CommonUtils.isBlank(SharedPreferencesUtil.getString(SaoErWeiMaActivity.this, "Token"))) {
                                Bundle bundle = new Bundle();
                                bundle.putString("CodeInfo",result);
                                startActivityWithExtras(RegisterActivity.class, bundle);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("CodeInfo",result);
                                startActivityWithExtras(ScanResultActivity.class,bundle);
                            }
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(SaoErWeiMaActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
