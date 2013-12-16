/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = LoadingPopupView.class)
public class LoadingPopupPresenter
        extends LazyPresenter<LoadingPopupPresenter.LoadingPopupViewInterface, RootEventBus> {

    public interface LoadingPopupViewInterface extends LazyView {

        LoadingPopupView getWidget();
    }

    public void onLoadingShow(String message) {
        view.getWidget().show(message);
    }

    public void onLoadingHide() {
        view.getWidget().hide();
    }
}
