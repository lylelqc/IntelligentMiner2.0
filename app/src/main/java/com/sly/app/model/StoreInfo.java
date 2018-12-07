package com.sly.app.model;

/**
 * 作者 by K
 * 时间：on 2017/9/13 15:28
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途： 店铺信息
 * 最后修改：
 */
public class StoreInfo {
    protected String Id;
    protected String name;
    protected boolean isChoosed;
    private boolean isEdtor;
    private int storNum;

    public boolean isEdtor() {
        return isEdtor;
    }

    public void setIsEdtor(boolean isEdtor) {
        this.isEdtor = isEdtor;
    }

    public StoreInfo(int storNum, String name) {
        this.storNum = storNum;
        this.name = name;
    }

    public void setEdtor(boolean edtor) {
        isEdtor = edtor;
    }

    public int getStorNum() {
        return storNum;
    }

    public void setStorNum(int storNum) {
        this.storNum = storNum;
    }

    public StoreInfo(String id, String name) {
        Id = id;
        this.name = name;
    }


    public StoreInfo() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

    public StoreInfo(String id, String name, boolean isChoosed, boolean isEdtor, int storNum) {
        Id = id;
        this.name = name;
        this.isChoosed = isChoosed;
        this.isEdtor = isEdtor;
        this.storNum = storNum;
    }
}
