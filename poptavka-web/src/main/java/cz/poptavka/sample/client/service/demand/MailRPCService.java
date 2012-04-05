package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.exceptions.CommonException;

@RemoteServiceRelativePath("service/mail")
public interface MailRPCService extends RemoteService {

    Boolean sendMail(String recipient, String body, String subject, String sender) throws CommonException;
}
