/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.detail.views.OfferQuestionWindow;
import com.eprovement.poptavka.client.detail.views.DemandDetailView;
import com.eprovement.poptavka.client.detail.views.UserDetailView;
import com.eprovement.poptavka.client.detail.views.RatingDetailView;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.FullRatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.LinkedList;
import java.util.List;

/**
 * Detail module presenter.
 * @author Martin Slavkovsky
 */
@Presenter(view = DetailModuleView.class)
public class DetailModulePresenter
    extends LazyPresenter<DetailModulePresenter.IDetailWrapper, DetailModuleEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Class Attributes. **/
    private DetailModuleBuilder builder;
    private UniversalAsyncGrid table;

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface IDetailWrapper extends LazyView {

        Widget getWidgetView();

        TabLayoutPanel getContainer();

        SimplePanel getDemandDetailHolder();

        SimplePanel getAdvertisementHolder();

        DemandDetailView getDemandDetail();

        UserDetailView getSupplierDetail();

        RatingDetailView getRatingDetail();

        OfferQuestionWindow getReplyHolder();

        CellList getMessageList();

        ListDataProvider getMessageProvider();

        FluidContainer getConversationHolder();

        void loadingDivShow(Widget holderWidget);

        void loadingDivHide(Widget holderWidget);

        void setMessagePanelVisibility();
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    public void onForward() {
        // nothing by default
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    /**
     * Binds handlers.
     */
    @Override
    public void bindView() {
        view.getReplyHolder().getSubmitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //is message to send valid
                if (view.getReplyHolder().isValid()) {
                    //Deny next click until operation is finnished
                    view.getReplyHolder().getSubmitBtn().setEnabled(false);
                    // distinguish what kind of message should be sent
                    //Kedze reply widget je robeny tak, ze najprv sa urci ci chcem poslat spravu alebo ponuku
                    //zobrazi sa podla toho widget - budna poslanie spravy alebo na poslanie ponuky.
                    //Potom samotne poslanie sa vykona kliknutim na tlacidlo Submit, teda musime zistit co
                    //za akciu ma ten submit vykonat, teda ci poslat spravu alebo ponuku, podla skor zvolenej akcie.
                    switch (view.getReplyHolder().getSelectedResponse()) {
                        case OfferQuestionWindow.RESPONSE_QUESTION:
                            MessageDetail questionMessageToSend =
                                    view.getReplyHolder().updateSendingMessage(
                                    view.getReplyHolder().getCreatedMessage());
                            questionMessageToSend.setSenderId(Storage.getUser().getUserId());
                            eventBus.sendQuestionMessage(questionMessageToSend);
                            break;
                        case OfferQuestionWindow.RESPONSE_OFFER:
                            OfferMessageDetail offerMessageToSend =
                                    view.getReplyHolder().updateSendingOfferMessage(
                                    view.getReplyHolder().getCreatedOfferMessage());
                            offerMessageToSend.setSenderId(Storage.getUser().getUserId());
                            eventBus.sendOfferMessage(offerMessageToSend);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        view.getContainer().addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                requestActualTabData();
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Initialize widget and sets his type.
     *
     * @param detailSection
     *            holder for widget
     * @param type
     *            type of view, where is this widget loaded
     */
    public void onInitDetailSection(UniversalAsyncGrid grid, SimplePanel detailSection) {
        detailSection.setWidget(view.getWidgetView());
        this.table = grid;
        onDisplayAdvertisement();
    }

    /**
     * Builds detail section tabs.
     * According to given builder creates tabs by setting its visibility.
     * Appropriate details are not retrieved immediatelly. The builder is stored
     * to be abble to access detail IDs when needed. Details are retrieved when user selects different tabs.
     *
     * @param builder representing how detail section tabs are created
     */
    public void onBuildDetailSectionTabs(DetailModuleBuilder builder) {
        reset();
        this.builder = builder;
        setTabVisibility(DetailModuleBuilder.DEMAND_DETAIL_TAB, builder.isDemandTab());
        setTabVisibility(DetailModuleBuilder.USER_DETAIL_TAB, builder.isUserTab());
        setTabVisibility(DetailModuleBuilder.CONVERSATION_TAB, builder.isConversationTab());
        setTabVisibility(DetailModuleBuilder.RATING_TAB, builder.isRatingTab());
        setTabVisibility(DetailModuleBuilder.ADVERTISEMENT_TAB, builder.isAdvertisementTab());
        view.getContainer().selectTab(builder.getSelectedTabIdx(), false);
        requestActualTabData();
    }

    /**
     * Request for detail for particular tab.
     * If tab is selected, request for tab detail object.
     */
    private void requestActualTabData() {
        switch (view.getContainer().getSelectedIndex()) {
            case DetailModuleBuilder.DEMAND_DETAIL_TAB:
                requestDemandDetail(this.builder.getDemandId());
                break;
            case DetailModuleBuilder.USER_DETAIL_TAB:
                requestSupplierDetail(this.builder.getUserId());
                break;
            case DetailModuleBuilder.RATING_TAB:
                requestRatingDetail(this.builder.getDemandId());
                break;
            case DetailModuleBuilder.CONVERSATION_TAB:
                requestConversation(
                    this.builder.getThreadRootId(),
                    this.builder.getSenderId(),
                    Storage.getUser().getUserId());
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Displayes question message and enables reply window again.
     *
     * @param sentMessage
     */
    public void onAddConversationMessage(MessageDetail sentMessage) {
        addMessage(sentMessage);
        view.getReplyHolder().setDefaultStyle();
        //Always will be only one item.
        MultiSelectionModel<TableDisplayUserMessage> selectionModel = (MultiSelectionModel) table.getSelectionModel();
        for (TableDisplayUserMessage detail : selectionModel.getSelectedSet()) {
            detail.setUserMessageId(sentMessage.getUserMessageId());
            detail.setUnreadMessagesCount(detail.getUnreadMessagesCount() + 1);
            detail.setRead(sentMessage.isRead());
            detail.setStarred(sentMessage.isStarred());
        }
        table.redraw();
    }

    /**
     * Displays thank you poup for placing offer and forwards to your <b>offers</b> widget.
     */
    public void onResponseSendOfferMessage() {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_OFFERS);
            }
        };
        eventBus.showThankYouPopup(Storage.MSGS.thankYouSendOffer(), additionalAction);
    }

    /**
     * Set custom widget on given tab position.
     * @param tabIndex
     * @param customWidget
     */
    public void onSetCustomWidget(int tabIndex, Widget customWidget) {
        switch (tabIndex) {
            case DetailModuleBuilder.DEMAND_DETAIL_TAB:
                if (customWidget != null) {
                    view.getDemandDetailHolder().setWidget(customWidget);
                } else {
                    view.getDemandDetailHolder().setWidget(view.getDemandDetail());
                    requestActualTabData();
                }
                break;
            case DetailModuleBuilder.USER_DETAIL_TAB:
                break;
            case DetailModuleBuilder.CONVERSATION_TAB:
                break;
            case DetailModuleBuilder.ADVERTISEMENT_TAB:
                break;
            default:
                break;
        }
    }

    /**
     * Resets tabs to its default widgets.
     * Removes all custom widgets set and restores default widgets.
     */
    private void ensureDefaultTabWidgets() {
        //Default Demand
        if (!(view.getDemandDetailHolder().getWidget() instanceof DemandDetailView)) {
            view.getDemandDetailHolder().setWidget(view.getDemandDetail());
        }
        //Default User
        //DefaultConversation
        //Default Advertisement
    }

    /**
     * Sets additional custom selection handler.
     * @param selectionHandler
     */
    public void onSetCustomSelectionHandler(SelectionHandler<Integer> selectionHandler) {
        view.getContainer().addSelectionHandler(selectionHandler);
    }

    /**
     * Allows sending offer by making sending offer button visible.
     */
    public void onAllowSendingOffer() {
        view.getReplyHolder().setSendingOfferEnabled(true);
    }

    /**
     * Sets all tabs visible and clear tabs' widgets' attributes.
     */
    public void reset() {
        ensureDefaultTabWidgets();
        view.getReplyHolder().setSendingOfferEnabled(false);
        clear();
    }

    /**************************************************************************/
    /* Request methods                                                        */
    /**************************************************************************/
    /**
     * Requests demand detail for detail section tab: Demand.
     *
     * @param demandId
     */
    public void requestDemandDetail(Long demandId) {
        view.getDemandDetail().setVisible(false);
        view.loadingDivShow(view.getDemandDetailHolder().getParent());
        eventBus.requestDemandDetail(demandId);
    }

    /**
     * Requests client detail for detail section tab: User - Client.
     *
     * @param clientId
     */
    public void requestClientDetail(Long clientId) {
        view.getSupplierDetail().setVisible(false);
        view.loadingDivShow(view.getSupplierDetail().getParent());
        eventBus.requestClientDetail(clientId);
    }

    /**
     * Requests supplier detail for detail section tab: User - Supplier.
     *
     * @param supplierId
     */
    public void requestSupplierDetail(Long supplierId) {
        view.getSupplierDetail().setVisible(false);
        view.loadingDivShow(view.getSupplierDetail().getParent());
        eventBus.requestSupplierDetail(supplierId);
    }

    /**
     * Requests rating detail for detail section tab: RATING.
     *
     * @param demandId
     */
    public void requestRatingDetail(Long demandId) {
        view.getRatingDetail().setVisible(false);
        view.loadingDivShow(view.getRatingDetail().getParent());
        eventBus.requestRatingDetail(demandId);
    }

    /**
     * Requests conversation messages for detail section tab: Conversations.
     * Loads conversation between client and supplier. Each conversation begins
     * with threadRoot message that must be passed here as a parameter. Other paramters contain userId of currently
     * logged user and userId of counterParty.
     *
     * @param threadRootId - root message i.e. first demand message in the conversation always created by client
     * @param loggedUserId - userId of currently logged user
     * @param counterPartyUserId - userId of counterparty
     *
     */
    public void requestConversation(long threadRootId, long loggedUserId, long counterPartyUserId) {
        view.getConversationHolder().setVisible(false);
        view.loadingDivShow(view.getConversationHolder().getParent().getParent());
        eventBus.requestConversation(threadRootId, loggedUserId, counterPartyUserId);
    }

    /**************************************************************************/
    /* Response methods                                                       */
    /**************************************************************************/
    /**
     * Response method for fetching demandDetail.
     *
     * @param demandDetail detail to be displayed
     */
    public void onResponseDemandDetail(FullDemandDetail demandDetail) {
        view.getDemandDetail().setDemanDetail(demandDetail);
        view.getDemandDetail().setVisible(true);
        view.loadingDivHide(view.getDemandDetailHolder().getParent());
    }

    /**
     * Response method for fetching clientDetail.
     *
     * @param clientDetail to be displayed
     */
    public void onResponseClientDetail(FullClientDetail clientDetail) {
        view.getSupplierDetail().setClientDetail(clientDetail);
        view.getSupplierDetail().setVisible(true);
        view.loadingDivHide(view.getSupplierDetail().getParent());
    }

    /**
     * Response method for fetching userDetail.
     *
     * @param userDetail detail to be displayed
     */
    public void onResponseSupplierDetail(FullSupplierDetail supplierDetail) {
        view.getSupplierDetail().setSupplierDetail(supplierDetail);
        view.getSupplierDetail().setVisible(true);
        view.loadingDivHide(view.getSupplierDetail().getParent());
    }

    /**
     * Response method for fetching ratingDetail.
     *
     * @param userDetail detail to be displayed
     */
    public void onResponseRatingDetail(FullRatingDetail ratingDetail) {
        view.getRatingDetail().setRatingDetail(ratingDetail);
        view.getRatingDetail().setVisible(true);
        view.loadingDivHide(view.getRatingDetail().getParent());
    }

    /**
     * Response method for fetching demand-related chat.
     *
     * @param chatMessages - demand-related conversation
     */
    public void onResponseConversation(List<MessageDetail> chatMessages) {
        if (!chatMessages.isEmpty()) {
            setMessageList(new LinkedList<MessageDetail>(chatMessages), true);
            eventBus.updateUserMessagesReadStatus(Storage.getUser().getUserId(), chatMessages);
            view.getConversationHolder().setVisible(true);
        }
        view.loadingDivHide(view.getConversationHolder().getParent().getParent());
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets tab visibility.
     * @param tab index
     * @param visible - true if visibile, false otherwise
     */
    private void setTabVisibility(int tab, boolean visible) {
        view.getContainer().getTabWidget(tab).getParent().setVisible(visible);
    }

    /**
     * Displays advertisement.
     * Selects tab with advertisement.
     */
    public void onDisplayAdvertisement() {
        setTabVisibility(DetailModuleBuilder.DEMAND_DETAIL_TAB, false);
        setTabVisibility(DetailModuleBuilder.USER_DETAIL_TAB, false);
        setTabVisibility(DetailModuleBuilder.RATING_TAB, false);
        setTabVisibility(DetailModuleBuilder.CONVERSATION_TAB, false);
        setTabVisibility(DetailModuleBuilder.ADVERTISEMENT_TAB, true);
        view.getContainer().selectTab(DetailModuleBuilder.ADVERTISEMENT_TAB, false);
    }

    /**
     * Clears detail section means reseting widget's attributes of all tabs widgets.
     */
    private void clear() {
        view.getDemandDetail().clear();
        view.getSupplierDetail().clear();
        view.getMessageProvider().getList().clear();
        view.getReplyHolder().clear();
    }

    /**************************************************************************/
    /* Conversation Methods                                                   */
    /**************************************************************************/
    /**
     * Display list of messages. Whole conversation panel consists of OfferQuestionWidget
     * and cellList. Where first message is displayed in OfferQuestionWidget and the rest in cellList.
     *
     * @param messages list of messages to be displayed
     */
    public void setMessageList(LinkedList<MessageDetail> messages, boolean collapsed) {
        view.getReplyHolder().setMessage(messages.removeFirst());
        view.getMessageProvider().setList(messages);
        view.setMessagePanelVisibility();
    }

    /**
     * Adds message to conversation panel. Whole conversation panel consists of OfferQuestionWidget
     * and cellList. Therefore adding new message involve shifting message from reply widget to list
     * at the beginning and after that set new given message to reply holder.
     *
     * @param lastMessage
     */
    public void addMessage(MessageDetail lastMessage) {
        LinkedList linkedList = new LinkedList(view.getMessageProvider().getList());
        linkedList.addFirst(view.getReplyHolder().getMessage());
        view.getMessageProvider().setList(linkedList);
        view.getReplyHolder().setMessage(lastMessage);
        view.setMessagePanelVisibility();
    }

    /**
     * This message is sent by system without user's interaction. Status message is sent in cases when user clicks
     * on status buttons like Accept Offer, Finish Offer, Close Demand and so on.
     *
     * @param statusMessageBody to create system message
     */
    public void onSendStatusMessage(String statusMessageBody) {
        MessageDetail statusMessage = new MessageDetail();
        statusMessage.setBody(statusMessageBody);
        MessageDetail questionMessageToSend = view.getReplyHolder().updateSendingMessage(statusMessage);
        questionMessageToSend.setSenderId(Storage.getUser().getUserId());
        eventBus.sendQuestionMessage(questionMessageToSend);
    }
}
