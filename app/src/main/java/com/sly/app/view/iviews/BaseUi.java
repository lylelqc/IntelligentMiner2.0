package com.sly.app.view.iviews;

/**
 * Created by on 15/12/14.
 */
public interface BaseUi {

    void showToastLong(String msg);

    void showToastShort(String msg);

    void showProgress(String label);

    void showProgress(String label, boolean cancelable);

    void dimissProgress();
}
