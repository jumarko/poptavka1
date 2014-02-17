/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.forms;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User registration widget represent user's registration form.
 * It creates BusinessUserDetail. Provides field validation.
 * @author Martin Slavkovsky
 */
public class CompanyInfoForm extends Composite implements ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static CompanyInfoFormUiBinder uiBinder = GWT.create(CompanyInfoFormUiBinder.class);

    interface CompanyInfoFormUiBinder extends UiBinder<Widget, CompanyInfoForm> {
    }

    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor companyName, vatNumber, taxNumber;
    @UiField Tooltip taxIdTooltip;
    /** Class attributes. **/
    private final int tooltipDisplayTime = 5000;
    private Timer tooltipDisplayTimer = new Timer() {
        @Override
        public void run() {
            taxIdTooltip.hide();
        }
    };

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates CompanyInfoForm view's compotnents.
     */
    public CompanyInfoForm() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
        bindViewEvents();
    }

    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        companyName = createValidationMonitor(UserField.COMPANY_NAME);
        taxNumber = createValidationMonitor(UserField.TAX_NUMBER);
        vatNumber = createValidationMonitor(UserField.VAT_NUMBER);
    }

    /**
     * Creates validation monitor.
     * @param field - validation field
     * @return validation monitor
     */
    private ValidationMonitor createValidationMonitor(UserField field) {
        return new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class, field.getValue());
    }

    /**
     * Set action handlers that cannot be accessed by UiHandlers on widget load.
     */
    private void bindViewEvents() {
        ((TextBox) taxNumber.getWidget()).addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                taxIdTooltip.show();
                tooltipDisplayTimer.schedule(tooltipDisplayTime);
            }
        });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the company name validation monitor
     */
    public ValidationMonitor getCompanyName() {
        return companyName;
    }

    /**
     * @return the vat number validation monitor
     */
    public ValidationMonitor getVatNumber() {
        return vatNumber;
    }

    /**
     * @return the taxt number validation monitor
     */
    public ValidationMonitor getTaxNumber() {
        return taxNumber;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = companyName.isValid() && valid;
        valid = taxNumber.isValid() && valid;
        valid = vatNumber.isValid() && valid;
        return valid;
    }
}