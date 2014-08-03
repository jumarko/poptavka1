/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.creditAnnouncer;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.user.widget.creditAnnouncer.
        CreditStatusAnnouncerPresenter.ICreditStatusAnnouncerView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Jaro
 */
@Presenter(view = CreditStatusAnnouncerView.class)
public class CreditStatusAnnouncerPresenter extends
        LazyPresenter<ICreditStatusAnnouncerView, RootEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface ICreditStatusAnnouncerView extends LazyView, IsWidget {

        IsWidget getWidgetView();

        Button getRechargeButton();

        void setCreditStatus(int credits);
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Binds events for updating categories and localities - edit, submit, close button handlers.
     */
    @Override
    public void bindView() {
        view.getRechargeButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO: eventbus call of the modal for credit charge

                // TODO: this is just a test call to see how the widget changes
                onSetCreditStatus(10);
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Sets credit announcer view to given panel.
     * @param creditAnnouncerPanel - holder panel
     */
    public void onSetCreditAnnouncer(SimplePanel creditAnnouncerPanel) {
        creditAnnouncerPanel.setWidget(view.getWidgetView());
    }
    /**
     * Sets credit widget status
     * @param credit - user's current credits.
     */
    public void onSetCreditStatus(int credit) {
        view.setCreditStatus(credit);
    }
}
