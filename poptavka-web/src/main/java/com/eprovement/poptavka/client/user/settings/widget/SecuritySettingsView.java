/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * View consists of forms for changing password and email (not implemented yet).
 * @author Martin Slavkovsky
 */
public class SecuritySettingsView extends Composite
        implements SecuritySettingsPresenter.SecuritySettingsViewInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static UserSettingsViewUiBinder uiBinder = GWT.create(UserSettingsViewUiBinder.class);

    interface UserSettingsViewUiBinder extends UiBinder<Widget, SecuritySettingsView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor emailMonitor;
    @UiField(provided = true) ValidationMonitor passwordCurrentMonitor, passwordNewMonitor, passwordNewConfirmMonitor;
    @UiField Button changeBtn;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates SecuritySettings view's compontents.
     */
    @Override
    public void createView() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Inits validation monitors.
     */
    private void initValidationMonitors() {
        emailMonitor = createBusinessUserValidationMonitor(BusinessUserDetail.UserField.EMAIL);
        passwordCurrentMonitor = createBusinessUserValidationMonitor(BusinessUserDetail.UserField.PASSWORD);
        passwordNewMonitor = createBusinessUserValidationMonitor(BusinessUserDetail.UserField.PASSWORD);
        passwordNewConfirmMonitor = createBusinessUserValidationMonitor(BusinessUserDetail.UserField.PASSWORD);
    }

    /**
     * Creates validation monitors
     * @param field - validation field
     * @return created validation monitor
     */
    private ValidationMonitor createBusinessUserValidationMonitor(BusinessUserDetail.UserField fieldField) {
        return new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class, fieldField.getValue());
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets security settings data.
     * @param detail object carring security data
     */
    @Override
    public void setSecuritySettings(SettingDetail detail) {
        emailMonitor.setValue(detail.getUser().getEmail());
    }

    /**
     * Sets password validation monitor styles.
     * @param correct true if valid, false if invalid
     */
    @Override
    public void setCurrentPasswordStyles(boolean correct) {
        if (correct) {
            passwordCurrentMonitor.setExternalValidation(ControlGroupType.SUCCESS, "");
        } else {
            passwordCurrentMonitor.setExternalValidation(ControlGroupType.ERROR,
                    Storage.MSGS.userSettingsPasswordIncorrect());
        }
    }

    /**
     * Resets validation monitors' styles.
     */
    @Override
    public void setDefaultPasswordsStyles() {
        passwordCurrentMonitor.resetValidation();
        passwordNewMonitor.resetValidation();
        passwordNewConfirmMonitor.resetValidation();
        ((TextBox) passwordCurrentMonitor.getWidget()).setText("");
        ((TextBox) passwordNewMonitor.getWidget()).setText("");
        ((TextBox) passwordNewConfirmMonitor.getWidget()).setText("");
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @return the email validation monitor
     */
    @Override
    public ValidationMonitor getEmailMonitor() {
        return emailMonitor;
    }

    /**
     * @return the current password validation monitor
     */
    @Override
    public ValidationMonitor getPasswordCurrentMonitor() {
        return passwordCurrentMonitor;
    }

    /**
     * @return the new password validation monitor
     */
    @Override
    public ValidationMonitor getPasswordNewMonitor() {
        return passwordNewMonitor;
    }

    /**
     * @return the new password confirm validation monitor
     */
    @Override
    public ValidationMonitor getPasswordNewConfirmMonitor() {
        return passwordNewConfirmMonitor;
    }

    /**
     * @return the change button
     */
    @Override
    public Button getChangeBtn() {
        return changeBtn;
    }

    /**
     * Validates if password change form is valid.
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isNewPasswordValid() {
        boolean valid = true;
        valid = passwordCurrentMonitor.isValid() && valid;
        valid = passwordNewMonitor.isValid() && valid;
        valid = passwordNewConfirmMonitor.isValid() && valid;
        return valid;
    }

    /**
     * @return widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
