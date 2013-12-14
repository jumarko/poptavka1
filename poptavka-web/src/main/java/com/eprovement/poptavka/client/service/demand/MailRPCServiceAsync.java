package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.message.ContactUsDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailRPCServiceAsync {

    void sendMail(ContactUsDetail emailDialogDetail, AsyncCallback<Boolean> callback);
}
