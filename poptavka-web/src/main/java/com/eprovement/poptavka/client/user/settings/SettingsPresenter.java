/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.settings.interfaces.ISettings;
import com.eprovement.poptavka.client.user.settings.toolbar.SettingsToolbarView;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * Used to view and update user's data.
 * @author Martin Slavkovsky
 */
@Presenter(view = SettingsView.class)
public class SettingsPresenter
        extends LazyPresenter<ISettings.View, SettingsEventBus>
        implements ISettings.Presenter {

    /**************************************************************************/
    /* Attributes events                                                      */
    /**************************************************************************/
    /** Class attributes. **/
    private SettingDetail settingsDetail;
    /**
     * Since we don't have holder for each widget, validate widget while leaving current tab.
     * If invalid, shows notifier window to user.
     */
    boolean valid = true;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    /**
     * Every call of onForward method invokes updateUnreadMessagesCount event that is secured thus user without
     * particular access role can't access it and loginPopupView will be displayed.
     * Sets body, footer, toolbar and update unread messages count
     */
    public void onForward() {
        eventBus.setBody(view);
        eventBus.setFooter(view.getFooterContainer());
        eventBus.setToolbarContent("My Profile", view.getToolbarContent());
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
        }
    }

    /**
     * Asks for confirmation when leaving module.
     * Widgets updates data, therefore notify user that he can lose them without saving.
     * @param event the NavigationEventCommand
     */
    @Override
    public void confirm(NavigationEventCommand event) {
        String navigationLeaveModule = event.toString().substring(0, event.toString().indexOf("$"));
        //check if contains service selector, in case of SupplierSettings widget where
        //ServiceSelector is presented and it fires navigation event on forward but it is still part of setting module
        if (!navigationLeaveModule.contains("ServiceSelector")) {
            if (Window.confirm(Storage.MSGS.settingsNotificationLeavingPage())) {
                //Leaving without saving - reset validation
                event.fireEvent();
            }
        } else {
            //if same module but different method do nothing
        }
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    /**
     * Binds toolbar & menu button handlers.
     */
    @Override
    public void bindView() {
        ((SettingsToolbarView) view.getToolbarContent()).getUpdateButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (isValid()) {
                    //Just to ensure update won't fire multiple times
                    //in case user click update button multiple times by mistake
                    Timer timer = new Timer() {
                        @Override
                        public void run() {
                            ((SettingsToolbarView) view.getToolbarContent()).getUpdateButton().setEnabled(true);
                        }
                    };
                    timer.schedule(2000);
                    ((SettingsToolbarView) view.getToolbarContent()).getUpdateButton().setEnabled(false);
                    updateProfile();
                }
            }
        });
        view.getTabBtn(ISettings.SettingsTab.USER).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                if (isValid()) {
                    initUserSettings(view.getContentPanel());
                }
            }
        });
        view.getTabBtn(ISettings.SettingsTab.CLIENT).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                if (isValid()) {
                    initClientSettings(view.getContentPanel());
                }
            }
        });
        view.getTabBtn(ISettings.SettingsTab.SUPPLIER).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                if (isValid()) {
                    initSupplierSettings(view.getContentPanel());
                }
            }
        });
        view.getTabBtn(ISettings.SettingsTab.SYSTEM).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                if (isValid()) {
                    initSystemSettings(view.getContentPanel());
                }
            }
        });
        view.getTabBtn(ISettings.SettingsTab.SECURITY).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                if (isValid()) {
                    initSecuritySettings(view.getContentPanel());
                }
            }
        });
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * Creates Settings module.
     */
    public void onGoToSettingsModule() {
        eventBus.loadingShow(Storage.MSGS.loading());
        eventBus.setNavigationConfirmation(this);

        view.allowSupplierSettings(
                Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.SUPPLIER));

        GWT.log("User ID for settings" + Storage.getUser().getUserId());

        //Need to retrieve business user data as SettingDetail
        //BusinessUser is only subset of settingDetail, and there is no point while
        //login retrieving userSettings because of time it requires.
        eventBus.getLoggedUser(Storage.getUser().getUserId());
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    /**
     * Sets settings data.
     * @param detail the SettingDetail carrying data
     */
    public void onSetSettings(SettingDetail detail) {
        this.settingsDetail = detail;
        //set userSettings widget because it is loaded on startup
        initUserSettings(view.getContentPanel());
        eventBus.loadingHide();
    }

    /**
     * Shows thank you popup after updating data.
     * @param updated true if successful, false otherwise
     */
    public void onResponseUpdateSettings(Boolean updated) {
        eventBus.loadingHide();
        if (updated) {
            eventBus.showThankYouPopup(SafeHtmlUtils.fromString(Storage.MSGS.settingsUpdatedOK()), null);
        } else {
            eventBus.showThankYouPopup(SafeHtmlUtils.fromString(Storage.MSGS.settingsUpdatedNotOK()), null);
        }
    }

    /**************************************************************************/
    /* Init Methods                                                           */
    /**************************************************************************/
    /**
     * Inits UserSettings widget.
     * @param holder panel
     */
    public void initUserSettings(SimplePanel holder) {
        eventBus.initUserSettings(holder);
        eventBus.setUserSettings(settingsDetail);
        ((SettingsToolbarView) view.getToolbarContent()).getUpdateButton().setVisible(true);
        view.settingsTabStyleChange(ISettings.SettingsTab.USER);
    }

    /**
     * Inits ClientSettings widget.
     * @param holder panel
     */
    public void initClientSettings(SimplePanel holder) {
        eventBus.initClientSettings(holder);
        eventBus.setClientSettings(settingsDetail);
        ((SettingsToolbarView) view.getToolbarContent()).getUpdateButton().setVisible(true);
        view.settingsTabStyleChange(ISettings.SettingsTab.CLIENT);
    }

    /**
     * Inits SupplierSettings widget.
     * @param holder panel
     */
    public void initSupplierSettings(SimplePanel holder) {
        eventBus.initSupplierSettings(holder);
        eventBus.setSupplierSettings(settingsDetail);
        ((SettingsToolbarView) view.getToolbarContent()).getUpdateButton().setVisible(true);
        view.settingsTabStyleChange(ISettings.SettingsTab.SUPPLIER);
    }

    /**
     * Inits SystemSettings widget.
     * @param holder panel
     */
    public void initSystemSettings(SimplePanel holder) {
        eventBus.initSystemSettings(holder);
        eventBus.setSystemSettings(settingsDetail);
        ((SettingsToolbarView) view.getToolbarContent()).getUpdateButton().setVisible(true);
        view.settingsTabStyleChange(ISettings.SettingsTab.SYSTEM);
    }

    /**
     * Inits SecuritySettings widget.
     * @param holder panel
     */
    public void initSecuritySettings(SimplePanel holder) {
        eventBus.initSecuritySettings(holder);
        eventBus.setSecuritySettings(settingsDetail);
        ((SettingsToolbarView) view.getToolbarContent()).getUpdateButton().setVisible(false);
        view.settingsTabStyleChange(ISettings.SettingsTab.SECURITY);
    }

    /**************************************************************************/
    /* Update settings methods                                                */
    /**************************************************************************/
    /**
     * Validates widgets' components.
     * @return true if valid, false otherwise
     */
    private boolean isValid() {
        if (view.getContentPanel().getWidget() != null) {
            valid = ((ProvidesValidate) view.getContentPanel().getWidget()).isValid();
        }
        if (!valid) {
            eventBus.showAlertPopup(LocalizableMessages.INSTANCE.settingsInvalidFields());
        }
        return valid;
    }

    /**
     * Updates user profile data.
     */
    private void updateProfile() {
        eventBus.loadingShow(Storage.MSGS.progressUpdatingProfile());

        eventBus.fillUserSettings(settingsDetail);
        eventBus.fillClientSettings(settingsDetail);
        eventBus.fillSupplierSettings(settingsDetail);
        eventBus.fillSystemSettings(settingsDetail);

        eventBus.requestUpdateSettings(settingsDetail);
    }
}