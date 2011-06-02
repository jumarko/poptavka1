package cz.poptavka.sample.client.user.demands.widgets;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.messages.ReplyWindowPresenter;
import cz.poptavka.sample.client.user.messages.UserConversationPanel;
import cz.poptavka.sample.client.user.messages.UserMessage;
import cz.poptavka.sample.client.user.messages.UserMessagePresenter;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@Presenter(view = DetailWrapperView.class, multiple = true)
public class DetailWrapperPresenter extends
        LazyPresenter<DetailWrapperPresenter.IDetailWrapper, UserEventBus> {

    public interface IDetailWrapper extends LazyView {
        Widget getWidgetView();

        void setDetail(Widget demandDetailWidget);

        UserConversationPanel getConversationPanel();

        SimplePanel getReplyHolder();
    }

    private DetailType type;

    private ReplyWindowPresenter replyPresenter = null;

    /**
     * Initialize widget and sets his type.
     *
     * @param detailSection holder for widget
     * @param type type of view, where is this widget loaded
     */
    public void initDetailWrapper(SimplePanel detailSection, DetailType detailType) {
        GWT.log("DEMAND DETAIL Presenter LOADED");
        detailSection.setWidget(view.getWidgetView());
        this.type = detailType;
    }

    /**
     * Response when user click demand to see the details. DemandDetails widget,
     * past conversation regarding this demand and reply widget is created.
     *
     * @param detail DemandDetail to be displayed
     * @param typeOfDetail type of what detail section should handle this event
     */
    public void onSetDemandDetail(DemandDetail detail, DetailType typeOfDetail) {
        if (!typeOfDetail.equals(type)) {
            //event not for this instance of presenter
            return;
        }

        // initialization of parts of view

        //detail part
        // TODO selection if editable or not
        view.setDetail(new DemandDetailView(detail));

        // reply part
        // TODO it's not necessary to define new instance of reply window
        // it's just for devel. Just one creation and then check if null
        if (typeOfDetail.equals(DetailType.OFFER)) {
//            view.getConversationPanel().setClickHandlers(acceptHandler, replyHandler, deleteHandler);
            return;
        }


        // GUI visual event
        eventBus.loadingHide();
    }


    private ClickHandler bindReplyWindowAction() {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // sending message only when valid
                GWT.log("START OF CLICK EVENT");
                if (replyPresenter.isMessageValid()) {
                    GWT.log("* * valid");
                    //distinguish what kind of message should be sent
                    if (replyPresenter.hasResponseQuestion()) {
                        MessageDetail messageToSend = replyPresenter.getCreatedMessage();
                        messageToSend = view.getConversationPanel().updateSendingMessage(messageToSend);
                        eventBus.bubbleMessageSending(messageToSend);
                    } else {
                        // TODO offer branch
                        OfferDetail offerToSend = replyPresenter.getOfferMessage();
                        offerToSend.setMessageDetail(view.getConversationPanel()
                                .updateSendingMessage(offerToSend.getMessageDetail()));
                        eventBus.bubbleOfferSending(offerToSend);
                    }
                }
                GWT.log("END OF CLICK EVENT");
            }
        };
    }

    public void onSetPotentialDemandConversation(ArrayList<MessageDetail> messageList, DetailType wrapperhandlerType) {
        if (!wrapperhandlerType.equals(type)) {
            //event not for this instance of presenter
            GWT.log(view.getWidgetView().getClass().getName()
                    + " does NOT handle method onSetPotentialDemandConversation");
            return;
        }
        if (messageList == null) {
            GWT.log(" ** messageList is empty()");
            return;
        }
        view.getConversationPanel().setMessageList(messageList, true);
    }

    public void onAddReplyToPotentailDemandConversation(MessageDetail result, DetailType wrapperhandlerType) {
        if (!wrapperhandlerType.equals(type)) {
            //event not for this instance of presenter
            GWT.log(view.getWidgetView().getClass().getName()
                    + " does NOT handle method onSetPotentialDemandConversation");
            return;
        }
        replyPresenter.enableResponse();
        view.getConversationPanel();
    }

    /**
     * Visual sign, that demand detail is loading.
     * @param demandId
     * @param typeOfDetail
     */
    public void onGetDemandDetail(Long demandId, DetailType typeOfDetail) {
        if (!typeOfDetail.equals(type)) {
            return;
        }
        Widget anchor = view.getWidgetView().getParent();
        anchor.getElement().getStyle().setBorderColor("red");
        anchor.getElement().getStyle().setBorderStyle(BorderStyle.DASHED);
        anchor.getElement().getStyle().setBorderWidth(12, Unit.PX);
        eventBus.loadingShowWithAnchor(" ", anchor);
    }

    public void onSetOfferMessage(OfferDetail offerDetail) {
        MessageDetail offerMessage = offerDetail.getMessageDetail();
        UserMessage displayedOffer = new UserMessage(offerMessage, false);
//        displayedOffer.addAcceptHandler(createAcceptOfferHandler(offerDetail));
        displayedOffer.setEventBusCall(eventBus, offerDetail);

        UserMessagePresenter presenter = eventBus.addHandler(UserMessagePresenter.class);
        presenter.setOfferDetail(offerDetail);

        view.getConversationPanel().addOfferMessagePresenter(presenter);
    }

//    public UserEventBus createAcceptOfferHandler(final OfferDetail offerDetail) {
//        return
//    }

    public void setReplyWidget(Long demandId) {
        if (replyPresenter != null) {
            eventBus.removeHandler(replyPresenter);
        }
        replyPresenter = eventBus.addHandler(ReplyWindowPresenter.class);
        replyPresenter.initReplyWindow(view.getReplyHolder());
        replyPresenter.addSubmitHandler(bindReplyWindowAction(), demandId);
    }

}
