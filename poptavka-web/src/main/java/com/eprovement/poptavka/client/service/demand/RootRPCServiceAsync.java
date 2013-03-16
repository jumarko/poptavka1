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

    void getConversation(long threadId, long userId, AsyncCallback<List<MessageDetail>> callback);

    /**************************************************************************/
    /* Message methods                                                        */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

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
}
