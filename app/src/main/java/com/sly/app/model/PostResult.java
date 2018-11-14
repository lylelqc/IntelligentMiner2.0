package com.sly.app.model;

import java.io.Serializable;

/**
 * 用于传递eventBus广播数据
 */
public class PostResult implements Serializable {

    public String tag;//eventbus标签，用来区分

    public Object result;//eventbus 传递的结果

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public PostResult(){

    }
    public PostResult(String tag){
        setTag(tag);
        setResult("");
    }

    public PostResult(String tag, Object result){
        setTag(tag);
        setResult(result);
    }

}
