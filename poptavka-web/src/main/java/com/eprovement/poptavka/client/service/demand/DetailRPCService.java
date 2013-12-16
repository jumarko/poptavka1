package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.FullRatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(DetailRPCService.URL)
public interface DetailRPCService extends RemoteService {

    String URL = "service/detail";

    FullDemandDetail getFullDemandDetail(long demandId) throws RPCException;

    FullClientDetail getFullClientDetail(long clientId) throws RPCException;

    FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException;

    FullRatingDetail getFullRatingDetail(long demandId) throws RPCException;

    List<MessageDetail> getConversation(long threadRootId, long loggedUserId, long counterPartyUserId)
        throws RPCException;

    void updateUserMessagesReadStatus(long userId, List<MessageDetail> messages) throws RPCException;

    MessageDetail sendQuestionMessage(MessageDetail messageToSend) throws RPCException;

    MessageDetail sendOfferMessage(OfferMessageDetail offerMessageToSend) throws RPCException;
}
