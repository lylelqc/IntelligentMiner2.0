package com.sly.app.utils;

import android.content.Context;
import android.net.Uri;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TException;

import java.io.File;

/**
 * Created by Administrator on 2017/6/1.
 */

public class PhotoUtils {

    public static Uri getPhotoFilePath(Context context){
        String filepath = CommonUtil2.getInnerSDCardPath()+"/sly/"+ System.currentTimeMillis()+".jpg";
        File file=new File(filepath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    /***
     * 用戶頭像處理
     */
    public static CompressConfig getPhotoCompress(){

        return new CompressConfig.Builder() .setMaxSize(100*1024).create();

    }

    /**
     * 裁剪
     * */
    public static void setCrop(TakePhoto takePhoto, Uri imageUri, Uri outPutUri){
        CropOptions cropOptions=new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        try {
            takePhoto.onCrop(imageUri, outPutUri, cropOptions);
        } catch (TException e) {
            e.printStackTrace();
        }

    }

}
