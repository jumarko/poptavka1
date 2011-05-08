package cz.poptavka.sample.client.common.messages;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.problems.Problem;


@Presenter(view = MessagesView.class)
public class MessagesPresenter extends BasePresenter<MessagesPresenter.MessagesViewInterface, UserEventBus> {

    private static final Logger LOGGER = Logger.getLogger(MessagesPresenter.class.getName());

    public interface MessagesViewInterface {

        HasClickHandlers getReplyBtn();

        void removeMessage();

        void reply();

        void displayMessages(Problem problem);

        Widget getWidgetView();
    }

    /**
     * Bind objects and theirs action handlers.
     */
    public void bind() {
        LOGGER.info("BIND Messages Presenter");
        view.getReplyBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.reply();
            }
        });
    }

    public void onDisplayMessages(Problem problem) {
        LOGGER.info("onDisplayMessages in MessagesPresenter");
        view.displayMessages(problem);
    }

    public void onReply() {
        LOGGER.info("onReply in MessagesPresenter");
        view.reply();
    }

    public void onDiscard() {
        LOGGER.info("onDiscard in MessagesPresenter");
        view.removeMessage();
    }
}
