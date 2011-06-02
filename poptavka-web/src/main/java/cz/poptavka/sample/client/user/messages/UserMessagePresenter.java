package cz.poptavka.sample.client.user.messages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.OfferStateDetail;

@Presenter(view = UserActionMessage.class, multiple = true)
public class UserMessagePresenter extends LazyPresenter<UserMessagePresenter.ActionMessageInterface, UserEventBus> {

    public interface ActionMessageInterface extends LazyView {
        Widget getWidgetView();

        Anchor getAcceptButton();

        Anchor getReplyButton();

        Anchor getDeleteButton();

        void setOfferDetail(OfferDetail detail);

        OfferDetail getOfferDetail();
    }

    public void bindView() {
        view.getAcceptButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                OfferDetail d = view.getOfferDetail();
                d.setState(OfferStateDetail.ACCEPTED.getValue());
                eventBus.getOfferStatusChange(d);
            }
        });
    }

    public void setOfferDetail(OfferDetail detail) {
        view.setOfferDetail(detail);
    }

    public Widget getWidgetView() {
        return view.getWidgetView();
    }
}
