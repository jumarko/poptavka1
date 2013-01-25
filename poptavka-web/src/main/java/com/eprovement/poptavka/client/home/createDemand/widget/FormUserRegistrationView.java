package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

public class FormUserRegistrationView extends Composite
        implements FormUserRegistrationPresenter.FormRegistrationInterface, ProvidesValidate {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static FormUserRegistrationUiBinder uiBinder = GWT.create(FormUserRegistrationUiBinder.class);

    interface FormUserRegistrationUiBinder extends UiBinder<Widget, FormUserRegistrationView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Class attributes. **/
    private ArrayList<TextBox> widgets = new ArrayList<TextBox>();
    private ArrayList<TextBox> companyWidgets = new ArrayList<TextBox>();
    /** UiBinder attributes. **/
    @UiField
    RadioButton personRadio, companyRadio;
    @UiField
    Button backBtn, registerBtn;
    @UiField
    TextBox companyNameBox, companyIdBox, companyTaxBox, websiteBox;
    @UiField
    TextBox nameBox, surnameBox, phoneBox, mailBox;
    @UiField
    PasswordTextBox passBox, passConfirmBox;
    @UiField
    StatusIconLabel mailStatus, pwdStatus, pwdCheckStatus;
    @UiField
    SimplePanel addressHolder;
    private boolean mailFlag = false;
    private boolean passFlag = false;
    private boolean passLength = false;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        companyWidgets.add(companyNameBox);
        companyWidgets.add(companyIdBox);
        companyWidgets.add(companyTaxBox);

        widgets.add(nameBox);
        widgets.add(surnameBox);
        widgets.add(phoneBox);
        widgets.add(mailBox);
        widgets.add(passBox);

    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
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

    @Override
    public void toggleCompanyButtons(boolean toggle) {
        companyNameBox.setEnabled(toggle);
        companyIdBox.setEnabled(toggle);
        companyTaxBox.setEnabled(toggle);
        if (toggle) {
            companyIdBox.removeStyleName(StyleResource.INSTANCE.common().disabledText());
            companyNameBox.removeStyleName(StyleResource.INSTANCE.common().disabledText());
            companyTaxBox.removeStyleName(StyleResource.INSTANCE.common().disabledText());
        } else {
            companyIdBox.setText("");
            companyNameBox.setText("");
            companyTaxBox.setText("");
            companyIdBox.setStyleName(StyleResource.INSTANCE.common().disabledText());
            companyNameBox.setStyleName(StyleResource.INSTANCE.common().disabledText());
            companyTaxBox.setStyleName(StyleResource.INSTANCE.common().disabledText());
        }
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** PANEL. **/
    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

    /** BUTTONS. **/
    @Override
    public Button getBackButton() {
        return backBtn;
    }

    @Override
    public Button getRegisterButton() {
        return registerBtn;
    }

    @Override
    public RadioButton getPersonButton() {
        return personRadio;
    }

    @Override
    public RadioButton getCompanyButton() {
        return companyRadio;
    }

    /** TEXTBOX. **/
    @Override
    public HasValueChangeHandlers<String> getEmailBox() {
        return mailBox;
    }

    @Override
    public PasswordTextBox getPwdBox() {
        return passBox;
    }

    @Override
    public PasswordTextBox getPwdConfirmBox() {
        return passConfirmBox;
    }

    /** STATUS ICON LABELS/. **/
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

    /** DATA. **/
    @Override
    public BusinessUserDetail getNewClient() {
        BusinessUserDetail client = new BusinessUserDetail();

        client.setEmail(mailBox.getText());
        client.setPassword(passBox.getText());
        client.setCompanyName(companyNameBox.getText());
        client.setIdentificationNumber(companyIdBox.getText());
        client.setTaxId(companyTaxBox.getText());

        client.setFirstName(nameBox.getText());
        client.setLastName(surnameBox.getText());
        client.setPhone(phoneBox.getText());
        client.setWebsite(websiteBox.getText());

        ArrayList<AddressDetail> addresses = new ArrayList<AddressDetail>();
        AddressSelectorView address = (AddressSelectorView) addressHolder.getWidget();
        addresses.add(address.createAddress());
        client.setAddresses(addresses);

        return client;
    }

    @Override
    public boolean isValid() {
        int errorCount = 0;
        for (TextBox item : widgets) {
            ((Widget) item).removeStyleName(StyleResource.INSTANCE.common().errorField());
            if (item.getText().length() == 0) {
                errorCount++;
                ((Widget) item).setStyleName(StyleResource.INSTANCE.common().errorField());
            }
        }
        if (companyRadio.getValue()) {
            for (TextBox item : companyWidgets) {
                ((Widget) item).removeStyleName(StyleResource.INSTANCE.common().errorField());
                if (item.getText().length() == 0) {
                    errorCount++;
                    ((Widget) item).setStyleName(StyleResource.INSTANCE.common().errorField());
                }
            }
        }
        if (!(mailFlag && passFlag && passLength)) {
            errorCount++;
        }
        return errorCount == 0;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
