/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
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
    DisclosurePanel disclosureAddress;

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

    /** TEXTBOXES. **/
    @Override
    public TextBox getCompanyName() {
        return companyName;
    }

    @Override
    public TextBox getWeb() {
        return web;
    }

    @Override
    public TextBox getEmail() {
        return email;
    }

    @Override
    public TextBox getPhone() {
        return phone;
    }

    @Override
    public TextBox getFirstName() {
        return firstName;
    }

    @Override
    public TextBox getLastName() {
        return lastName;
    }

    @Override
    public TextBox getIdentificationNumber() {
        return identificationNumber;
    }

    @Override
    public TextBox getTaxNumber() {
        return taxNumber;
    }

    @Override
    public TextArea getDescriptionBox() {
        return descriptionBox;
    }

    /** OTHERS. **/
    @Override
    public AddressDetail getAddress() {
        SimplePanel addressHolder = (SimplePanel) disclosureAddress.getContent();
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        if (addressWidget == null) {
            return null;
        } else {
            return addressWidget.createAddress();
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
