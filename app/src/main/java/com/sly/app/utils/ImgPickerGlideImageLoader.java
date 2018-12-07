package com.sly.app.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import vip.devkit.view.common.ImgPicker.loader.ImageLoader;

/**
 * 作者 by K
 * 时间：on 2017/9/25 13:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ImgPickerGlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        //Glide 加载图片简单用法
        Glide.with(activity)                             //配置上下文
                .load(path)//设置图片路径
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

    }

    @Override
    public void clearMemoryCache() {

    }
}
