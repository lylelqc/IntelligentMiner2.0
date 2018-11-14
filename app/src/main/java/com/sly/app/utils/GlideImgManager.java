package com.sly.app.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sly.app.model.GlideRoundTransform;
import com.sly.app.view.CircleTransform;

/**
 * 作者 by K
 * 时间：on 2017/9/20 11:49
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class GlideImgManager {
    /**
     * @param url
     * @param erroImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, ImageView iv) {
        //原生 API
            Glide.with(context).load(url).placeholder(erroImg).error(erroImg).into(iv);
    }

    /**
     * 圆型
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new CircleTransform(context)).into(iv);
    }

    /**
     * lo圆角
     *
     * @param context
     * @param res
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, int res, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
            Glide.with(context).load(res).placeholder(emptyImg).error(erroImg).transform(new CircleTransform(context)).into(iv);
    }

    /**
     * lo圆角
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param tag
     */
    public static void glideLoader(Context context, int url, int erroImg, int emptyImg, ImageView iv, int tag) {

        //原生 API
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, tag)).into(iv);
    }


}