package cz.poptavka.sample.client.user.messages;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.user.messages.SimpleMessageWindow.MessageDisplayType;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;

/**
 * SimpleMessageWindow holder, enabling features:
 * <ul>
 * TODO
 * <li>collapsing</li>
 * TODO
 * <li>expanding</li>
 * TODO
 * <li>hiding</li>
 * TODO
 * <li>displaying tree of messages</li>
 * </ul>
 * Will need  presenter later, when handling this events.
 * (maybe not, if actions will be bind in wrapper presenter)
 *
 * @author Beho
 *
 */
public class UserConversationPanel extends Composite {

    private static UserConversationPanelUiBinder uiBinder = GWT.create(UserConversationPanelUiBinder.class);
    interface UserConversationPanelUiBinder extends UiBinder<Widget, UserConversationPanel> {
    }

    private ArrayList<OfferWindowPresenter> offerPresenters = new ArrayList<OfferWindowPresenter>();

    @UiField FlowPanel messagePanel;

    ClickHandler acceptHandler = null;
    ClickHandler replyHandler = null;
    ClickHandler deleteHandler = null;

    private int messageCount = 0;
    private MessageDetail replyToMessage;

    public UserConversationPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Display list of messages. When messages are set, control panell should be displayed as well.
     * Message List size is at least always 1
     *
     * @param messages list of messages to be displayed
     */
    public void setMessageList(ArrayList<MessageDetail> messages, boolean collapsed) {
        messagePanel.clear();

        // Last message is visible, when there are more messages
        // last message is always stored for reply
        replyToMessage = messages.get(messages.size() - 1);

        GWT.log(" **** size: " + messages.size());

        if (messages.size() > 1) {
            for (int i = 0; i < messages.size(); i++) {
                messagePanel.add(new SimpleMessageWindow(messages.get(i), collapsed));
            }
            ((SimpleMessageWindow) messagePanel.getWidget(0)).setMessageStyle(MessageDisplayType.FIRST);
            ((SimpleMessageWindow) messagePanel.getWidget(messages.size() - 1))
                .setMessageStyle(MessageDisplayType.LAST);
        }

        messageCount = messagePanel.getWidgetCount();

        if (messageCount == 1) {
            ((SimpleMessageWindow) messagePanel.getWidget(0)).setMessageStyle(MessageDisplayType.BOTH);
        }
    }

    public void addMessage(MessageDetail lastMessage) {
        MessageDisplayType newLastMessage = MessageDisplayType.BOTH;
        if (messageCount > 0) {
            SimpleMessageWindow last = (SimpleMessageWindow) messagePanel.getWidget(messageCount - 1);
            last.setMessageStyle(MessageDisplayType.NORMAL);
            last.setCollapsed(true);
            newLastMessage = MessageDisplayType.LAST;
        }
        messagePanel.add(new SimpleMessageWindow(lastMessage, false, newLastMessage));
        replyToMessage = lastMessage;


        messageCount = messagePanel.getWidgetCount();
    }

    public void addOfferMessagePresenter(OfferWindowPresenter presenter) {
        offerPresenters.add(presenter);
        messagePanel.add(presenter.getWidgetView());
    }

    public ArrayList<OfferWindowPresenter> clearContent() {
        return offerPresenters;
    }

    /**
     * Received the message being sent and fills it with necessary attributes, from stored message.
     *
     * @param MessageDetail message being sent
     * @return updated message
     */
    public OfferMessageDetail updateSendingOfferMessage(OfferMessageDetail messageDetail) {
        messageDetail.setThreadRootId(replyToMessage.getThreadRootId());
        messageDetail.setReceiverId(replyToMessage.getSenderId());
        messageDetail.setParentId(replyToMessage.getMessageId());
        messageDetail.setDemandId(replyToMessage.getDemandId());
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
        messageDetail.setReceiverId(replyToMessage.getSenderId());
        messageDetail.setParentId(replyToMessage.getMessageId());
        messageDetail.setDemandId(replyToMessage.getDemandId());
        return messageDetail;
    }

}
