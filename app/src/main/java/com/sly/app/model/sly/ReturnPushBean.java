package com.sly.app.model.sly;

import com.google.gson.annotations.SerializedName;

public class ReturnPushBean {

    private int error_code;
    private String data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @SerializedName("data")


    @Override
    public String toString() {
        return "ReturnBean{" +
                "error_code='" + error_code + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
