package cz.poptavka.sample.client.user.admin;

import com.google.gwt.user.client.ui.IsWidget;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.admin.tab.AdminAccessRolesPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminClientInfoPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminClientsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminDemandInfoPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminDemandsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminEmailActivationsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminInvoiceInfoPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminInvoicesPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminMessagesPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminOffersPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminOurPaymentDetailsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminPaymentMethodsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminPermissionsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminPreferencesPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminProblemsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminSupplierInfoPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminSuppliersPresenter;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.InvoiceDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.PaymentDetail;
import cz.poptavka.sample.shared.domain.PaymentMethodDetail;
import cz.poptavka.sample.shared.domain.PermissionDetail;
import cz.poptavka.sample.shared.domain.PreferenceDetail;
import cz.poptavka.sample.shared.domain.ProblemDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.Map;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = AdminModuleView.class, module = AdminModule.class)
public interface AdminModuleEventBus extends EventBus {

    /**************************************************************************/
    /* Navigation | Initialization events. */
    //production init method
    //during development used multiple instancing
    @Event(handlers = AdminDemandsPresenter.class, historyConverter = AdminModuleHistory.class)
    void initDemands(SearchModuleDataHolder filter);

    @Event(handlers = AdminClientsPresenter.class, historyConverter = AdminModuleHistory.class)
    void initClients(SearchModuleDataHolder filter);

    @Event(handlers = AdminSuppliersPresenter.class, historyConverter = AdminModuleHistory.class)
    void initSuppliers(SearchModuleDataHolder filter);

    @Event(handlers = AdminOffersPresenter.class, historyConverter = AdminModuleHistory.class)
    void initOffers(SearchModuleDataHolder filter);

    @Event(handlers = AdminAccessRolesPresenter.class, historyConverter = AdminModuleHistory.class)
    void initAccessRoles(SearchModuleDataHolder filter);

    @Event(handlers = AdminEmailActivationsPresenter.class, historyConverter = AdminModuleHistory.class)
    void initEmailsActivation(SearchModuleDataHolder filter);

    @Event(handlers = AdminInvoicesPresenter.class, historyConverter = AdminModuleHistory.class)
    void initInvoices(SearchModuleDataHolder filter);

    @Event(handlers = AdminMessagesPresenter.class, historyConverter = AdminModuleHistory.class)
    void initMessages(SearchModuleDataHolder filter);

    @Event(handlers = AdminPaymentMethodsPresenter.class, historyConverter = AdminModuleHistory.class)
    void initPaymentMethods(SearchModuleDataHolder filter);

    @Event(handlers = AdminPermissionsPresenter.class, historyConverter = AdminModuleHistory.class)
    void initPermissions(SearchModuleDataHolder filter);

    @Event(handlers = AdminPreferencesPresenter.class, historyConverter = AdminModuleHistory.class)
    void initPreferences(SearchModuleDataHolder filter);

