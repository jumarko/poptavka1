/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector.serviceItem;

import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Martin Slavkovsky
 * @since 5.9.2014
 */
public class ServiceItem extends Composite implements HasClickHandlers {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ServiceItemUiBinder uiBinder = GWT.create(ServiceItemUiBinder.class);

    interface ServiceItemUiBinder extends UiBinder<Widget, ServiceItem> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField VerticalPanel panel;
    @UiField Label title, credits, price;
    @UiField Image icon, selected;
    /** Class attributes. **/
    private ServiceDetail serviceDetail;
    private boolean isSelected;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ServiceSelector view's compontents.
     */
    public ServiceItem(ServiceDetail detail) {
        initWidget(uiBinder.createAndBindUi(this));
        serviceDetail = detail;
        title.setText(detail.getTitle());
        credits.setText(detail.getCredits() + "credits");
        price.setText(detail.getPrice() + " dollars");
    }

    /**************************************************************************/
    /* HasClickHandlers                                                       */
    /**************************************************************************/
    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setSelected(boolean value) {
        if (value) {
            panel.addStyleName("selected");
        } else {
            panel.removeStyleName("selected");
        }
        selected.setVisible(value);
        isSelected = value;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return service detail represented by this widget
     */
    public ServiceDetail getServiceDetail() {
        return serviceDetail;
    }

    /**
     * @return true if selected styles are set, false otherwise
     */
    public boolean isIsSelected() {
        return isSelected;
    }
}
