/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * Loading popup that contains Want-Somehitng logo and some text.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = LoadingPopupView.class)
public class LoadingPopupPresenter
        extends LazyPresenter<LoadingPopupPresenter.LoadingPopupViewInterface, RootEventBus> {

    public interface LoadingPopupViewInterface extends LazyView {

        LoadingPopupView getWidget();
    }

    /**
     * Displays popup with given message.
     * @param message to be displayed
     */
    public void onLoadingShow(String message) {
        view.getWidget().show(message);
    }

    /**
     * Hides popup.
     */
    public void onLoadingHide() {
        view.getWidget().hide();
    }
}
