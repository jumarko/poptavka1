/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.interfaces.ISecuritySettings;
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
public class SecuritySettingsView extends Composite implements ISecuritySettings.View {

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
     * {@inheritDoc}
     */
    @Override
    public void setSecuritySettings(SettingDetail detail) {
        emailMonitor.setValue(detail.getUser().getEmail());
    }

    /**
     * {@inheritDoc}
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

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationMonitor getEmailMonitor() {
        return emailMonitor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationMonitor getPasswordCurrentMonitor() {
        return passwordCurrentMonitor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationMonitor getPasswordNewMonitor() {
        return passwordNewMonitor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationMonitor getPasswordNewConfirmMonitor() {
        return passwordNewConfirmMonitor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Button getChangeBtn() {
        return changeBtn;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        passwordCurrentMonitor.reset();
        passwordNewMonitor.reset();
        passwordNewConfirmMonitor.reset();
        ((TextBox) passwordCurrentMonitor.getWidget()).setText("");
        ((TextBox) passwordNewMonitor.getWidget()).setText("");
        ((TextBox) passwordNewConfirmMonitor.getWidget()).setText("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return true;
    }
}
