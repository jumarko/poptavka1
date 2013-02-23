package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.message.EmailDialogDetail;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.exceptions.RPCException;

@RemoteServiceRelativePath(MailRPCService.URL)
public interface MailRPCService extends RemoteService {

    String URL = "service/mail";

    Boolean sendMail(EmailDialogDetail emailDialogDetail) throws RPCException;
}
