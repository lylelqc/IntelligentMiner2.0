package com.sly.app.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.sly.app.R;
import com.sly.app.base.BaseActivity;
import com.sly.app.http.type1.HttpClient;
import com.sly.app.http.type1.HttpResponseHandler;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;
import vip.devkit.library.Logcat;

/**
 * Created by queen
 *
 * @time 2016/12/8. 11:55.
 * @des 意见反馈
 */
public class UserOpinionActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_goback;
    private Button sure_tv;
    private EditText option_et;
    protected LoadingDialog loadingDialog;//加载中
    /**
     * 网络请求参数
     **/
    /**
     * 反馈内容
     **/
    private String content;
    /**
     * 反馈分类
     **/
    private String classID;
    /**
     * 反馈的会员
     **/
    private String memberCode;

    /**
     * 登录凭证
     **/
    private String token;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private ArrayList listUri = new ArrayList();
    private ArrayList listBitmap = new ArrayList();
    private boolean isMember;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);

    }


    protected void initView() {
        ll_goback = (LinearLayout) findViewById(R.id.ll_goback);
        option_et = (EditText) findViewById(R.id.option_et);
        sure_tv = (Button) findViewById(R.id.sure_tv);
    }
    private void setFocus(){
        option_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    option_et.setFocusable(true);
                    option_et.setFocusableInTouchMode(true);
                } else {
                    option_et.clearFocus();
                }
            }
        });
    }
    protected void initData() {
        isMember = SharedPreferencesUtil.getBoolean(getApplicationContext(), "isMember", false);
        memberCode = SharedPreferencesUtil.getString(getApplicationContext(), "MemberCode");
        token = SharedPreferencesUtil.getString(getApplicationContext(), "token");
        setFocus();
        ll_goback.setOnClickListener(this);
        option_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    content = s.toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sure_tv.setOnClickListener(this);
    }

    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_goback:
                finish();
                break;
            case R.id.sure_tv:
                initProgressDialog();
                loadingDialog.setCanceledOnTouchOutside(false);
//                    showLoading();
                    CreateMessageBoard();

                break;

        }
    }
    public void initProgressDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, R.style.loading_dialog);
            loadingDialog.setText("上传中...");
        }
        if (!isFinishing() && !loadingDialog.isShowing()) {
            loadingDialog = new LoadingDialog(this, R.style.loading_dialog);
            loadingDialog.setText("上传中...");
            loadingDialog.show();
        }
        loadingDialog.setCanceledOnTouchOutside(true);
    }

    public void dismissProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
    /**
     * 创建意见反馈网络请求
     **/
    private void CreateMessageBoard() {
        UserOption userOption = new UserOption();
        userOption.setContent(content);
        userOption.setMemberCode("8888");
        userOption.setToken(token);
//        System.out.println("意见反馈获取的json           " + listUP.size());
        Gson gson = new Gson();
        String json = gson.toJson(userOption);
        HttpClient.postJson("", json, new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, final String content) {
                super.onSuccess(statusCode, content);

                // 从服务器端获取Json字符串
                String backlogJsonStr = content;
                // 声明中间变量进行处理  C2016112317291393650
                String backlogJsonStrTmp = backlogJsonStr.replace("\\", "");
                // 处理完成后赋值回去
                backlogJsonStr = backlogJsonStrTmp.substring(1, backlogJsonStrTmp.length() - 1);
                Logcat.i("意见反馈获取" + backlogJsonStr+ statusCode);
                try {
                    JSONObject jsonObject = new JSONObject(backlogJsonStr);
                    if ("成功".equals(jsonObject.getString("msg"))) {
                        ToastUtils.showToast(jsonObject.getString("msg"));
                        dismissProgressDialog();
                        finish();
                    } else {
                        ToastUtils.showToast(jsonObject.getString("data"));
                        dismissProgressDialog();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                ToastUtils.showToast("网络连接错误，请检查网络");
                dismissProgressDialog();
            }
        });
    }

    // 点击空白区域 自动隐藏软键盘
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    private class UserOption {
        private String Remark;
        private String MemberCode;
        private String Token;

        public String getContent() {
            return Remark;
        }

        public void setContent(String content) {
            this.Remark = content;
        }


        public String getMemberCode() {
            return MemberCode;
        }

        public void setMemberCode(String memberCode) {
            this.MemberCode = memberCode;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String token) {
            this.Token = token;
        }
    }
}
