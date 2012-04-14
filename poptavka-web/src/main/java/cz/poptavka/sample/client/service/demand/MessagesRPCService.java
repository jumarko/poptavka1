/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath("service/messagesmodule")
public interface MessagesRPCService extends RemoteService {

    ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId) throws RPCException;

    MessageDetail sendInternalMessage(MessageDetail messageDetailImpl) throws RPCException;

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws RPCException;

    List<UserMessageDetail> getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder)
        throws RPCException;

    List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder)
        throws RPCException;

    List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder)
        throws RPCException;

    void deleteMessages(List<Long> messagesIds) throws RPCException;
}
