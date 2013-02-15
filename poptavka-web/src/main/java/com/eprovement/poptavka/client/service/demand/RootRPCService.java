package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(RootRPCService.URL)
public interface RootRPCService extends RemoteService {

    String URL = "service/root";

    /**************************************************************************/
    /* Localities methods                                                     */
    /**************************************************************************/
    /**
     * Returns locality list.
     *
     * @param type
     * @return list locality list according to type
     */
    List<LocalityDetail> getLocalities(LocalityType type) throws RPCException;

    /**
     * Returns locality list.
     *
     * @param locId id of parent locality
     * @return list locality children list
     */
    List<LocalityDetail> getLocalities(Long locId) throws RPCException;

    /**************************************************************************/
    /* Categories methods                                                     */
    /**************************************************************************/
    List<CategoryDetail> getCategories() throws RPCException;

    List<CategoryDetail> getCategoryChildren(Long category) throws RPCException;

    /**************************************************************************/
    /* User methods                                                           */
    /**************************************************************************/
    BusinessUserDetail getUserById(Long userId) throws RPCException;

    /**
     * Finds user by specified email.
     * @param email
     * @return
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    BusinessUserDetail getBusinessUserByEmail(String email) throws RPCException;

    /**************************************************************************/
    /* DevelDetailWrapper widget methods                                      */
    /**************************************************************************/
    FullDemandDetail getFullDemandDetail(long demandId) throws RPCException;

    FullClientDetail getFullClientDetail(long clientId) throws RPCException;

    FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException;

    List<MessageDetail> getConversation(long threadId, long userId) throws RPCException;

    /**************************************************************************/
    /* Message methods                                                        */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException;

    MessageDetail sendQuestionMessage(MessageDetail messageToSend) throws RPCException;

    MessageDetail sendOfferMessage(OfferMessageDetail offerMessageToSend) throws RPCException;

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    UserActivationResult activateUser(BusinessUserDetail user, String activationCode) throws RPCException;

    boolean sendActivationCodeAgain(BusinessUserDetail client) throws RPCException;

    /**************************************************************************/
    /* Supplier Services methods                                              */
    /**************************************************************************/
    ArrayList<ServiceDetail> getSupplierServices() throws RPCException;

    Boolean updateDemands(long demandId, ArrayList<ChangeDetail> changes) throws
            RPCException, ApplicationSecurityException;

    /**************************************************************************/
    /* Registration user methods - Account info                               */
    /**************************************************************************/
    /**
     * Checks wheter given {@code email} is available.
     * @param email ęmail address to be checked
     */
    boolean checkFreeEmail(String email) throws RPCException;
}
