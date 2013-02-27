/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.settings.NotificationDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    TextBox companyName, web, email, phone, firstName, lastName, identificationNumber, taxNumber, status;
    @UiField
    TextArea descriptionBox;
    @UiField
    VerticalPanel notifications;
//    @UiField
//    DisclosurePanel disclosureAddress;
    @UiField
    SimplePanel addressHolder;
    /** Class attributes. **/
    //Store string between focus and blur events and compare if any changes.
    private String stringStorage;
    private String[] stringStorageCity = new String[3];
    /* If change to some data was made and then that change was reverted to original,
     * we must detect it. We must restore flag pointing that something has changed.
     * This list stores original strings if there was made change to that string.
     * And if that change is reverted there will be match in this list telling us
     * this that that string was reverted to original, therefore don't remeber it as
     * there is some change.
     */
    private Map<String, String> originalsStorage = new HashMap<String, String>();
    //
    private FocusHandler focus = null;
    private ChangeHandler change = null;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
//        disclosureAddress.setOpen(true);
//        disclosureAddress.sinkEvents(Event.CLICK);
        focus = new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                HasText source = (HasText) event.getSource();
                stringStorage = source.getText();
            }
        };
        change = new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                TextBoxBase source = (TextBoxBase) event.getSource();
                //case if there was a change but reverted
                if (originalsStorage.containsKey(source.getTitle())) {
                    if (originalsStorage.get(source.getTitle()).equals(source.getText())) {
                        originalsStorage.remove(source.getTitle());
                    }
                } else {
                    originalsStorage.put(source.getTitle(), stringStorage);
                }
                updateStatus();
            }
        };

        companyName.addDomHandler(focus, FocusEvent.getType());
        companyName.addDomHandler(change, ChangeEvent.getType());
        web.addDomHandler(focus, FocusEvent.getType());
        web.addDomHandler(change, ChangeEvent.getType());
        email.addDomHandler(focus, FocusEvent.getType());
        email.addDomHandler(change, ChangeEvent.getType());
        phone.addDomHandler(focus, FocusEvent.getType());
        phone.addDomHandler(change, ChangeEvent.getType());
        firstName.addDomHandler(focus, FocusEvent.getType());
        firstName.addDomHandler(change, ChangeEvent.getType());
        lastName.addDomHandler(focus, FocusEvent.getType());
        lastName.addDomHandler(change, ChangeEvent.getType());
        identificationNumber.addDomHandler(focus, FocusEvent.getType());
        identificationNumber.addDomHandler(change, ChangeEvent.getType());
        taxNumber.addDomHandler(focus, FocusEvent.getType());
        taxNumber.addDomHandler(change, ChangeEvent.getType());
        descriptionBox.addDomHandler(focus, FocusEvent.getType());
        descriptionBox.addDomHandler(change, ChangeEvent.getType());
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setUserSettings(SettingDetail detail) {
        originalsStorage.clear();
        companyName.setText(detail.getUser().getCompanyName());
        web.setText(detail.getUser().getWebsite());
        email.setText(detail.getUser().getEmail());
        phone.setText(detail.getUser().getPhone());
        firstName.setText(detail.getUser().getFirstName());
        lastName.setText(detail.getUser().getLastName());
        identificationNumber.setText(detail.getUser().getIdentificationNumber());
        taxNumber.setText(detail.getUser().getTaxId());
        descriptionBox.setText(detail.getUser().getDescription());
        //notifications
        List<Period> periodList = Arrays.asList(Period.values());
        for (NotificationDetail item : detail.getNotifications()) {
            NotificationItemView notificationWidget = new NotificationItemView();
            notificationWidget.getEnabled().setValue(item.isEnabled());
            notificationWidget.getName().setText(item.getName());
            notificationWidget.getPeriod().setSelectedIndex(periodList.indexOf(item.getPeriod()));
            notificationWidget.getStatus().addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    updateStatus();
                }
            });
            notifications.add(notificationWidget);
        }

        setAddressesContent(detail.getUser().getAddresses().get(0));
    }

    @Override
    public SettingDetail updateUserSettings(SettingDetail detail) {
        detail.getUser().setCompanyName(companyName.getText());
        detail.getUser().setWebsite(web.getText());
        detail.getUser().setEmail(email.getText());
        detail.getUser().setPhone(phone.getText());
        detail.getUser().setFirstName(firstName.getText());
        detail.getUser().setLastName(lastName.getText());
        detail.getUser().setIdentificationNumber(identificationNumber.getText());
        detail.getUser().setTaxId(taxNumber.getText());
        detail.getUser().setDescription(descriptionBox.getText());
        //notifications
        for (int i = 0; i < detail.getNotifications().size(); i++) {
            NotificationItemView notificationWidget = (NotificationItemView) notifications.getWidget(i);
            NotificationDetail notificationDetail = detail.getNotifications().get(i);

            notificationDetail.setEnabled(notificationWidget.getEnabled().getValue());
            notificationDetail.setName(notificationWidget.getName().getText());
            notificationDetail.setPeriod(Period.values()[notificationWidget.getPeriod().getSelectedIndex()]);
        }

        if (getAddress() != null) {
            detail.getUser().setAddresses(Arrays.asList(getAddress()));
        }
        return detail;
    }

