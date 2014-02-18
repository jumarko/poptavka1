/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Timer;
import com.mvp4g.client.annotation.Event;

/**
 * Gateway interface for Address selector module.
 * Defines which methods are accessible to the rest of application.
 *
 * @author Martin Slavkovsky
 */
public interface InfoWidgetsGateway {

    @Event(forwardToParent = true)
    void displayError(int errorResponseCode, String errorId);

    @Event(forwardToParent = true)
    void sendUsEmail(int subject, String errorId);

    @Event(forwardToParent = true)
    void showThankYouPopup(SafeHtml message, Timer timer);

    @Event(forwardToParent = true)
    void showAlertPopup(String message);

    @Event(forwardToParent = true)
    void showTermsAndConditionsPopup();
}
