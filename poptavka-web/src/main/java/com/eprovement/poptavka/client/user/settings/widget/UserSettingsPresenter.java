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
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
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

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface UserSettingsViewInterface extends LazyView {

        //Panels
        DisclosurePanel getDisclosureAddress();

        //TextBoxes
        TextBox getCompanyName();

        TextBox getWeb();

        TextBox getEmail();

        TextBox getPhone();

        TextBox getFirstName();

        TextBox getLastName();

        TextBox getIdentificationNumber();

        TextBox getTaxNumber();

        TextArea getDescriptionBox();

        //Others
        AddressDetail getAddress();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static final int MAX_DESC_CHARS = 50;
    //
    private SettingDetail settingsDetail;

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        addDisclosureAddressHandlers();
    }

    /**************************************************************************/
    /* BIND - Helper methods                                                  */
    /**************************************************************************/
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

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    public void onSetUserSettings(SettingDetail detail) {
        this.settingsDetail = detail;

        view.getCompanyName().setText(detail.getCompanyName());
        view.getWeb().setText(detail.getWebsite());
        view.getEmail().setText(detail.getEmail());
        view.getPhone().setText(detail.getPhone());
        view.getFirstName().setText(detail.getFirstName());
        view.getLastName().setText(detail.getLastName());
        view.getIdentificationNumber().setText(detail.getIdentificationNumber());
        view.getTaxNumber().setText(detail.getTaxId());
        view.getDescriptionBox().setText(detail.getDescription());

        setAddressesHeader(detail.getAddresses().get(0).toString());

        eventBus.loadingHide();
    }

    /**************************************************************************/
    /* HELPER METHODS                                                         */
    /**************************************************************************/
    private void setAddressesContent(SettingDetail detail) {
        SimplePanel addressHolder = (SimplePanel) view.getDisclosureAddress().getContent();
        AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
        if (detail.getAddresses() != null && !detail.getAddresses().isEmpty()) {
            AddressDetail addrDetail = detail.getAddresses().get(0);
            addressWidget.getCitySuggestBox().setText(addrDetail.getCity() + ", " + addrDetail.getRegion());
            addressWidget.getZipCodeTextBox().setText(addrDetail.getZipCode());
            addressWidget.getStreetTextBox().setText(addrDetail.getStreet());
        }
    }

    private void setAddressesHeader(String address) {
        SafeHtmlBuilder header = new SafeHtmlBuilder();
        buildHeaderBold(header, Storage.MSGS.address());
        header.appendEscaped(address);
        ((HTML) view.getDisclosureAddress().getHeader()).setHTML(header.toSafeHtml());
    }

    private void buildHeaderBold(SafeHtmlBuilder header, String headerStart) {
        header.appendHtmlConstant("<strong>");
        header.appendEscaped(headerStart);
        header.appendEscaped(": ");
        header.appendHtmlConstant("</strong>");
    }
}
