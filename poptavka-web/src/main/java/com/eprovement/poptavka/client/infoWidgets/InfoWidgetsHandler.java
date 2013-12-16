package com.eprovement.poptavka.client.infoWidgets;

import com.eprovement.poptavka.client.infoWidgets.widgets.AlertBoxPopup;
import com.eprovement.poptavka.client.infoWidgets.widgets.ThankYouPopup;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

/**
 * @author Martin Slavkovsky
 */
@EventHandler
public class InfoWidgetsHandler extends BaseEventHandler<InfoWidgetsEventBus> {

    @Inject ThankYouPopup thankYouPopup;
    @Inject AlertBoxPopup alertBoxPopup;

    public void onShowThankYouPopup(SafeHtml message, Timer timer) {
        thankYouPopup.create(message, timer);
    }

    public void onShowAlertPopup(String message) {
        alertBoxPopup.create(eventBus, message);
    }
}
