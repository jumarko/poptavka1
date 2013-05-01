package com.eprovement.poptavka.client.common.userRegistration;

import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;
import javax.validation.groups.Default;

/**
 * User registration widget represent user's registration form.
 * It creates BusinessUserDetail. Provides field validation.
 * @author Martin Slavkovsky
 */
public class UserRegistrationFormView extends Composite
        implements UserRegistrationFormPresenter.AccountFormInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static AccountRegistrationFormViewUiBinder uiBinder = GWT.create(AccountRegistrationFormViewUiBinder.class);

    interface AccountRegistrationFormViewUiBinder extends UiBinder<Widget, UserRegistrationFormView> {
    }
    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor phone, email, firstName, lastName, password, passwordConfirm;
    @UiField(provided = true) ValidationMonitor website, companyName, identificationNumber, taxId, description;
    @UiField SimplePanel addressHolder;
    @UiField HTMLPanel companyChoicePanel;
    @UiField Button personBtn, companyBtn;
    @UiField FluidRow companyInfoPanel;
    /** Class attributes. **/
    private List<ValidationMonitor> validationMonitorsPersonalOnly;
    private List<ValidationMonitor> validationMonitorsCompanyOnly;
    private boolean companySelected;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));

        //set validation monitors as array for easier access.
        validationMonitorsPersonalOnly = Arrays.asList(
                phone, email, firstName, lastName, password, passwordConfirm, description);
        validationMonitorsCompanyOnly = Arrays.asList(companyName, taxId,
                phone, email, firstName, lastName, password, passwordConfirm, description);

        setCompanyPanelVisibility(false);
    }

    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        firstName = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, UserField.FIRST_NAME.getValue());
        lastName = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, UserField.LAST_NAME.getValue());
        phone = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, UserField.PHONE.getValue());
        email = new ValidationMonitor<UserDetail>(
                UserDetail.class, Default.class, UserField.EMAIL.getValue());
        password = new ValidationMonitor<UserDetail>(
                UserDetail.class, Default.class, UserField.PASSWORD.getValue());
        passwordConfirm = new ValidationMonitor<UserDetail>(
                UserDetail.class, Default.class, UserField.PASSWORD.getValue());
        companyName = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, UserField.COMPANY_NAME.getValue());
        description = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, UserField.DESCRIPTION.getValue());
        website = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, UserField.WEBSITE.getValue());
        identificationNumber = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, UserField.IDENTIF_NUMBER.getValue());
        taxId = new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, UserField.TAX_ID.getValue());
    }

    /**
     * Set action handlers that cannot be accessed by UiHandlers on widget load.
     */
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
        //website is not necessary therefore if user type same website,
        //validate its input by initialization of validation monitor, but when
        //user end up leaving empty box after playing with input,
        //reset validation monitor that was initialized by that inputs.
        ((TextBox) website.getWidget()).addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (((TextBox) website.getWidget()).getText().isEmpty()) {
                    website.reset();
                }
            }
        });
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("personBtn")
    public void personRadioClickHandler(ClickEvent e) {
        companySelected = false;
        setCompanyPanelVisibility(false);
    }

    @UiHandler("companyBtn")
    public void personCompanyClickHandler(ClickEvent e) {
        companySelected = true;
        setCompanyPanelVisibility(true);
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Toggle company info.
     * @param showCompanyPanel
     */
    public void setCompanyPanelVisibility(boolean showCompanyPanel) {
        companyInfoPanel.setVisible(showCompanyPanel);
        if (showCompanyPanel) {
            companyChoicePanel.removeStyleName(Storage.RSCS.common().switchLeft());
            companyChoicePanel.addStyleName(Storage.RSCS.common().switchRight());
        } else {
            companyChoicePanel.removeStyleName(Storage.RSCS.common().switchRight());
            companyChoicePanel.addStyleName(Storage.RSCS.common().switchLeft());
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public boolean isValid() {
        boolean valid = true;
        if (companySelected) {
            for (ValidationMonitor box : validationMonitorsCompanyOnly) {
                valid = box.isValid() && valid;
            }
        }
        for (ValidationMonitor box : validationMonitorsPersonalOnly) {
            valid = box.isValid() && valid;
        }
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
    public ValidationMonitor getEmailBox() {
        return email;
    }

    @Override
    public BusinessUserDetail createBusinessUserDetail() {
        BusinessUserDetail user = new BusinessUserDetail();
        user.setEmail((String) email.getValue());
        user.setPassword((String) password.getValue());
        user.setPersonFirstName((String) firstName.getValue());
        user.setPersonLastName((String) lastName.getValue());
        user.setPhone((String) phone.getValue());
        user.setCompanyName((String) companyName.getValue());
        user.setDescription((String) description.getValue());
        user.setIdentificationNumber((String) identificationNumber.getValue());
        user.setTaxId((String) taxId.getValue());
        user.setWebsite((String) website.getValue());
        user.setAddresses(Arrays.asList(((AddressSelectorView) addressHolder.getWidget()).createAddress()));
        return user;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Validate email field.
     * @param isAvailable
     */
    @Override
    public void initVisualFreeEmailCheck(Boolean isAvailable) {
        email.getErrorPanel().setVisible(true);
        email.setHideErrorPanel(false);
        if (isAvailable) {
            email.getErrorLabel().setText(Storage.MSGS.formUserRegMailAvailable());
            email.getErrorLabel().setType(LabelType.SUCCESS);
            email.getControlGroup().setType(ControlGroupType.SUCCESS);
        } else {
            email.setValid(false);
            email.getErrorLabel().setText(Storage.MSGS.formUserRegMailNotAvailable());
            email.getErrorLabel().setType(LabelType.IMPORTANT);
            email.getControlGroup().setType(ControlGroupType.ERROR);
        }
    }

    /**
     * Validate password field.
     * @param event
     */
    private void initVisualPasswordCheck(KeyUpEvent event) {
        password.setHideErrorPanel(false);
        int passwordLength = ((String) password.getValue()).length();
        if ((passwordLength <= Constants.LONG_PASSWORD) && (passwordLength > Constants.SHORT_PASSWORD)) {
            password.getErrorPanel().setVisible(true);
            password.getControlGroup().setType(ControlGroupType.WARNING);
            password.getErrorLabel().setText(Storage.MSGS.formUserRegSemiStrongPassword());
            password.getErrorLabel().setType(LabelType.WARNING);
        }
        if (passwordLength > Constants.LONG_PASSWORD) {
            password.getErrorPanel().setVisible(true);
            password.getControlGroup().setType(ControlGroupType.SUCCESS);
            password.getErrorLabel().setText(Storage.MSGS.formUserRegStrongPassword());
            password.getErrorLabel().setType(LabelType.SUCCESS);
        }
    }

    /**
     * Validate password confirm field.
     * @param event
     */
    private void initVisualPasswordConfirmCheck(KeyUpEvent event) {
        passwordConfirm.setHideErrorPanel(false);
        if (!(password.getValue()).equals(passwordConfirm.getValue())) {
            passwordConfirm.getErrorPanel().setVisible(true);
            passwordConfirm.getControlGroup().setType(ControlGroupType.ERROR);
            passwordConfirm.getErrorLabel().setText(Storage.MSGS.formUserRegPasswordsUnmatch());
            passwordConfirm.getErrorLabel().setType(LabelType.IMPORTANT);
        } else {
            passwordConfirm.setValid(false);
            passwordConfirm.getErrorPanel().setVisible(true);
            passwordConfirm.getControlGroup().setType(ControlGroupType.SUCCESS);
            passwordConfirm.getErrorLabel().setText(Storage.MSGS.formUserRegPasswordsMatch());
            passwordConfirm.getErrorLabel().setType(LabelType.SUCCESS);
        }
    }
}