/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter.UserSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = UserSettingsView.class, multiple = true)
public class UserSettingsPresenter extends LazyPresenter<UserSettingsViewInterface, SettingsEventBus> {

    public interface UserSettingsViewInterface extends LazyView {

        DisclosurePanel getDisclosureAddress();

        DisclosurePanel getDisclosureCommon();

        DisclosurePanel getDisclosureContact();

        DisclosurePanel getDisclosureNotification();

        DisclosurePanel getDisclosurePayment();

        DisclosurePanel getDisclosureDescription();

        Widget getWidgetView();
    }
    //
    private static final int MAX_DESC_CHARS = 50;
    //
    private SettingsDetail settingsDetail;

    @Override
    public void bindView() {
        addDisclosureAddressHandlers();
//        addDisclosureCommonHandlers();
//        addDisclosureContactHandlers();
//        addDisclosureNotificationHandlers();
//        addDisclosurePaymentHandlers();
        addDisclosureDescriptionHandlers();
    }

    private void addDisclosureAddressHandlers() {
        view.getDisclosureAddress().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                eventBus.initAddressWidget((SimplePanel) view.getDisclosureAddress().getContent());
                setAddressesContent(settingsDetail);
            }
        });
        view.getDisclosureAddress().addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> event) {
                SimplePanel addressHolder = ((SimplePanel) view.getDisclosureAddress().getContent());
                AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
                setAddressesHeader(addressWidget.createAddress().toString());
            }
        });
    }

    private void addDisclosureDescriptionHandlers() {
        view.getDisclosureDescription().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                setDescriptionContent(settingsDetail);
            }
        });
        view.getDisclosureDescription().addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> event) {
                TextArea descriptionWidget = ((TextArea) view.getDisclosureDescription().getContent());
                setDescriptionHeader(descriptionWidget.getText());
            }
        });
    }

    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
        view.getDisclosureCommon().setOpen(true);
        view.getDisclosureContact().setOpen(true);
        eventBus.loadingHide();
    }

    public void onSetUserSettings(SettingsDetail detail) {
        this.settingsDetail = detail;

        setAddressesHeader(detail.getAddresses().get(0).toString());

        setDescriptionHeader(detail.getDescription());
    }

    private void setAddressesContent(SettingsDetail detail) {
        SimplePanel addressHolder = (SimplePanel) view.getDisclosureAddress().getContent();
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        AddressDetail addrDetail = detail.getAddresses().get(0);
        addressWidget.getCitySuggestBox().setText(addrDetail.getCity() + ", " + addrDetail.getRegion());
        addressWidget.getZipCodeTextBox().setText(addrDetail.getZipCode());
        addressWidget.getStreetTextBox().setText(addrDetail.getStreet());
    }

    private void setDescriptionContent(SettingsDetail detail) {
        TextArea descriptionWidget = ((TextArea) view.getDisclosureDescription().getContent());
        descriptionWidget.setText(detail.getDescription());
    }

    private void setAddressesHeader(String address) {
        view.getDisclosureAddress().getHeaderTextAccessor().setText(
                Storage.MSGS.address() + ": " + address);
    }

    private void setDescriptionHeader(String description) {
        if (description == null) {
            description = "";
        }
        if (description.length() > MAX_DESC_CHARS) {
            description = description.substring(0, MAX_DESC_CHARS) + "...";
        }
        view.getDisclosureDescription().getHeaderTextAccessor().setText(
                Storage.MSGS.description() + ": " + description);
    }

    private void buildHeaderBold(SafeHtmlBuilder header, String headerStart) {
        header.appendHtmlConstant("<strong>");
        header.appendEscaped(headerStart);
        header.appendEscaped(": ");
        header.appendHtmlConstant("</strong>");
    }
}
