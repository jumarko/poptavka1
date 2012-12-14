/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter.UserSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.SimplePanel;
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

        void setUserSettings(SettingDetail detail);

        void setAddressesHeader(String address);

        void setAddressesContent(SettingDetail detail);

        SettingDetail updateUserSettings(SettingDetail detail);

        //Panels
        DisclosurePanel getDisclosureAddress();

        //Others
        AddressDetail getAddress();

        boolean isSettingChange();

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
                view.setAddressesContent(settingsDetail);
            }
        });
        view.getDisclosureAddress().addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> event) {
                SimplePanel addressHolder = ((SimplePanel) view.getDisclosureAddress().getContent());
                AddressSelectorView addressWidget = (AddressSelectorView) addressHolder.getWidget();
                view.setAddressesHeader(addressWidget.createAddress().toString());
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
        view.setUserSettings(detail);
        eventBus.loadingHide();
    }
}
