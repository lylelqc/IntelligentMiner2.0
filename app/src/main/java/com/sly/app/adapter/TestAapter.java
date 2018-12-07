package com.sly.app.adapter;

import android.content.Context;

import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/8/29 17:05
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class TestAapter extends CommonAdapter<Object> {

    public TestAapter(Context context, List<Object> mDatas, int layoutId) {
        super(context, mDatas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, Object o, int i) {

    }
}
