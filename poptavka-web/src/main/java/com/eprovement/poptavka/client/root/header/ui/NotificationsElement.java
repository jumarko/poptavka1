package com.eprovement.poptavka.client.root.header.ui;

import com.eprovement.poptavka.client.root.header.ui.NotificationsElement.NotificationsElementUiBinder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;

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
    @UiField PushButton pushButton;
    @UiField Label newMessagesCount;
    @UiField PushButton pushSystemButton;
    @UiField Label newSystemMessagesCount;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public NotificationsElement() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getter                                                                 */
    /**************************************************************************/
    public Label getNewMessagesCount() {
        return newMessagesCount;
    }

    /**
     * @return the newSystemMessagesCount
     */
    public Label getNewSystemMessagesCount() {
        return newSystemMessagesCount;
    }

    /**
     * @return the pushButton
     */
    public PushButton getPushButton() {
        return pushButton;
    }

    /**
     * @return the pushSystemButton
     */
    public PushButton getPushSystemButton() {
        return pushSystemButton;
    }
}
