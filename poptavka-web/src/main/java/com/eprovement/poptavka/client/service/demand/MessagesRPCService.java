/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath(MessagesRPCService.URL)
public interface MessagesRPCService extends RemoteService {

    String URL = "service/messagesmodule";

    ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId) throws RPCException,
            ApplicationSecurityException;

    MessageDetail sendInternalMessage(MessageDetail messageDetailImpl) throws RPCException,
            ApplicationSecurityException;

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException,
            ApplicationSecurityException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws RPCException,
            ApplicationSecurityException;

    /** INBOX. **/
    Integer getInboxMessagesCount(Long recipientId, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    List<MessageDetail> getInboxMessages(Long recipientId, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder)
        throws RPCException, ApplicationSecurityException;

    List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder)
        throws RPCException, ApplicationSecurityException;

    void deleteMessages(List<Long> messagesIds) throws RPCException, ApplicationSecurityException;

    UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException;
}
