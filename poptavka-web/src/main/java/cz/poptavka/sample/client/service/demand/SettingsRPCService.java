package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.settings.SettingsDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

@RemoteServiceRelativePath(SettingsRPCService.URL)
public interface SettingsRPCService extends RemoteService {

    String URL = "service/settings";

    SettingsDetail getUserSettings(long userId) throws RPCException;
}
