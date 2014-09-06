/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.serviceSelector.interfaces.IServiceSelectorModule;
import com.eprovement.poptavka.client.serviceSelector.serviceItem.ServiceItem;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * View consists of Table.
 *
 * @author Martin Slavkovsky
 */
public class ServiceSelectorView extends Composite implements IServiceSelectorModule.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierServiceUiBinder uiBinder = GWT.create(SupplierServiceUiBinder.class);

    interface SupplierServiceUiBinder extends UiBinder<Widget, ServiceSelectorView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField HorizontalPanel servicesHolder;
    @UiField Label infoLabel;
    @UiField FormElement form;
    @UiField InputElement formReturnUrl, formItemName, formItemNumber, formItemId, formAmount;

    /** Class attributes. **/
    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ServiceSelector view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setInfoLabel(String text) {
        infoLabel.setText(text);
    }

    @Override
    public void setPaymentDetails(String returnRul, UserServiceDetail userServiceDetail) {
        formReturnUrl.setValue(returnRul);
        formItemName.setValue("Buy now");
        formItemNumber.setValue(userServiceDetail.getService().getTitle());
        formItemId.setValue(Long.toString(userServiceDetail.getOrderNumber()));
        formAmount.setValue(userServiceDetail.getService().getPrice().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        for (int i = 0; i < servicesHolder.getWidgetCount(); i++) {
            ServiceItem item = (ServiceItem) servicesHolder.getWidget(i);
            item.setSelected(false);
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return services holder
     */
    @Override
    public HorizontalPanel getServicesHolder() {
        return servicesHolder;
    }

    /**
     * @return payment form
     */
    @Override
    public FormElement getPaymentForm() {
        return form;
    }

    /**
     * Validates view's components.
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid() {
        int countValid = 0; //only one service should be selected
        for (int i = 0; i < servicesHolder.getWidgetCount(); i++) {
            ServiceItem item = (ServiceItem) servicesHolder.getWidget(i);
            if (item.isIsSelected()) {
                countValid++;
            }
        }
        if (countValid == 0) {
            Window.alert("Please select one service.");
        } else if (countValid > 0) {
            Window.alert("Only one serivce must be selected.");
        }
        return countValid == 1;
    }
}
