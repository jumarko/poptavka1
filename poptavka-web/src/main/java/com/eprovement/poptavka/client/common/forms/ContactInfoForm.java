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
public class ContactInfoForm extends Composite implements ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ContactInfoFormUiBinder uiBinder = GWT.create(ContactInfoFormUiBinder.class);

    interface ContactInfoFormUiBinder extends UiBinder<Widget, ContactInfoForm> {
    }

    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor phone, firstName, lastName;
    @UiField Tooltip phoneTooltip;
    /** Class attributes. **/
    private final int tooltipDisplayTime = 5000;
    private Timer tooltipDisplayTimer = new Timer() {

        @Override
        public void run() {
            phoneTooltip.hide();
        }
    };


    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates ContanctInfoForm view's compotnents.
     */
    public ContactInfoForm() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
        bindViewEvents();
    }

    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        firstName = createValidationMonitor(UserField.FIRST_NAME);
        lastName = createValidationMonitor(UserField.LAST_NAME);
        phone = createValidationMonitor(UserField.PHONE);
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
        ((TextBox) phone.getWidget()).addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                phoneTooltip.show();
                tooltipDisplayTimer.schedule(tooltipDisplayTime);
            }
        });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the phone validation monitor
     */
    public ValidationMonitor getPhone() {
        return phone;
    }

    /**
     * @return the first name validation monitor
     */
    public ValidationMonitor getFirstName() {
        return firstName;
    }

    /**
     * @return the last name validation monitor
     */
    public ValidationMonitor getLastName() {
        return lastName;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = firstName.isValid() && valid;
        valid = lastName.isValid() && valid;
        valid = phone.isValid() && valid;
        return valid;
    }
}