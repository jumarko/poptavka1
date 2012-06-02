/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;

import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Drobcek
 */
public interface MessagesRPCServiceAsync {

    void sendInternalMessage(MessageDetail messageDetailImpl, AsyncCallback<MessageDetail> callback);

    void getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder,
            AsyncCallback<List<UserMessageDetail>> callback);

    void getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder,
            AsyncCallback<List<UserMessageDetail>> callback);

    void getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder,
            AsyncCallback<List<UserMessageDetail>> callback);

    void getConversationMessages(long threadRootId, long subRootId,
            AsyncCallback<ArrayList<MessageDetail>> callback);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void setMessageStarStatus(List<Long> list, boolean newStatus, AsyncCallback<Void> callback);

    void deleteMessages(List<Long> messagesIds, AsyncCallback<List<UserMessageDetail>> callback);
}
