/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets;

import com.eprovement.poptavka.client.infoWidgets.widgets.AlertBoxPopup;
import com.eprovement.poptavka.client.infoWidgets.widgets.ThankYouPopup;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

/**
 * EventHandler serves for creating info widget popups.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class InfoWidgetsHandler extends BaseEventHandler<InfoWidgetsEventBus> {

    @Inject ThankYouPopup thankYouPopup;
    @Inject AlertBoxPopup alertBoxPopup;

    /**
     * Creates thank you popup.
     * @param message that is shown to user
     * @param timer define how long will be popup displayed and following action.
     * Provide null if default timer with no action is enough.
     */
    public void onShowThankYouPopup(SafeHtml message, Timer timer) {
        thankYouPopup.create(message, timer);
    }

    /**
     * Creates alert popup.
     * @param message that is shown to user
     */
    public void onShowAlertPopup(String message) {
        alertBoxPopup.create(eventBus, message);
    }
}
