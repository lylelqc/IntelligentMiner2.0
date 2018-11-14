package com.sly.app.listener;

import android.widget.ImageView;
import android.widget.TextSwitcher;

/**
 * Created by Administrator on 2017/5/19.
 * 收藏
 */

public interface IRecylerCollectionViewItemListener extends IRecyclerViewItemListener{

    void onItemCollectionClick(ImageView iv, TextSwitcher tv, int position);
}
