/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.address.AddressSelectorPresenter.AddressSelectorInterface;
import com.eprovement.poptavka.client.common.address.AddressSelectorView;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter.UserSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

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

        void setChangeHandler(ChangeHandler changeHandler);

        void setUserSettings(SettingDetail detail);

        void setAddressSettings(AddressDetail detail);

        void setCurrentPasswordStyles(boolean correct);

        void setDefaultPasswordsStyles();

        SettingDetail updateUserSettings(SettingDetail detail);

        SimplePanel getAddressHolder();

        ValidationMonitor getPasswordCurrentMonitor();

        ValidationMonitor getPasswordNewMonitor();

        ValidationMonitor getPasswordNewConfirmMonitor();

        Button getChangeBtn();

        //Others
        AddressDetail getAddress();

        void commit();

        boolean isNewPasswordValid();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //history of changes
    private ArrayList<ChangeDetail> updatedFields = new ArrayList<ChangeDetail>();
    private SettingDetail settingsDetail;

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        setUserSettingsMonitorsChangeHandlers();
        setChangeBtnClickHandler();
    }

    /**************************************************************************/
    /* BIND - Helper methods                                                  */
    /**************************************************************************/
    private void setUserSettingsMonitorsChangeHandlers() {
        view.setChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                ChangeMonitor source = (ChangeMonitor) event.getSource();
                source.getChangeDetail().setValue(source.getValue());
                if (source.isModified()) {
                    //if contains already - remove before adding new
                    if (updatedFields.contains(source.getChangeDetail())) {
                        updatedFields.remove(source.getChangeDetail());
                    }
                    updatedFields.add(source.getChangeDetail());
                } else {
                    updatedFields.remove(source.getChangeDetail());
                }
                eventBus.updateUserStatus(isSettingChanged());
            }
        });
    }

    private void setAddressMonitorsChangeHandlers() {
        final AddressSelectorView addressSelectorView = (AddressSelectorView) view.getAddressHolder().getWidget();
        addressSelectorView.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                eventBus.updateUserStatus(addressSelectorView.isAddressChanged());
            }
        });
    }

    private void setChangeBtnClickHandler() {
        view.getChangeBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (view.isNewPasswordValid()) {
                    eventBus.requestCheckCurrentPassword(Storage.getUser().getUserId(),
                            (String) view.getPasswordCurrentMonitor().getValue());
                }
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
    }

    public void onNotifyAddressWidgetListeners(AddressSelectorInterface addressWidget) {
        addressWidget.setChangeMonitorsEnabled(true);
        setAddressMonitorsChangeHandlers();
    }

    public void onResponseResetPassword(boolean result) {
        if (result) {
            Window.alert(Storage.MSGS.userSettingsPasswordChangedSucceeded());
            view.setDefaultPasswordsStyles();
        } else {
            Window.alert(Storage.MSGS.userSettingsPasswordChangedFailed());
        }
    }

    public void onResponseCheckCurrentPassword(boolean correct) {
        view.setCurrentPasswordStyles(correct);
        if (correct) {
            eventBus.requestResetPassword(Storage.getUser().getUserId(),
                    (String) view.getPasswordNewMonitor().getValue());
        }
    }

    public SettingDetail updateUserSettings(SettingDetail detail) {
        return view.updateUserSettings(detail);
    }

    public boolean isSettingChanged() {
        return !updatedFields.isEmpty();
    }
}
