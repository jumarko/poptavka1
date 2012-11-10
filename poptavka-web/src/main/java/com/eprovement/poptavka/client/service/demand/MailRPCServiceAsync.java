package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.message.EmailDialogDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailRPCServiceAsync {

    void sendMail(EmailDialogDetail emailDialogDetail, AsyncCallback<Boolean> callback);
}
