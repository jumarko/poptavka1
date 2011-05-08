package cz.poptavka.sample.client.common.messages.message;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;



@Presenter(view = MessageView.class)
public class MessagePresenter extends BasePresenter<MessagePresenter.MessageViewInterface, UserEventBus> {

    private static final Logger LOGGER = Logger.getLogger(MessagePresenter.class.getName());

    public interface MessageViewInterface {

        HasClickHandlers getSendMessageBtn();

        HasClickHandlers getDiscardMessageBtn();

        DisclosurePanel getPanelBody();

        HorizontalPanel getPanelFooter();

        Widget getWidgetView();
    }

    /**
     * Bind objects and theirs action handlers.
     */
    public void bind() {
        LOGGER.info("BIND");
        view.getSendMessageBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.send();
            }
        });
        view.getDiscardMessageBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                LOGGER.info("CLICK");
                eventBus.discard();
            }
        });
    }

    /**
     * Shows existing messages.
     */
    public void initialize() {

    }

    public void onSend() {

    }


}