//    @Override
    public void setAddressesContent(AddressDetail detail) {
        //set data
//        SimplePanel addressHolder = (SimplePanel) disclosureAddress.getContent();
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        if (detail != null) {
            addressWidget.getCitySuggestBox().setText(detail.getCity() + ", " + detail.getRegion());
            addressWidget.getZipCodeTextBox().setText(detail.getZipCode());
            addressWidget.getStreetTextBox().setText(detail.getStreet());
        }
        //register handlers
        addressWidget.getCitySuggestBox().getTextBox().addDomHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
                stringStorageCity[0] = addressWidget.getCitySuggestBox().getText();
                stringStorageCity[1] = addressWidget.getZipCodeTextBox().getText();
                stringStorageCity[2] = addressWidget.getStreetTextBox().getText();
            }
        }, FocusEvent.getType());
        addressWidget.getCitySuggestBox().getTextBox().addDomHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
                TextBoxBase source = (TextBoxBase) event.getSource();
                for (int i = 0; i < stringStorageCity.length; i++) {
                    switch (i) {
                        case 0:
                            source = addressWidget.getCitySuggestBox().getTextBox();
                            break;
                        case 1:
                            source = addressWidget.getZipCodeTextBox();
                            break;
                        case 2:
                            source = addressWidget.getStreetTextBox();
                            break;
                        default:
                            break;
                    }
                    //ak uz je zaznam a novy string sa rovna tomu zaznamu, tak sa zmena vratila
                    if (originalsStorage.containsKey(source.getTitle())) {
                        if (originalsStorage.get(source.getTitle()).equals(source.getText())) {
                            originalsStorage.remove(source.getTitle());
                        }
                    } else {
                        originalsStorage.put(source.getTitle(), stringStorageCity[i]);
                    }
                }
                updateStatus();
            }
        }, ChangeEvent.getType());

        addressWidget.getZipCodeTextBox().addDomHandler(focus, FocusEvent.getType());
        addressWidget.getZipCodeTextBox().addDomHandler(change, ChangeEvent.getType());
        addressWidget.getStreetTextBox().addDomHandler(focus, FocusEvent.getType());
        addressWidget.getStreetTextBox().addDomHandler(change, ChangeEvent.getType());
    }

//    @Override
//    public void setAddressesHeader(String address) {
//        SafeHtmlBuilder header = new SafeHtmlBuilder();
//        buildHeaderBold(header, Storage.MSGS.address());
//        header.appendEscaped(address);
//        ((HTML) disclosureAddress.getHeader()).setHTML(header.toSafeHtml());
//    }
    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** PANELS. **/
//    @Override
//    public DisclosurePanel getDisclosureAddress() {
//        return disclosureAddress;
//    }
    /** OTHERS. **/
    @Override
    public AddressDetail getAddress() {
//        SimplePanel addressHolder = (SimplePanel) disclosureAddress.getContent();
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        if (addressWidget == null) {
            return null;
        } else {
            return addressWidget.createAddress();
        }
    }

    @Override
    public TextBox getStatus() {
        return status;
    }

    @Override
    public SimplePanel getAddressHolder() {
        return addressHolder;
    }

    @Override
    public boolean isSettingChange() {
        /** Notifications. **/
        //Check if any notification has changed
        for (int i = 0; i < notifications.getWidgetCount(); i++) {
            if (((NotificationItemView) notifications.getWidget(i)).isNotificationChange()) {
                return true;
            }
        }
        /** Others. **/
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
//    private void buildHeaderBold(SafeHtmlBuilder header, String headerStart) {
//        header.appendHtmlConstant("<strong>");
//        header.appendEscaped(headerStart);
//        header.appendEscaped(": ");
//        header.appendHtmlConstant("</strong>");
//    }
    private void updateStatus() {
//        status.setText(originalsStorage.toString());
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), status);
    }
}
