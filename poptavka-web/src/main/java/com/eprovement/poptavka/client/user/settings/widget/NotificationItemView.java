/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.ui.WSListBox;
import com.eprovement.poptavka.client.common.ui.WSListBoxData;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.settings.NotificationDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * NotificationItem represents one notification from user's profile.
 * Consists of name label, on/off switches, period listBox
 *
 * @author Martin Slavkovsky
 */
public class NotificationItemView extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static NotificationItemVIewUiBinder uiBinder = GWT.create(NotificationItemVIewUiBinder.class);

    interface NotificationItemVIewUiBinder extends UiBinder<Widget, NotificationItemView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Label name;
    @UiField HTMLPanel notificationChoicePanel;
    @UiField Button onBtn;
    @UiField Button offBtn;
    @UiField(provided = true) WSListBox period;
    /** Class attributes. **/
    private boolean isEnabled;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates NotificationItem view's compontents.
     */
    public NotificationItemView(NotificationDetail item) {
        createPeriodListBox(item.getPeriod().getValue());

        initWidget(uiBinder.createAndBindUi(this));

        name.setText(item.getName());
        isEnabled = item.isEnabled();
        period.setSelected(item.getPeriod().getValue());

        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**
     * Creates period WSListBox.
     * @param periodName that will be selected by default
     */
    private void createPeriodListBox(String periodName) {
        WSListBoxData demandTypeData = new WSListBoxData();
        int key = 0;
        int selectIdx = 0;
        for (Period item : Period.values()) {
            if (item.getValue().equals(periodName)) {
                selectIdx = key;
            }
            demandTypeData.insertItem(item.getValue(), key++);
        }
        period = WSListBox.createListBox(demandTypeData, selectIdx);
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    /**
     * Binds <b>on</b> button handler.
     */
    @UiHandler("onBtn")
    public void onBtnClickHandler(ClickEvent e) {
        isEnabled = true;
        notificationChoicePanel.removeStyleName(StyleResource.INSTANCE.common().switchRight());
        notificationChoicePanel.addStyleName(StyleResource.INSTANCE.common().switchLeft());
    }

    /**
     * Binds <b>off</b> button handler.
     */
    @UiHandler("offBtn")
    public void offBtnClickHandler(ClickEvent e) {
        isEnabled = false;
        notificationChoicePanel.addStyleName(StyleResource.INSTANCE.common().switchRight());
        notificationChoicePanel.removeStyleName(StyleResource.INSTANCE.common().switchLeft());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the name label
     */
    public Label getName() {
        return name;
    }

    /**
     * @return true if notification is enabled (is ON), false otherwise
     */
    public boolean getEnabled() {
        return isEnabled;
    }

    /**
     * @return the notification period
     */
    public String getPeriod() {
        return period.getSelected();
    }
}
