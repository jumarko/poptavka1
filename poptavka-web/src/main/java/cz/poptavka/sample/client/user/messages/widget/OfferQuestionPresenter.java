package cz.poptavka.sample.client.user.messages.widget;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;

/**
 * Widget for potentail demands view. Enables supplier to send an offer or just ask question.
 * @author Beho
 *
 */
@Presenter(view = OfferQuestionWindow.class, multiple = true)
public class OfferQuestionPresenter extends LazyPresenter<OfferQuestionPresenter.ReplyInterface, UserEventBus> {

    public interface ReplyInterface extends LazyView {
        Widget getWidgetView();

//        void addClickHandler(ClickHandler submitButtonHandler, long demandId);

        void addClickHandler(ClickHandler submitButtonHandler);

        boolean isValid();

        MessageDetail getCreatedMessage();

        OfferMessageDetail getCreatedOfferMessage();

        void setSendingStyle();

        void setNormalStyle();

        // TODO maybe not necessary, check
        boolean isResponseQuestion();

        // TODO maybe not necessary, check
        void setResponseToQuestion();

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

    public boolean hasResponseQuestion() {
        return view.isResponseQuestion();
    }

}
