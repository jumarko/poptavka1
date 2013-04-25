package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.shared.domain.settings.SettingDetail;

public interface SettingsRPCServiceAsync {

    void getUserSettings(long userId, AsyncCallback<SettingDetail> callback);

    void updateUnreadMessagesCount(AsyncCallback<UnreadMessagesDetail> callback);

    void updateSettings(SettingDetail settingsDetail, AsyncCallback<Boolean> callback);

    void checkCurrentPassword(long userId, String password, AsyncCallback<Boolean> callback);

    void resetPassword(long userId, String newPassword, AsyncCallback<Boolean> callback);
}
