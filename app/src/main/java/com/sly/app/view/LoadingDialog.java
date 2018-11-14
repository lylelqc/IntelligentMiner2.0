package com.sly.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.sly.app.R;

/**
 * 作者 by K
 * 时间：on 2017/8/21 16:50
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class LoadingDialog extends Dialog {
    private TextView tip_Tv;
    private ImageView loding_Iv;
    private Context ctx;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int style) {
        super(context, style);
        this.ctx = context;
        setContentView(R.layout.dialog_loading);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        loding_Iv = findViewById(R.id.loding_Iv);
        tip_Tv = findViewById(R.id.tip_Tv);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        hyperspaceJumpAnimation.setInterpolator(new LinearInterpolator());
        loding_Iv.startAnimation(hyperspaceJumpAnimation);
        setCancelable(false);//
    }

    public LoadingDialog(Context context, int style, int textColor) {
        super(context, style);
        this.ctx = context;
        setContentView(R.layout.dialog_loading);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        loding_Iv = findViewById(R.id.loding_Iv);
        tip_Tv = findViewById(R.id.tip_Tv);
        tip_Tv.setTextColor(context.getResources().getColor(R.color.bot_text_color_selected));
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        hyperspaceJumpAnimation.setInterpolator(new LinearInterpolator());
        loding_Iv.startAnimation(hyperspaceJumpAnimation);
        setCancelable(false);//
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * @param content
     * @Description:[设置加载信息]
     */
    public void setText(String content) {
        if (!isBlank(content)) {
            tip_Tv.setText(content);
            tip_Tv.setVisibility(View.VISIBLE);
        }
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

}

