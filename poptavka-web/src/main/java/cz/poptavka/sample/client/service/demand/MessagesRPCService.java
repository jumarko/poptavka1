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
import cz.poptavka.sample.shared.exceptions.CommonException;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath("service/messagesmodule")
public interface MessagesRPCService extends RemoteService {

    ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId) throws CommonException;

    MessageDetail sendInternalMessage(MessageDetail messageDetailImpl) throws CommonException;

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws CommonException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws CommonException;

    List<UserMessageDetail> getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder)
        throws CommonException;

    List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder)
        throws CommonException;

    List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder)
        throws CommonException;

    void deleteMessages(List<Long> messagesIds) throws CommonException;
}
