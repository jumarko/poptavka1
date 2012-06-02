package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;

public interface SettingsRPCServiceAsync {

    void getUserSettings(long userId, AsyncCallback<SettingsDetail> callback);
}
