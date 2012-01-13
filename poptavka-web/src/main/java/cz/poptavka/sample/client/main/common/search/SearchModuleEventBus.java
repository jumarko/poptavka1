package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.client.main.common.search.views.AdminAccessRolesViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminClientsViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminDemandsViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminEmailActivationViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminInvoicesViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminMessagesViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminOffersViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminPaymentMethodsViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminPermissionsViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminPreferencesViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminProblemsViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.AdminSuppliersViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.HomeDemandViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.HomeSuppliersViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.MessagesTabViewPresenter;
import cz.poptavka.sample.client.main.common.search.views.PotentialDemandMessagesViewPresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.PaymentMethodDetail;
import java.util.ArrayList;
import java.util.List;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = SearchModuleView.class, module = SearchModule.class)
public interface SearchModuleEventBus extends EventBus {

    // MODULES INIT METHODS
    /**************************************************************************/
    @Event(forwardToParent = true)
    void initHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void initHomeSupplierModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void initAdminModule(SearchModuleDataHolder filter);

//    @Event(forwardToParent = true)
//    void initDemandsTabModule(SearchModuleDataHolder filter);
//
//    @Event(forwardToParent = true)
//    void initMessagesTabModule(SearchModuleDataHolder filter);
//
//    @Event(forwardToParent = true)
//    void initAdminsTabModule(SearchModuleDataHolder filter);
    // VIEWS INIT METHODS
    /**************************************************************************/
    /* Navigation | Initialization events. */
    //production init method
    @Event(handlers = HomeDemandViewPresenter.class)
    void initHomeDemandView(PopupPanel popupPanel);

    @Event(handlers = HomeSuppliersViewPresenter.class)
    void initHomeSuppliersView(PopupPanel popupPanel);

    @Event(handlers = AdminAccessRolesViewPresenter.class)
    void initAdminAccessRolesView(PopupPanel popupPanel);

    @Event(handlers = AdminClientsViewPresenter.class)
    void initAdminClientsRolesView(PopupPanel popupPanel);

    @Event(handlers = AdminDemandsViewPresenter.class)
    void initAdminDemandsView(PopupPanel popupPanel);

    @Event(handlers = AdminEmailActivationViewPresenter.class)
    void initAdminEmailActivationView(PopupPanel popupPanel);

    @Event(handlers = AdminInvoicesViewPresenter.class)
    void initAdminInvoicesView(PopupPanel popupPanel);

    @Event(handlers = AdminMessagesViewPresenter.class)
    void initAdminMessagesView(PopupPanel popupPanel);

    @Event(handlers = AdminOffersViewPresenter.class)
    void initAdminOffersView(PopupPanel popupPanel);

    @Event(handlers = AdminPaymentMethodsViewPresenter.class)
    void initAdminPaymentMethodsView(PopupPanel popupPanel);

    @Event(handlers = AdminPermissionsViewPresenter.class)
    void initAdminPermissionsView(PopupPanel popupPanel);

    @Event(handlers = AdminPreferencesViewPresenter.class)
    void initAdminPreferencesView(PopupPanel popupPanel);

    @Event(handlers = AdminProblemsViewPresenter.class)
    void initAdminProblemsView(PopupPanel popupPanel);

    @Event(handlers = AdminSuppliersViewPresenter.class)
    void initAdminSuppliersView(PopupPanel popupPanel);

    @Event(handlers = PotentialDemandMessagesViewPresenter.class)
    void initPotentialDemandMessagesView(PopupPanel popupPanel);

    @Event(handlers = MessagesTabViewPresenter.class)
    void initMessagesTabView(PopupPanel popupPanel);

    /**************************************************************************/
    /* Business events. */
    /* Business events handled by DemandModulePresenter. */
    //init demands module - left user_type menu and initial content
    @Event(handlers = SearchModulePresenter.class)
    void initSearchModule(SimplePanel panel);
    //display widget in content area
//    @Event(handlers = SearchModulePresenter.class)
//    void displayView(Widget content);

    /**************************************************************************/
    /* Business events. */
    /* Business events handled by ALL VIEW presenters. */
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
    @Event(handlers = SearchModuleHandler.class)
    void requestCategories();

    @Event(handlers = SearchModuleHandler.class)
    void requestLocalities();

    @Event(handlers = {HomeDemandViewPresenter.class, HomeSuppliersViewPresenter.class,
            AdminSuppliersViewPresenter.class })
    void responseCategories(final ArrayList<CategoryDetail> list);

    @Event(handlers = {HomeDemandViewPresenter.class, HomeSuppliersViewPresenter.class,
            AdminSuppliersViewPresenter.class })
    void responseLocalities(final ArrayList<LocalityDetail> list);

    @Event(handlers = SearchModuleHandler.class)
    void requestPaymentMethods();

    @Event(handlers = AdminInvoicesViewPresenter.class)
    void responsePaymentMethods(final List<PaymentMethodDetail> list);
}
