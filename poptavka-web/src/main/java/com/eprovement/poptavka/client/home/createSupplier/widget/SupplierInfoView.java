package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

public class SupplierInfoView extends Composite
        implements SupplierInfoPresenter.SupplierInfoInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierInfoViewUiBinder uiBinder = GWT.create(SupplierInfoViewUiBinder.class);

    interface SupplierInfoViewUiBinder extends UiBinder<Widget, SupplierInfoView> {
    }
    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true)
    ValidationMonitor phone, email, website, firstName, lastName;
    @UiField(provided = true)
    ValidationMonitor companyName, identificationNumber, taxId;
    @UiField(provided = true)
    ValidationMonitor description, password, passwordConfirm;
    @UiField
    SimplePanel addressHolder;
    @UiField
    StatusIconLabel mailStatus;
    @UiField
    StatusIconLabel passwordStatus;
    @UiField
    StatusIconLabel passwordCheckStatus;
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
        companyName = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        description = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        firstName = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        lastName = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        phone = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        website = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        email = new ValidationMonitor<UserDetail>(UserDetail.class);
        password = new ValidationMonitor<UserDetail>(UserDetail.class);
        passwordConfirm = new ValidationMonitor<UserDetail>(UserDetail.class);
        identificationNumber = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);
        taxId = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class);

        initWidget(uiBinder.createAndBindUi(this));

        validationMonitors = Arrays.asList(phone, email, website, firstName, lastName,
                companyName, identificationNumber, taxId, description, password, passwordConfirm);
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
        companyName.isValid();
        boolean validAddress = ((AddressSelectorView) addressHolder.getWidget()).isValid();
        return valid && validAddress;
    }

    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
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
    public BusinessUserDetail createBusinessUserDetail() {
        BusinessUserDetail user = new BusinessUserDetail();
        user.setEmail((String) email.getValue());
        user.setPassword((String) password.getValue());
        user.setCompanyName((String) companyName.getValue());
        user.setDescription((String) description.getValue());
        user.setFirstName((String) firstName.getValue());
        user.setLastName((String) lastName.getValue());
        user.setPhone((String) phone.getValue());
        user.setIdentificationNumber((String) identificationNumber.getValue());
        user.setTaxId((String) taxId.getValue());
        user.setWebsite((String) website.getValue());
        user.setAddresses(Arrays.asList(((AddressSelectorView) addressHolder.getWidget()).createAddress()));
        return user;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void initVisualPasswordCheck(KeyUpEvent event) {
        if (password.isValid()) {
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
        } else {
            passwordStatus.setVisible(false);
        }
    }

    private void initVisualPasswordConfirmCheck(KeyUpEvent event) {
        if (passwordConfirm.isValid()) {
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
        } else {
            passwordCheckStatus.setVisible(false);
        }
    }
}
