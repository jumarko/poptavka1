package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.StatusIconLabel;
import com.eprovement.poptavka.client.main.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.main.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.SupplierDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.validation.client.Validation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class SupplierInfoView extends Composite
        implements SupplierInfoPresenter.SupplierInfoInterface, ProvidesValidate, Editor<FullSupplierDetail> {

    private static FormUserRegistrationUiBinder uiBinder = GWT.create(FormUserRegistrationUiBinder.class);

    interface FormUserRegistrationUiBinder extends UiBinder<Widget, SupplierInfoView> {
    }
    @UiField TextBox companyName;
    @UiField TextBox identificationNumber;
    @UiField TextBox taxId;
    @UiField TextBox website;
    @UiField @Ignore TextBox street;
    @UiField @Ignore TextBox city;
    @UiField @Ignore TextBox zip;
    @UiField TextBox firstName;
    @UiField TextBox lastName;
    @UiField TextBox phone;
    @UiField TextBox email;
    @UiField PasswordTextBox password;
    @UiField PasswordTextBox passwordConfirm;
    @UiField StatusIconLabel mailStatus;
    @UiField StatusIconLabel pwdStatus;
    @UiField StatusIconLabel pwdCheckStatus;
    @UiField TextArea description;
    private boolean mailFlag = false;
    private boolean passFlag = false;
    private boolean passLength = false;
    //Constants
    private static final int SHORT_PASSWORD = 5;
    private static final int LONG_PASSWORD = 8;
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    private final static String COMPANY_NAME = "companyName";
    private final static String IDENTIFICATION_NUMBER = "identificationNumber";
    private final static String TAX_ID = "taxId";
    private final static String STREET = "street";
    private final static String CITY = "city";
    private final static String ZIP = "zip";
    private final static String FIRST_NAME = "firstName";
    private final static String LAST_NAME = "lastName";
    private final static String PHONE = "phone";
    private final static String EMAIL = "email";
    private final static String PASSWORD = "password";
    private final static String PASSWORD_CONFIRM = "passwordConfirm";
    private final static String DESCRIPTION = "description";
    // Validation

    interface Driver extends SimpleBeanEditorDriver<FullSupplierDetail, SupplierInfoView> {
    }
    private SupplierInfoView.Driver driver = GWT.create(SupplierInfoView.Driver.class);
    private Validator validator = null;
    private FullSupplierDetail fullSupplierDetail = new FullSupplierDetail();
    private Set<String> valid = new HashSet<String>();
    private StatusIconLabel statusIconLabel = null;

    @Override
    public void createView() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
        this.driver.edit(fullSupplierDetail);

        companyName.setName(COMPANY_NAME);
        identificationNumber.setName(IDENTIFICATION_NUMBER);
        taxId.setName(TAX_ID);
        street.setName(STREET);
        city.setName(CITY);
        zip.setName(ZIP);
        firstName.setName(FIRST_NAME);
        lastName.setName(LAST_NAME);
        phone.setName(PHONE);
        email.setName(EMAIL);
        password.setName(PASSWORD);
        passwordConfirm.setName(PASSWORD_CONFIRM);
        description.setName(DESCRIPTION);

        companyName.addBlurHandler(blurHandler);
        identificationNumber.addBlurHandler(blurHandler);
        taxId.addBlurHandler(blurHandler);
        street.addBlurHandler(blurHandler);
        city.addBlurHandler(blurHandler);
        zip.addBlurHandler(blurHandler);
        firstName.addBlurHandler(blurHandler);
        lastName.addBlurHandler(blurHandler);
        phone.addBlurHandler(blurHandler);
        email.addBlurHandler(blurHandler);
        password.addBlurHandler(blurHandler);
        passwordConfirm.addBlurHandler(blurHandler);
        description.addBlurHandler(blurHandler);
    }
    private BlurHandler blurHandler = new BlurHandler() {

        @Override
        public void onBlur(BlurEvent event) {
            validateItem((TextBoxBase) event.getSource());
        }
    };

    private void validateItem(TextBoxBase item) {
        fullSupplierDetail = driver.flush();
        Set<ConstraintViolation<FullSupplierDetail>> violations = validator.validateValue(
                FullSupplierDetail.class, item.getName(), get(item.getName()), Default.class);
        displayErrors(item, violations);
    }

    private Object get(String item) {
        if (item.equals(COMPANY_NAME)) {
            return fullSupplierDetail.getCompanyName();
        } else if (item.equals(IDENTIFICATION_NUMBER)) {
            return fullSupplierDetail.getIdentificationNumber();
        } else if (item.equals(FIRST_NAME)) {
            return fullSupplierDetail.getFirstName();
        } else if (item.equals(LAST_NAME)) {
            return fullSupplierDetail.getLastName();
        } else if (item.equals(TAX_ID)) {
            return fullSupplierDetail.getTaxId();
        } else if (item.equals(PHONE)) {
            return fullSupplierDetail.getPhone();
        } else if (item.equals(EMAIL)) {
            return fullSupplierDetail.getEmail();
        } else if (item.equals(PASSWORD)) {
            return fullSupplierDetail.getPassword();
        } else if (item.equals(PASSWORD_CONFIRM)) {
            return fullSupplierDetail.getPasswordConfirm();
        } else if (item.equals(DESCRIPTION)) {
            return fullSupplierDetail.getDescription();
        } else {
            return null;
        }
    }

    @UiHandler("password")
    public void checkPassword(KeyUpEvent e) {
        initVisualPwdCheck(password.getText());
    }

    @UiHandler("passwordConfirm")
    public void checkPasswordConfirm(KeyUpEvent e) {
        initVisualPwdConfirmCheck();
    }

    @Override
    public boolean isValid() {
        validateItem(companyName);
        validateItem(identificationNumber);
        validateItem(taxId);
        validateItem(firstName);
        validateItem(lastName);
        validateItem(phone);
        validateItem(email);
        validateItem(password);
        validateItem(passwordConfirm);
        validateItem(description);
        this.statusIconLabel.setMessage("Wrong Inputs");
        return valid.size() == 0;
    }

    @Override
    public void setStatusIconLabel(StatusIconLabel statusIconLabel) {
        this.statusIconLabel = statusIconLabel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public HasValueChangeHandlers<String> getEmailBox() {
        return email;
    }

    @Override
    public StatusIconLabel getMailStatus() {
        return mailStatus;
    }

    @Override
    public StatusIconLabel getPwdStatus() {
        return pwdStatus;
    }

    @Override
    public StatusIconLabel getPwdConfirmStatus() {
        return pwdCheckStatus;
    }

    @Override
    public UserDetail createSupplier() {
        UserDetail user = new UserDetail();
        user.setEmail(email.getText());
        user.setPassword(password.getText());
        user.setCompanyName(companyName.getText());
        user.setIdentificationNumber(identificationNumber.getText());
        user.setTaxId(taxId.getText());
        SupplierDetail supplier = new SupplierDetail();
        supplier.setDescription(description.getValue());
        user.setSupplier(supplier);
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        AddressDetail address = new AddressDetail();
        address.setCityName(city.getText());
        address.setStreet(street.getText());
        address.setZipCode(zip.getText());

        List<AddressDetail> addresses = new ArrayList<AddressDetail>();
        addresses.add(address);
        user.setAddresses(addresses);

        return user;
    }

    @Override
    public void setMailFlag(boolean flag) {
        this.mailFlag = flag;
    }

    @Override
    public void setPasswordFlag(boolean flag) {
        this.passFlag = flag;
    }

    @Override
    public void setPasswordLengthFlag(boolean flag) {
        this.passLength = flag;
    }

    private void displayErrors(TextBoxBase item, Set<ConstraintViolation<FullSupplierDetail>> violations) {
        for (ConstraintViolation<FullSupplierDetail> violation : violations) {
            setError(item, ERROR_STYLE, violation.getMessage());
            valid.add(item.getName());
            return;
        }
        setError(item, NORMAL_STYLE, "");
        valid.remove(item.getName());
    }

    /**
     * Set style and error message to given item.
     *
     * @param item - use class constant CITY, STATE, STREET, ZIP
     * @param style - user class constant NORMAL_STYLE, ERROR_STYLE
     * @param errorMessage - message of item's ErrorLabel
     */
    private void setError(TextBoxBase item, String style, String errorMessage) {
        if (style.equals(ERROR_STYLE)) {
            this.statusIconLabel.setState(StatusIconLabel.State.ERROR_16);
        } else {
            this.statusIconLabel.setState(StatusIconLabel.State.ACCEPT_16);
        }
        this.statusIconLabel.setTexts(errorMessage, errorMessage);
        item.setStyleName(style);
    }

    private void initVisualPwdCheck(String value) {
        pwdStatus.setVisible(true);
        if (value.length() <= SHORT_PASSWORD) {

            // TODO change to global status changer eventBus call
            pwdStatus.setStateWithDescription(StatusIconLabel.State.ERROR_16, Storage.MSGS.shortPassword());
            setPasswordLengthFlag(false);
            return;
        }
        setPasswordLengthFlag(true);
        if ((value.length() <= LONG_PASSWORD) && (value.length() > SHORT_PASSWORD)) {
            setPasswordFlag(true);

            // TODO change to global status changer eventBus call
            getPwdStatus().setStateWithDescription(StatusIconLabel.State.INFO_16, Storage.MSGS.semiStrongPassword());
        }
        if (value.length() > LONG_PASSWORD) {
            setPasswordFlag(true);

            // TODO change to global status changer eventBus call
            pwdStatus.setStateWithDescription(StatusIconLabel.State.ACCEPT_16, Storage.MSGS.strongPassword());
        }
    }

    private void initVisualPwdConfirmCheck() {
        pwdCheckStatus.setVisible(true);
        if (!password.getText().equals(passwordConfirm.getText())) {
            setMailFlag(false);

            // TODO change to global status changer eventBus call
            pwdCheckStatus.setStateWithDescription(State.ERROR_16, Storage.MSGS.passwordsUnmatch());
        } else {
            setMailFlag(true);

            // TODO change to global status changer eventBus call
            pwdCheckStatus.setStateWithDescription(State.ACCEPT_16, Storage.MSGS.passwordsMatch());
        }
    }
}
