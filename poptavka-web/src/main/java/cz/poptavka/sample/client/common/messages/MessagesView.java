package cz.poptavka.sample.client.common.messages;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.domain.mail.Message;

public class MessagesView extends Composite
    implements MessagesPresenter.MessagesViewInterface {

    private static MessagesUiBinder uiBinder = GWT.create(MessagesUiBinder.class);

    private static final Logger LOGGER = Logger.getLogger(MessagesView.class.getName());

    interface MessagesUiBinder extends UiBinder<Widget, MessagesView> {
    }

    @UiField
    VerticalPanel messagesPanel;

    public MessagesView() {
        initWidget(uiBinder.createAndBindUi(this));
        LOGGER.info("MessagesView created");
    }

    @Override
    public void displayMessages(List<Message> messages) {
        int i = 0;
        MessageView message;
        for (Message m : messages) {
            message = new MessageView(m);
            if (i == messages.size() - 1) {
                message.getPanelBody().setOpen(true);
            }
            LOGGER.info("Adding to Panel");
            messagesPanel.add(new Label("test"));
            i++;
        }
    }
}
