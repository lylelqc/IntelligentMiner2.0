package com.sly.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 01 on 2017/6/16.
 */
public class OneListView extends ListView {
    public OneListView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public OneListView(Context context) {

        super(context);

    }

    public OneListView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }

    @Override

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
