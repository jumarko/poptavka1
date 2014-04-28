/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.unsubscribe;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.mvp4g.client.annotation.Presenter;

import com.eprovement.poptavka.client.root.interfaces.IUnsubscribe;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * @author Martin Slavkovsky
 * @since 9.4.2014
 */
@Presenter(view = UnsubscribeView.class)
public class UnsubscribePresenter extends LazyPresenter<IUnsubscribe.View, RootEventBus>
    implements IUnsubscribe.Presenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private String password;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void onInitUnsubscribe(String password) {
        this.password = password;
        eventBus.setToolbarContent(LocalizableMessages.INSTANCE.unsubscribeToolbar(), null);
        eventBus.setBody(view);
        eventBus.setFooter(view.getFooterContainer());
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getCancel().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeWelcomeModule();
            }
        });
        view.getUnsubscribe().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.unsubscribeUser(password);
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onResponseUnsubscribe(Boolean result) {
        Timer timer = new Timer() {

            @Override
            public void run() {
                eventBus.goToHomeWelcomeModule();
            }
        };
        eventBus.showThankYouPopup(
            SafeHtmlUtils.fromString(LocalizableMessages.INSTANCE.unsubscribeSuccessfull()), timer);
        timer.schedule(3000);
    }
}
