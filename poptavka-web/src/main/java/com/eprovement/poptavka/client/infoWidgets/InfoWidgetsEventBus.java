/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets;

import com.eprovement.poptavka.client.infoWidgets.widgets.ContactUsPopupPresenter;
import com.eprovement.poptavka.client.infoWidgets.widgets.ErrorPresenter;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.event.EventBusWithLookup;
import com.mvp4g.client.presenter.NoStartPresenter;

/**
 * Contains notification widgets and popups.
 * <ul>
 *    <li>* Error module - displays all erroneous states on body page</li>
 *    <li>* ContactUs - "contact us" form popup for sending emails on Want-Something.com</li>
 *    <li>* AlertBox - popup for displaying various alerts</li>
 *    <li>* ThankYou - popup for displaying workflow information, can be disabled in user's settings</li>
 * </ul>
 *
 * <b><i>Note:</i></b>
 * This module has neither presenter or view. For creating info popups that can
 * stand alone is event handler more than enough.
 *
 * @author ivlcek, Martin Slavkovsky
 */
@Events(startPresenter = NoStartPresenter.class, module = InfoWidgetsModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface InfoWidgetsEventBus extends EventBusWithLookup {

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void setBody(IsWidget body);

    @Event(forwardToParent = true)
    void setToolbarContent(String title, Widget toolbarContent);

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = ErrorPresenter.class)
    void displayError(int errorResponseCode, String errorId);

    @Event(handlers = ContactUsPopupPresenter.class)
    void sendUsEmail(int subject, String errorId);

    @Event(handlers = InfoWidgetsHandler.class)
    void showThankYouPopup(SafeHtml message, Timer timer);

    @Event(handlers = InfoWidgetsHandler.class)
    void showAlertPopup(String message);
}