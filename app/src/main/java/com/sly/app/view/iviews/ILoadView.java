package com.sly.app.view.iviews;

import android.view.View;

/**
 * Created by on 15/12/8.
 */
public interface ILoadView {

    View inflate();

    void showLoadingView(View parentView);

    void showErrorView(View parentView);

    void showFinishView(View parentView);
}
