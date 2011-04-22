package cz.poptavka.sample.client.common.messages;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;


@Presenter(view = MessageView.class)
public class MessagePresenter extends BasePresenter<MessagePresenter.MessageViewInterface, MessageEventBus> {

    private static final Logger LOGGER = Logger.getLogger(MessagePresenter.class.getName());

    public interface MessageViewInterface {

        HasClickHandlers getReplyMessageBtn();

        HasClickHandlers getReplyToAllMessageBtn();

        HasClickHandlers getForwardMessageBtn();

        HasClickHandlers getSendMessageBtn();

        HasClickHandlers getDeleteMessageBtn();

        DisclosurePanel getPanelBody();

        Widget getWidgetView();
    }

    /**
     * Bind objects and theirs action handlers.
     */
    public void bind() {
        view.getReplyMessageBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.reply();
            }
        });
        view.getReplyToAllMessageBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.replyToAll();
            }
        });
        view.getForwardMessageBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.forward();
            }
        });
        view.getSendMessageBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.send();
            }
        });
        view.getDeleteMessageBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.discard();
            }
        });
    }

    /**
     * Shows existing messages.
     */
    public void initialize() {

    }

    public void onReply() {

    }

    public void onReplyToAll() {

    }

    public void onForward() {

    }

    public void onSend() {

    }

    public void onDelete() {

    }
}
