package com.sly.app.utils.http;


import com.sly.app.utils.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;

import vip.devkit.library.Logcat;

/**
 */
public class HttpStatusUtil {

    // 得到状态码
    public static boolean getStatus(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            Logcat.e("status>>>", jsonObject.optString("status"));
            if (jsonObject.optString("status").equals("1")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 得到状态提示
     *
     * @param json
     * @return
     */
    public static String getStatusMsg(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String message = jsonObject.getString("msg");

            if (!StringHelper.isEmpty(message)&&!"成功".equals(message)) {
//                ToastUtils.showToast(message);
                return message;
            } else {
                return json;
            }
        } catch (Exception ex) {

            return json;
        }
    }

    /**
     * 得到状态异常码
     *
     * @param json
     * @return
     */
    public static int getStatusError(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String statusCode = jsonObject.getString("status");

            Integer error = Integer.valueOf(statusCode);
            return error;
        } catch (Exception ex) {

            return 0;
        }
    }

    /**
     * 判断状态是否需要重新登录
     *
     * @param json
     * @return
     */
    public static boolean isRelogin(String json) {

        String error = getStatusMsg(json);
        return "会话过期或非法访问".equals(error);

    }


    /**
     * 判断状态是否需要登录才做控制
     *
     * @param json
     * @return
     */
    public static boolean isLoginUser(String json) {

        int error = getStatusError(json);

        return error == 6000;

    }
}
