package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@RemoteServiceRelativePath(SettingsRPCService.URL)
public interface SettingsRPCService extends RemoteService {

    String URL = "service/settings";

    SettingDetail getUserSettings(long userId) throws RPCException, ApplicationSecurityException;

    UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException;

    Boolean updateSettings(SettingDetail settingsDetail) throws RPCException, ApplicationSecurityException;

    Boolean checkCurrentPassword(long userId, String password) throws RPCException, ApplicationSecurityException;

    Boolean resetPassword(long userId, String newPassword) throws RPCException, ApplicationSecurityException;
}
