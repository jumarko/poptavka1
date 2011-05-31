package cz.poptavka.sample.client.user.messages;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.user.messages.UserMessageView.MessageType;
import cz.poptavka.sample.shared.domain.MessageDetail;

/**
 * UserMessage holder, enabling features:
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

    @UiField FlowPanel messagePanel;

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
        GWT.log("Widget count: " + messagePanel.getWidgetCount());
        // add new messageList
        messagePanel.clear();
        messageCount = 0;

        // Last message is visible, when there are more messages
        // last message is always stored for reply
        replyToMessage = messages.get(messages.size() - 1);
        if (messages.size() > 1) {
            messagePanel.add(new UserMessageView(messages.get(1), collapsed, MessageType.FIRST));
            messageCount++;
            for (int i = 2; i < (messages.size() - 1); i++) {
                messagePanel.add(new UserMessageView(messages.get(i), collapsed));
                messageCount++;
            }
            messagePanel.add(new UserMessageView(replyToMessage, false, MessageType.LAST));
            messageCount++;
        }
        if (messageCount == 1) {
            ((UserMessageView) messagePanel.getWidget(0)).setMessageStyle(MessageType.BOTH);
        }
        GWT.log("- - - MSG count: " + messageCount);
    }

    public void addMessage(MessageDetail lastMessage) {
        UserMessageView last = (UserMessageView) messagePanel.getWidget(messageCount - 1);
        last.setMessageStyle(MessageType.NONE);
        messagePanel.add(new UserMessageView(lastMessage, false, MessageType.LAST));
        messageCount++;
    }

}
