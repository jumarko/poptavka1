package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailRPCServiceAsync {

    void sendMail(String recipient, String body, String subject, String sender, AsyncCallback<Boolean> callback);
}
