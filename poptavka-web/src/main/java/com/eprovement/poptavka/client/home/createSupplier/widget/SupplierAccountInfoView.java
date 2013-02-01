package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

public class SupplierAccountInfoView extends Composite
        implements SupplierAccountInfoPresenter.SupplierAccountInfoInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierAccountInfoViewUiBinder uiBinder = GWT.create(SupplierAccountInfoViewUiBinder.class);

    interface SupplierAccountInfoViewUiBinder extends UiBinder<Widget, SupplierAccountInfoView> {
    }
    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor phone, email, firstName, lastName, password, passwordConfirm;
    /** Class attributes. **/
    private List<ValidationMonitor> validationMonitors;
    //Constants
    private static final int SHORT_PASSWORD = 5;
    private static final int LONG_PASSWORD = 8;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        firstName = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        lastName = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        phone = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        email = new ValidationMonitor<UserDetail>(UserDetail.class);
        password = new ValidationMonitor<UserDetail>(UserDetail.class);
        passwordConfirm = new ValidationMonitor<UserDetail>(UserDetail.class);

        initWidget(uiBinder.createAndBindUi(this));

        validationMonitors = Arrays.asList(phone, email, firstName, lastName, password, passwordConfirm);
    }

    @Override
    public void onLoad() {
        ((TextBox) password.getWidget()).addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPasswordCheck(event);
            }
        });

        ((TextBox) passwordConfirm.getWidget()).addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPasswordConfirmCheck(event);
            }
        });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public boolean isValid() {
        boolean valid = true;
        for (ValidationMonitor box : validationMonitors) {
            valid = box.isValid() && valid;
        }
        return valid;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public ValidationMonitor getEmailBox() {
        return email;
    }

    @Override
    public BusinessUserDetail updateBusinessUserDetail(BusinessUserDetail user) {
        user.setEmail((String) email.getValue());
        user.setPassword((String) password.getValue());
        user.setFirstName((String) firstName.getValue());
        user.setLastName((String) lastName.getValue());
        user.setPhone((String) phone.getValue());
        return user;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    @Override
    public void initVisualFreeEmailCheck(Boolean isAvailable) {
        email.getErrorPanel().setVisible(true);
        email.setHideErrorPanel(false);
        if (isAvailable) {
            email.getErrorLabel().setText(Storage.MSGS.formUserRegMailAvailable());
            email.getErrorLabel().setType(LabelType.SUCCESS);
            email.getControlGroup().setType(ControlGroupType.SUCCESS);
        } else {
            email.getErrorLabel().setText(Storage.MSGS.formUserRegMailNotAvailable());
            email.getErrorLabel().setType(LabelType.IMPORTANT);
            email.getControlGroup().setType(ControlGroupType.ERROR);
        }
    }

    private void initVisualPasswordCheck(KeyUpEvent event) {
        password.setHideErrorPanel(false);
        int passwordLength = ((String) password.getValue()).length();
        if ((passwordLength <= LONG_PASSWORD) && (passwordLength > SHORT_PASSWORD)) {
            password.getErrorPanel().setVisible(true);
            password.getControlGroup().setType(ControlGroupType.WARNING);
            password.getErrorLabel().setText(Storage.MSGS.formUserRegSemiStrongPassword());
            password.getErrorLabel().setType(LabelType.WARNING);
        }
        if (passwordLength > LONG_PASSWORD) {
            password.getErrorPanel().setVisible(true);
            password.getControlGroup().setType(ControlGroupType.SUCCESS);
            password.getErrorLabel().setText(Storage.MSGS.formUserRegStrongPassword());
            password.getErrorLabel().setType(LabelType.SUCCESS);
        }
    }

    private void initVisualPasswordConfirmCheck(KeyUpEvent event) {
        passwordConfirm.setHideErrorPanel(false);
        if (!((String) password.getValue()).equals(passwordConfirm.getValue())) {
            passwordConfirm.getErrorPanel().setVisible(true);
            passwordConfirm.getControlGroup().setType(ControlGroupType.ERROR);
            passwordConfirm.getErrorLabel().setText(Storage.MSGS.formUserRegPasswordsUnmatch());
            passwordConfirm.getErrorLabel().setType(LabelType.IMPORTANT);
        } else {
            passwordConfirm.getErrorPanel().setVisible(true);
            passwordConfirm.getControlGroup().setType(ControlGroupType.SUCCESS);
            passwordConfirm.getErrorLabel().setText(Storage.MSGS.formUserRegPasswordsMatch());
            passwordConfirm.getErrorLabel().setType(LabelType.SUCCESS);
        }
    }
}
