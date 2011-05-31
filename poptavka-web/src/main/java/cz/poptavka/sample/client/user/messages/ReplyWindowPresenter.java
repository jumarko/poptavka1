package cz.poptavka.sample.client.user.messages;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

@Presenter(view = ReplyWindow.class, multiple = true)
public class ReplyWindowPresenter extends LazyPresenter<ReplyWindowPresenter.ReplyInterface, UserEventBus> {

    public interface ReplyInterface extends LazyView {
        Widget getWidgetView();

        void addClickHandler(ClickHandler submitButtonHandler, long demandId);

        boolean isValid();

        MessageDetail getCreatedMessage();

        OfferDetail getCreatedOffer();

        void setSendingStyle();

        void setNormalStyle();

        boolean isResponseQuestion();

    }

    public void initReplyWindow(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    public void addSubmitHandler(ClickHandler submitButtonHandler, long demandId) {
        view.addClickHandler(submitButtonHandler, demandId);
    }

    public MessageDetail getCreatedMessage() {
        view.setSendingStyle();
        return view.getCreatedMessage();
    }

    public OfferDetail getOfferMessage() {
        view.setSendingStyle();
        return view.getCreatedOffer();
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
