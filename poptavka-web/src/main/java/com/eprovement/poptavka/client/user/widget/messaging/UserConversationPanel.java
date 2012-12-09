package com.eprovement.poptavka.client.user.widget.messaging;

import com.eprovement.poptavka.client.common.session.Storage;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.user.widget.messaging.SimpleMessageWindow.MessageDisplayType;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import java.util.List;

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
    @UiField
    FlowPanel messagePanel;
    ClickHandler acceptHandler = null;
    ClickHandler replyHandler = null;
    ClickHandler deleteHandler = null;
    private int messageCount = 0;
    private MessageDetail replyToMessage;

    public UserConversationPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Display list of messages. When messages are set, control panel should be displayed as well.
     * Message List size is at least always 1
     *
     * @param messages list of messages to be displayed
     */
    public void setMessageList(List<MessageDetail> messages, boolean collapsed) {
        messagePanel.clear();

        /* Find last message from client and store it for possible reply.
         * If last message is supplier's, replyToMessage will be pointing at that
         * message -> if supplier would like to send another question without
         * waiting for client's response on previous one, he will basically
         * send question message to himselft -> there is no sense to do that
         * in this scenario - chat messages */
        for (int j = messages.size() - 1; j >= 0; j--) {
            //Check if sender is not the same as logged user - in case two messages from same user
            long senderId = messages.get(j).getSenderId();
            long userId = Storage.getUser().getUserId();
//            if (messages.get(j).getSenderId() != Storage.getUser().getUserId()) {
            if (senderId != userId) {
                replyToMessage = messages.get(j);
            }
        }

        GWT.log("UserConversation MessageList size: " + messages.size());

        for (int i = 0; i < messages.size(); i++) {
            if (i == messages.size() - 1) {
                messagePanel.add(new SimpleMessageWindow(messages.get(i), false));
            } else {
                messagePanel.add(new SimpleMessageWindow(messages.get(i), collapsed));
            }
        }
        ((SimpleMessageWindow) messagePanel.getWidget(0)).setMessageStyle(MessageDisplayType.FIRST);
        ((SimpleMessageWindow) messagePanel.getWidget(
                messagePanel.getWidgetCount() - 1)).setMessageStyle(MessageDisplayType.LAST);

        messageCount = messagePanel.getWidgetCount();

        if (messageCount == 1) {
            ((SimpleMessageWindow) messagePanel.getWidget(0)).setMessageStyle(MessageDisplayType.BOTH);
        }
    }

    public void addMessage(MessageDetail lastMessage) {
        GWT.log("adding newly created message");
        MessageDisplayType newLastMessage = MessageDisplayType.BOTH;
        if (messageCount > 0) {
            SimpleMessageWindow last = (SimpleMessageWindow) messagePanel.getWidget(messageCount - 1);
            last.setMessageStyle(MessageDisplayType.NORMAL);
            last.setCollapsed(true);
            newLastMessage = MessageDisplayType.LAST;
        }
        messagePanel.add(new SimpleMessageWindow(lastMessage, false, newLastMessage));
        /* Why setting replyToMessage here?
         * This method is called only when supplier posted new question message and that
         * mesasge is added to messages list. If user whould like to send another message
         * replyToMessage will be pointing to his last message -> he will basically send
         * question message to himselft -> there is no sense in that in this scenario (chat messages).
         * How will be working refresh? If Client send reply and that reply is going to be
         * added to supplier message list by this method, we must differ this situations
         * if (lastMessage.getSenderId() != Storage.getUser().getUserId()) check must be applied : */
//        replyToMessage = lastMessage;

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
        // TODO Marin - tu si musime overit ci dodavatel posiela odpoved na spravu od klienta. Ak dodavatel posle po
        // dve spravy tak musime nastavit setReceiverId na klienta a nie na suppliera. Rovnako funguje GMAIL. ak poslem
        // dve odpovede po sebe tak sa mi automaticky nastavi reveiver na adresata. Overit si to mozes napr. cez Storage
        // kde mas aktualneho dodavatela a ak replyToMessage.getSenderId = storage.userId tak musis nastavit ako
        // receivera klienta
        messageDetail.setReceiverId(replyToMessage.getSenderId());
        messageDetail.setParentId(replyToMessage.getMessageId());
        return messageDetail;
    }

    public void toggleVisible() {
        if (messagePanel.isVisible()) {
            messagePanel.getElement().getStyle().setDisplay(Display.NONE);
        } else {
            messagePanel.getElement().getStyle().setDisplay(Display.BLOCK);
        }
    }
}
