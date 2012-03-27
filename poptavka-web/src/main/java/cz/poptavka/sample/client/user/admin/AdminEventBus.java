package cz.poptavka.sample.client.user.admin;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.admin.tab.AdminAccessRolesPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminClientsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminDemandInfoPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminDemandsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminEmailActivationsPresenter;
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
import cz.poptavka.sample.shared.domain.adminModule.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.adminModule.ClientDetail;
import cz.poptavka.sample.shared.domain.adminModule.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.adminModule.InvoiceDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.adminModule.OfferDetail;
import cz.poptavka.sample.shared.domain.adminModule.PaymentDetail;
import cz.poptavka.sample.shared.domain.adminModule.PaymentMethodDetail;
import cz.poptavka.sample.shared.domain.adminModule.PermissionDetail;
import cz.poptavka.sample.shared.domain.adminModule.PreferenceDetail;
import cz.poptavka.sample.shared.domain.adminModule.ProblemDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.Map;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = AdminView.class, module = AdminModule.class)
public interface AdminEventBus extends EventBus {

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
    @Event(handlers = AdminPresenter.class, historyConverter = AdminHistoryConverter.class)
    String goToAdminModule(SearchModuleDataHolder searchDataHolder, int loadWidget);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    /* Admin module forward section - comunication with parent widget*/
    @Event(forwardToParent = true)
    void clearSearchContent();

    /**************************************************************************/
    /* Business Initialization events                                         */
    /**************************************************************************/

    /** Module Initializatin section **/
    //display widget in content area
    @Event(handlers = AdminPresenter.class)
    void displayView(Widget content);

