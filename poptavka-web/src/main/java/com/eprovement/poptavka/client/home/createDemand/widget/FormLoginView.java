package com.eprovement.poptavka.client.home.createDemand.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;

//TODO Martin - 21.11.2012 - not used any more, delete
public class FormLoginView extends Composite implements FormLoginPresenter.FormLoginInterface, ProvidesValidate {

    private static FormLoginViewUiBinder uiBinder = GWT.create(FormLoginViewUiBinder.class);

    interface FormLoginViewUiBinder extends UiBinder<Widget, FormLoginView> {
    }
    @UiField
    TextBox mailBox;
    @UiField
    PasswordTextBox passBox;
    @UiField
    Button loginBtn;
    @UiField
    HTML errorMsg;
    @UiField
    Button registerBtn;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public HasClickHandlers getLoginBtn() {
        return loginBtn;
    }

    @Override
    public HasClickHandlers getRegisterBtn() {
        return registerBtn;
    }

    @Override
    public String getLogin() {
        return mailBox.getText();
    }

    @Override
    public String getPassword() {
        return passBox.getText();
    }

    @Override
    public boolean isValid() {
        boolean isValid = true;
        if (passBox.getText().equals("")) {
            passBox.setStyleName(StyleResource.INSTANCE.common().errorField());
            isValid = false;
        }
        if (mailBox.getText().equals("")) {
            mailBox.setStyleName(StyleResource.INSTANCE.common().errorField());
            isValid = false;
        }
        return isValid;
    }
}
