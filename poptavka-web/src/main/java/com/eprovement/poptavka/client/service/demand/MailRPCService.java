package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.message.ContactUsDetail;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.exceptions.RPCException;

@RemoteServiceRelativePath(MailRPCService.URL)
public interface MailRPCService extends RemoteService {

    String URL = "service/mail";

    Boolean sendMail(ContactUsDetail emailDialogDetail) throws RPCException;
}
