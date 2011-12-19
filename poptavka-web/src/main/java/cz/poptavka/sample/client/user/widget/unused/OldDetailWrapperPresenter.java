package cz.poptavka.sample.client.user.widget.unused;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.widget.LoadingDiv;
import cz.poptavka.sample.client.user.widget.detail.DemandDetailView;
import cz.poptavka.sample.client.user.widget.messaging.OfferQuestionPresenter;
import cz.poptavka.sample.client.user.widget.messaging.OfferWindowPresenter;
import cz.poptavka.sample.client.user.widget.messaging.QuestionPresenter;
import cz.poptavka.sample.client.user.widget.messaging.UserConversationPanel;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

@Presenter(view = OldDetailWrapperView.class, multiple = true)
public class OldDetailWrapperPresenter extends
        LazyPresenter<OldDetailWrapperPresenter.IDetailWrapper, UserEventBus> {

    public interface IDetailWrapper extends LazyView {
        Widget getWidgetView();

        void setDetail(Widget demandDetailWidget);

        SimplePanel getDetailHolder();

        UserConversationPanel getConversationPanel();

        SimplePanel getReplyHolder();

        StackLayoutPanel getStackConversationPanel();

        DemandDetailView getDemandDetail();
    }

    private ViewType type;

    private OfferQuestionPresenter potentialViewReplyWiget = null;
    private QuestionPresenter myDemandsViewReplyWiget = null;

    private LoadingDiv detailLoader;
    private LoadingDiv conversationLoader;

    /**
     * Initialize widget and sets his type.
     *
     * @param detailSection
     *            holder for widget
     * @param type
     *            type of view, where is this widget loaded
     */
    public void initDetailWrapper(SimplePanel detailSection, ViewType viewType) {
        detailSection.setWidget(view.getWidgetView());
        this.type = viewType;
    }

    /**
     * Response when user click demand to see the details. DemandDetails widget,
     * past conversation regarding this demand and reply widget is created.
     *
     * @param detail
     *            FullDemandDetail to be displayed
     * @param typeOfDetail
     *            type of what detail section should handle this event
     */
    public void onSetFullDemandDetail(FullDemandDetail detail) {
        view.setDetail(new DemandDetailView(detail));
        // GUI visual event
        view.getDemandDetail().setDemanDetail(detail);
        toggleDetailLoading();
    }

    public void onSetBaseDemandDetail(BaseDemandDetail detail) {
        view.setDetail(new DemandDetailView(detail));
        // GUI visual event
        toggleDetailLoading();
    }
    /**
     * Loads conversation.
     *
     * @param messageList
     *            list of messages, that belongs to the selected demand
     * @param wrapperhandlerType
     */
    // TODO rename to SetDemandRelatedConversation
    public void onSetPotentialDemandConversation(
            ArrayList<MessageDetail> messageList, ViewType wrapperhandlerType) {
        // TODO test
        // TODO this should be enough, testing needed
        // if (!(!wrapperhandlerType.equals(type) || messageList == null)) {
        // return;
        // }
        // old and long, test commented one
        if (!wrapperhandlerType.equals(type)) {
            return;
        }
        if (messageList == null) {
            return;
        }
        if (type.equals(ViewType.POTENTIAL)) {
            // need demand id to know, to what demand are we replying to
            // maybe more attributes will be needed to be passed, so I;m sending
            // the whole object
            // can be decreased after analysis
            setPotentialViewReplyWidget(messageList.get(0));
        }
        view.getConversationPanel().setMessageList(messageList, true);
        toggleConversationLoading();
    }

    // TODO merge with method above, the same method body + different options
    // for different types
    public void onSetSingleDemandConversation(
            ArrayList<MessageDetail> messageList) {
        if (!type.equals(ViewType.EDITABLE)) {
            return;
        }
        if (messageList == null) {
            return;
        }
        if (type.equals(ViewType.EDITABLE)) {
            setMyDemandsViewReplyWidget();
        }
        view.getStackConversationPanel().setVisible(true);
        GWT.log("Visible stackLayouPanel:" + view.getStackConversationPanel().isVisible());
        view.getConversationPanel().setMessageList(messageList, true);
        toggleConversationLoading();
    }

    public void onAddMessageToPotentailDemandConversation(MessageDetail result,
            ViewType wrapperhandlerType) {
        if (!wrapperhandlerType.equals(type)) {
            return;
        }
        // display of newly added message and enabling response again
        view.getConversationPanel().addMessage(result);
        // SUP : potentialDemandsView
        if (potentialViewReplyWiget != null) {
            potentialViewReplyWiget.enableResponse();
        }
        // CLI : myDemandsView
        if (myDemandsViewReplyWiget != null) {
            myDemandsViewReplyWiget.enableResponse();
        }

    }

    /**
     * Visual sign, that demand detail is loading it's children. clear all it's
     * children;
     *
     * @param demandId
     *            not needed for this method
     * @param typeOfDetail
     */
    // TODO REWORK completely
    public void onGetDemandDetail(Long demandId, ViewType typeOfDetail) {
        if (!typeOfDetail.equals(type)) {
            return;
        }
        toggleDetailLoading();

        // clear offersPresenters for OFFER
        // messages are cleared in the widget itself
        // ArrayList<OfferWindowPresenter> listToClear =
        // view.getConversationPanel().clearContent();
        // for (OfferWindowPresenter p : listToClear) {
        // eventBus.removeHandler(p);
        // }

        // reply window stay always loaded, do not do anything
    }

    /**
     * Visual SIGN ONLY. For getting conversation
     *
     * @param threadRootId
     *            threadRootID
     * @param messageId
     *            messageID
     */
    public void onRequestSingleConversation(long threadRootId, long messageId) {
        if (ViewType.EDITABLE.equals(type)) {
            toggleConversationLoading();
        }
    }

    /**
     * CLIENT ONLY Display offer message from presenter. Client can react to it.
     *
     * @param fullOfferDetail
     *            offer detail
     */
    public void onSetOfferMessage(FullOfferDetail fullOfferDetail) {

        OfferWindowPresenter presenter = eventBus.addHandler(OfferWindowPresenter.class);
        presenter.setFullOfferDetail(fullOfferDetail);
        GWT.log("OFFER ID: " + fullOfferDetail.getOfferDetail().getId());
        view.getConversationPanel().addOfferMessagePresenter(presenter);
    }

    /**
     * SUPPLIER ONLY Creates reply window for creating Offer/Question message.
     *
     * @param messageDetail
     */
    // messageDetail is it necessary?
    public void setPotentialViewReplyWidget(MessageDetail messageDetail) {
        if (potentialViewReplyWiget != null) {
            eventBus.removeHandler(potentialViewReplyWiget);
        }
        potentialViewReplyWiget = eventBus
                .addHandler(OfferQuestionPresenter.class);
        potentialViewReplyWiget.initReplyWindow(view.getReplyHolder());
        potentialViewReplyWiget.addSubmitHandler(bindReplyWindowAction());
    }

    private ClickHandler bindReplyWindowAction() {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // sending message only when valid
                if (potentialViewReplyWiget.isMessageValid()) {
                    // distinguish what kind of message should be sent
                    if (potentialViewReplyWiget.hasResponseQuestion()) {
                        MessageDetail messageToSend = potentialViewReplyWiget
                                .getCreatedMessage();
                        messageToSend = view.getConversationPanel()
                                .updateSendingMessage(messageToSend);
                        eventBus.bubbleMessageSending(messageToSend, type);
                    } else {
                        // TODO finish sending of
                        OfferMessageDetail offer = new OfferMessageDetail();
                        offer = (OfferMessageDetail) view
                                .getConversationPanel().updateSendingOfferMessage(
                                        offer);
                        MessageDetail offerMessage = potentialViewReplyWiget
                                .getCreatedMessage();
                        offer.setBody(offerMessage.getBody());
                        //offer.setDemandId(offerMessage.getDemandId());
                        eventBus.bubbleOfferSending(offer);
                    }
                }
            }
        };
    }

    private void setMyDemandsViewReplyWidget() {
        if (myDemandsViewReplyWiget != null) {
            eventBus.removeHandler(myDemandsViewReplyWiget);
        }
        myDemandsViewReplyWiget = eventBus.addHandler(QuestionPresenter.class);
        myDemandsViewReplyWiget.initReplyWindow(view.getReplyHolder());
        myDemandsViewReplyWiget
                .addSubmitHandler(bindConversationReplyWindowAction());
    }

    private ClickHandler bindConversationReplyWindowAction() {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // sending message only when valid
                if (myDemandsViewReplyWiget.isMessageValid()) {
                    // distinguish what kind of message should be sent
                    MessageDetail messageToSend = myDemandsViewReplyWiget
                            .getCreatedMessage();
                    messageToSend = view.getConversationPanel()
                            .updateSendingMessage(messageToSend);
                    eventBus.bubbleMessageSending(messageToSend, type);
                }
            }
        };
    }

    /** loading events **/
    // just to enable loading
    public void onRequestPotentialDemandConversation(long messageId,
            long userMessageId) {
        toggleConversationLoading();
    }

    public void toggleDetailLoading() {
        if (detailLoader == null) {
            GWT.log("  - loading created");
            detailLoader = new LoadingDiv(view.getDetailHolder());
        } else {
            GWT.log("  - loading removed");
            detailLoader.getElement().removeFromParent();
            detailLoader = null;
        }
    }

    public void toggleConversationLoading() {
        if (conversationLoader == null) {
            GWT.log("  - loading created");
            view.getConversationPanel().getElement().setAttribute("style", "min-height: 50px");
            conversationLoader = new LoadingDiv(view.getConversationPanel());
        } else {
            GWT.log("  - loading removed");
            view.getConversationPanel().getElement().removeAttribute("style");
            conversationLoader.getElement().removeFromParent();
            conversationLoader = null;
        }
    }

}
