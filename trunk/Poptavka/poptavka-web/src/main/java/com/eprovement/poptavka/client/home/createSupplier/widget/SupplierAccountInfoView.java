package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
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
    @UiField StatusIconLabel mailStatus;
    @UiField StatusIconLabel passwordStatus;
    @UiField StatusIconLabel passwordCheckStatus;
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
    public boolean validateEmail() {
        return email.isValid();
    }

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
    public HasValueChangeHandlers<String> getEmailBox() {
        return (TextBox) email.getWidget();
    }

    @Override
    public StatusIconLabel getMailStatus() {
        return mailStatus;
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
    private void initVisualPasswordCheck(KeyUpEvent event) {
        passwordStatus.setVisible(true);
        int passwordLength = ((String) password.getValue()).length();
        if (passwordLength <= SHORT_PASSWORD) {
            passwordStatus.setStateWithDescription(
                    StatusIconLabel.State.ERROR_16,
                    Storage.MSGS.formUserRegShortPassword());
            return;
        }
        if ((passwordLength <= LONG_PASSWORD) && (passwordLength > SHORT_PASSWORD)) {
            passwordStatus.setStateWithDescription(
                    StatusIconLabel.State.INFO_16,
                    Storage.MSGS.formUserRegSemiStrongPassword());
        }
        if (passwordLength > LONG_PASSWORD) {
            passwordStatus.setStateWithDescription(
                    StatusIconLabel.State.ACCEPT_16,
                    Storage.MSGS.formUserRegStrongPassword());
        }
    }

    private void initVisualPasswordConfirmCheck(KeyUpEvent event) {
        passwordCheckStatus.setVisible(true);
        if (!((String) password.getValue()).equals(passwordConfirm.getValue())) {
            passwordCheckStatus.setStateWithDescription(
                    State.ERROR_16,
                    Storage.MSGS.formUserRegPasswordsUnmatch());
        } else {
            passwordCheckStatus.setStateWithDescription(
                    State.ACCEPT_16,
                    Storage.MSGS.formUserRegPasswordsMatch());
        }
    }
}
