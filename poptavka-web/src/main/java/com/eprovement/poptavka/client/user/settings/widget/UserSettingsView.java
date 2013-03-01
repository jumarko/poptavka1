/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

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
    @UiField SimplePanel addressHolder;
    /** Class attributes. **/
    private List<ChangeMonitor> monitors;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    private void initValidationMonitors() {
        companyNameMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.COMPANY_NAME.getValue()));
        websiteMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.WEBSITE.getValue()));
        emailMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.EMAIL.getValue()));
        phoneMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.PHONE.getValue()));
        descriptionMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.DESCRIPTION.getValue()));
        firstNameMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.FIRST_NAME.getValue()));
        lastNameMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.LAST_NAME.getValue()));
        identifNumberMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.IDENTIF_NUMBER.getValue()));
        taxNumberMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.TAX_ID.getValue()));
        descriptionMonitor = new ChangeMonitor<BusinessUserDetail>(
                BusinessUserDetail.class, new ChangeDetail(BusinessUserDetail.UserField.DESCRIPTION.getValue()));
        monitors = Arrays.asList(
                companyNameMonitor, websiteMonitor, emailMonitor, phoneMonitor, descriptionMonitor,
                firstNameMonitor, lastNameMonitor, identifNumberMonitor, taxNumberMonitor);
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    @Override
    public void resetFields() {
        for (ChangeMonitor monitor : monitors) {
            monitor.reset();
        }
    }

    @Override
    public void revertFields() {
        for (ChangeMonitor monitor : monitors) {
            monitor.revert();
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
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
