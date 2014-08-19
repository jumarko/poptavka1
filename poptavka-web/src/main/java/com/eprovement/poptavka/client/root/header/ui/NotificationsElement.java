/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header.ui;

import com.eprovement.poptavka.client.root.header.ui.NotificationsElement.NotificationsElementUiBinder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;

/**
 * Notification element includes push buttons for messages and system messages notifications.
 * Visible for all screens but only for logged user.
 * @author Martin Slavkovsky
 */
public class NotificationsElement extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static NotificationsElementUiBinder uiBinder = GWT.create(NotificationsElementUiBinder.class);

    interface NotificationsElementUiBinder extends UiBinder<Widget, NotificationsElement> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField PushButton creditButton;
    @UiField Label creditCount;
    @UiField PushButton pushButton;
    @UiField Label newMessagesCount;
    @UiField PushButton pushSystemButton;
    @UiField Label newSystemMessagesCount;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    /**
     * Creates notification element view's compontents.
     */
    public NotificationsElement() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getter                                                                 */
    /**************************************************************************/
    /**
     * @return the newMessagesCount label
     */
    public Label getNewMessagesCount() {
        return newMessagesCount;
    }

    /**
     * @return the newSystemMessagesCount label
     */
    public Label getNewSystemMessagesCount() {
        return newSystemMessagesCount;
    }

    /**
     * @return the pushButton push button
     */
    public PushButton getPushButton() {
        return pushButton;
    }

    /**
     * @return the pushSystemButton push button
     */
    public PushButton getPushSystemButton() {
        return pushSystemButton;
    }
}