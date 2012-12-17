/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter.UserSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.SimplePanel;
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

        void setUserSettings(SettingDetail detail);

//        void setAddressesHeader(String address);

//        void setAddressesContent(AddressDetail detail);

        SettingDetail updateUserSettings(SettingDetail detail);

        //Panels
//        DisclosurePanel getDisclosureAddress();

        SimplePanel getAddressHolder();

        //Others
        AddressDetail getAddress();

        TextBox getStatus();

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
        view.getStatus().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                eventBus.updateUserStatus(view.isSettingChange());
            }
        });
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
        eventBus.initAddressWidget(view.getAddressHolder());
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