    @Event(handlers = AdminProblemsPresenter.class, historyConverter = AdminModuleHistory.class)
    void initProblems(SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Business events. */
    /* Business events handled by AdminModulePresenter. */
    //init demands module - left user_type menu and initial content
    @Event(handlers = AdminModulePresenter.class)
    void initAdminModule();

    //display widget in content area
    @Event(handlers = AdminModulePresenter.class)
    void displayView(Widget content);

    /**************************************************************************/
    @Event(forwardToParent = true)
    void setBodyHolderWidget(IsWidget body);
    /**************************************************************************/
    /**********************************************************************************************
     *********************** ADMIN SECTION *******************************************************
     **********************************************************************************************/
    /* ----------------- ADMIN DEMANDS -------------------->>>>>>>>> */
    @Event(handlers = AdminModuleHandler.class)
    void getAdminDemandsCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminDemandsPresenter.class)
    void createAdminDemandsAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminDemands(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedDemands(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void updateDemand(FullDemandDetail demand);

    @Event(handlers = AdminDemandsPresenter.class)
    void displayAdminTabDemands(List<FullDemandDetail> demands);

    @Event(handlers = AdminDemandInfoPresenter.class)
    void showAdminDemandDetail(FullDemandDetail selectedObject);

    @Event(handlers = AdminDemandsPresenter.class)
    void responseAdminDemandDetail(Widget widget);

//    @Event(handlers = AdminModulePresenter.class)
//    void displayAdminContent(Widget contentWidget);
    @Event(handlers = AdminDemandsPresenter.class)
    void addDemandToCommit(FullDemandDetail data);

    @Event(handlers = AdminDemandsPresenter.class)
    void setDetailDisplayedDemand(Boolean displayed);

    @Event(handlers = AdminDemandInfoPresenter.class)
    void displayAdminTabDemandsLoop(List<FullDemandDetail> list);

    // ---- DemandsInfo
    @Event(handlers = AdminModuleHandler.class)
    void getAdminDemandRootCategories();

    @Event(handlers = AdminModuleHandler.class)
    void getAdminDemandSubCategories(Long catId);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminDemandParentCategories(Long catId);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminDemandRootLocalities();

    @Event(handlers = AdminModuleHandler.class)
    void getAdminDemandSubLocalities(String locCode);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminDemandParentLocalities(String locCode);

    @Event(handlers = AdminDemandInfoPresenter.class)
    void displayAdminDemandCategories(List<CategoryDetail> list);

    @Event(handlers = AdminDemandInfoPresenter.class)
    void displayAdminDemandLocalities(List<LocalityDetail> list);

    @Event(handlers = AdminDemandInfoPresenter.class)
    void doBackDemandCategories(List<CategoryDetail> list);

    @Event(handlers = AdminDemandInfoPresenter.class)
    void doBackDemandLocalities(List<LocalityDetail> list);

    /* <<<<<<<<<<-------- ADMIN DEMANDS -------------------- */
    /* ----------------- ADMIN SUPPLIERS -------------------->>>>>>>>> */
    @Event(handlers = AdminModuleHandler.class)
    void getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminSuppliersPresenter.class)
    void createAdminSuppliersAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminSuppliers(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminSuppliersPresenter.class)
    void displayAdminTabSuppliers(List<FullSupplierDetail> suppliers);

    @Event(handlers = AdminModuleHandler.class)
    void updateSupplier(FullSupplierDetail supplier);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void showAdminSupplierDetail(FullSupplierDetail selectedObject);

    @Event(handlers = AdminSuppliersPresenter.class)
    void responseAdminSupplierDetail(Widget widget);

    @Event(handlers = AdminSuppliersPresenter.class)
    void addSupplierToCommit(FullSupplierDetail data);

    @Event(handlers = AdminSuppliersPresenter.class)
    void setDetailDisplayedSupplier(Boolean displayed);

    // ---- SuppliersInfo
    @Event(handlers = AdminModuleHandler.class)
    void getAdminSupplierRootCategories();

    @Event(handlers = AdminModuleHandler.class)
    void getAdminSupplierSubCategories(Long catId);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminSupplierParentCategories(Long catId);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminSupplierRootLocalities();

    @Event(handlers = AdminModuleHandler.class)
    void getAdminSupplierSubLocalities(String locCode);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminSupplierParentLocalities(String locCode);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void displayAdminSupplierCategories(List<CategoryDetail> list);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void displayAdminSupplierLocalities(List<LocalityDetail> list);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void doBackSupplierCategories(List<CategoryDetail> list);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void doBackSupplierLocalities(List<LocalityDetail> list);

    /* <<<<<<<<<<-------- ADMIN SUPPLIERS -------------------- */
    /* ----------------- ADMIN OFFERS -------------------->>>>>>>>> */
    @Event(handlers = AdminModuleHandler.class)
    void getAdminOffersCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminOffersPresenter.class)
    void createAdminOffersAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminOffers(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedOffers(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void updateOffer(OfferDetail demand);

    @Event(handlers = AdminOffersPresenter.class)
    void displayAdminTabOffers(List<OfferDetail> demands);

    @Event(handlers = AdminOffersPresenter.class)
    void addOfferToCommit(OfferDetail data);

    /* <<<<<<<<<<-------- ADMIN OFFERS -------------------- */
    /* ----------------- ADMIN CLIENT -------------------->>>>>>>>> */
    @Event(handlers = AdminClientInfoPresenter.class)
    void showAdminClientDetail(ClientDetail clientDetail);

    @Event(handlers = AdminClientsPresenter.class)
    void setDetailDisplayedClient(Boolean value);

    @Event(handlers = AdminClientsPresenter.class)
    void addClientToCommit(ClientDetail clientDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminClientsCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedClients(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminClients(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminClientsPresenter.class)
    void createAdminClientsAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminClientsPresenter.class)
    void displayAdminTabClients(List<ClientDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updateClient(ClientDetail supplier);

    @Event(handlers = AdminClientsPresenter.class)
    void responseAdminClientDetail(Widget widget);

    /* <<<<<<<<<<-------- ADMIN CLIENT -------------------- */
    /* ----------------- ACCESS ROLE -------------------->>>>>>>>> */
    @Event(handlers = AdminAccessRolesPresenter.class)
    void addAccessRoleToCommit(AccessRoleDetail clientDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedAccessRoles(int start, int count, SearchModuleDataHolder searchDataHolder,
//            Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminAccessRoles(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminAccessRolesPresenter.class)
    void createAdminAccessRoleAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminAccessRolesPresenter.class)
    void displayAdminTabAccessRoles(List<AccessRoleDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updateAccessRole(AccessRoleDetail accessRole);

    @Event(handlers = AdminAccessRolesPresenter.class)
    void showDialogBox();

    /* <<<<<<<<<<-------- ACCESS ROLE -------------------- */
    /* ----------------- EMAIL ACTIVATION -------------------->>>>>>>>> */
    @Event(handlers = AdminEmailActivationsPresenter.class)
    void addEmailActivationToCommit(EmailActivationDetail clientDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedEmailsActivation(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminEmailsActivation(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminEmailActivationsPresenter.class)
    void createAdminEmailsActivationAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminEmailActivationsPresenter.class)
    void displayAdminTabEmailsActivation(List<EmailActivationDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updateEmailActivation(EmailActivationDetail accessRole);

    /* <<<<<<<<<<-------- EMAIL ACTIVATION -------------------- */
    /* ----------------- INVOICE -------------------->>>>>>>>> */
    @Event(handlers = AdminInvoicesPresenter.class)
    void addInvoiceToCommit(InvoiceDetail clientDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedInvoices(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminInvoices(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminInvoicesPresenter.class)
    void createAdminInvoicesAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminInvoicesPresenter.class)
    void displayAdminTabInvoices(List<InvoiceDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updateInvoice(InvoiceDetail accessRole);

    @Event(handlers = AdminInvoiceInfoPresenter.class)
    void showAdminInvoicesDetail(InvoiceDetail clientDetail);

    @Event(handlers = AdminInvoicesPresenter.class)
    void setDetailDisplayedInvoices(Boolean value);

    /* <<<<<<<<<<-------- INVOICE -------------------- */
    /* ----------------- MESSAGE -------------------->>>>>>>>> */
    @Event(handlers = AdminMessagesPresenter.class)
    void addMessageToCommit(MessageDetail messageDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminMessagesCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedMessages(int start, int count,
//            Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminMessages(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminMessagesPresenter.class)
    void createAdminMessagesAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminMessagesPresenter.class)
    void displayAdminTabMessages(List<MessageDetail> messages);

    @Event(handlers = AdminModuleHandler.class)
    void updateMessage(MessageDetail accessRole);

    /* <<<<<<<<<<-------- MESSAGE DETAILS -------------------- */
    /* ----------------- OUR PAYMENT DETAILS -------------------->>>>>>>>> */
    @Event(handlers = AdminOurPaymentDetailsPresenter.class)
    void addOurPaymentDetailToCommit(PaymentDetail paymentDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminOurPaymentDetails(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminOurPaymentDetailsPresenter.class)
    void createAdminOurPaymentDetailAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminOurPaymentDetailsPresenter.class)
    void displayAdminTabOurPaymentDetails(List<PaymentDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updateOurPaymentDetail(PaymentDetail accessRole);

    /* <<<<<<<<<<-------- OUR PAYMENT DETAILS -------------------- */
    /* ----------------- PaymentMethodS DETAILS -------------------->>>>>>>>> */
    @Event(handlers = AdminPaymentMethodsPresenter.class)
    void addPaymentMethodToCommit(PaymentMethodDetail paymentMethodDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedPaymentMethods(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminPaymentMethods(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminPaymentMethodsPresenter.class)
    void createAdminPaymentMethodAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminPaymentMethodsPresenter.class)
    void displayAdminTabPaymentMethods(List<PaymentMethodDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updatePaymentMethod(PaymentMethodDetail accessRole);

    /* <<<<<<<<<<-------- PERMISSIONS DETAILS -------------------- */
    /* ----------------- PERMISSIONS DETAILS -------------------->>>>>>>>> */
    @Event(handlers = AdminPermissionsPresenter.class)
    void addPermissionToCommit(PermissionDetail permissionDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedPermissions(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminPermissions(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminPermissionsPresenter.class)
    void createAdminPermissionAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminPermissionsPresenter.class)
    void displayAdminTabPermissions(List<PermissionDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updatePermission(PermissionDetail accessRole);

    /* <<<<<<<<<<-------- PERMISSIONS DETAILS -------------------- */
    /* ----------------- PREFERENCES DETAILS -------------------->>>>>>>>> */
    @Event(handlers = AdminPreferencesPresenter.class)
    void addPreferenceToCommit(PreferenceDetail preferenceDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedPreferences(int start, int count, Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminPreferences(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminPreferencesPresenter.class)
    void createAdminPreferenceAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminPreferencesPresenter.class)
    void displayAdminTabPreferences(List<PreferenceDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updatePreference(PreferenceDetail accessRole);

    /* <<<<<<<<<<-------- PREFERENCES DETAILS -------------------- */

    /* ----------------- PROBLEM DETAILS -------------------->>>>>>>>> */
    @Event(handlers = AdminProblemsPresenter.class)
    void addProblemToCommit(ProblemDetail problemDetail);

    @Event(handlers = AdminModuleHandler.class)
    void getAdminProblemsCount(SearchModuleDataHolder searchDataHolder);

//    @Event(handlers = AdminModuleHandler.class)
//    void getSortedProblems(int start, int count,
//            Map<String, OrderType> orderColumns);
    @Event(handlers = AdminModuleHandler.class)
    void getAdminProblems(int start, int count, SearchModuleDataHolder searchDataHolder,
        Map<String, OrderType> orderColumns);

    @Event(handlers = AdminProblemsPresenter.class)
    void createAdminProblemAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminProblemsPresenter.class)
    void displayAdminTabProblems(List<ProblemDetail> clients);

    @Event(handlers = AdminModuleHandler.class)
    void updateProblem(ProblemDetail accessRole);

    /* <<<<<<<<<<-------- PROBLEM DETAILS -------------------- */
}
