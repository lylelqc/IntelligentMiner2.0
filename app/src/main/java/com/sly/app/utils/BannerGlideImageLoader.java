package com.sly.app.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import vip.devkit.view.common.banner.loader.BannerImageLoader;

/**
 * 作者 by K
 * 时间：on 2017/9/18 11:02
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class BannerGlideImageLoader extends BannerImageLoader {
    @Override
    public void displayImage(Context mContext, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(mContext)                             //配置上下文
                .load(path)//设置图片路径
                .into(imageView);

    }
}
