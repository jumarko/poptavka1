package com.eprovement.poptavka.client.user.widget.messaging;

import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
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
public class UserConversationPanel2 extends Composite {

    private static UserConversationPanelUiBinder uiBinder = GWT.create(UserConversationPanelUiBinder.class);

    interface UserConversationPanelUiBinder extends UiBinder<Widget, UserConversationPanel2> {
    }
    private ArrayList<OfferWindowPresenter> offerPresenters = new ArrayList<OfferWindowPresenter>();
    @UiField(provided = true)
    CellList messageList;
    private ListDataProvider messageProvider = new ListDataProvider(MessageDetail.KEY_PROVIDER);
    private SingleSelectionModel selectionModel = new SingleSelectionModel(MessageDetail.KEY_PROVIDER);
//    ClickHandler acceptHandler = null;
//    ClickHandler replyHandler = null;
//    ClickHandler deleteHandler = null;
//    private int messageCount = 0;
    private MessageDetail replyToMessage;

    public UserConversationPanel2() {
        messageList = new CellList<MessageDetail>(new MessageCell());
//        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//
//            @Override
//            public void onSelectionChange(SelectionChangeEvent event) {
//                MessageCell cell = (MessageCell) event.getSource();
//                cell.setBody("doriti");
//                messageList.redraw();
//            }
//        });
        messageList.setSelectionModel(selectionModel);
        messageProvider.addDataDisplay(messageList);
        initWidget(uiBinder.createAndBindUi(this));


//        messageList.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler<MessageDetail>() {
//
//            @Override
//            public void onSelectionChange(SelectionChangeEvent<MessageDetail> event) {
//                messageList.redraw();
//            }
//        });
    }

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
        messageProvider.flush();
        messageProvider.refresh();
        messageList.redraw();
//        messageList.clear();
//
//        replyToMessage = messages.get(messages.size() - 1);
//
//        GWT.log("UserConversation MessageList size: " + messages.size());
//
//        for (int i = 0; i < messages.size(); i++) {
//            if (i == messages.size() - 1) {
//                messageList.add(new SimpleMessageView1(messages.get(i), false));
//            } else {
//                messageList.add(new SimpleMessageView1(messages.get(i), false));
//            }
//        }

//        ((MessageHeaderView) messagePanel.getWidget(0))
//                .setMessageStyle(MessageDisplayType.FIRST);
//        ((MessageHeaderView) messagePanel.getWidget(messagePanel.getWidgetCount() - 1))
//                .setMessageStyle(MessageDisplayType.LAST);

//        messageCount = messageList.getWidgetCount();

//        if (messageCount == 1) {
//            ((SimpleMessageWindow) messagePanel.getWidget(0)).setMessageStyle(MessageDisplayType.BOTH);
//        }
    }

    public void addMessage(MessageDetail lastMessage) {
        messageProvider.getList().add(lastMessage);
//        GWT.log("adding newly created message");
//        if (messageCount > 0) {
//            SimpleMessageView1 last = (SimpleMessageView1) messageList.getWidget(messageCount - 1);
//            last.getMessageWidget().setOpen(false);
//        }
//        messageList.add(new SimpleMessageView1(lastMessage, true));
//
//        replyToMessage = lastMessage;
//
//        messageCount = messageList.getWidgetCount();
    }

    public void addOfferMessagePresenter(OfferWindowPresenter presenter) {
        offerPresenters.add(presenter);
//        messageList.add(presenter.getWidgetView());
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

    public void toggleVisible() {
        if (messageList.isVisible()) {
            messageList.getElement().getStyle().setDisplay(Display.NONE);
        } else {
            messageList.getElement().getStyle().setDisplay(Display.BLOCK);
        }
    }

    public void clear() {
        messageProvider.getList().clear();
    }
}
