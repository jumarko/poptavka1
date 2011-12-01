package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.settings.SettingsDetail;

@RemoteServiceRelativePath("service/settings")
public interface SettingsRPCService extends RemoteService {

    SettingsDetail getUserSettings(String sessionId);
}
