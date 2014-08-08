/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.system;

import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.PropertiesDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
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
public class PropertyItemView extends Composite implements HasClickHandlers {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static PropertyItemVIewUiBinder uiBinder = GWT.create(PropertyItemVIewUiBinder.class);

    interface PropertyItemVIewUiBinder extends UiBinder<Widget, PropertyItemView> {
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addHandler(handler, ClickEvent.getType());
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Label name;
    @UiField HTMLPanel notificationChoicePanel;
    @UiField Button onBtn;
    @UiField Button offBtn;
    /** Class attributes. **/
    private PropertiesDetail propertiesDetail;
    private boolean isEnabled;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates NotificationItem view's compontents.
     */
    public PropertyItemView(PropertiesDetail propertiesDetail) {
        initWidget(uiBinder.createAndBindUi(this));

        this.propertiesDetail = propertiesDetail;
        this.name.setText(propertiesDetail.getTitle());
        setOnOffStyles(Boolean.valueOf(propertiesDetail.getValue()));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    /**
     * Binds <b>on</b> button handler.
     */
    @UiHandler("onBtn")
    public void onBtnClickHandler(ClickEvent e) {
        setOnOffStyles(true);
        fireEvent(e);
    }

    /**
     * Binds <b>off</b> button handler.
     */
    @UiHandler("offBtn")
    public void offBtnClickHandler(ClickEvent e) {
        setOnOffStyles(false);
        fireEvent(e);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public PropertiesDetail getPropertiesDetail() {
        propertiesDetail.setValue(Boolean.toString(isEnabled));
        return propertiesDetail;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Sets on-off style according to given value.
     * @param isOn true if ON should be selected, false if OFF
     */
    private void setOnOffStyles(boolean isOn) {
        isEnabled = isOn;
        if (isOn) {
            notificationChoicePanel.removeStyleName(StyleResource.INSTANCE.common().switchRight());
            notificationChoicePanel.addStyleName(StyleResource.INSTANCE.common().switchLeft());
        } else {
            notificationChoicePanel.removeStyleName(StyleResource.INSTANCE.common().switchLeft());
            notificationChoicePanel.addStyleName(StyleResource.INSTANCE.common().switchRight());
        }
    }
}
