package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.user.admin.detail.AdminDetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminAccessRolesPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminClientsPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminDemandsPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminEmailActivationsPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminInvoicesPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminMessagesPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminNewDemandsPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminOffersPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminOurPaymentDetailsPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminPaymentMethodsPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminPermissionsPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminPreferencesPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminProblemsPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AdminSuppliersPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PreferenceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ProblemDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = AdminPresenter.class, module = AdminModule.class)
public interface AdminEventBus extends EventBusWithLookup, IEventBusData, BaseChildEventBus {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = AdminPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = AdminPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     * @param loadWidget - prosim doplnit ???
     */
    @Event(handlers = AdminPresenter.class, historyConverter = AdminHistoryConverter.class, navigationEvent = true)
    String goToAdminModule(SearchModuleDataHolder searchDataHolder, int loadWidget);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void atAccount();

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel embedToWidget, int checkboxes, int displayCountsOfWhat,
            List<CategoryDetail> categoriesToSet);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel embedToWidget, int checkboxes, int displayCountsOfWhat,
            List<LocalityDetail> categoriesToSet);

    @Event(forwardToParent = true)
    void setUpdatedUnreadMessagesCount(int numberOfMessages);

    /**************************************************************************/
    /* Business Initialization events                                         */
    /**************************************************************************/
    /** Module Initializatin section. **/
    //display widget in content area
    @Event(handlers = AdminPresenter.class)
    void displayView(Widget content);

    /** Submodule Initializatin section. **/
    @Event(handlers = AdminDemandsPresenter.class)
    void initDemands(SearchModuleDataHolder filter);

    @Event(handlers = AdminClientsPresenter.class)
    void initClients(SearchModuleDataHolder filter);

    @Event(handlers = AdminSuppliersPresenter.class)
    void initSuppliers(SearchModuleDataHolder filter);

    @Event(handlers = AdminOffersPresenter.class)
    void initOffers(SearchModuleDataHolder filter);

    @Event(handlers = AdminAccessRolesPresenter.class)
    void initAccessRoles(SearchModuleDataHolder filter);

    @Event(handlers = AdminEmailActivationsPresenter.class)
    void initEmailsActivation(SearchModuleDataHolder filter);

    @Event(handlers = AdminInvoicesPresenter.class)
    void initInvoices(SearchModuleDataHolder filter);

    @Event(handlers = AdminMessagesPresenter.class)
    void initMessages(SearchModuleDataHolder filter);

    @Event(handlers = AdminPaymentMethodsPresenter.class)
    void initPaymentMethods(SearchModuleDataHolder filter);

    @Event(handlers = AdminPermissionsPresenter.class)
    void initPermissions(SearchModuleDataHolder filter);

    @Event(handlers = AdminPreferencesPresenter.class)
    void initPreferences(SearchModuleDataHolder filter);

    @Event(handlers = AdminProblemsPresenter.class)
    void initProblems(SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Business events handled by handlers                                    */
    /**************************************************************************/
    /**********************************************************************************************
     *********************** SUBWIDGETS SECTION *******************************************************
     **********************************************************************************************/
    /*********************** UPDATE **************************************************************/
    @Event(handlers = AdminHandler.class)
    void updateDemands(HashMap<Long, ArrayList<ChangeDetail>> changes);

    @Event(handlers = AdminHandler.class)
    void updateSupplier(FullSupplierDetail supplier);

    @Event(handlers = AdminHandler.class)
    void updateOffer(OfferDetail demand);

    @Event(handlers = AdminHandler.class)
    void updateClient(ClientDetail supplier);

    @Event(handlers = AdminHandler.class)
    void updateAccessRole(AccessRoleDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updateEmailActivation(ActivationEmailDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updateInvoice(InvoiceDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updateMessage(MessageDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updateOurPaymentDetail(PaymentDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updatePaymentMethod(PaymentMethodDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updatePermission(PermissionDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updatePreference(PreferenceDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updateProblem(ProblemDetail accessRole);

    /*********************** DISPLAY DATA ********************************************************/
    @Event(handlers = AdminDemandsPresenter.class)
    void displayAdminTabDemands(List<FullDemandDetail> demands);

    @Event(handlers = AdminSuppliersPresenter.class)
    void displayAdminTabSuppliers(List<FullSupplierDetail> suppliers);

    @Event(handlers = AdminOffersPresenter.class)
    void displayAdminTabOffers(List<OfferDetail> demands);

    @Event(handlers = AdminClientsPresenter.class)
    void displayAdminTabClients(List<ClientDetail> clients);

    @Event(handlers = AdminAccessRolesPresenter.class)
    void displayAdminTabAccessRoles(List<AccessRoleDetail> clients);

    @Event(handlers = AdminEmailActivationsPresenter.class)
    void displayAdminTabEmailsActivation(List<ActivationEmailDetail> clients);

    @Event(handlers = AdminInvoicesPresenter.class)
    void displayAdminTabInvoices(List<InvoiceDetail> clients);

    @Event(handlers = AdminMessagesPresenter.class)
    void displayAdminTabMessages(List<MessageDetail> messages);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void displayAdminNewDemands(List<FullDemandDetail> demands);

    @Event(handlers = AdminPaymentMethodsPresenter.class)
    void displayAdminTabPaymentMethods(List<PaymentMethodDetail> clients);

    @Event(handlers = AdminOurPaymentDetailsPresenter.class)
    void displayAdminTabOurPaymentDetails(List<PaymentDetail> clients);

    @Event(handlers = AdminPermissionsPresenter.class)
    void displayAdminTabPermissions(List<PermissionDetail> clients);

    @Event(handlers = AdminPreferencesPresenter.class)
    void displayAdminTabPreferences(List<PreferenceDetail> clients);

    @Event(handlers = AdminProblemsPresenter.class)
    void displayAdminTabProblems(List<ProblemDetail> clients);

    /*********************** SHOW DETAILS ********************************************************/
    @Event(handlers = AdminAccessRolesPresenter.class)
    void showDialogBox();

    /*********************** COMMIT **************************************************************/
    @Event(handlers = AdminSuppliersPresenter.class)
    void addSupplierToCommit(FullSupplierDetail data);

    @Event(handlers = AdminOffersPresenter.class)
    void addOfferToCommit(OfferDetail data);

    @Event(handlers = AdminClientsPresenter.class)
    void addClientToCommit(ClientDetail clientDetail);

    @Event(handlers = AdminAccessRolesPresenter.class)
    void addAccessRoleToCommit(AccessRoleDetail clientDetail);

    @Event(handlers = AdminEmailActivationsPresenter.class)
    void addEmailActivationToCommit(ActivationEmailDetail clientDetail);

    @Event(handlers = AdminInvoicesPresenter.class)
    void addInvoiceToCommit(InvoiceDetail clientDetail);

    @Event(handlers = AdminMessagesPresenter.class)
    void addMessageToCommit(MessageDetail messageDetail);

    @Event(handlers = AdminOurPaymentDetailsPresenter.class)
    void addOurPaymentDetailToCommit(PaymentDetail paymentDetail);

    @Event(handlers = AdminPaymentMethodsPresenter.class)
    void addPaymentMethodToCommit(PaymentMethodDetail paymentMethodDetail);

    @Event(handlers = AdminPermissionsPresenter.class)
    void addPermissionToCommit(PermissionDetail permissionDetail);

    @Event(handlers = AdminPreferencesPresenter.class)
    void addPreferenceToCommit(PreferenceDetail preferenceDetail);

    @Event(handlers = AdminProblemsPresenter.class)
    void addProblemToCommit(ProblemDetail problemDetail);

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface.                          */
    /* Should be called only from UniversalAsyncGrid.                         */
    /**************************************************************************/
    @Override
    @Event(handlers = AdminHandler.class)
    void getDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition);

    @Override
    @Event(handlers = AdminHandler.class)
    void getData(SearchDefinition searchDefinition);

    @Event(handlers = AdminHandler.class)
    void updateUnreadMessagesCount();

    @Event(handlers = AdminDemandsPresenter.class)
    void responseUpdateDemands(Boolean result);

    @Event(handlers = AdminHandler.class)
    void requestApproveDemands(UniversalAsyncGrid grid, Set<FullDemandDetail> demandsToApprove);

    /**************************************************************************/
    /* AdminDetailWrapperPresentera.                                          */
    /**************************************************************************/
    /**
     * Request/Response method pair for admin details wrapper.
     * @param demandDetail
     */
    @Event(handlers = AdminPresenter.class)
    void requestAdminDetailWrapperPresenter();

    //pozor staci ak sa prezenter zavola raz a uz je aktivny
    @Event(handlers = AdminNewDemandsPresenter.class)
    void responseAdminDetailWrapperPresenter(AdminDetailsWrapperPresenter detailSection);

    /**
     * Request/Response method pair.
     * Get <b>Thread root message id</b>.
     * @param demandDetail
     */
    @Event(handlers = AdminHandler.class)
    void requestThreadRootId(long demandId);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void responseThreadRootId(long threadRootId);

    /**
     * Request/Response method pair.
     * Send <b>Message</b> and notify user.
     * @param supplierId
     * @param supplierDetail
     */
    @Event(handlers = AdminHandler.class)
    void requestSendAdminMessage(MessageDetail messageToSend);

    @Event(handlers = AdminDetailsWrapperPresenter.class)
    void responseSendAdminMessage(MessageDetail messageToSend);

    /**
     * Request/Response method pair.
     * Fetch and display <b>Demand</b> detail
     * @param supplierId
     * @param supplierDetail
     */
    @Event(handlers = AdminHandler.class)
    void requestDemandDetail(Long demandId);

    @Event(handlers = AdminDetailsWrapperPresenter.class, passive = true)
    void responseDemandDetail(FullDemandDetail demandDetail);

    /**
     * Request/Response method pair.
     * Create conversation and return its threadRootId
     * @param supplierId
     * @param supplierDetail
     */
    @Event(handlers = AdminHandler.class)
    void requestCreateConversation(long demandId);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void responseCreateConversation(long threadRootId);

    /*
     * Request/Response method pair
     * Fetch and display chat(conversation) for admin new demands list
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    @Event(handlers = AdminHandler.class)
    void requestConversation(Long threadId);

    @Event(handlers = AdminDetailsWrapperPresenter.class)
    void responseConversation(List<MessageDetail> chatMessages);

    /**
     * Request/Response method pair.
     * Fetch and display chat(<b>conversation</b>) for admin conversations
     * @param threadId
     * @param userId
     * @param chatMessages
     */
    @Event(handlers = AdminHandler.class)
    void requestConversationForAdmin(Long userId);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void responseConversationForAdmin(List<MessageDetail> chatMessages);
}
