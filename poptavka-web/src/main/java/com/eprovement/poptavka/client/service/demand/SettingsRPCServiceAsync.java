package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;

public interface SettingsRPCServiceAsync {

    void getUserSettings(long userId, AsyncCallback<SettingsDetail> callback);

    void updateUnreadMessagesCount(AsyncCallback<UnreadMessagesDetail> callback);
}
