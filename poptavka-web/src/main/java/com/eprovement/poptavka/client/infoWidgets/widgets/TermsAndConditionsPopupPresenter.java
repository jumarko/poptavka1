/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.infoWidgets.InfoWidgetsEventBus;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Archo
 */
@Presenter(view = TermsAndConditionsPopup.class)
public class TermsAndConditionsPopupPresenter
    extends LazyPresenter<TermsAndConditionsPopupPresenter.ITermsAndConditionsPopup, InfoWidgetsEventBus> {

    /**************************************************************************/
    /*  View interface                                                        */
    /**************************************************************************/
    public interface ITermsAndConditionsPopup extends LazyView, IsWidget {

        //Getters
        HasClickHandlers getCloseButton();

        //Setter
        TermsAndConditionsPopup getWidgetView();
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Bind send & close button handlers.
     */
    @Override
    public void bindView() {
        view.getCloseButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hideView();
            }
        });
    }

    /**
     * Hides the TermsAndConditionsPopup.
     */
    private void hideView() {
        view.getWidgetView().hide();
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Displays popup.
     */
    public void onDisplayTermsAndConditionsPopup() {
        view.getWidgetView().show();
    }

}
