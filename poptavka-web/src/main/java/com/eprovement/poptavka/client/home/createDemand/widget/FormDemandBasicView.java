/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.DemandField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Defines form for basic step in demand creation process.
 * @author Beho, Martin Slavkovsky
 */
public class FormDemandBasicView extends Composite
        implements FormDemandBasicPresenter.FormDemandBasicInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    private static FormDemandBasicUiBinder uiBinder = GWT.create(FormDemandBasicUiBinder.class);

    interface FormDemandBasicUiBinder extends UiBinder<Widget, FormDemandBasicView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor titleMonitor, priceMonitor, descriptionMonitor, endDateMonitor;
    /** Class attributes. **/
    ArrayList<ValidationMonitor> monitors;

    /**************************************************************************/
    /* Initializatiob                                                         */
    /**************************************************************************/
    /**
     * Creates form basic demand view's compontents.
     */
    @Override
    public void createView() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
        initEndDateDatePricker();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public FullDemandDetail updateBasicDemandInfo(FullDemandDetail demandToUpdate) {
        demandToUpdate.setDemandTitle((String) titleMonitor.getValue());
        demandToUpdate.setPrice((BigDecimal) priceMonitor.getValue());
        demandToUpdate.setEndDate((Date) endDateMonitor.getValue());
        demandToUpdate.setDescription((String) descriptionMonitor.getValue());
        return demandToUpdate;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        titleMonitor.reset();
        priceMonitor.reset();
        endDateMonitor.reset();
        descriptionMonitor.reset();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        for (ValidationMonitor monitor : monitors) {
            valid = monitor.isValid() && valid;
        }
        return valid;
    }

    /**************************************************************************/
    /* Helpser methods                                                        */
    /**************************************************************************/
    /**
     * Inits validation monitors.
     */
    private void initValidationMonitors() {
        titleMonitor = createValidationMonitor(DemandField.TITLE);
        priceMonitor = createValidationMonitor(DemandField.PRICE);
        endDateMonitor = createValidationMonitor(DemandField.END_DATE);
        descriptionMonitor = createValidationMonitor(DemandField.DESCRIPTION);
        monitors = new ArrayList<ValidationMonitor>(
                Arrays.asList(titleMonitor, priceMonitor, endDateMonitor, descriptionMonitor));
    }

    /**
     * Creates validation monitors
     * @param field - validation field
     * @param groups - validation groups
     * @return created validation monitor
     */
    private ValidationMonitor createValidationMonitor(DemandField field) {
        return new ValidationMonitor<FullDemandDetail>(FullDemandDetail.class, field.getValue());
    }

    /**
     * Initializes endDate datepicker.
     */
    private void initEndDateDatePricker() {
        ((DateBox) endDateMonitor.getWidget()).setFormat(new DateBox.DefaultFormat(Storage.get().getDateTimeFormat()));
        ((DateBox) endDateMonitor.getWidget()).getDatePicker().getParent().addHandler(
                new CloseHandler<PopupPanel>() {
                @Override
                public void onClose(CloseEvent event) {
                    endDateMonitor.validate();
                }
            }, CloseEvent.getType());
    }
}