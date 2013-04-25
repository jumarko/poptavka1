/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;
import javax.validation.groups.Default;

/**
 *
 * @author Martin Slavkovsky
 */
public class UserSettingsView extends Composite implements UserSettingsPresenter.UserSettingsViewInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static UserSettingsView.UserSettingsViewUiBinder uiBinder = GWT
            .create(UserSettingsView.UserSettingsViewUiBinder.class);

    interface UserSettingsViewUiBinder extends
            UiBinder<Widget, UserSettingsView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ChangeMonitor companyNameMonitor, websiteMonitor, emailMonitor, phoneMonitor;
    @UiField(provided = true) ChangeMonitor firstNameMonitor, lastNameMonitor, identifNumberMonitor;
    @UiField(provided = true) ChangeMonitor descriptionMonitor, taxNumberMonitor;
    @UiField(provided = true) ValidationMonitor passwordCurrentMonitor, passwordNewMonitor, passwordNewConfirmMonitor;
    @UiField Button changeBtn;
    @UiField SimplePanel addressHolder;
    /** Class attributes. **/
    private List<ChangeMonitor> monitors;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initChangeMonitors();
        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    private void initChangeMonitors() {
        companyNameMonitor = createBusinessUserChangeMonitor(UserField.COMPANY_NAME);
        websiteMonitor = createBusinessUserChangeMonitor(UserField.WEBSITE);
        emailMonitor = createBusinessUserChangeMonitor(UserField.EMAIL);
        phoneMonitor = createBusinessUserChangeMonitor(UserField.PHONE);
        descriptionMonitor = createBusinessUserChangeMonitor(UserField.DESCRIPTION);
        firstNameMonitor = createBusinessUserChangeMonitor(UserField.FIRST_NAME);
        lastNameMonitor = createBusinessUserChangeMonitor(UserField.LAST_NAME);
        identifNumberMonitor = createBusinessUserChangeMonitor(UserField.IDENTIF_NUMBER);
        taxNumberMonitor = createBusinessUserChangeMonitor(UserField.TAX_ID);
        descriptionMonitor = createBusinessUserChangeMonitor(UserField.DESCRIPTION);
        monitors = Arrays.asList(
                companyNameMonitor, websiteMonitor, emailMonitor, phoneMonitor, descriptionMonitor,
                firstNameMonitor, lastNameMonitor, identifNumberMonitor, taxNumberMonitor);
    }

    private void initValidationMonitors() {
        passwordCurrentMonitor = createBusinessUserValidationMonitor(UserField.PASSWORD);
        passwordNewMonitor = createBusinessUserValidationMonitor(UserField.PASSWORD);
        passwordNewConfirmMonitor = createBusinessUserValidationMonitor(UserField.PASSWORD);
    }

    private ChangeMonitor createBusinessUserChangeMonitor(UserField fieldField) {
        return new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(fieldField.getValue()));
    }

    private ValidationMonitor createBusinessUserValidationMonitor(UserField fieldField) {
        return new ValidationMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, Default.class, fieldField.getValue());
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Set action handlers that cannot be accessed by UiHandlers on widget load.
     */
    @Override
    public void onLoad() {
        /** Focus **/
        ((TextBox) passwordCurrentMonitor.getWidget()).addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                passwordCurrentMonitor.reset();
                ((TextBox) passwordCurrentMonitor.getWidget()).setText("");
            }
        });
        ((TextBox) passwordNewMonitor.getWidget()).addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                passwordNewMonitor.reset();
                ((TextBox) passwordNewMonitor.getWidget()).setText("");
            }
        });
        ((TextBox) passwordNewConfirmMonitor.getWidget()).addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                passwordNewConfirmMonitor.reset();
                ((TextBox) passwordNewConfirmMonitor.getWidget()).setText("");
            }
        });
        /** Key up events **/
        ((TextBox) passwordNewMonitor.getWidget()).addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPasswordCheck(event);
            }
        });
        ((TextBox) passwordNewConfirmMonitor.getWidget()).addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPasswordConfirmCheck(event);
            }
        });
    }

    /**************************************************************************/
    /* Change monitoring methods                                              */
    /**************************************************************************/
    @Override
    public void commit() {
        for (ChangeMonitor monitor : monitors) {
            monitor.commit();
        }
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setChangeHandler(ChangeHandler handler) {
        for (ChangeMonitor monitor : monitors) {
            monitor.addChangeHandler(handler);
        }
    }

    @Override
    public void setUserSettings(SettingDetail detail) {
        companyNameMonitor.setBothValues(detail.getUser().getCompanyName());
        websiteMonitor.setBothValues(detail.getUser().getWebsite());
        emailMonitor.setBothValues(detail.getUser().getEmail());
        phoneMonitor.setBothValues(detail.getUser().getPhone());
        firstNameMonitor.setBothValues(detail.getUser().getPersonFirstName());
        lastNameMonitor.setBothValues(detail.getUser().getPersonLastName());
        identifNumberMonitor.setBothValues(detail.getUser().getIdentificationNumber());
        taxNumberMonitor.setBothValues(detail.getUser().getTaxId());
        descriptionMonitor.setBothValues(detail.getUser().getDescription());
    }

    @Override
    public void setAddressSettings(AddressDetail detail) {
        //set data
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        if (detail != null) {
            addressWidget.getCityMonitor().setBothValues(detail.getCity() + ", " + detail.getRegion());
            addressWidget.getZipcodeMonitor().setBothValues(detail.getZipCode());
            addressWidget.getStreetMonitor().setBothValues(detail.getStreet());
        }
    }

    @Override
    public SettingDetail updateUserSettings(SettingDetail detail) {
        detail.getUser().setCompanyName((String) companyNameMonitor.getValue());
        detail.getUser().setWebsite((String) websiteMonitor.getValue());
        detail.getUser().setEmail((String) emailMonitor.getValue());
        detail.getUser().setPhone((String) phoneMonitor.getValue());
        detail.getUser().setPersonFirstName((String) firstNameMonitor.getValue());
        detail.getUser().setPersonLastName((String) lastNameMonitor.getValue());
        detail.getUser().setIdentificationNumber((String) identifNumberMonitor.getValue());
        detail.getUser().setTaxId((String) taxNumberMonitor.getValue());
        detail.getUser().setDescription((String) descriptionMonitor.getValue());

        if (getAddress() != null) {
            detail.getUser().setAddresses(Arrays.asList(getAddress()));
        }
        return detail;
    }

    @Override
    public void setCurrentPasswordStyles(boolean correct) {
        passwordCurrentMonitor.setHideErrorPanel(correct);
        if (correct) {
            passwordCurrentMonitor.getErrorPanel().setVisible(true);
            passwordCurrentMonitor.getControlGroup().setType(ControlGroupType.SUCCESS);
            passwordCurrentMonitor.getErrorLabel().setText("");
            passwordCurrentMonitor.getErrorLabel().setType(LabelType.SUCCESS);
        } else {
            passwordCurrentMonitor.getErrorPanel().setVisible(true);
            passwordCurrentMonitor.getControlGroup().setType(ControlGroupType.ERROR);
            passwordCurrentMonitor.getErrorLabel().setText(Storage.MSGS.userSettingsPasswordIncorrect());
            passwordCurrentMonitor.getErrorLabel().setType(LabelType.IMPORTANT);
        }
    }

    @Override
    public void setDefaultPasswordsStyles() {
        passwordCurrentMonitor.reset();
        passwordNewMonitor.reset();
        passwordNewConfirmMonitor.reset();
        ((TextBox) passwordCurrentMonitor.getWidget()).setText("");
        ((TextBox) passwordNewMonitor.getWidget()).setText("");
        ((TextBox) passwordNewConfirmMonitor.getWidget()).setText("");
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** OTHERS. **/
    @Override
    public AddressDetail getAddress() {
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        if (addressWidget == null) {
            return null;
        } else {
            return addressWidget.createAddress();
        }
    }

    @Override
    public ValidationMonitor getPasswordCurrentMonitor() {
        return passwordCurrentMonitor;
    }

    @Override
    public ValidationMonitor getPasswordNewMonitor() {
        return passwordNewMonitor;
    }

    @Override
    public ValidationMonitor getPasswordNewConfirmMonitor() {
        return passwordNewConfirmMonitor;
    }

    @Override
    public Button getChangeBtn() {
        return changeBtn;
    }

    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

    @Override
    public boolean isNewPasswordValid() {
        boolean valid = true;
        valid = passwordCurrentMonitor.isValid() && valid;
        valid = passwordNewMonitor.isValid() && valid;
        valid = passwordNewConfirmMonitor.isValid() && valid;
        return valid;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Validate password field.
     * @param event
     */
    private void initVisualPasswordCheck(KeyUpEvent event) {
        passwordNewMonitor.setHideErrorPanel(false);
        int passwordLength = ((String) passwordNewMonitor.getValue()).length();
        if ((passwordLength <= Constants.LONG_PASSWORD) && (passwordLength > Constants.SHORT_PASSWORD)) {
            passwordNewMonitor.getErrorPanel().setVisible(true);
            passwordNewMonitor.getControlGroup().setType(ControlGroupType.WARNING);
            passwordNewMonitor.getErrorLabel().setText(Storage.MSGS.formUserRegSemiStrongPassword());
            passwordNewMonitor.getErrorLabel().setType(LabelType.WARNING);
        }
        if (passwordLength > Constants.LONG_PASSWORD) {
            passwordNewMonitor.getErrorPanel().setVisible(true);
            passwordNewMonitor.getControlGroup().setType(ControlGroupType.SUCCESS);
            passwordNewMonitor.getErrorLabel().setText(Storage.MSGS.formUserRegStrongPassword());
            passwordNewMonitor.getErrorLabel().setType(LabelType.SUCCESS);
        }
    }

    /**
     * Validate password confirm field.
     * @param event
     */
    private void initVisualPasswordConfirmCheck(KeyUpEvent event) {
        passwordNewConfirmMonitor.setHideErrorPanel(false);
        if (!(passwordNewMonitor.getValue()).equals(passwordNewConfirmMonitor.getValue())) {
            passwordNewConfirmMonitor.getErrorPanel().setVisible(true);
            passwordNewConfirmMonitor.getControlGroup().setType(ControlGroupType.ERROR);
            passwordNewConfirmMonitor.getErrorLabel().setText(Storage.MSGS.formUserRegPasswordsUnmatch());
            passwordNewConfirmMonitor.getErrorLabel().setType(LabelType.IMPORTANT);
        } else {
            passwordNewConfirmMonitor.setValid(false);
            passwordNewConfirmMonitor.getErrorPanel().setVisible(true);
            passwordNewConfirmMonitor.getControlGroup().setType(ControlGroupType.SUCCESS);
            passwordNewConfirmMonitor.getErrorLabel().setText(Storage.MSGS.formUserRegPasswordsMatch());
            passwordNewConfirmMonitor.getErrorLabel().setType(LabelType.SUCCESS);
        }
    }
}
