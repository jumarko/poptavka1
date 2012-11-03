package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@RemoteServiceRelativePath(SettingsRPCService.URL)
public interface SettingsRPCService extends RemoteService {

    String URL = "service/settings";

    SettingsDetail getUserSettings(long userId) throws RPCException;
}
