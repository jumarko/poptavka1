package com.eprovement.poptavka.client.user.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsView;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.SystemSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SettingsView.class)
public class SettingsPresenter
        extends LazyPresenter<SettingsPresenter.HomeSettingsViewInterface, SettingsEventBus>
        implements NavigationConfirmationInterface {

    private UserSettingsPresenter userPresenter = null;
    private ClientSettingsPresenter clientPresenter = null;
    private SupplierSettingsPresenter supplierPresenter = null;
    private SystemSettingsPresenter systemPresenter = null;
    //
    private SettingDetail settingsDetail;

    //IsWidget musi byt kvoli funkcii ChildAutoDisplay
    public interface HomeSettingsViewInterface extends LazyView, IsWidget {

        /** Setters. **/
        void updateUserStatus(boolean isChange);

        void updateClientStatus(boolean isChange);

        void updateSupplierStatus(boolean isChange);

        void updateSystemStatus(boolean isChange);

        /** Getters. **/
        SimplePanel getContentPanel();

        Button getUpdateButton();

        Button getMenuUserBtn();

        Button getMenuClientBtn();

        Button getMenuSupplierBtn();

        Button getMenuSystemBtn();

        void showNofity(String message, Boolean updated);

        //widget
        void initWidgetDefaults();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    /**
     * Every call of onForward method invokes updateUnreadMessagesCount event that is secured thus user without
     * particular access role can't access it and loginPopupView will be displayed.
     */
    public void onForward() {
        eventBus.setBody(view.getWidgetView());
        view.initWidgetDefaults();
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
        }
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        //pseudo method to verify if the view has changed
        boolean isUserChange = false;
        boolean isClientChange = false;
        boolean isSupplierChange = false;
//        if (view.getUserSettingsPanel().getWidget() != null) { //if not yet even initialized
//            isUserChange = ((UserSettingsView) view.getUserSettingsPanel().getWidget()).isSettingChange();
//        }
//        if (view.getClientSettingsPanel().getWidget() != null) { //if not yet even initialized
//            isClientChange = ((ClientSettingsView) view.getClientSettingsPanel().getWidget()).isSettingChange();
//        }
//        if (view.getSupplierSettingsPanel().getWidget() != null) { //if not yet even initialized
//            isSupplierChange = ((SupplierSettingsView) view.getSupplierSettingsPanel().getWidget()).isSettingChange();
//        }
        if (isUserChange || isClientChange || isSupplierChange) {
            //Window shouldn't be used inside a presenter
            //this is just to give a simple example
            if (Window.confirm(Storage.MSGS.settingsNotificationLeavingPage())) {
                event.fireEvent();
            }
        } else {
            event.fireEvent();
        }
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getUpdateButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                updateProfile();
            }
        });
        view.getMenuUserBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                initUserSettings(view.getContentPanel());
            }
        });
        view.getMenuClientBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                initClientSettings(view.getContentPanel());
            }
        });
        view.getMenuSupplierBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                initSupplierSettings(view.getContentPanel());
            }
        });
        view.getMenuSystemBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                initSystemSettings(view.getContentPanel());
            }
        });
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToSettingsModule() {
        eventBus.loadingShow(Storage.MSGS.loading());
        eventBus.setNavigationConfirmation(this);

        view.getMenuSupplierBtn().setVisible(
                Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.CLIENT));

        GWT.log("User ID for settings" + Storage.getUser().getUserId());

        //Need to retrieve business user data as SettingDetail
        //BusinessUser is only subset of settingDetail, and there is no point while
        //login retrieving userSettings because of time it requires.
        eventBus.getLoggedUser(Storage.getUser().getUserId());
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onSetSettings(SettingDetail detail) {
        this.settingsDetail = detail;
        //set userSettings widget because it is loaded on startup
        initUserSettings(view.getContentPanel());
    }

    public void onResponseUpdateSettings(Boolean updated) {
        eventBus.loadingHide();
        if (updated) {
            view.showNofity(Storage.MSGS.settingsUpdatedOK(), updated);
        } else {
            view.showNofity(Storage.MSGS.settingsUpdatedNotOK(), updated);
        }
    }

    public void onUpdateUserStatus(boolean isChange) {
        view.updateUserStatus(isChange);
    }

    public void onUpdateClientStatus(boolean isChange) {
        view.updateClientStatus(isChange);
    }

    public void onUpdateSupplierStatus(boolean isChange) {
        view.updateSupplierStatus(isChange);
    }

    public void onUpdateSystemStatus(boolean isChange) {
        view.updateSystemStatus(isChange);
    }

    /**************************************************************************/
    /* Init Methods                                                           */
    /**************************************************************************/
    public void initUserSettings(SimplePanel holder) {
        if (userPresenter == null) {
            userPresenter = eventBus.addHandler(UserSettingsPresenter.class);
            userPresenter.onSetUserSettings(settingsDetail);
        }
        userPresenter.initUserSettings(holder);
    }

    public void initClientSettings(SimplePanel holder) {
        if (clientPresenter == null) {
            clientPresenter = eventBus.addHandler(ClientSettingsPresenter.class);
            clientPresenter.onSetClientSettings(settingsDetail);
        }
        clientPresenter.initUserSettings(holder);
    }

    public void initSupplierSettings(SimplePanel holder) {
        if (supplierPresenter == null) {
            supplierPresenter = eventBus.addHandler(SupplierSettingsPresenter.class);
            supplierPresenter.onSetSupplierSettings(settingsDetail);
        }
        supplierPresenter.initUserSettings(holder);
    }

    public void initSystemSettings(SimplePanel holder) {
        if (systemPresenter == null) {
            systemPresenter = eventBus.addHandler(SystemSettingsPresenter.class);
            systemPresenter.onSetSystemSettings(settingsDetail);
        }
        systemPresenter.initSystemSettings(holder);
    }

    /**************************************************************************/
    /* Update settings methods                                                */
    /**************************************************************************/
    /**
     * Update that part of SettingsDetail that belongs to UserSettings widget.
     */
    private boolean updateUserSettings() {
        if (userPresenter != null && userPresenter.isSettingChanged()) {
            settingsDetail = userPresenter.updateUserSettings(settingsDetail);
            return true;
        }
        return false;
    }

    /**
     * Update that part of SettingsDetail that belongs to ClientSettings widget.
     */
    private boolean updateClientSettings() {
        ClientSettingsView clientSettings = (ClientSettingsView) view.getContentPanel().getWidget();

        //check if clientSettings was event loaded
        if (clientSettings != null && clientSettings.isSettingChange()) {
            settingsDetail = clientSettings.updateClientSettings(settingsDetail);
            return true;
        }
        return false;
    }

    /**
     * Update that part of SettingsDetail that belongs to SupplierSettings widget.
     */
    private boolean updateSupplierSettings() {
        //check if supplierSettings was event loaded
        if (supplierPresenter != null && supplierPresenter.isSupplierSettingChanged()) {
            settingsDetail = supplierPresenter.updateSupplierSettings(settingsDetail);
            return true;
        }
        return false;
    }

    /**
     * Update that part of SettingsDetail that belongs to SystemSettings widget.
     */
    private boolean updateSystemSettings() {
        //check if supplierSettings was event loaded
        if (systemPresenter != null && systemPresenter.isSystemSettingsCHanged()) {
            settingsDetail = systemPresenter.updateSystemSettings(settingsDetail);
            return true;
        }
        return false;
    }

    private void updateProfile() {
        eventBus.loadingShow(Storage.MSGS.progressUpdatingProfile());
        boolean update = updateUserSettings();
        update = update || updateClientSettings();
        update = update || updateSupplierSettings();
        update = update || updateSystemSettings();
        if (update) {
            eventBus.requestUpdateSettings(settingsDetail);
        } else {
            eventBus.loadingHide();
            view.showNofity(Storage.MSGS.settingsNothingToUpdate(), true);
        }
    }
}
