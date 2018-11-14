package com.sly.app.view;

import android.widget.BaseAdapter;

/**
 * Created by Administrator on 2017/6/2.
 */

public abstract class DragGridAdapter extends BaseAdapter {

    public abstract void hideView(int pos);

    public abstract void showHideView();

    public abstract void removeView(int pos);

    public abstract void swapView(int draggedPos, int destPos);

    public abstract int getDragType(int pos);

    public abstract void dragStatus(boolean isDrag);
}
