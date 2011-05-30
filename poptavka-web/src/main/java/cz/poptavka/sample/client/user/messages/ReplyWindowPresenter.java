package cz.poptavka.sample.client.user.messages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;

@Presenter(view = ReplyWindow.class, multiple = true)
public class ReplyWindowPresenter extends LazyPresenter<ReplyWindowPresenter.ReplyInterface, UserEventBus> {

    public interface ReplyInterface extends LazyView {
        Widget getWidgetView();

        Long getMessageToReplyId();

        void addClickHandlers(ClickHandler submitButtonHandler);
    }

    public void bindView() {
        view.addClickHandlers(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO call real event
//                eventBus.send();
                Window.alert("Message Sent! \n\nLocation: ReplyWindowPresenter.bindView()");
            }
        });
    }

}
