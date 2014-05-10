package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginUnsecRPCServiceAsync {

    /**
     * Finds user by specified email.
     * @param email - user email to find
     */
    void getBusinessUserByEmail(String email, AsyncCallback<BusinessUserDetail> async);

    /**
     * Reset password for user who forgot his password. New random password is saved into database.
     * @param userId whose password will be reset
     * @return new random password
     */
    void resetPassword(long userId, AsyncCallback<String> callback);

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    void activateUser(BusinessUserDetail user, String activationCode, AsyncCallback<UserActivationResult> callback);

    void sendActivationCodeAgain(BusinessUserDetail client, AsyncCallback<Boolean> callback);

    void hasActivationEmail(BusinessUserDetail user, AsyncCallback<Void> callback);
}
