package com.eprovement.poptavka.client.common.forms;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
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
    public AdditionalInfoForm() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        website = createValidationMonitor(UserField.WEBSITE);
        description = createValidationMonitor(UserField.DESCRIPTION);
    }

    private ValidationMonitor createValidationMonitor(UserField field) {
        return new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class, field.getValue());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public ValidationMonitor getWebsite() {
        return website;
    }

    public ValidationMonitor getDescription() {
        return description;
    }

    @Override
    public boolean isValid() {
        return website.isValid() && description.isValid();
    }
}