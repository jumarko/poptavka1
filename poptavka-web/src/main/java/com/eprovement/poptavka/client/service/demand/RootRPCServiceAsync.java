package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public interface RootRPCServiceAsync {

    /**************************************************************************/
    /* Locality methods                                                       */
    /**************************************************************************/
    void getLocalities(LocalityType type, AsyncCallback<List<LocalityDetail>> callback);

    void getLocalities(String locCode, AsyncCallback<List<LocalityDetail>> callback);

    /**************************************************************************/
    /* Category methods                                                       */
    /**************************************************************************/
    void getCategories(AsyncCallback<List<CategoryDetail>> callback);

    void getCategoryChildren(Long category, AsyncCallback<List<CategoryDetail>> callback);

    /**************************************************************************/
    /* User methods                                                           */
    /**************************************************************************/
    void getUserById(Long userId, AsyncCallback<BusinessUserDetail> callback);

    /**************************************************************************/
    /* DevelDetailWrapper widget methods                                      */
    /**************************************************************************/
    void getFullDemandDetail(long demandId, AsyncCallback<FullDemandDetail> callback);

    void getFullSupplierDetail(long supplierId, AsyncCallback<FullSupplierDetail> callback);

    void getConversation(
            long threadId, long userId, long userMessageId, AsyncCallback<List<MessageDetail>> callback);

    /**************************************************************************/
    /* Message methods                                                        */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void sendQuestionMessage(MessageDetail messageToSend, AsyncCallback<MessageDetail> callback);

    void sendOfferMessage(OfferMessageDetail offerMessageToSend, AsyncCallback<OfferMessageDetail> callback);
}
