package com.sly.app.view.pwd;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.utils.EncryptUtil;

import java.util.ArrayList;

/**
 * 作者 by K
 * 时间：on 2017/1/9 10:52
 * 邮箱 by yingzikeji@qq.com
 * <p/>
 * 类用途：安全密码键盘
 * 最后修改：
 */
public class SercurityDialog extends Dialog implements View.OnClickListener {
    /**
     * 0-9,数字按钮
     */
    private Button mNum1;
    private Button mNum2;
    private Button mNum3;
    private Button mNum4;
    private Button mNum5;
    private Button mNum6;
    private Button mNum7;
    private Button mNum8;
    private Button mNum9;
    private Button mNum0;
    private LinearLayout mDelPwd;
    private RelativeLayout mLayoutBack;
    private ArrayList<ImageView> mPwdImgs = new ArrayList<ImageView>();

    private ImageView mPwdImg1;
    private ImageView mPwdImg2;
    private ImageView mPwdImg3;
    private ImageView mPwdImg4;
    private ImageView mPwdImg5;
    private ImageView mPwdImg6;

    private TextView mTv;
    private TextView mOrderPrice;

    private Context mContext;
    private int mPwdCountNum;
    private String mString;
    private InputCompleteListener mInputCompleteListener;

    public interface InputCompleteListener {
        void inputComplete(String passWord);
    }

    public void setOnInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.mInputCompleteListener = inputCompleteListener;
    }

    public SercurityDialog(Context context, String mString) {
        super(context, R.style.SercurityDialogTheme);
        this.mString = mString;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //给dialog设置布局
        setContentView(R.layout.dialog_security_pwd);
        //通过window设置获取dialog参数
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        //获取屏幕的宽高
        WindowManager manager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        //设置dialog的宽
        params.width = width;
        //设置dialog在屏幕中的位置
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        //设置dialog属性
        window.setAttributes(params);

        initView();
        initListener();
    }

    private void initListener() {
        mNum0.setOnClickListener(this);
        mNum1.setOnClickListener(this);
        mNum2.setOnClickListener(this);
        mNum3.setOnClickListener(this);
        mNum4.setOnClickListener(this);
        mNum5.setOnClickListener(this);
        mNum6.setOnClickListener(this);
        mNum7.setOnClickListener(this);
        mNum8.setOnClickListener(this);
        mNum9.setOnClickListener(this);
        mDelPwd.setOnClickListener(this);

    }


    private void initView() {
        mNum0 = (Button) findViewById(R.id.button0);
        mNum1 = (Button) findViewById(R.id.button1);
        mNum2 = (Button) findViewById(R.id.button2);
        mNum3 = (Button) findViewById(R.id.button3);
        mNum4 = (Button) findViewById(R.id.button4);
        mNum5 = (Button) findViewById(R.id.button5);
        mNum6 = (Button) findViewById(R.id.button6);
        mNum7 = (Button) findViewById(R.id.button7);
        mNum8 = (Button) findViewById(R.id.button8);
        mNum9 = (Button) findViewById(R.id.button9);
        //洗牌
        int[] num = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        shuffleSort(num);
        mNum0.setText(String.valueOf(num[0]));
        mNum1.setText(String.valueOf(num[1]));
        mNum2.setText(String.valueOf(num[2]));
        mNum3.setText(String.valueOf(num[3]));
        mNum4.setText(String.valueOf(num[4]));
        mNum5.setText(String.valueOf(num[5]));
        mNum6.setText(String.valueOf(num[6]));
        mNum7.setText(String.valueOf(num[7]));
        mNum8.setText(String.valueOf(num[8]));
        mNum9.setText(String.valueOf(num[9]));

        mDelPwd = (LinearLayout) findViewById(R.id.button_del);
        mLayoutBack = (RelativeLayout) findViewById(R.id.rl_back);
        mLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getOwnerActivity().finish();
                dismiss();
            }
        });

        mPwdImg1 = (ImageView) findViewById(R.id.pwd_1);
        mPwdImg2 = (ImageView) findViewById(R.id.pwd_2);
        mPwdImg3 = (ImageView) findViewById(R.id.pwd_3);
        mPwdImg4 = (ImageView) findViewById(R.id.pwd_4);
        mPwdImg5 = (ImageView) findViewById(R.id.pwd_5);
        mPwdImg6 = (ImageView) findViewById(R.id.pwd_6);
        mPwdImgs.add(mPwdImg1);
        mPwdImgs.add(mPwdImg2);
        mPwdImgs.add(mPwdImg3);
        mPwdImgs.add(mPwdImg4);
        mPwdImgs.add(mPwdImg5);
        mPwdImgs.add(mPwdImg6);

        mOrderPrice = (TextView) findViewById(R.id.tv_OrderPrice);
        mOrderPrice.setText("需支付金额为：" + mString);

    }

    //洗牌算法
    private void shuffleSort(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            int j = (int) (data.length * Math.random());
            swap(data, i, j);
        }
    }

    private void swap(int[] data, int i, int j) {
        if (i == j) {
            return;
        }
        data[i] = data[i] + data[j];
        data[j] = data[i] - data[j];
        data[i] = data[i] - data[j];
    }

    private String mPassWord = "";

    @Override
    public void onClick(View view) {

        //删除 如果输入密码个数是0 return ，要不就mPwdCountNum 减1
        if (view.getId() == R.id.button_del) {
            if (mPwdCountNum == 0) {
                return;
            } else {
                mPwdCountNum = mPwdCountNum - 1;

                if (mPwdCountNum == 0) {
                    mPassWord = mPassWord.substring(0, 0);
                } else {
                    // 这里-2的原因是因为数字之间有一个逗号，逗号+数字=2
                    mPassWord = mPassWord.substring(0, mPassWord.length() - 2);
                }

                showPwdImages(mPwdCountNum);
            }
        } else {
            mPwdCountNum++;
            showPwdImages(mPwdCountNum);
            inputPwd(view);
        }

        /**
         * 当统计的当前输入的密码位数大于等于6，表示输入完成，数据处理在UI线程中处理
         */
        if (mPwdCountNum >= 6) {
            SystemClock.sleep(100);
            dismiss();              // 关闭对话框
            /**
             * 开启一个线程，将密码数字传送给UI线程去加密
             *
             * MD5  加密先不用jIn
             */
            mPassWord = mPassWord.replaceAll(",", "");
            mInputCompleteListener.inputComplete(EncryptUtil.MD5(mPassWord));
//            mInputCompleteListener.inputComplete(mPassWord);
        }
    }

    /**
     * 通过获取Button上的数字字符来合成字符串
     *
     * @param view
     */
    private void inputPwd(View view) {
        if (mPwdCountNum > 1) {
            mPassWord = mPassWord + ",";
        }

        mPassWord += ((Button) view).getText();
    }

    /**
     * 根据传入的参数来设置显示的密码的图片张数
     *
     * @param pwdCountNum
     */
    private void showPwdImages(int pwdCountNum) {

        for (int i = 0; i < mPwdImgs.size(); i++) {
            if (i < pwdCountNum) {
                mPwdImgs.get(i).setVisibility(View.VISIBLE);
            } else {
                mPwdImgs.get(i).setVisibility(View.GONE);
            }
        }
    }

}
