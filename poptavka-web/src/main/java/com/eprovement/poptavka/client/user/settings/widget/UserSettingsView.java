/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.forms.AdditionalInfoForm;
import com.eprovement.poptavka.client.common.forms.CompanyInfoForm;
import com.eprovement.poptavka.client.common.forms.ContactInfoForm;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.settings.interfaces.IUserSettings;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View consists of forms for updating Address info, Company info, Contanct info, Additional info.
 *
 * @author Martin Slavkovsky
 */
public class UserSettingsView extends Composite implements IUserSettings.View {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static UserSettingsViewUiBinder uiBinder = GWT.create(UserSettingsViewUiBinder.class);

    interface UserSettingsViewUiBinder extends
            UiBinder<Widget, UserSettingsView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel addressHolder;
    @UiField CompanyInfoForm companyInfoForm;
    @UiField ContactInfoForm contactInfoForm;
    @UiField AdditionalInfoForm additionalInfoForm;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates UserSettings view's components.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserSettings(SettingDetail detail) {
        companyInfoForm.setVisible(!detail.getUser().getCompanyName().isEmpty());
        if (companyInfoForm.isVisible()) {
            companyInfoForm.getCompanyName().setValue(detail.getUser().getCompanyName());
            companyInfoForm.getTaxNumber().setValue(detail.getUser().getIdentificationNumber());
            companyInfoForm.getVatNumber().setValue(detail.getUser().getTaxId());
        }
        contactInfoForm.getPhone().setValue(detail.getUser().getPhone());
        contactInfoForm.getFirstName().setValue(detail.getUser().getPersonFirstName());
        contactInfoForm.getLastName().setValue(detail.getUser().getPersonLastName());
        additionalInfoForm.getWebsite().setValue(detail.getUser().getWebsite());
        additionalInfoForm.getDescription().setValue(detail.getUser().getDescription());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillUserSettings(SettingDetail detail) {
        if (companyInfoForm.isVisible()) {
            detail.getUser().setCompanyName((String) companyInfoForm.getCompanyName().getValue());
            detail.getUser().setIdentificationNumber((String) companyInfoForm.getTaxNumber().getValue());
            detail.getUser().setTaxId((String) companyInfoForm.getVatNumber().getValue());
        }
        detail.getUser().setPhone((String) contactInfoForm.getPhone().getValue());
        detail.getUser().setPersonFirstName((String) contactInfoForm.getFirstName().getValue());
        detail.getUser().setPersonLastName((String) contactInfoForm.getLastName().getValue());
        detail.getUser().setWebsite((String) additionalInfoForm.getWebsite().getValue());
        detail.getUser().setDescription((String) additionalInfoForm.getDescription().getValue());
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = contactInfoForm.isValid() && valid;
        if (companyInfoForm.isVisible()) {
            valid = companyInfoForm.isValid() && valid;
        }
        valid = ((ProvidesValidate) addressHolder.getWidget()).isValid() && valid;
        valid = additionalInfoForm.isValid() && valid;
        return valid;
    }
}