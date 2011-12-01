package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.settings.SettingsDetail;

public interface SettingsRPCServiceAsync {

    void getUserSettings(String sessionId, AsyncCallback<SettingsDetail> callback);
}
