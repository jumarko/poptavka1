package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.FakeMessage;

/**
 * Dummy UserMessage holder
 *
 * @author Beho
 *
 */
public class UserConversationView extends Composite {

    private static UserConversationViewUiBinder uiBinder = GWT.create(UserConversationViewUiBinder.class);
    interface UserConversationViewUiBinder extends UiBinder<Widget, UserConversationView> {
    }

    @UiField FlowPanel master;

    // TODO will contain parameter
    public UserConversationView(Widget replyWidget) {
        initWidget(uiBinder.createAndBindUi(this));
        master.add(new UserMessageView(new FakeMessage(), true));
        // TODO cycle for all messages from parameter list
        master.add(new UserMessageView(new FakeMessage()));
        master.add(new UserMessageView(new FakeMessage(), false));

        // set ReplyWindow
        master.add(replyWidget);

        // set ReplyMessageID
//        ((ReplyWindow) replyWidget).setMessageToReplyId(messageToReplyId);

    }

}