    /** Submodule Initializatin section **/
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
    /*********************** COUNT DATA ********************************************************/
    @Event(handlers = AdminHandler.class)
    void getAdminDemandsCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminOffersCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminClientsCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminMessagesCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = AdminHandler.class)
    void getAdminProblemsCount(SearchModuleDataHolder searchDataHolder);

    /*********************** ASYNCH DATAPROVIDERS *************************************************/
    @Event(handlers = AdminDemandsPresenter.class)
    void createAdminDemandsAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminSuppliersPresenter.class)
    void createAdminSuppliersAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminOffersPresenter.class)
    void createAdminOffersAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminClientsPresenter.class)
    void createAdminClientsAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminAccessRolesPresenter.class)
    void createAdminAccessRoleAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminEmailActivationsPresenter.class)
    void createAdminEmailsActivationAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminInvoicesPresenter.class)
    void createAdminInvoicesAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminMessagesPresenter.class)
    void createAdminMessagesAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminOurPaymentDetailsPresenter.class)
    void createAdminOurPaymentDetailAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminPaymentMethodsPresenter.class)
    void createAdminPaymentMethodAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminPermissionsPresenter.class)
    void createAdminPermissionAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminPreferencesPresenter.class)
    void createAdminPreferenceAsyncDataProvider(final int totalFound);

    @Event(handlers = AdminProblemsPresenter.class)
    void createAdminProblemAsyncDataProvider(final int totalFound);

    /*********************** GET DATA ************************************************************/
    @Event(handlers = AdminHandler.class)
    void getAdminDemands(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminSuppliers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminOffers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminClients(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminAccessRoles(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminEmailsActivation(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminInvoices(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminMessages(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminOurPaymentDetails(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminPaymentMethods(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminPermissions(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminPreferences(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    @Event(handlers = AdminHandler.class)
    void getAdminProblems(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns);

    /*********************** UPDATE **************************************************************/
    @Event(handlers = AdminHandler.class)
    void updateDemand(FullDemandDetail demand);

    @Event(handlers = AdminHandler.class)
    void updateSupplier(FullSupplierDetail supplier);

    @Event(handlers = AdminHandler.class)
    void updateOffer(OfferDetail demand);

    @Event(handlers = AdminHandler.class)
    void updateClient(ClientDetail supplier);

    @Event(handlers = AdminHandler.class)
    void updateAccessRole(AccessRoleDetail accessRole);

    @Event(handlers = AdminHandler.class)
    void updateEmailActivation(EmailActivationDetail accessRole);

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
    void displayAdminTabEmailsActivation(List<EmailActivationDetail> clients);

    @Event(handlers = AdminInvoicesPresenter.class)
    void displayAdminTabInvoices(List<InvoiceDetail> clients);

    @Event(handlers = AdminMessagesPresenter.class)
    void displayAdminTabMessages(List<MessageDetail> messages);

    @Event(handlers = AdminPaymentMethodsPresenter.class)
    void displayAdminTabPaymentMethods(List<PaymentMethodDetail> clients);

    @Event(handlers = AdminPermissionsPresenter.class)
    void displayAdminTabPermissions(List<PermissionDetail> clients);

    @Event(handlers = AdminPreferencesPresenter.class)
    void displayAdminTabPreferences(List<PreferenceDetail> clients);

    @Event(handlers = AdminProblemsPresenter.class)
    void displayAdminTabProblems(List<ProblemDetail> clients);

    /*********************** DISPLAY LOOP DATA ***************************************************/
    @Event(handlers = AdminDemandInfoPresenter.class)
    void displayAdminTabDemandsLoop(List<FullDemandDetail> list);

    /*********************** SHOW DETAILS ********************************************************/
    @Event(handlers = AdminDemandInfoPresenter.class)
    void showAdminDemandDetail(FullDemandDetail selectedObject);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void showAdminSupplierDetail(FullSupplierDetail selectedObject);

    @Event(handlers = AdminOurPaymentDetailsPresenter.class)
    void showAdminTabOurPaymentDetails(List<PaymentDetail> clients);

    //DIALOGBOX
    @Event(handlers = AdminAccessRolesPresenter.class)
    void showDialogBox();

    /*********************** RESPONSE METHODS ****************************************************/
    @Event(handlers = AdminDemandsPresenter.class)
    void responseAdminDemandDetail(Widget widget);

    @Event(handlers = AdminSuppliersPresenter.class)
    void responseAdminSupplierDetail(Widget widget);

    /*********************** COMMIT **************************************************************/
    @Event(handlers = AdminDemandsPresenter.class)
    void addDemandToCommit(FullDemandDetail data);

    @Event(handlers = AdminSuppliersPresenter.class)
    void addSupplierToCommit(FullSupplierDetail data);

    @Event(handlers = AdminOffersPresenter.class)
    void addOfferToCommit(OfferDetail data);

    @Event(handlers = AdminClientsPresenter.class)
    void addClientToCommit(ClientDetail clientDetail);

    @Event(handlers = AdminAccessRolesPresenter.class)
    void addAccessRoleToCommit(AccessRoleDetail clientDetail);

    @Event(handlers = AdminEmailActivationsPresenter.class)
    void addEmailActivationToCommit(EmailActivationDetail clientDetail);

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

    /*********************** CONTROL METHODS *****************************************************/
    @Event(handlers = AdminDemandsPresenter.class)
    void setDetailDisplayedDemand(Boolean displayed);

    @Event(handlers = AdminSuppliersPresenter.class)
    void setDetailDisplayedSupplier(Boolean displayed);

    /*********************** CATEGORIES **********************************************************/
    //GET ROOT
    @Event(handlers = AdminHandler.class)
    void getAdminDemandRootCategories();

    @Event(handlers = AdminHandler.class)
    void getAdminSupplierRootCategories();

    //GET SUB
    @Event(handlers = AdminHandler.class)
    void getAdminDemandSubCategories(Long catId);

    @Event(handlers = AdminHandler.class)
    void getAdminSupplierSubCategories(Long catId);

    //GET PARENT
    @Event(handlers = AdminHandler.class)
    void getAdminDemandParentCategories(Long catId);

    @Event(handlers = AdminHandler.class)
    void getAdminSupplierParentCategories(Long catId);

    //DISPLAY
    @Event(handlers = AdminDemandInfoPresenter.class)
    void displayAdminDemandCategories(List<CategoryDetail> list);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void displayAdminSupplierCategories(List<CategoryDetail> list);

    //OTHER
    @Event(handlers = AdminDemandInfoPresenter.class)
    void doBackDemandCategories(List<CategoryDetail> list);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void doBackSupplierCategories(List<CategoryDetail> list);

    /*********************** LOCALITIES ***********************************************************/
    //GET ROOT
    @Event(handlers = AdminHandler.class)
    void getAdminDemandRootLocalities();

    @Event(handlers = AdminHandler.class)
    void getAdminSupplierRootLocalities();

    //GET SUB
    @Event(handlers = AdminHandler.class)
    void getAdminDemandSubLocalities(String locCode);

    @Event(handlers = AdminHandler.class)
    void getAdminSupplierSubLocalities(String locCode);

    //GET PARENT
    @Event(handlers = AdminHandler.class)
    void getAdminDemandParentLocalities(String locCode);

    @Event(handlers = AdminHandler.class)
    void getAdminSupplierParentLocalities(String locCode);

    //DISPLAY
    @Event(handlers = AdminDemandInfoPresenter.class)
    void displayAdminDemandLocalities(List<LocalityDetail> list);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void displayAdminSupplierLocalities(List<LocalityDetail> list);

    //OTHER
    @Event(handlers = AdminDemandInfoPresenter.class)
    void doBackDemandLocalities(List<LocalityDetail> list);

    @Event(handlers = AdminSupplierInfoPresenter.class)
    void doBackSupplierLocalities(List<LocalityDetail> list);

}
