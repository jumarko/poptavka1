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

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath("service/messagesmodule")
public interface MessagesRPCService extends RemoteService {

    ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId);

    MessageDetail sendInternalMessage(MessageDetail messageDetailImpl);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead);

    void setMessageStarStatus(List<Long> list, boolean newStatus);

    List<UserMessageDetail> getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder);

    List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder);

    List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder);

    void deleteMessages(List<Long> messagesIds);
}
