/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.settings.NotificationDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
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
    @UiField
    TextBox companyName, web, email, phone, firstName, lastName, identificationNumber, taxNumber;
    @UiField
    TextArea descriptionBox;
//    @UiField
//    CheckBox newMessageButton, newDemandButton, newSystemMessageButton,
//            newOperatorMessageButton, demandStateChangeButton;
//    @UiField
//    ListBox newMessageOptions, newDemandOptions, newSystemMessageOptions,
//            newOperatorMessageOptions, demandStateChangeOptions;
    @UiField
    VerticalPanel notifications;
    @UiField
    DisclosurePanel disclosureAddress;
    /** Class attributes. **/
    //Store string between focus and blur events and compare if any changes.
    private String stringStorage;
    /* If change to some data was made and then that change was reverted to original,
     * we must detect it. We must restore flag pointing that something has changed.
     * This list stores original strings if there was made change to that string.
     * And if that change is reverted there will be match in this list telling us
     * this that that string was reverted to original, therefore don't remeber it as
     * there is some change.
     */
    private List<String> originalsStorage = new ArrayList<String>();

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        FocusHandler focus = new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                stringStorage = ((HasText) event.getSource()).getText();
            }
        };
        BlurHandler blur = new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                String actualString = ((HasText) event.getSource()).getText();
                //was change but reverted
                if (stringStorage.contains(actualString)) {
                    originalsStorage.remove(stringStorage);
                } else {
                    originalsStorage.add(stringStorage);
                }
            }
        };
        companyName.addDomHandler(focus, FocusEvent.getType());
        companyName.addDomHandler(blur, BlurEvent.getType());
        web.addDomHandler(focus, FocusEvent.getType());
        web.addDomHandler(blur, BlurEvent.getType());
        email.addDomHandler(focus, FocusEvent.getType());
        email.addDomHandler(blur, BlurEvent.getType());
        phone.addDomHandler(focus, FocusEvent.getType());
        phone.addDomHandler(blur, BlurEvent.getType());
        firstName.addDomHandler(focus, FocusEvent.getType());
        firstName.addDomHandler(blur, BlurEvent.getType());
        lastName.addDomHandler(focus, FocusEvent.getType());
        lastName.addDomHandler(blur, BlurEvent.getType());
        identificationNumber.addDomHandler(focus, FocusEvent.getType());
        identificationNumber.addDomHandler(blur, BlurEvent.getType());
        taxNumber.addDomHandler(focus, FocusEvent.getType());
        taxNumber.addDomHandler(blur, BlurEvent.getType());
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setUserSettings(SettingDetail detail) {
        companyName.setText(detail.getCompanyName());
        web.setText(detail.getWebsite());
        email.setText(detail.getEmail());
        phone.setText(detail.getPhone());
        firstName.setText(detail.getFirstName());
        lastName.setText(detail.getLastName());
        identificationNumber.setText(detail.getIdentificationNumber());
        taxNumber.setText(detail.getTaxId());
        descriptionBox.setText(detail.getDescription());
        //notifications
        List<Period> periodList = Arrays.asList(Period.values());
        for (NotificationDetail item : detail.getNotifications()) {
            NotificationItemView notification = new NotificationItemView();
            notification.getEnabled().setValue(item.isEnabled());
            notification.getName().setText(item.getName());
            notification.getPeriod().setSelectedIndex(periodList.indexOf(item.getPeriod()));
            notifications.add(notification);
        }

        setAddressesHeader(detail.getAddresses().get(0).toString());
    }

    @Override
    public SettingDetail updateUserSettings(SettingDetail detail) {
        detail.setCompanyName(companyName.getText());
        detail.setWebsite(web.getText());
        detail.setEmail(email.getText());
        detail.setPhone(phone.getText());
        detail.setFirstName(firstName.getText());
        detail.setLastName(lastName.getText());
        detail.setIdentificationNumber(identificationNumber.getText());
        detail.setTaxId(taxNumber.getText());
        detail.setDescription(descriptionBox.getText());
        //notifications
        for (int i = 0; i < detail.getNotifications().size(); i++) {
            NotificationItemView notificationWidget = (NotificationItemView) notifications.getWidget(i);
            NotificationDetail notificationDetail = detail.getNotifications().get(i);

            notificationDetail.setEnabled(notificationWidget.getEnabled().getValue());
            notificationDetail.setName(notificationWidget.getName().getText());
            notificationDetail.setPeriod(Period.values()[notificationWidget.getPeriod().getSelectedIndex()]);
        }

        SimplePanel addressHolder = (SimplePanel) disclosureAddress.getContent();
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        if (addressWidget != null) {
            detail.setAddresses(Arrays.asList(addressWidget.createAddress()));
        }
        return detail;
    }

    @Override
    public void setAddressesContent(SettingDetail detail) {
        SimplePanel addressHolder = (SimplePanel) disclosureAddress.getContent();
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        if (detail.getAddresses() != null && !detail.getAddresses().isEmpty()) {
            AddressDetail addrDetail = detail.getAddresses().get(0);
            addressWidget.getCitySuggestBox().setText(addrDetail.getCity() + ", " + addrDetail.getRegion());
            addressWidget.getZipCodeTextBox().setText(addrDetail.getZipCode());
            addressWidget.getStreetTextBox().setText(addrDetail.getStreet());
        }
    }

    @Override
    public void setAddressesHeader(String address) {
        SafeHtmlBuilder header = new SafeHtmlBuilder();
        buildHeaderBold(header, Storage.MSGS.address());
        header.appendEscaped(address);
        ((HTML) disclosureAddress.getHeader()).setHTML(header.toSafeHtml());
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public DisclosurePanel getDisclosureAddress() {
        return disclosureAddress;
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
    public boolean isSettingChange() {
        //Check if any notification has changed
        for (int i = 0; i < notifications.getWidgetCount(); i++) {
            if (((NotificationItemView) notifications.getWidget(i)).isNotificationChange()) {
                return true;
            }
        }
        //if notificatoin has not changes, check if anything elsa has.
        return !originalsStorage.isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void buildHeaderBold(SafeHtmlBuilder header, String headerStart) {
        header.appendHtmlConstant("<strong>");
        header.appendEscaped(headerStart);
        header.appendEscaped(": ");
        header.appendHtmlConstant("</strong>");
    }
}
