package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public interface RootRPCServiceAsync {


    /**************************************************************************/
    /* User methods                                                           */
    /**************************************************************************/
    void getUserById(Long userId, AsyncCallback<BusinessUserDetail> callback);

    /**************************************************************************/
    /* DevelDetailWrapper widget methods                                      */
    /**************************************************************************/
    void getFullDemandDetail(long demandId, AsyncCallback<FullDemandDetail> callback);

    void getFullClientDetail(long clientId, AsyncCallback<FullClientDetail> callback);

    void getFullSupplierDetail(long supplierId, AsyncCallback<FullSupplierDetail> callback);

    void getConversation(long threadRootId, long loggedUserId, long counterPartyUserId,
            AsyncCallback<List<MessageDetail>> callback);

    void updateUserMessagesReadStatus(long userId, List<MessageDetail> messages, AsyncCallback<Void> callback);

    /**************************************************************************/
    /* Message methods                                                        */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void setMessageStarStatus(List<Long> list, boolean newStatus, AsyncCallback<Void> callback);

    void sendQuestionMessage(MessageDetail messageToSend, AsyncCallback<MessageDetail> callback);

    void sendOfferMessage(OfferMessageDetail offerMessageToSend, AsyncCallback<MessageDetail> callback);

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    void activateUser(BusinessUserDetail user, String activationCode, AsyncCallback<UserActivationResult> callback);

    void sendActivationCodeAgain(BusinessUserDetail client, AsyncCallback<Boolean> callback);

    /**************************************************************************/
    /* Supplier Services methods                                              */
    /**************************************************************************/
    void getSupplierServices(AsyncCallback<ArrayList<ServiceDetail>> callback);

    /**
     * Finds user by specified email.
     *
     * @param email
     * @return
     * @throws com.eprovement.poptavka.shared.exceptions.RPCException
     *
     * @throws com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException
     *
     */
    void getBusinessUserByEmail(String email, AsyncCallback<BusinessUserDetail> async);

    /**************************************************************************/
    /* Registration user methods - Account info                               */
    /**************************************************************************/
    /** @see UserRPCService#checkFreeEmail(String) */
    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);

    /**
     * Reset password for user who forgot his password. New random password is saved into database.
     * @param userId whose password will be reset
     * @return new random password
     */
    void resetPassword(long userId, AsyncCallback<String> callback);
}
