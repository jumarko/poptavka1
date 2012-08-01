package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = SearchModulePresenter.class, module = SearchModule.class)
public interface SearchModuleEventBus extends EventBus {

    // MODULES INIT METHODS
    /**************************************************************************/
    // TODO praso - preco tu mame tieto metody. Vsetko su to navigacne eventy mali
    // by predsa byt v HomeMenu. Naco ich mat dva krat v dvoch presenteroch?
    // IV: tak pohopil som, ze su nutne kvoli vysledkom, ktore sa moju zobrazit po
    // zadani vyhladavacich kriterii do search panelu. Po kliknuti search uzivatelia
    // presmerujeme na jeden z pohladov suppliers/demands/messages
    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToDemandModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToAdminModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToMessagesModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holder, int checkboxes);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel embedWidget);

    @Event(handlers = SearchModulePresenter.class)
    void clearSearchContent();
    // VIEWS INIT METHODS

    /**************************************************************************/
    /* Business events. */
    /* Business events handled by DemandModulePresenter. */
    //init demands module - left user_type menu and initial content
    @Event(handlers = SearchModulePresenter.class)
    void goToSearchModule();
    //display widget in content area
//    @Event(handlers = SearchModulePresenter.class)
//    void displayView(Widget content);

    /**************************************************************************/
    /* Business events. */
    /* Business events handled by ALL VIEW presenters. */
    @Event(handlers = SearchModulePresenter.class)
    void setSearchBarEnables(boolean category, boolean locality, boolean advanceBtn);
    /**
     * Send/Response method pair
     * Sends message and receive the answer in a form of the same message to be displayed on UI.
     * @param messageToSend
     * @param type type of handling view
     */
//    @Event(handlers = DemandModuleMessageHandler.class)
//    void sendMessage(MessageDetail messageToSend, ViewType type);
//    //IMPORTANT: all view-resenters have to handle this method, if view handles conversation displaying
//    @Event(handlers = {SupplierListPresenter.class }, passive = true)
//    void sendMessageResponse(MessageDetail sentMessage, ViewType type);
//
    /**************************************************************************/
    /* Business events handled by SupplierListPresenter. */

    /*
     * Request/Response Method pair
     * Demands for CLIENT - his demands
     */
//    @Event(handlers = DemandModuleContentHandler.class)
//    void requestClientsDemands();
//    @Event(handlers = ClientListPresenter.class)
//    void responseClientsDemands(ArrayList<ClientDemandMessageDetail> result);
    /**************************************************************************/
    /* Business events handled by SupplierListPresenter. */

    /*
     * Request/Response Method pair
     * NEW demands for SUPPLIER
    //     */
//    @Event(handlers = DemandModuleContentHandler.class)
//    void requestSupplierNewDemands();
//    @Event(handlers = SupplierListPresenter.class)
//    void responseSupplierNewDemands(ArrayList<PotentialDemandMessage> result);
//
//
//    @Event(handlers = DemandModuleContentHandler.class)
//    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);
//
//    @Event(handlers = DemandModuleContentHandler.class)
//    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);
    /**************************************************************************/
    /* Business events handled by DevelDetailWrapperPresenter. */

    /*
     * Request/Response Method pair
     * DemandDetail for detail section
     * @param demandId
     * @param type
     */
//    @Event(handlers = DemandModuleContentHandler.class)
//    void requestDemandDetail(Long demandId, ViewType type);
//    @Event(handlers = DevelDetailWrapperPresenter.class, passive = true)
//    void responseDemandDetail(FullDemandDetail demandDetail, ViewType type);

    /*
     * Request/Response method pair
     * Fetch and display chat(conversation) for supplier new demands list
     * @param messageId
     * @param userMessageId
     * @param userId
     */
//    @Event(handlers = DemandModuleMessageHandler.class)
//    void requestChatForSupplierList(long messageId, Long userMessageId, Long userId);
//    @Event(handlers = DevelDetailWrapperPresenter.class)
//    void responseChatForSupplierList(ArrayList<MessageDetail> chatMessages, ViewType supplierListType);
//    @Event(handlers = SearchModuleHandler.class)
//    void requestCategories();
//
//    @Event(handlers = SearchModuleHandler.class)
//    void requestLocalities();
//
//    @Event(handlers = SearchModulePresenter.class)
//    void responseCategories(final ArrayList<CategoryDetail> list);
//
//    @Event(handlers = SearchModulePresenter.class)
//    void responseLocalities(final ArrayList<LocalityDetail> list);
    // TODO Praso - zakomentoval som tuto metodu pretoze mam pocit, ze sa nepouziva a v podstate
    // ani nema co robit v module SearchModule
//    @Event(handlers = SearchModuleHandler.class)
//    void requestPaymentMethods();
//
//    @Event(handlers = SearchModulePresenter.class)
//    void responsePaymentMethods(final List<PaymentMethodDetail> list);
}
