package com.eprovement.poptavka.client.user.widget;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsEventBus;
import com.eprovement.poptavka.client.user.widget.messaging.DevelOfferQuestionPresenter;
import com.eprovement.poptavka.client.user.widget.messaging.UserConversationPanel;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;

@Presenter(view = DevelDetailWrapperView.class, multiple = true)
public class DevelDetailWrapperPresenter
        extends LazyPresenter<DevelDetailWrapperPresenter.IDetailWrapper, ClientDemandsEventBus> {

    public static final int DEMAND = 0;
    public static final int SUPPLIER = 1;
    public static final int CHAT = 2;

    public interface IDetailWrapper extends LazyView {

        Widget getWidgetView();

        void setDemandDetail(FullDemandDetail demandDetail);

        void setSupplierDetail(FullSupplierDetail supplierDetail);

        UserConversationPanel getConversationPanel();

        SimplePanel getReplyHolder();

        void toggleDemandLoading();

        void toggleSupplierLoading();

        void toggleConversationLoading();

        void setChat(ArrayList<MessageDetail> chatMessages, boolean collapsed);
    }
    private ViewType type;
    /**
     * TODO.
     * IMPORTANT ..... NEED TO BE DISCUSSED
     *
     * my thouht:
     * Instead of havimg multible replyWidgets for various views we should have one common ReplyWidget interface
     * to provide common methods for:
     * - enabling replyWidget after message sending
     * - ... more to come?
     */
    private DevelOfferQuestionPresenter offerQuestionReply = null;
//
//    private OfferQuestionPresenter potentialViewReplyWiget = null;
//    private QuestionPresenter myDemandsViewReplyWiget = null;
//
//    private LoadingDiv detailLoader;
//    private LoadingDiv conversationLoader;

    /**
     * DEVEL METHOD
     *
     * Used for JRebel correct refresh. It is called from DemandModulePresenter, when removing instance of
     * SupplierListPresenter. it has to remove it's detailWrapper first.
     */
    public void develRemoveReplyWidget() {
        if (offerQuestionReply != null) {
            eventBus.removeHandler(offerQuestionReply);
        }
    }

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
     * Initializes reply widget, when first demand is clicked.
     */
    public void initReplyWidget() {
        if (offerQuestionReply == null) {
            offerQuestionReply = eventBus.addHandler(DevelOfferQuestionPresenter.class);
        }
        offerQuestionReply.initReplyWindow(view.getReplyHolder());
    }

    /**
     * Response method for fetching demandDetail.
     *
     * @param demandDetail detail to be displayed
     */
    public void onResponseDemandDetail(FullDemandDetail demandDetail, ViewType wrapperType) {
        //neccessary check for method to be executed only in appropriate presenter
        if (type.equals(wrapperType)) {
            view.setDemandDetail(demandDetail);
        }
    }

    /**
     * Response method for fetching supplierDetail.
     *
     * @param demandDetail detail to be displayed
     */
    public void onResponseSupplierDetail(FullSupplierDetail supplierDetail, ViewType wrapperType) {
        //neccessary check for method to be executed only in appropriate presenter
        if (type.equals(wrapperType)) {
            view.setSupplierDetail(supplierDetail);
        }
    }

    /**
     * Response method for fetching demand-related chat.
     * Internally creates clickHandler for ReplyWidgets submit button.
     *
     * @param chatMessages - demand-related conversation
     * @param supplierListType
     */
    public void onResponseConversation(ArrayList<MessageDetail> chatMessages, ViewType wrapperType) {
        //neccessary check for method to be executed only in appropriate presenter
        if (type.equals(wrapperType)) {
            //display chat
            //TODO
            //boolean param - for collapsed conversation can be fetched from UserDetail object (some kind of setting)
            //example
            //Storage.getUser().getMessageCollapsedSetting();
            view.setChat(chatMessages, true);
            //init replyWidget
            offerQuestionReply.addSubmitHandler(bindReplyWindowAction());
        }
    }

    /**
     * Creates click action for reply widget of Qffer/Query type.
     *
     * According to chosen type of message to be sent, message is filled with mandatory attributes and sent. If query
     * is eent.
     *
     * @return
     */
    private ClickHandler bindReplyWindowAction() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                GWT.log(" - ReplyWidget: CLICK ACTION called");
                // sending message only when valid
                if (offerQuestionReply.isMessageValid()) {
                    // distinguish what kind of message should be sent
                    if (offerQuestionReply.hasResponseQuestion()) {
                        MessageDetail messageToSend = offerQuestionReply.getCreatedMessage();
                        messageToSend = view.getConversationPanel().updateSendingMessage(messageToSend);
                        messageToSend.setSenderId(Storage.getUser().getUserId());
                        eventBus.sendMessage(messageToSend, type);
//                        Window.alert(messageToSend.getMessageId() + "");
                    } else {
                        // TODO
                        //finish sending of offer
//                      OfferMessageDetail offer = new OfferMessageDetail();
//                      offer = (OfferMessageDetail) view
//                              .getConversationPanel().updateSendingOfferMessage(
//                                      offer);
//                      MessageDetail offerMessage = offerQuestionReply
//                              .getCreatedMessage();
//                      offer.setBody(offerMessage.getBody());
//                      offer.setDemandId(offerMessage.getDemandId());
//                      eventBus.bubbleOfferSending(offer);
                    }
                }
            }
        };
    }

    /**
     * Sent message is displayed and reply window is enabled again.
     *
     * @param sentMessage
     */
    public void addConversationMessage(MessageDetail sentMessage) {
        view.getConversationPanel().addMessage(sentMessage);
        //TODO
        //if switched to one common interface, this should be replaced.
        offerQuestionReply.enableResponse();
    }

    /**
     * *GUI* Toggle loading icon when getting clicking on some demand Depending on its argument,
     * chosen element is toggled.
     *
     * @param value value of component do toggle loading image.
     */
    public void showLoading(int value) {
        switch (value) {
            case DEMAND:
                view.toggleDemandLoading();
                break;
            case SUPPLIER:
                view.toggleSupplierLoading();
                break;
            case CHAT:
                view.toggleConversationLoading();
                break;
            default:
                break;
        }
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
//    public void onSetFullDemandDetail(FullDemandDetail detail) {
//        view.setDetail(new DemandDetailView(detail));
//        // GUI visual event
//        toggleDetailLoading();
//    }
//    public void onSetBaseDemandDetail(BaseDemandDetail detail) {
//        view.setDetail(new DemandDetailView(detail));
//        // GUI visual event
//        toggleDetailLoading();
//    }
    /**
     * Loads conversation.
     *
     * @param messageList
     *            list of messages, that belongs to the selected demand
     * @param wrapperhandlerType
     */
    // TODO rename to SetDemandRelatedConversation
//    public void onSetPotentialDemandConversation(
//            ArrayList<MessageDetail> messageList, ViewType wrapperhandlerType) {
//        // TODO test
//        // TODO this should be enough, testing needed
//        // if (!(!wrapperhandlerType.equals(type) || messageList == null)) {
//        // return;
//        // }
//        // old and long, test commented one
//        if (!wrapperhandlerType.equals(type)) {
//            return;
//        }
//        if (messageList == null) {
//            return;
//        }
//        if (type.equals(ViewType.POTENTIAL)) {
//            // need demand id to know, to what demand are we replying to
//            // maybe more attributes will be needed to be passed, so I;m sending
//            // the whole object
//            // can be decreased after analysis
//            setPotentialViewReplyWidget(messageList.get(0));
//        }
//        view.getConversationPanel().setMessageList(messageList, true);
//        toggleConversationLoading();
//    }
    // TODO merge with method above, the same method body + different options
    // for different types
//    public void onSetSingleDemandConversation(
//            ArrayList<MessageDetail> messageList) {
//        if (!type.equals(ViewType.EDITABLE)) {
//            return;
//        }
//        if (messageList == null) {
//            return;
//        }
//        if (type.equals(ViewType.EDITABLE)) {
//            setMyDemandsViewReplyWidget();
//        }
//        view.getStackConversationPanel().setVisible(true);
//        GWT.log("Visible stackLayouPanel:" + view.getStackConversationPanel().isVisible());
//        view.getConversationPanel().setMessageList(messageList, true);
//        toggleConversationLoading();
//    }
//    public void onAddMessageToPotentailDemandConversation(MessageDetail result,
//            ViewType wrapperhandlerType) {
//        if (!wrapperhandlerType.equals(type)) {
//            return;
//        }
//        // display of newly added message and enabling response again
//        view.getConversationPanel().addMessage(result);
//        // SUP : potentialDemandsView
//        if (potentialViewReplyWiget != null) {
//            potentialViewReplyWiget.enableResponse();
//        }
//        // CLI : myDemandsView
//        if (myDemandsViewReplyWiget != null) {
//            myDemandsViewReplyWiget.enableResponse();
//        }
//
//    }
    /**
     * Visual sign, that demand detail is loading it's children. clear all it's
     * children;
     *
     * @param demandId
     *            not needed for this method
     * @param typeOfDetail
     */
    // TODO REWORK completely
//    public void onGetDemandDetail(Long demandId, ViewType typeOfDetail) {
//        if (!typeOfDetail.equals(type)) {
//            return;
//        }
////        toggleDetailLoading();
//
//        // clear offersPresenters for OFFER
//        // messages are cleared in the widget itself
//        // ArrayList<OfferWindowPresenter> listToClear =
//        // view.getConversationPanel().clearContent();
//        // for (OfferWindowPresenter p : listToClear) {
//        // eventBus.removeHandler(p);
//        // }
//
//        // reply window stay always loaded, do not do anything
//    }
    /**
     * Visual SIGN ONLY. For getting conversation
     *
     * @param threadRootId
     *            threadRootID
     * @param messageId
     *            messageID
     */
//    public void onRequestSingleConversation(long threadRootId, long messageId) {
//        if (ViewType.EDITABLE.equals(type)) {
//            toggleConversationLoading();
//        }
//    }
    /**
     * CLIENT ONLY Display offer message from presenter. Client can react to it.
     *
     * @param offerDetail
     *            offer detail
     */
//    public void onSetOfferMessage(OfferDetail offerDetail) {
//
//        OfferWindowPresenter presenter = eventBus
//                .addHandler(OfferWindowPresenter.class);
//        presenter.setOfferDetail(offerDetail);
//        GWT.log("OFFER ID: " + offerDetail.getOfferId());
//        view.getConversationPanel().addOfferMessagePresenter(presenter);
//    }
    /**
     * SUPPLIER ONLY Creates reply window for creating Offer/Question message.
     *
     * @param messageDetail
     */
    // messageDetail is it necessary?
//    public void setPotentialViewReplyWidget(MessageDetail messageDetail) {
//        if (potentialViewReplyWiget != null) {
//            eventBus.removeHandler(potentialViewReplyWiget);
//        }
//        potentialViewReplyWiget = eventBus
//                .addHandler(OfferQuestionPresenter.class);
//        potentialViewReplyWiget.initReplyWindow(view.getReplyHolder());
//        potentialViewReplyWiget.addSubmitHandler(bindReplyWindowAction());
//    }
//    private ClickHandler bindReplyWindowAction() {
//        return new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                // sending message only when valid
//                if (potentialViewReplyWiget.isMessageValid()) {
//                    // distinguish what kind of message should be sent
//                    if (potentialViewReplyWiget.hasResponseQuestion()) {
//                        MessageDetail messageToSend = potentialViewReplyWiget
//                                .getCreatedMessage();
//                        messageToSend = view.getConversationPanel()
//                                .updateSendingMessage(messageToSend);
//                        eventBus.bubbleMessageSending(messageToSend, type);
//                    } else {
//                        // TODO finish sending of
//                        OfferMessageDetail offer = new OfferMessageDetail();
//                        offer = (OfferMessageDetail) view
//                                .getConversationPanel().updateSendingOfferMessage(
//                                        offer);
//                        MessageDetail offerMessage = potentialViewReplyWiget
//                                .getCreatedMessage();
//                        offer.setBody(offerMessage.getBody());
//                        offer.setDemandId(offerMessage.getDemandId());
//                        eventBus.bubbleOfferSending(offer);
//                    }
//                }
//            }
//        };
//    }
//    private void setMyDemandsViewReplyWidget() {
//        if (myDemandsViewReplyWiget != null) {
//            eventBus.removeHandler(myDemandsViewReplyWiget);
//        }
//        myDemandsViewReplyWiget = eventBus.addHandler(QuestionPresenter.class);
//        myDemandsViewReplyWiget.initReplyWindow(view.getReplyHolder());
//        myDemandsViewReplyWiget
//                .addSubmitHandler(bindConversationReplyWindowAction());
//    }
//    private ClickHandler bindConversationReplyWindowAction() {
//        return new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                // sending message only when valid
//                if (myDemandsViewReplyWiget.isMessageValid()) {
//                    // distinguish what kind of message should be sent
//                    MessageDetail messageToSend = myDemandsViewReplyWiget
//                            .getCreatedMessage();
//                    messageToSend = view.getConversationPanel()
//                            .updateSendingMessage(messageToSend);
//                    eventBus.bubbleMessageSending(messageToSend, type);
//                }
//            }
//        };
//    }
    /** loading events **/
    // just to enable loading
//    public void onRequestPotentialDemandConversation(long messageId,
//            long userMessageId) {
//        toggleConversationLoading();
//    }
//
//    public void toggleDetailLoading() {
//        if (detailLoader == null) {
//            GWT.log("  - loading created");
//            detailLoader = new LoadingDiv(view.getDetailHolder());
//        } else {
//            GWT.log("  - loading removed");
//            detailLoader.getElement().removeFromParent();
//            detailLoader = null;
//        }
//    }
//    public void toggleConversationLoading() {
//        if (conversationLoader == null) {
//            GWT.log("  - loading created");
//            view.getConversationPanel().getElement().setAttribute("style", "min-height: 50px");
//            conversationLoader = new LoadingDiv(view.getConversationPanel());
//        } else {
//            GWT.log("  - loading removed");
//            view.getConversationPanel().getElement().removeAttribute("style");
//            conversationLoader.getElement().removeFromParent();
//            conversationLoader = null;
//        }
//    }
}
