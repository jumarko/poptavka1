package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.FullRatingDetail;
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
public interface DetailRPCServiceAsync {

    void getFullDemandDetail(long demandId, AsyncCallback<FullDemandDetail> callback);

    void getFullClientDetail(long clientId, AsyncCallback<FullClientDetail> callback);

    void getFullSupplierDetail(long supplierId, AsyncCallback<FullSupplierDetail> callback);

    void getFullRatingDetail(long demandId, AsyncCallback<FullRatingDetail> callback);

    void getConversation(long threadRootId, long loggedUserId, long counterPartyUserId,
            AsyncCallback<List<MessageDetail>> callback);

    void updateUserMessagesReadStatus(long userId, List<MessageDetail> messages, AsyncCallback<Void> callback);

    void sendQuestionMessage(MessageDetail messageToSend, AsyncCallback<MessageDetail> callback);

    void sendOfferMessage(OfferMessageDetail offerMessageToSend, AsyncCallback<MessageDetail> callback);

    void substractCredit(long userId, int credits, AsyncCallback<Boolean> callback);
}
