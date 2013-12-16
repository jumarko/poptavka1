package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.forms.AdditionalInfoForm;
import com.eprovement.poptavka.client.common.forms.CompanyInfoForm;
import com.eprovement.poptavka.client.common.forms.ContactInfoForm;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public class UserSettingsView extends Composite
    implements UserSettingsPresenter.UserSettingsViewInterface, ProvidesValidate {

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
    @UiField SimplePanel addressHolder;
    @UiField CompanyInfoForm companyInfoForm;
    @UiField ContactInfoForm contactInfoForm;
    @UiField AdditionalInfoForm additionalInfoForm;
    /** Class attributes. **/

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setUserSettings(SettingDetail detail) {
        companyInfoForm.getParent().setVisible(!detail.getUser().getCompanyName().isEmpty());
        companyInfoForm.getCompanyName().setValue(detail.getUser().getCompanyName());
        companyInfoForm.getTaxNumber().setValue(detail.getUser().getIdentificationNumber());
        companyInfoForm.getVatNumber().setValue(detail.getUser().getTaxId());
        contactInfoForm.getPhone().setValue(detail.getUser().getPhone());
        contactInfoForm.getFirstName().setValue(detail.getUser().getPersonFirstName());
        contactInfoForm.getLastName().setValue(detail.getUser().getPersonLastName());
        additionalInfoForm.getWebsite().setValue(detail.getUser().getWebsite());
        additionalInfoForm.getDescription().setValue(detail.getUser().getDescription());
    }

    @Override
    public SettingDetail updateUserSettings(SettingDetail detail) {
        detail.getUser().setCompanyName((String) companyInfoForm.getCompanyName().getValue());
        detail.getUser().setIdentificationNumber((String) companyInfoForm.getTaxNumber().getValue());
        detail.getUser().setTaxId((String) companyInfoForm.getVatNumber().getValue());
        detail.getUser().setPhone((String) contactInfoForm.getPhone().getValue());
        detail.getUser().setPersonFirstName((String) contactInfoForm.getFirstName().getValue());
        detail.getUser().setPersonLastName((String) contactInfoForm.getLastName().getValue());
        detail.getUser().setWebsite((String) additionalInfoForm.getWebsite().getValue());
        detail.getUser().setDescription((String) additionalInfoForm.getDescription().getValue());

        return detail;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = contactInfoForm.getPhone().isValid() && valid;
        valid = contactInfoForm.getFirstName().isValid() && valid;
        valid = contactInfoForm.getLastName().isValid() && valid;
        valid = additionalInfoForm.getWebsite().isValid() && valid;
        valid = additionalInfoForm.getDescription().isValid() && valid;
        if (companyInfoForm.isVisible()) {
            valid = companyInfoForm.getCompanyName().isValid() && valid;
            valid = companyInfoForm.getTaxNumber().isValid() && valid;
            valid = companyInfoForm.getVatNumber().isValid() && valid;
        }
        return valid;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}