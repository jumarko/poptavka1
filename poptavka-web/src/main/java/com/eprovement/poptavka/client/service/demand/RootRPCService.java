package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
     * @param locCode
     * @return list locality children list
     */
    List<LocalityDetail> getLocalities(String locCode) throws RPCException;

    /**************************************************************************/
    /* Categories methods                                                     */
    /**************************************************************************/
    List<CategoryDetail> getCategories() throws RPCException;

    List<CategoryDetail> getCategoryChildren(Long category) throws RPCException;

    /**************************************************************************/
    /* User methods                                                           */
    /**************************************************************************/
    BusinessUserDetail getUserById(Long userId) throws RPCException;

    /**************************************************************************/
    /* DevelDetailWrapper widget methods                                      */
    /**************************************************************************/
    FullDemandDetail getFullDemandDetail(long demandId);

    FullSupplierDetail getFullSupplierDetail(long supplierId);

    List<MessageDetail> getConversation(
            long threadId, long userId, long userMessageId) throws RPCException;

    /**************************************************************************/
    /* Message methods                                                        */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException;

    MessageDetail sendMessage(MessageDetail messageToSend) throws RPCException;
}
