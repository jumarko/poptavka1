package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.exceptions.RPCException;

@RemoteServiceRelativePath(MailRPCService.URL)
public interface MailRPCService extends RemoteService {

    String URL = "service/mail";

    Boolean sendMail(String recipient, String body, String subject, String sender) throws RPCException;
}
