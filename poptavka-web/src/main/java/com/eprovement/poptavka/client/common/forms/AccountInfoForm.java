package com.eprovement.poptavka.client.common.forms;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User registration widget represent user's registration form.
 * It creates BusinessUserDetail. Provides field validation.
 * @author Martin Slavkovsky
 */
public class AccountInfoForm extends Composite implements ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static CompanyInfoFormUiBinder uiBinder = GWT.create(CompanyInfoFormUiBinder.class);

    interface CompanyInfoFormUiBinder extends UiBinder<Widget, AccountInfoForm> {
    }
    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true)
    ValidationMonitor email, password, passwordConfirm;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    public AccountInfoForm() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        email = createValidationMonitor(UserField.EMAIL);
        password = createValidationMonitor(UserField.PASSWORD);
        passwordConfirm = createValidationMonitor(UserField.PASSWORD);
    }

    private ValidationMonitor createValidationMonitor(UserField field) {
        return new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class, field.getValue());
    }

    /**
     * Set action handlers that cannot be accessed by UiHandlers on widget load.
     */
    @Override
    public void onLoad() {
        ((TextBox) password.getWidget()).addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPasswordCheck();
            }
        });

        ((TextBox) passwordConfirm.getWidget()).addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPasswordConfirmCheck();
            }
        });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public ValidationMonitor getEmail() {
        return email;
    }

    public ValidationMonitor getPassword() {
        return password;
    }

    public ValidationMonitor getPasswordConfirm() {
        return passwordConfirm;
    }

    @Override
    public boolean isValid() {
        return email.isValid() && password.isValid() && passwordConfirm.isValid();
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Validate email field.
     * @param isAvailable
     */
    public void initVisualFreeEmailCheck(Boolean isAvailable) {
        if (isAvailable) {
            email.setExternalValidation(ControlGroupType.SUCCESS, Storage.MSGS.formUserRegMailAvailable());
        } else {
            email.setExternalValidation(ControlGroupType.ERROR, Storage.MSGS.formUserRegMailNotAvailable());
        }
    }

    /**
     * Validate password field.
     * @param event
     */
    private void initVisualPasswordCheck() {
        int passwordLength = ((String) password.getValue()).length();
        if ((passwordLength <= Constants.LONG_PASSWORD) && (passwordLength > Constants.SHORT_PASSWORD)) {
            password.setExternalValidation(ControlGroupType.WARNING, Storage.MSGS.formUserRegSemiStrongPassword());
        }
        if (passwordLength > Constants.LONG_PASSWORD) {
            password.setExternalValidation(ControlGroupType.SUCCESS, Storage.MSGS.formUserRegStrongPassword());
        }
    }

    /**
     * Validate password confirm field.
     * @param event
     */
    private void initVisualPasswordConfirmCheck() {
        if (!(password.getValue()).equals(passwordConfirm.getValue())) {
            passwordConfirm.setExternalValidation(ControlGroupType.ERROR, Storage.MSGS.formUserRegPasswordsUnmatch());
        } else {
            passwordConfirm.setExternalValidation(ControlGroupType.SUCCESS, Storage.MSGS.formUserRegPasswordsMatch());
        }
    }
}