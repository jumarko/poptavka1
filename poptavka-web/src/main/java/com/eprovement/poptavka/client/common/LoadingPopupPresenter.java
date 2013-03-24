/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = LoadingPopupView.class, multiple = true)
public class LoadingPopupPresenter
        extends LazyPresenter<LoadingPopupPresenter.LoadingPopupViewInterface, RootEventBus> {

    public interface LoadingPopupViewInterface extends LazyView {

        HasClickHandlers getReportButton();

        LoadingPopupView getWidget();
    }

    @Override
    public void bindView() {
        view.getReportButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initEmailDialogPopup();
            }
        });
    }

    public void show(String message) {
        view.getWidget().show(message);
    }

    public void hide() {
        view.getWidget().hide();
    }
}
