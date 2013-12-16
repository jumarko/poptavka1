/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.service.demand.SettingsRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;

/**
 * Handler RPC calls for Settings module.
 * @author Martin Slavkovsky
 */
@EventHandler
public class SettingsHandler extends BaseEventHandler<SettingsEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    @Inject
    SettingsRPCServiceAsync settingsService;

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Request logged user.
     * @param userId
     */
    public void onGetLoggedUser(long userId) {
        GWT.log("HomeSettingsHandler handling user" + userId);
        settingsService.getUserSettings(userId, new SecuredAsyncCallback<SettingDetail>(eventBus) {
            @Override
            public void onSuccess(SettingDetail result) {
                GWT.log("uspesny settingsDetail");
                eventBus.setSettings(result);

            }
        });
    }

    /**
     * Updates unread messages count.
     */
    public void onUpdateUnreadMessagesCount() {
        settingsService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                eventBus.setUpdatedUnreadMessagesCount(result);
            }
        });
    }

    /**
     * Updates settings.
     * @param settingsDetail to be updated
     */
    public void onRequestUpdateSettings(SettingDetail settingsDetail) {
        settingsService.updateSettings(settingsDetail, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                eventBus.responseUpdateSettings(result);
            }
        });
    }

    /**
     * Checks current password.
     * @param userId
     * @param password user's current password
     */
    public void onRequestCheckCurrentPassword(long userId, String password) {
        settingsService.checkCurrentPassword(userId, password, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean correct) {
                eventBus.responseCheckCurrentPassword(correct.booleanValue());
            }
        });
    }

    /**
     * Resets user's password
     * @param userId
     * @param newPassword
     */
    public void onRequestResetPassword(long userId, String newPassword) {
        settingsService.resetPassword(userId, newPassword, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                eventBus.responseResetPassword(result.booleanValue());
            }
        });
    }
}
