package com.eprovement.poptavka.client.user.widget.messaging;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import java.util.List;

/**
 * Displaying messages list.
 *
 * @author Martin Slavkovsky
 *
 */
public class UserConversationPanel2 extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static UserConversationPanelUiBinder uiBinder = GWT.create(UserConversationPanelUiBinder.class);

    interface UserConversationPanelUiBinder extends UiBinder<Widget, UserConversationPanel2> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) CellList messageList;
    /** Class attributes. **/
    private ListDataProvider messageProvider = new ListDataProvider(MessageDetail.KEY_PROVIDER);
    private MessageDetail replyToMessage;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public UserConversationPanel2() {
        messageList = new CellList<MessageDetail>(new MessageCell());
        messageProvider.addDataDisplay(messageList);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/

    public CellList getMessagePanel() {
        return messageList;
    }

    /**
     * Display list of messages. When messages are set, control panel should be displayed as well.
     * Message List size is at least always 1
     *
     * @param messages list of messages to be displayed
     */
    public void setMessageList(List<MessageDetail> messages, boolean collapsed) {
        messageProvider.setList(messages);
    }

    public void addMessage(MessageDetail lastMessage) {
        messageProvider.getList().add(lastMessage);
    }

    /**
     * Received the message being sent and fills it with necessary attributes, from stored message.
     *
     * @param MessageDetail message being sent
     * @return updated message
     */
    public OfferMessageDetail updateSendingOfferMessage(OfferMessageDetail messageDetail) {
        messageDetail.setThreadRootId(replyToMessage.getThreadRootId());
        messageDetail.setParentId(replyToMessage.getMessageId());
        messageDetail.setSupplierId(Storage.getBusinessUserDetail().getSupplierId());
        return messageDetail;
    }

    /**
     * Received the message being sent and fills it with necessary attributes, from stored message.
     *
     * @param MessageDetail message being sent
     * @return updated message
     */
    public MessageDetail updateSendingMessage(MessageDetail messageDetail) {
        messageDetail.setThreadRootId(replyToMessage.getThreadRootId());
        messageDetail.setParentId(replyToMessage.getMessageId());
        return messageDetail;
    }

    public void clear() {
        messageProvider.getList().clear();
    }
}
