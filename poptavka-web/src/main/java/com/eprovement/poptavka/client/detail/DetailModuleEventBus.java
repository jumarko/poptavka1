/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail;

import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModule;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.FullRatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

/**
 * DetailModuleEventBus serves all events for DetailModule.
 *
 * @author martin.slavkovsky
 */
@Events(startPresenter = DetailModulePresenter.class, module = DetailModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface DetailModuleEventBus extends EventBusWithLookup, InfoWidgetsGateway {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = DetailModulePresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is
     * nothing to carry out in this method we should remove forward event to
     * save the number of method invocations.
     */
    @Forward
    @Event(handlers = DetailModulePresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    @Event(forwardToModules = SupplierDemandsModule.class)
    void goToSupplierDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(handlers = DetailModulePresenter.class)
    void resize(int actualWidth);

    /**************************************************************************/
    /* Business events handled by DetailModulePresenter - init methods        */
    /**************************************************************************/
    @Event(handlers = DetailModulePresenter.class)
    void initDetailSection(UniversalAsyncGrid grid, SimplePanel detailSection);

    @Event(handlers = DetailModulePresenter.class)
    void buildDetailSectionTabs(DetailModuleBuilder builder);

    /**************************************************************************/
    /* Business events handled by DevelDetailWrapperPresenter.                */
    /**************************************************************************/
    @Event(handlers = DetailModulePresenter.class)
    void setCustomWidget(int tabIndex, Widget customWidget);

    @Event(handlers = DetailModulePresenter.class)
    void setCustomSelectionHandler(SelectionHandler<Integer> selectionHandler);

    @Event(handlers = DetailModulePresenter.class)
    void displayAdvertisement();

    @Event(handlers = DetailModulePresenter.class)
    void allowSendingOffer();
    /*
     * Request/Response Method pair
     * DemandDetail for detail section
     * @param demandId
     * @param type
     */
    @Event(handlers = DetailModuleHandler.class)
    void requestDemandDetail(Long demandId);

    @Event(handlers = DetailModulePresenter.class)
    void responseDemandDetail(FullDemandDetail demandDetail);

    @Event(handlers = DetailModuleHandler.class)
    void requestClientDetail(Long clientId);

    @Event(handlers = DetailModulePresenter.class)
    void responseClientDetail(FullClientDetail clientDetail);

    @Event(handlers = DetailModuleHandler.class)
    void requestSupplierDetail(Long supplierId);

    @Event(handlers = DetailModulePresenter.class)
    void responseSupplierDetail(FullSupplierDetail supplierDetail);

    @Event(handlers = DetailModuleHandler.class)
    void requestRatingDetail(Long demandId);

    @Event(handlers = DetailModulePresenter.class)
    void responseRatingDetail(FullRatingDetail ratingDetail);

    /*
     * Request conversation messages for detail section tab: Conversations between currently logged user and its
     * counterparty.
     *
     * @param threadRootId - root message i.e. first demand message in the conversation always created by client
     * @param loggedUserId - userId of currently logged user
     * @param counterPartyUserId - userId of counterparty
     */
    @Event(handlers = DetailModuleHandler.class)
    void requestConversation(long threadRootId, long loggedUserId, long counterPartyUserId);

    @Event(handlers = DetailModuleHandler.class)
    void updateUserMessagesReadStatus(Long userId, List<MessageDetail> messages);

    @Event(handlers = DetailModulePresenter.class)
    void responseConversation(List<MessageDetail> chatMessages);
    /**************************************************************************/
    /* Messages                                                               */
    /**************************************************************************/
    /**
     * Send/Response method pair
     * Sends message and receive the answer in a form of the same message to be displayed on UI.
     * @param messageToSend
     */
    @Event(handlers = DetailModuleHandler.class)
    void sendQuestionMessage(MessageDetail messageToSend);

    @Event(handlers = DetailModuleHandler.class)
    void sendOfferMessage(OfferMessageDetail offerMessageToSend);

    @Event(handlers = DetailModulePresenter.class)
    void responseSendOfferMessage();

    @Event(handlers = DetailModulePresenter.class)
    void addConversationMessage(MessageDetail sentMessage);

    /**
     * Send status message i.e. when user clicks on action buttons like Accept Offer, Finish Offer, Close Demand etc.
     * @param statusMessageBody representing status message text
     */
    @Event(handlers = DetailModulePresenter.class, passive = true)
    void sendStatusMessage(String statusMessageBody);
}
