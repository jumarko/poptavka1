package com.eprovement.poptavka.client.user.widget.messaging;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.CyclePresenter;
import com.mvp4g.client.view.CycleView;

import com.eprovement.poptavka.client.user.UserEventBus;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.type.OfferStateType;

@Presenter(view = OfferWindow.class, multiple = true)
public class OfferWindowPresenter extends CyclePresenter<OfferWindowPresenter.ActionMessageInterface, UserEventBus> {

    public interface ActionMessageInterface extends CycleView {

        Widget getWidgetView();

        Anchor getAcceptButton();

        Anchor getReplyButton();

        Anchor getDeclineButton();

        void setFullOfferDetail(FullOfferDetail fullDetail);

        FullOfferDetail getFullOfferDetail();

        SimplePanel getResponseHolder();
    }
    private OfferQuestionPresenter replyPresenter = null;

    public void bindView() {
        view.getAcceptButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                FullOfferDetail d = view.getFullOfferDetail();
                d.getOfferDetail().setState(OfferStateType.ACCEPTED.getValue());
                eventBus.getOfferStatusChange(d.getOfferDetail());
            }
        });
        view.getDeclineButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                FullOfferDetail d = view.getFullOfferDetail();
                d.getOfferDetail().setState(OfferStateType.DECLINED.getValue());
                eventBus.getOfferStatusChange(d.getOfferDetail());
            }
        });
        view.getReplyButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                FullOfferDetail d = view.getFullOfferDetail();
                d.getOfferDetail().setState(OfferStateType.DECLINED.getValue());
                eventBus.getOfferStatusChange(d.getOfferDetail());
            }
        });
    }

    public void setFullOfferDetail(FullOfferDetail detail) {
        view.setFullOfferDetail(detail);
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
//    private void createReplyWindow() {
//        replyPresenter = eventBus.addHandler(OfferQuestionPresenter.class);
////        replyPresenter.createOfferReplyWindow(new ClickHandler() {
////            @Override
////            public void onClick(ClickEvent event) {
////                eventBus.removeHandler(replyPresenter);
////            }
////        });
//        replyPresenter.addSubmitHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                // TODO Auto-generated method stub
//                Window.alert("CLick");
//            }
//        });
//    }
}
