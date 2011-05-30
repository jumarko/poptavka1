package cz.poptavka.sample.client.user.messages;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.FakeMessage;
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

    public UserConversationPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Display list of messages. When messages are set, control panell should be displayed as well.
     *
     * @param messages list of messages to be displayed
     */
    public void setMessageList(ArrayList<MessageDetail> messages) {
        if (messages.size() == 0) {
            return;
        }
        if (messages.size() == 1) {
            messagePanel.add(new UserMessageView(new FakeMessage(), false));
            return;
        }
        messagePanel.add(new UserMessageView(new FakeMessage(), true));
        ((UserMessageView) messagePanel.getWidget(messagePanel.getWidgetCount() - 1)).toggleMessageBody();
        for (int i = 1; i < (messages.size() - 1); i++) {
            messagePanel.add(new UserMessageView(new FakeMessage()));
            ((UserMessageView) messagePanel.getWidget(messagePanel.getWidgetCount() - 1)).toggleMessageBody();

        }
//        messagePanel.add(new UserMessageView(messages.get(messages.size() - 1)));
        messagePanel.add(new UserMessageView(new FakeMessage(), false));
    }

}
