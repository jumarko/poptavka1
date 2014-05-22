/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.forms;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserDataField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * User registration widget represent user's registration form.
 * It creates BusinessUserDetail. Provides field validation.
 * @author Martin Slavkovsky
 */
public class AdditionalInfoForm extends Composite implements ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static CompanyInfoFormUiBinder uiBinder = GWT.create(CompanyInfoFormUiBinder.class);

    interface CompanyInfoFormUiBinder extends UiBinder<Widget, AdditionalInfoForm> {
    }

    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor website, description;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates AdditionalInfoForm view's compotnents.
     */
    public AdditionalInfoForm() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        website = createValidationMonitor(UserDataField.WEBSITE);
        description = createValidationMonitor(UserDataField.DESCRIPTION);
    }

    /**
     * Creates validation monitor.
     * @param field - validation field
     * @return validation monitor
     */
    private ValidationMonitor createValidationMonitor(UserDataField field) {
        return new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class, field.getValue());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the website validation mointor
     */
    public ValidationMonitor getWebsite() {
        return website;
    }

    /**
     * @return the description validation monitor
     */
    public ValidationMonitor getDescription() {
        return description;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        website.reset();
        description.reset();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = website.isValid() && valid;
        valid = description.isValid() && valid;
        return valid;
    }
}