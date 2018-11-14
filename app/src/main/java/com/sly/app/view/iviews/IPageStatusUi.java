package com.sly.app.view.iviews;

/**
 */
public interface IPageStatusUi {

    void showPageStatusView(String message);

    void showPageStatusView(int iconRes, String message);

    void hidePageStatusView();
}
