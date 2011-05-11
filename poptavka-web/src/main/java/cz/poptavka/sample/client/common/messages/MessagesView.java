package cz.poptavka.sample.client.common.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.mvp4g.client.view.ReverseViewInterface;
import cz.poptavka.sample.client.common.messages.message.MessageView;
import cz.poptavka.sample.client.user.problems.Problem;
import cz.poptavka.sample.domain.message.Message;

import java.util.logging.Logger;

public class MessagesView extends Composite
    implements  ReverseViewInterface<MessagesPresenter>, MessagesPresenter.MessagesViewInterface {
//implements MessagesPresenter.MessageViewInterface {

    public interface IMessagesPresenter {
        void bind();

        void onDisplayMessages(Problem problem);

        void onReply();

        void onDiscard();
    }

    @Inject
    private MessagesPresenter presenter;

    @Override
    public void setPresenter(MessagesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public MessagesPresenter getPresenter() {
        return presenter;
    }



    private static MessagesViewUiBinder uiBinder = GWT
            .create(MessagesViewUiBinder.class);

    private static final Logger LOGGER = Logger.getLogger(MessagesView.class.getName());

    interface MessagesViewUiBinder extends UiBinder<Widget, MessagesView> {
    }

//    @Inject
    public MessagesView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Label title;
    @UiField
    Button buttonReply;

    @UiField
    VerticalPanel messagesPanel;

//    @Inject
    public MessagesView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }


    @Override
    public void displayMessages(Problem problem) {
        LOGGER.info("VIEW display messages");
        messagesPanel.clear();
        this.title.setText(problem.getRoot().getBody());

        int i = 0;
        MessageView message;
        for (Message m : problem.getAnswers()) {
            message = new MessageView(m);
            if (i == problem.getAnswers().size() - 1) {
                message.getPanelBody().setOpen(true);
            }
            messagesPanel.add(message);
            i++;
        }
    }

    @Override
    public void reply() {
        MessageView message = new MessageView();
        messagesPanel.add(message);
    }

    @Override
    public HasClickHandlers getReplyBtn() {
        return buttonReply;
    }

    @Override
    public void removeMessage() {
        LOGGER.info("SASDASDAS" + Integer.toString(messagesPanel.getWidgetCount()));
        messagesPanel.remove(messagesPanel.getWidgetCount() - 1);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
