package com.sly.app.model.sly;

import java.io.Serializable;

/**
 */
public class SelectImageModel implements Serializable {

    String imagePath;

    int imageType;

    String imageHttpPath;

    public static final int IMAGE_TYPE = 1;//图片类型
    public static final int ADD_IMAGE_TYPE = 2;//加号类型

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getImageHttpPath() {
        return imageHttpPath;
    }

    public void setImageHttpPath(String imageHttpPath) {
        this.imageHttpPath = imageHttpPath;
    }
}
