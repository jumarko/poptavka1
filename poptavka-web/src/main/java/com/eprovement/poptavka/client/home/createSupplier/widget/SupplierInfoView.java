package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.impl.Validation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class SupplierInfoView extends Composite
        implements SupplierInfoPresenter.SupplierInfoInterface, ProvidesValidate, Editor<FullSupplierDetail> {

    private static FormUserRegistrationUiBinder uiBinder = GWT.create(FormUserRegistrationUiBinder.class);

    interface FormUserRegistrationUiBinder extends UiBinder<Widget, SupplierInfoView> {
    }
    @UiField
    TextBox companyName, identificationNumber, taxId;
    @UiField TextBox firstName, lastName;
    @UiField TextBox phone, email, website;
    @UiField
    TextArea description;
//    @UiField TextBox street, city, zipCode;
    @UiField SimplePanel addressHolder;
    @UiField
    PasswordTextBox password, passwordConfirm;
    //StatusIconLabels
    @UiField StatusIconLabel mailStatus;
    @UiField StatusIconLabel passwordStatus;
    @UiField StatusIconLabel passwordCheckStatus;
    //Error Labels
    @UiField @Ignore
    Label companyNameErrorLabel;
    @UiField @Ignore Label identificationNumberErrorLabel;
    @UiField @Ignore Label taxIdErrorLabel;
    @UiField @Ignore Label websiteErrorLabel;
    @UiField @Ignore Label firstNameErrorLabel;
    @UiField @Ignore Label lastNameErrorLabel;
    @UiField @Ignore Label phoneErrorLabel;
    @UiField @Ignore Label emailErrorLabel;
    @UiField @Ignore Label passwordErrorLabel;
    @UiField @Ignore Label passwordConfirmErrorLabel;
    @UiField @Ignore Label descriptionErrorLabel;
    //Represent pairs <input,inputErrorLabel>
    private Map<TextBoxBase, Label> widgets = new HashMap<TextBoxBase, Label>();
    //Constants
    private static final int SHORT_PASSWORD = 5;
    private static final int LONG_PASSWORD = 8;
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    private final static String COMPANY_NAME = "companyName";
    private final static String IDENTIFICATION_NUMBER = "identificationNumber";
    private final static String TAX_ID = "taxId";
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
//    private StatusIconLabel statusIconLabel = null;
    //
    private BlurHandler blurHandler = new BlurHandler() {

        @Override
        public void onBlur(BlurEvent event) {
            validateItem((TextBoxBase) event.getSource());
        }
    };

    @Override
    public void createView() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
        this.driver.edit(fullSupplierDetail);

        //Fake data
        companyName.setText(COMPANY_NAME);
        identificationNumber.setText("12345");
        taxId.setText(TAX_ID);
        firstName.setText(FIRST_NAME);
        lastName.setText(LAST_NAME);
        phone.setText("0932321");
        email.setText("email@len.tak");
        password.setText("kreslo");
        passwordConfirm.setText("kreslo");
        description.setText("Data len tak aby netrebalo furt zadavat.");

        //Link TextBox with Constraint
        companyName.setName(COMPANY_NAME);
        identificationNumber.setName(IDENTIFICATION_NUMBER);
        taxId.setName(TAX_ID);
        firstName.setName(FIRST_NAME);
        lastName.setName(LAST_NAME);
        phone.setName(PHONE);
        email.setName(EMAIL);
        password.setName(PASSWORD);
        passwordConfirm.setName(PASSWORD_CONFIRM);
        description.setName(DESCRIPTION);

        //Make collection of TextBoxes and their Labels
        widgets.put(companyName, companyNameErrorLabel);
        widgets.put(identificationNumber, identificationNumberErrorLabel);
        widgets.put(taxId, taxIdErrorLabel);
        widgets.put(firstName, firstNameErrorLabel);
        widgets.put(lastName, lastNameErrorLabel);
        widgets.put(email, emailErrorLabel);
        widgets.put(password, passwordErrorLabel);
        widgets.put(passwordConfirm, passwordConfirmErrorLabel);
        widgets.put(phone, phoneErrorLabel);
        widgets.put(description, descriptionErrorLabel);

        //Add BlurHandler to all TextBoxes
        for (TextBoxBase box : widgets.keySet()) {
            box.addBlurHandler(blurHandler);
        }
    }

    private boolean validateItem(TextBoxBase item) {
        fullSupplierDetail = driver.flush();
        Set<ConstraintViolation<FullSupplierDetail>> violations = validator.validateValue(
                FullSupplierDetail.class, item.getName(), get(item.getName()), Default.class);
        displayErrors(item, violations);
        return violations.isEmpty();
    }

    @Override
    public boolean validateEmail() {
        return validateItem(email);
    }

    /**
     * Get object's attribute according to given attribute's name.
     *
     * @param attributeName
     * @return attribute value
     */
    private Object get(String attributeName) {
        if (attributeName.equals(COMPANY_NAME)) {
            return fullSupplierDetail.getCompanyName();
        } else if (attributeName.equals(IDENTIFICATION_NUMBER)) {
            return fullSupplierDetail.getIdentificationNumber();
        } else if (attributeName.equals(FIRST_NAME)) {
            return fullSupplierDetail.getFirstName();
        } else if (attributeName.equals(LAST_NAME)) {
            return fullSupplierDetail.getLastName();
        } else if (attributeName.equals(TAX_ID)) {
            return fullSupplierDetail.getTaxId();
        } else if (attributeName.equals(PHONE)) {
            return fullSupplierDetail.getPhone();
        } else if (attributeName.equals(EMAIL)) {
            return fullSupplierDetail.getEmail();
        } else if (attributeName.equals(PASSWORD)) {
            return fullSupplierDetail.getPassword();
        } else if (attributeName.equals(PASSWORD_CONFIRM)) {
            return fullSupplierDetail.getPasswordConfirm();
        } else if (attributeName.equals(DESCRIPTION)) {
            return fullSupplierDetail.getDescription();
        } else {
            return null;
        }
    }

    @Override
    public boolean isValid() {
        for (TextBoxBase box : widgets.keySet()) {
            if (!box.getName().equals(PASSWORD) || !box.getName().equals(PASSWORD_CONFIRM)) {
                validateItem(box);
            }
        }
        initVisualPasswordCheck(null);
        initVisualPasswordConfirmCheck(null);
        boolean validAddress = ((AddressSelectorView) addressHolder.getWidget()).isValid();
        return (valid.isEmpty() && validAddress);
    }

    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

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
    public BusinessUserDetail createSupplier() {
        BusinessUserDetail user = new BusinessUserDetail();
        user.setEmail(email.getText());
        user.setPassword(password.getText());
        user.setCompanyName(companyName.getText());
        user.setIdentificationNumber(identificationNumber.getText());
        user.setTaxId(taxId.getText());
        FullSupplierDetail supplier = new FullSupplierDetail();
        supplier.setDescription(description.getValue());
        user.setSupplier(supplier);
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        //Address
        user.addAddress(((AddressSelectorView) addressHolder.getWidget()).createAddress());

        return user;
    }

    private void displayErrors(TextBoxBase item, Set<ConstraintViolation<FullSupplierDetail>> violations) {
        for (ConstraintViolation<FullSupplierDetail> violation : violations) {
            this.widgets.get(item).setText(violation.getMessage());
            item.setStyleName(ERROR_STYLE);
            valid.add(item.getName());
            return;
        }
        this.widgets.get(item).setText("");
        item.setStyleName(NORMAL_STYLE);
        valid.remove(item.getName());
    }

    @UiHandler("password")
    public void initVisualPasswordCheck(KeyUpEvent event) {
        if (validateItem(password)) {
            passwordStatus.setVisible(true);
            if (password.getText().length() <= SHORT_PASSWORD) {
                passwordStatus.setStateWithDescription(
                        StatusIconLabel.State.ERROR_16,
                        Storage.MSGS.formUserRegShortPassword());
                return;
            }
            if ((password.getText().length() <= LONG_PASSWORD) && (password.getText().length() > SHORT_PASSWORD)) {
                passwordStatus.setStateWithDescription(
                        StatusIconLabel.State.INFO_16,
                        Storage.MSGS.formUserRegSemiStrongPassword());
            }
            if (password.getText().length() > LONG_PASSWORD) {
                passwordStatus.setStateWithDescription(
                        StatusIconLabel.State.ACCEPT_16,
                        Storage.MSGS.formUserRegStrongPassword());
            }
        } else {
            passwordStatus.setVisible(false);
        }
    }

    @UiHandler("passwordConfirm")
    public void initVisualPasswordConfirmCheck(KeyUpEvent event) {
        if (validateItem(passwordConfirm)) {
            passwordCheckStatus.setVisible(true);
            if (!password.getText().equals(passwordConfirm.getText())) {
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
