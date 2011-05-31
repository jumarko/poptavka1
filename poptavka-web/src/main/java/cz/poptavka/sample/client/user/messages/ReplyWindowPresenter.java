package cz.poptavka.sample.client.user.messages;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.MessageDetail;

@Presenter(view = ReplyWindow.class, multiple = true)
public class ReplyWindowPresenter extends LazyPresenter<ReplyWindowPresenter.ReplyInterface, UserEventBus> {

    public interface ReplyInterface extends LazyView {
        Widget getWidgetView();

        void addClickHandler(ClickHandler submitButtonHandler);

        MessageDetail getCreatedMessage();

        void setSendingStyle();

        void setNormalStyle();

//        MessageType getMessageType();
    }

    public void initReplyWindow(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    public void addSubmitHandler(ClickHandler submitButtonHandler) {
        view.addClickHandler(submitButtonHandler);
    }

    public MessageDetail getCreatedMessage() {
        view.setSendingStyle();
        return view.getCreatedMessage();
    }

    public void enableResponse() {
        view.setNormalStyle();
    }

}
