package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.service.demand.SettingsRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;

@EventHandler
public class SettingsHandler extends BaseEventHandler<SettingsEventBus> {

    @Inject
    SettingsRPCServiceAsync settingsService;

    public void onGetLoggedUser(long userId) {
        GWT.log("HomeSettingsHandler handling user" + userId);
        settingsService.getUserSettings(userId, new SecuredAsyncCallback<SettingsDetail>(eventBus) {
            @Override
            public void onSuccess(SettingsDetail result) {
                GWT.log("uspesny settingsDetail");
                eventBus.setSettings(result);

            }
        });
    }

    public void onUpdateUnreadMessagesCount() {
        settingsService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                // empty i.e number of new messages could be retrieved
                GWT.log("UpdateUnreadMessagesCount retrieved, number=" + result.getUnreadMessagesCount());
                eventBus.setUpdatedUnreadMessagesCount(result.getUnreadMessagesCount());
            }
        });
    }
}
