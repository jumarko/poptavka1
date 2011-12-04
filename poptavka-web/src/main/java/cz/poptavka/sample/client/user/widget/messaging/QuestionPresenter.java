package cz.poptavka.sample.client.user.widget.messaging;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.message.MessageDetail;

/**
 * Widget for potentail demands view. Enables supplier to send an offer or just ask question.
 * @author Beho
 *
 */
@Presenter(view = QuestionWindow.class, multiple = true)
public class QuestionPresenter extends LazyPresenter<QuestionPresenter.ReplyInterface, UserEventBus> {

    public interface ReplyInterface extends LazyView {
        Widget getWidgetView();

        void addClickHandler(ClickHandler submitButtonHandler);

        boolean isValid();

        MessageDetail getCreatedMessage();

        void setNormalStyle();

        void setSendingStyle();

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

    public boolean isMessageValid() {
        return view.isValid();
    }

}
