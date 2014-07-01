/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.userRegistration;

import com.eprovement.poptavka.client.addressSelector.AddressSelectorView;
import com.eprovement.poptavka.client.common.forms.AccountInfoForm;
import com.eprovement.poptavka.client.common.forms.AdditionalInfoForm;
import com.eprovement.poptavka.client.common.forms.CompanyInfoForm;
import com.eprovement.poptavka.client.common.forms.ContactInfoForm;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

/**
 * User registration widget represent user's registration form.
 * It creates BusinessUserDetail. Provides field validation.
 *
 * @author Martin Slavkovsky
 */
public class UserRegistrationView extends Composite
        implements UserRegistrationPresenter.AccountFormInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static AccountRegistrationFormViewUiBinder uiBinder = GWT.create(AccountRegistrationFormViewUiBinder.class);

    interface AccountRegistrationFormViewUiBinder extends UiBinder<Widget, UserRegistrationView> {
    }

    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel addressHolder;
    @UiField HTMLPanel companyChoicePanel;
    @UiField Button personBtn, companyBtn;
    @UiField FluidRow companyInfoPanel;
    @UiField AccountInfoForm accountInfoForm;
    @UiField CompanyInfoForm companyInfoForm;
    @UiField ContactInfoForm contactInfoForm;
    @UiField AdditionalInfoForm additionalInfoForm;
    /** Class attributes. **/
    private List<ValidationMonitor> validationMonitorsCommon;
    private List<ValidationMonitor> validationMonitorsCompanyOnly;
    private boolean companySelected;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates UserRegistration view's components.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        //set validation monitors as array for easier access.
        validationMonitorsCommon = Arrays.asList(
                contactInfoForm.getPhone(),
                contactInfoForm.getFirstName(),
                contactInfoForm.getLastName(),
                accountInfoForm.getEmail(),
                accountInfoForm.getPassword(),
                accountInfoForm.getPasswordConfirm(),
                additionalInfoForm.getDescription());
        validationMonitorsCompanyOnly = Arrays.asList(
                companyInfoForm.getCompanyName(),
                companyInfoForm.getTaxNumber(),
                companyInfoForm.getVatNumber());
        setCompanyPanelVisibility(false);
    }


    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    //Just for devel
//    @UiField Button develBtn;
//    @UiHandler("develBtn")
//    public void setDevelBtnClickHandler(ClickEvent e) {
//        setDevelData();
//    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Toggle company info.
     * @param showCompanyPanel
     */
    @Override
    public void setCompanyPanelVisibility(boolean showCompanyPanel) {
        companyInfoPanel.setVisible(showCompanyPanel);
        companySelected = showCompanyPanel;
        if (showCompanyPanel) {
            companyChoicePanel.removeStyleName(Storage.RSCS.common().switchLeft());
            companyChoicePanel.addStyleName(Storage.RSCS.common().switchRight());
        } else {
            companyChoicePanel.removeStyleName(Storage.RSCS.common().switchRight());
            companyChoicePanel.addStyleName(Storage.RSCS.common().switchLeft());
        }
        companyInfoForm.getCompanyName().reset();
        companyInfoForm.getTaxNumber().reset();
        companyInfoForm.getVatNumber().reset();
    }

    /**
     * Inits email validation.
     * @param isAvailable true if email is availbale, false otherwise
     */
    @Override
    public void initVisualFreeEmailCheck(Boolean isAvailable) {
        accountInfoForm.initVisualFreeEmailCheck(isAvailable);
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Updates given BusinessUserDetail's data with current widget's data
     * @param user to be updated
     */
    @Override
    public void createBusinessUserDetail(BusinessUserDetail user) {
        user.setEmail(((String) accountInfoForm.getEmail().getValue()).trim());
        user.setPassword((String) accountInfoForm.getPassword().getValue());
        //Contact information
        user.setPersonFirstName((String) contactInfoForm.getFirstName().getValue());
        user.setPersonLastName((String) contactInfoForm.getLastName().getValue());
        user.setPhone((String) contactInfoForm.getPhone().getValue());
        //Company information
        user.setCompanyName((String) companyInfoForm.getCompanyName().getValue());
        user.setIdentificationNumber((String) companyInfoForm.getVatNumber().getValue());
        user.setTaxId((String) companyInfoForm.getTaxNumber().getValue());
        //Additional information
        user.setWebsite((String) additionalInfoForm.getWebsite().getValue());
        user.setDescription((String) additionalInfoForm.getDescription().getValue());
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        if (accountInfoForm != null) {
            accountInfoForm.reset();
        }
        if (companyInfoForm != null) {
            contactInfoForm.reset();
        }
        if (companyInfoForm != null) {
            companyInfoForm.reset();
        }
        if (additionalInfoForm != null) {
            additionalInfoForm.reset();
        }
        if (addressHolder.getWidget() != null) {
            ((ProvidesValidate) addressHolder.getWidget()).reset();
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        if (companySelected) {
            for (ValidationMonitor box : validationMonitorsCompanyOnly) {
                valid = box.isValid() && valid;
            }
        }
        for (ValidationMonitor box : validationMonitorsCommon) {
            valid = box.isValid() && valid;
        }
        return ((ProvidesValidate) addressHolder.getWidget()).isValid() && valid;
    }

    /**
     * @return the address container
     */
    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

    /**
     * @return the email validation monitor
     */
    @Override
    public ValidationMonitor getEmailBox() {
        return accountInfoForm.getEmail();
    }

    /**
     * @return the person button
     */
    @Override
    public Button getPersonBtn() {
        return personBtn;
    }

    /**
     * @return the company button
     */
    @Override
    public Button getCompanyBtn() {
        return companyBtn;
    }

    /**
     * @return true if company is selected, false otherwise (person)
     */
    @Override
    public boolean getCompanySelected() {
        return companySelected;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void setDevelData() {
        accountInfoForm.getEmail().setValue("supplier15@mailinator.com");
        accountInfoForm.getPassword().setValue("123123");
        accountInfoForm.getPasswordConfirm().setValue("123123");

        contactInfoForm.getFirstName().setValue("FirstName");
        contactInfoForm.getLastName().setValue("LastName");
        contactInfoForm.getPhone().setValue("987654321");

        ((AddressSelectorView) addressHolder.getWidget()).getCitySuggestBox().setText("Alabaster, Alabama");
        ((AddressSelectorView) addressHolder.getWidget()).getStreetMonitorBox().setText("Street");
        ((AddressSelectorView) addressHolder.getWidget()).getZipcodeMonitorBox().setText("654321");

        additionalInfoForm.getDescription().setValue("oiuiup diua sd n l ansduh pasudhas nldkn as udh pa suhda");
    }
}