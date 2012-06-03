package com.eprovement.poptavka.client.home.createDemand;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.main.common.StatusIconLabel;
import com.eprovement.poptavka.client.main.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import java.util.List;

public class FormUserRegistrationView extends Composite
    implements FormUserRegistrationPresenter.FormRegistrationInterface, ProvidesValidate {

    private static FormUserRegistrationUiBinder uiBinder = GWT.create(FormUserRegistrationUiBinder.class);

    interface FormUserRegistrationUiBinder extends UiBinder<Widget, FormUserRegistrationView> {
    }

    @UiField RadioButton personRadio;
    @UiField RadioButton companyRadio;
    @UiField Button toLoginBtn;

    private ArrayList<TextBox> widgets = new ArrayList<TextBox>();
    private ArrayList<TextBox> companyWidgets = new ArrayList<TextBox>();

    @UiField TextBox companyNameBox;
    @UiField TextBox companyIdBox;
    @UiField TextBox companyTaxBox;
    @UiField TextBox websiteBox;
    @UiField TextBox streetBox;
    @UiField TextBox cityBox;
    @UiField TextBox zipBox;
    @UiField TextBox nameBox;
    @UiField TextBox surnameBox;
    @UiField TextBox phoneBox;
    @UiField TextBox mailBox;
    @UiField PasswordTextBox passBox, passConfirmBox;
    @UiField StatusIconLabel mailStatus;
    @UiField StatusIconLabel pwdStatus;
    @UiField StatusIconLabel pwdCheckStatus;
    private boolean mailFlag = false;
    private boolean passFlag = false;
    private boolean passLength = false;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        companyWidgets.add(companyNameBox);
        companyWidgets.add(companyIdBox);
        companyWidgets.add(companyTaxBox);

        widgets.add(streetBox);
        widgets.add(cityBox);
        widgets.add(zipBox);
        widgets.add(nameBox);
        widgets.add(surnameBox);
        widgets.add(phoneBox);
        widgets.add(mailBox);
        widgets.add(passBox);

    }

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

    @Override
    public RadioButton getPersonButton() {
        return personRadio;
    }

    @Override
    public RadioButton getCompanyButton() {
        return companyRadio;
    }

    @Override
    public UserDetail getNewClient() {
        UserDetail client = new UserDetail(mailBox.getText(), passBox.getText());

        client.setCompanyName(companyNameBox.getText());
        client.setIdentificationNumber(companyIdBox.getText());
        client.setTaxId(companyTaxBox.getText());

        client.setFirstName(nameBox.getText());
        client.setLastName(surnameBox.getText());
        client.setPhone(phoneBox.getText());
        client.setWebsite(websiteBox.getText());

        AddressDetail address = new AddressDetail();
        address.setCityName(cityBox.getText());
        address.setStreet(streetBox.getText());
        address.setZipCode(zipBox.getText());

        List<AddressDetail> addresses = new ArrayList<AddressDetail>();
        addresses.add(address);
        client.setAddresses(addresses);

        return client;
    }

    @Override
    public Button getToLoginButton() {
        return toLoginBtn;
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

    @Override
    public HasValueChangeHandlers<String> getEmailBox() {
        return mailBox;
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
    public PasswordTextBox getPwdBox() {
        return passBox;
    }

    @Override
    public PasswordTextBox getPwdConfirmBox() {
        return passConfirmBox;
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


}
