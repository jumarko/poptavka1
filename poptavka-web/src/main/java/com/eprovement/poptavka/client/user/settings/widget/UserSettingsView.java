/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

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
    @UiField
    TextBox companyName, web, email, phone, firstName, lastName, identificationNumber, taxNumber;
    @UiField
    TextArea descriptionBox;
    @UiField
    CheckBox newMessageButton, newDemandButton, newSystemMessageButton,
    newOperatorMessageButton, demandStateChangeButton;
    @UiField
    ListBox newMessageOptions, newDemandOptions, newSystemMessageOptions,
    newOperatorMessageOptions, demandStateChangeOptions;
    @UiField
    DisclosurePanel disclosureAddress, disclosureCommon, disclosureContact,
    disclosureNotification, disclosurePayment, disclosureDescription;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public DisclosurePanel getDisclosureAddress() {
        return disclosureAddress;
    }

    public DisclosurePanel getDisclosureCommon() {
        return disclosureCommon;
    }

    public DisclosurePanel getDisclosureContact() {
        return disclosureContact;
    }

    public DisclosurePanel getDisclosureNotification() {
        return disclosureNotification;
    }

    public DisclosurePanel getDisclosurePayment() {
        return disclosurePayment;
    }

    public DisclosurePanel getDisclosureDescription() {
        return disclosureDescription;
    }


    /** TEXTBOXES. **/
    public TextBox getCompanyName() {
        return companyName;
    }

    public TextBox getWeb() {
        return web;
    }

    public TextBox getEmail() {
        return email;
    }

    public TextBox getPhone() {
        return phone;
    }

    public TextBox getFirstName() {
        return firstName;
    }

    public TextBox getLastName() {
        return lastName;
    }

    public TextBox getIdentificationNumber() {
        return identificationNumber;
    }

    public TextBox getTaxNumber() {
        return taxNumber;
    }

    public TextArea getDescriptionBox() {
        return descriptionBox;
    }

    /** OTHERS. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
