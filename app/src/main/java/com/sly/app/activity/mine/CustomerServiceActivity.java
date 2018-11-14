package com.sly.app.activity.mine;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sly.app.R;
import com.sly.app.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 01 on 2016/10/20.
 */
public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_comm_layout)
    LinearLayout mLlCommLayout;
    private LinearLayout bt_back;
    private Button bt_service;
    private TextView tv_title;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mLlCommLayout).init();
    }

    @Override
    protected void initView() {
        bt_back = findViewById(R.id.btn_main_back);
        tv_title = findViewById(R.id.tv_main_title);
        bt_service = findViewById(R.id.bt_service);
        tv_title.setText("客服中心");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void setListener() {
        bt_back.setOnClickListener(this);
        bt_service.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_back:
                finish();
                break;
            case R.id.bt_service:
                dialog();
                break;
        }
    }

    protected void dialog() {
        final Dialog dialog = new Dialog(CustomerServiceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.clear_history_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) dialog.findViewById(R.id.textView5)).setText("是否拨打客服电话");
        dialog.findViewById(R.id.tv_dialog_detail).setVisibility(View.GONE);
        dialog.show();

        Button cancelButton = dialog.findViewById(R.id.cancel_action);
        cancelButton.setText("取消");
        cancelButton.setTextColor(getResources().getColor(R.color.bot_text_color_norm));
        Button confirmButton = dialog.findViewById(R.id.confirm_action);
        confirmButton.setText("确定");
        confirmButton.setTextColor(getResources().getColor(R.color.white));
        confirmButton.setBackgroundColor(getResources().getColor(R.color.jfdh_buy));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 针对即使获取了拨打电话的权限依然报错问题的解决方案
                final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(CustomerServiceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CustomerServiceActivity.this, Manifest.permission.CALL_PHONE)) {
                        // 返回值：
                        //如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                        //如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                        //如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(CustomerServiceActivity.this, "请授权！", Toast.LENGTH_LONG).show();
                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(CustomerServiceActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                } else {
                // 已经获得授权，可以打电话
                    CallPhone();
                }

                dialog.dismiss();
            }
        });
    }

    private void CallPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        //url:统一资源定位符
        //uri:统一资源标示符（更广）
        intent.setData(Uri.parse("tel:" + "4009205373"));
        //开启系统拨号器
        startActivity(intent);
    }

    @OnClick(R.id.ll_comm_layout)
    public void onViewClicked() {

    }
}
