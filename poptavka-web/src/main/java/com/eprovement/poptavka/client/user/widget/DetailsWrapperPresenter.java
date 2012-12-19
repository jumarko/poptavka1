package com.eprovement.poptavka.client.user.widget;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.user.widget.messaging.DevelOfferQuestionWindow;
import com.eprovement.poptavka.client.user.widget.messaging.UserConversationPanel;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;

@Presenter(view = DetailsWrapperView.class, multiple = true)
public class DetailsWrapperPresenter
        extends LazyPresenter<DetailsWrapperPresenter.IDetailWrapper, RootEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Constants. **/
    public static final int DEMAND = 0;
    public static final int SUPPLIER = 1;
    public static final int CHAT = 2;
    /** Class Attributes. **/
    private ViewType type;

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface IDetailWrapper extends LazyView {

        Widget getWidgetView();

        void setDemandDetail(FullDemandDetail demandDetail);

        void setSupplierDetail(FullSupplierDetail supplierDetail);

        TabLayoutPanel getContainer();

        UserConversationPanel getConversationPanel();

        DevelOfferQuestionWindow getReplyHolder();

        void toggleDemandLoading();

        void toggleSupplierLoading();

        void toggleConversationLoading();

        void setChat(List<MessageDetail> chatMessages, boolean collapsed);
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getReplyHolder().getSubmitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //is message to send valid
                if (view.getReplyHolder().isValid()) {
                    // distinguish what kind of message should be sent
                    //Kedze reply widget je robeny tak, ze najprv sa urci ci chcem poslat spravu alebo ponuku
                    //zobrazi sa podla toho widget - budna poslanie spravy alebo na poslanie ponuky.
                    //Potom samotne poslanie sa vykona kliknutim na tlacidlo Submit, teda musime zistit co
                    //za akciu ma ten submit vykonat, teda ci poslat spravu alebo ponuku, podla skor zvolenej akcie.
                    switch (view.getReplyHolder().getSelectedResponse()) {
                        case DevelOfferQuestionWindow.RESPONSE_QUESTION:
                            MessageDetail questionMessageToSend =
                                    view.getConversationPanel().updateSendingMessage(
                                    view.getReplyHolder().getCreatedMessage());
                            questionMessageToSend.setSenderId(Storage.getUser().getUserId());
                            eventBus.sendQuestionMessage(questionMessageToSend, type);
                            break;
                        case DevelOfferQuestionWindow.RESPONSE_OFFER:
                            OfferMessageDetail offerMessageToSend =
                                    view.getConversationPanel().updateSendingOfferMessage(
                                    view.getReplyHolder().getCreatedOfferMessage());
                            offerMessageToSend.setSenderId(Storage.getUser().getUserId());
                            eventBus.sendOfferMessage(offerMessageToSend, type);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
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

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    /**
     * Sent message is displayed and reply window is enabled again.
     *
     * @param sentMessage
     */
    public void onAddConversationMessage(MessageDetail sentMessage, ViewType handlingType) {
        if (type.equals(handlingType)) {
            view.getConversationPanel().addMessage(sentMessage);
            //TODO
            //if switched to one common interface, this should be replaced.
            view.getReplyHolder().setNormalStyle();
        }
    }

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
    /**************************************************************************/
    /* This methods                                                           */
    /**************************************************************************/
    public void setTabVisibility(int column, boolean visible) {
        view.getContainer().getTabWidget(column).getParent().setVisible(visible);
    }

    /**************************************************************************/
    /* Request methods                                                        */
    /**************************************************************************/
    //Radej takto, ako mat v kazdom eventbuse forward metody.
    /*
     * Request/Response Method pair
     * DemandDetail for detail section
     * @param demandId
     * @param type
     */
    public void requestDemandDetail(Long demandId, ViewType type) {
        showLoading(DetailsWrapperPresenter.DEMAND);
        eventBus.requestDemandDetail(demandId, type);
    }

    public void requestSupplierDetail(Long supplierId, ViewType type) {
        showLoading(DetailsWrapperPresenter.SUPPLIER);
        eventBus.requestSupplierDetail(supplierId, type);
    }

    /**
     * Loads conversation between client and supplier. Each conversation begins with threadRoot message that must be
     * passed here as a parameter.
     *
     * @param threadRootId - root message i.e. first demand message in the conversation always created by client
     * @param userMessageId - userMessageId
     * @param userId - user who's chatting messages we are going to retrieve
     */
    public void requestConversation(long threadRootId, Long userMessageId, Long userId) {
        showLoading(DetailsWrapperPresenter.CHAT);
        eventBus.requestConversation(threadRootId, userMessageId, userId);
    }

    /**************************************************************************/
    /* Response methods                                                       */
    /**************************************************************************/
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
    public void onResponseConversation(List<MessageDetail> chatMessages, ViewType wrapperType) {
        view.setChat(chatMessages, true);
    }

    /**************************************************************************/
    /* HELPER methods                                                         */
    /**************************************************************************/
    /**
     * *GUI* Toggle loading icon when getting clicking on some demand Depending on its argument,
     * chosen element is toggled.
     *
     * @param value value of component do toggle loading image.
     */
    private void showLoading(int value) {
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
}
