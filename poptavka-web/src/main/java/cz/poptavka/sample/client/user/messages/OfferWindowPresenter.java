package cz.poptavka.sample.client.user.messages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.CyclePresenter;
import com.mvp4g.client.view.CycleView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.type.OfferStateType;

@Presenter(view = OfferWindow.class, multiple = true)
public class OfferWindowPresenter extends CyclePresenter<OfferWindowPresenter.ActionMessageInterface, UserEventBus> {

    public interface ActionMessageInterface extends CycleView {
        Widget getWidgetView();

        Anchor getAcceptButton();

        Anchor getReplyButton();

        Anchor getDeclineButton();

        void setOfferDetail(OfferDetail detail);

        OfferDetail getOfferDetail();

        SimplePanel getResponseHolder();
    }

    private ReplyWindowPresenter replyPresenter = null;

    public void bindView() {
        view.getAcceptButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                OfferDetail d = view.getOfferDetail();
                d.setState(OfferStateType.ACCEPTED.getValue());
                eventBus.getOfferStatusChange(d);
            }
        });
        view.getDeclineButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                OfferDetail d = view.getOfferDetail();
                d.setState(OfferStateType.DECLINED.getValue());
                eventBus.getOfferStatusChange(d);
            }
        });
        view.getReplyButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                OfferDetail d = view.getOfferDetail();
                d.setState(OfferStateType.DECLINED.getValue());
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

    @Override
    public void onUnload() {
        if (replyPresenter != null) {
            eventBus.removeHandler(replyPresenter);
        }
    }

    private void createReplyWindow() {
        replyPresenter = eventBus.addHandler(ReplyWindowPresenter.class);
        replyPresenter.createOfferReplyWindow(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.removeHandler(replyPresenter);
            }
        });
        replyPresenter.addSubmitHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
                Window.alert("CLick");
            }
        }, 0);
    }

}
