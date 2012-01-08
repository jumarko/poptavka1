package cz.poptavka.sample.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.InvoiceDetail;
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

public interface GeneralRPCServiceAsync {

    //---------------------- DEMAND ------------------------------------------------
    void getAdminDemandsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminDemands(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<FullDemandDetail>> callback);

    void updateDemand(FullDemandDetail detailObject,
            AsyncCallback<FullDemandDetail> callback);
//    void getAdminSortedDemands(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<FullDemandDetail>> callback);

    //---------------------- CLIENT ------------------------------------------------
    void getAdminClientsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminClients(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<ClientDetail>> callback);

    void updateClient(ClientDetail detailObject,
            AsyncCallback<ClientDetail> callback);
//    void getAdminSortedClients(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<ClientDetail>> callback);

    //---------------------- SUPPLIER ------------------------------------------------
    void getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminSuppliers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<FullSupplierDetail>> callback);

    void updateSupplier(FullSupplierDetail detailObject,
            AsyncCallback<FullSupplierDetail> callback);
//    void getAdminSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<FullSupplierDetail>> callback);

    //---------------------- OFFER ------------------------------------------------
    void getAdminOffersCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminOffers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<OfferDetail>> callback);

    void updateOffer(OfferDetail detailObject,
            AsyncCallback<OfferDetail> callback);
//    void getAdminSortedOffers(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<OfferDetail>> callback);

    //---------------------- ACCESS ROLE ------------------------------------------------
    void getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminAccessRoles(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<AccessRoleDetail>> callback);

    void updateAccessRole(AccessRoleDetail detailObject,
            AsyncCallback<AccessRoleDetail> callback);
//    void getAdminSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<AccessRoleDetail>> callback);

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    void getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminEmailsActivation(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<EmailActivationDetail>> callback);

    void updateEmailActivation(EmailActivationDetail detailObject,
            AsyncCallback<EmailActivationDetail> callback);
//    void getAdminSortedEmailsActivation(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<EmailActivationDetail>> callback);

    //---------------------- INVOICE --------------------------------------------------
    void getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminInvoices(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<InvoiceDetail>> callback);

    void updateInvoice(InvoiceDetail detailObject,
            AsyncCallback<InvoiceDetail> callback);
//    void getAdminSortedInvoices(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<InvoiceDetail>> callback);

    //---------------------- MESSAGE ------------------------------------------------
    void getAdminMessagesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminMessages(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<MessageDetail>> callback);

    void updateMessage(MessageDetail detailObject,
            AsyncCallback<MessageDetail> callback);
//    void getAdminSortedMessages(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<MessageDetail>> callback);

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    void getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminOurPaymentDetails(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<PaymentDetail>> callback);

    void updateOurPaymentDetail(PaymentDetail detailObject,
            AsyncCallback<PaymentDetail> callback);
//    void getAdminSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<PaymentDetail>> callback);

    //---------------------- PAYMENT METHOD ------------------------------------------------
    void getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPaymentMethods(AsyncCallback<List<PaymentMethodDetail>> callback);

    void getAdminPaymentMethods(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<PaymentMethodDetail>> callback);

    void updatePaymentMethod(PaymentMethodDetail detailObject,
            AsyncCallback<PaymentMethodDetail> callback);
//    void getAdminSortedPaymentMethods(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<PaymentMethodDetail>> callback);

    //---------------------- PERMISSION ------------------------------------------------
    void getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPermissions(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<PermissionDetail>> callback);

    void updatePermission(PermissionDetail detailObject,
            AsyncCallback<PermissionDetail> callback);
//    void getAdminSortedPermissions(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<PermissionDetail>> callback);

    //---------------------- PREFERENCES ------------------------------------------------
    void getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPreferences(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<PreferenceDetail>> callback);

    void updatePreference(PreferenceDetail detailObject,
            AsyncCallback<PreferenceDetail> callback);
//    void getAdminSortedPreferences(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<PreferenceDetail>> callback);

    //---------------------- PROBLEM ------------------------------------------------
    void getAdminProblemsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminProblems(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<ProblemDetail>> callback);

    void updateProblem(ProblemDetail detailObject,
            AsyncCallback<ProblemDetail> callback);
//    void getAdminSortedProblems(int start, int count, Map<String, OrderType> orderColumns,
//            AsyncCallback<List<ProblemDetail>> callback);
}
